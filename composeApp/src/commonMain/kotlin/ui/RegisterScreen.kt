package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.AppContainer
import data.StringLegalState
import data.UserDataUtils
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_account_circle_24
import roaforum.composeapp.generated.resources.baseline_minecraft_missing_24
import roaforum.composeapp.generated.resources.baseline_minecraft_square_24
import roaforum.composeapp.generated.resources.baseline_password_24
import roaforum.composeapp.generated.resources.baseline_person_24
import roaforum.composeapp.generated.resources.baseline_visibility_24
import roaforum.composeapp.generated.resources.baseline_visibility_off_24
import roaforum.composeapp.generated.resources.cancel
import roaforum.composeapp.generated.resources.confirm_password
import roaforum.composeapp.generated.resources.confirm_password_not_same
import roaforum.composeapp.generated.resources.hide_password
import roaforum.composeapp.generated.resources.minecraft_id
import roaforum.composeapp.generated.resources.minecraft_uuid
import roaforum.composeapp.generated.resources.nickname
import roaforum.composeapp.generated.resources.nickname_char_request
import roaforum.composeapp.generated.resources.nickname_length_longest_request
import roaforum.composeapp.generated.resources.nickname_length_shortest_request
import roaforum.composeapp.generated.resources.nickname_not_empty_request
import roaforum.composeapp.generated.resources.password
import roaforum.composeapp.generated.resources.password_char_request
import roaforum.composeapp.generated.resources.password_length_request
import roaforum.composeapp.generated.resources.password_not_empty_request
import roaforum.composeapp.generated.resources.register
import roaforum.composeapp.generated.resources.show_password
import roaforum.composeapp.generated.resources.user_name
import roaforum.composeapp.generated.resources.username_char_request
import roaforum.composeapp.generated.resources.username_length_request
import roaforum.composeapp.generated.resources.username_not_empty_request

@Composable
fun RegisterScreen(
    appContainer: AppContainer,
    navController: NavController,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Surface(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Register(
                    appContainer = appContainer,
                    navController = navController,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp, 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Register(
    appContainer: AppContainer,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var doPasswordHidden
            by remember { mutableStateOf(true) }
    var doConfirmPasswordHidden
            by remember { mutableStateOf(true) }
    var userName
            by remember { mutableStateOf("") }
    var passwordLiteral
            by remember { mutableStateOf("") }
    var confirmPasswordLiteral
            by remember { mutableStateOf("") }
    var password
            by remember { mutableStateOf("") }
    var nickname
            by remember { mutableStateOf("") }
    var minecraftId
            by remember { mutableStateOf("") }
    var minecraftUuid
            by remember { mutableStateOf("") }

    var stateUserNameLegal
            by remember { mutableStateOf(StringLegalState.Unchecked) }
    var statePasswordLegal
            by remember { mutableStateOf(StringLegalState.Unchecked) }
    var stateIsConfirmPasswordSame
            by remember { mutableStateOf(true) }
    var stateNicknameLegal
            by remember { mutableStateOf(StringLegalState.Unchecked) }
    var stateMinecraftIdLegal
            by remember { mutableStateOf(false) }

    fun registry() {
        TODO()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.register),
            fontSize = MaterialTheme.typography.h5.fontSize,
            lineHeight = MaterialTheme.typography.h5.lineHeight,
            fontWeight = MaterialTheme.typography.h5.fontWeight
        )

        // username
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        stringResource(Res.string.user_name)
                    )
                },
                value = userName,
                onValueChange = {
                    userName = it
                    stateUserNameLegal = UserDataUtils.isUserNameLegal(userName)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_person_24),
                        contentDescription = stringResource(Res.string.user_name)
                    )
                },
                singleLine = true,
                colors = when (stateUserNameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        TextFieldDefaults.outlinedTextFieldColors()
                    else ->
                        TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.error)
                },
                isError = when (stateUserNameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        false
                    else ->
                        true
                },
                modifier = Modifier
            )

            Text(
                text = when (stateUserNameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked ->
                        ""
                    StringLegalState.Empty ->
                        stringResource(Res.string.username_not_empty_request)
                    StringLegalState.TooLong, StringLegalState.TooShort, StringLegalState.WrongLength ->
                        stringResource(Res.string.username_length_request)
                    else ->
                        stringResource(Res.string.username_char_request)
                },
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        // password
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        text = stringResource(Res.string.password)
                    )
                },
                value = passwordLiteral,
                onValueChange = {
                    passwordLiteral = it
                    statePasswordLegal = UserDataUtils.isPasswordLegal(passwordLiteral)
                    if (confirmPasswordLiteral.isNotEmpty()) {
                        stateIsConfirmPasswordSame = it == confirmPasswordLiteral
                    }
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_password_24),
                        contentDescription = stringResource(Res.string.password)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            doPasswordHidden = !doPasswordHidden
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (doPasswordHidden) {
                                    Res.drawable.baseline_visibility_24
                                } else {
                                    Res.drawable.baseline_visibility_off_24
                                }
                            ),
                            contentDescription = stringResource(
                                if (doPasswordHidden) {
                                    Res.string.show_password
                                } else {
                                    Res.string.hide_password
                                }
                            )
                        )
                    }
                },
                singleLine =  true,
                colors = when (statePasswordLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        TextFieldDefaults.outlinedTextFieldColors()
                    else ->
                        TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.error)
                },
                isError = when (statePasswordLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        false
                    else ->
                        true
                },
                visualTransformation = if (doPasswordHidden) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier
            )

            Text(
                text = when (statePasswordLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked ->
                        ""
                    StringLegalState.Empty ->
                        stringResource(Res.string.password_not_empty_request)
                    StringLegalState.TooLong, StringLegalState.TooShort, StringLegalState.WrongLength ->
                        stringResource(Res.string.password_length_request)
                    else ->
                        stringResource(Res.string.password_char_request)
                },
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        // confirm password
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        text = stringResource(Res.string.confirm_password)
                    )
                },
                value = confirmPasswordLiteral,
                onValueChange = {
                    confirmPasswordLiteral = it
                    stateIsConfirmPasswordSame = it == passwordLiteral
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_password_24),
                        contentDescription = stringResource(Res.string.confirm_password)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            doConfirmPasswordHidden = !doConfirmPasswordHidden
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (doConfirmPasswordHidden) {
                                    Res.drawable.baseline_visibility_24
                                } else {
                                    Res.drawable.baseline_visibility_off_24
                                }
                            ),
                            contentDescription = stringResource(
                                if (doConfirmPasswordHidden) {
                                    Res.string.show_password
                                } else {
                                    Res.string.hide_password
                                }
                            )
                        )
                    }
                },
                singleLine =  true,
                colors = if (stateIsConfirmPasswordSame) {
                    TextFieldDefaults.outlinedTextFieldColors()
                } else {
                    TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.error)
                },
                isError = !stateIsConfirmPasswordSame,
                visualTransformation = if (doConfirmPasswordHidden) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier
            )

            Text(
                text = if (stateIsConfirmPasswordSame) {
                    ""
                } else {
                    stringResource(Res.string.confirm_password_not_same)
                },
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        // nickname
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        stringResource(Res.string.nickname)
                    )
                },
                value = nickname,
                onValueChange = {
                    nickname = it
                    stateNicknameLegal = UserDataUtils.isNicknameLegal(nickname)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_account_circle_24),
                        contentDescription = stringResource(Res.string.nickname)
                    )
                },
                singleLine = true,
                colors = when (stateNicknameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        TextFieldDefaults.outlinedTextFieldColors()
                    else ->
                        TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.error)
                },
                isError = when (stateNicknameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked, StringLegalState.Empty ->
                        false
                    else ->
                        true
                },
                modifier = Modifier
            )

            Text(
                text = when (stateNicknameLegal) {
                    StringLegalState.Legal, StringLegalState.Unchecked ->
                        ""
                    StringLegalState.Empty ->
                        stringResource(Res.string.nickname_not_empty_request)
                    StringLegalState.TooLong ->
                        stringResource(Res.string.nickname_length_longest_request)
                    StringLegalState.TooShort->
                        stringResource(Res.string.nickname_length_shortest_request)
                    else ->
                        stringResource(Res.string.nickname_char_request)
                },
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        // minecraft id
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        stringResource(Res.string.minecraft_id)
                    )
                },
                value = minecraftId,
                onValueChange = {
                    minecraftId = it
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_minecraft_square_24),
                        contentDescription = stringResource(Res.string.minecraft_id)
                    )
                },
                singleLine = true,
                modifier = Modifier
            )

            Text(
                text = "",
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        // minecraft uuid
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                label = {
                    Text(
                        stringResource(Res.string.minecraft_uuid)
                    )
                },
                value = userName,
                onValueChange = {
                    minecraftUuid = it
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.baseline_minecraft_missing_24),
                        contentDescription = stringResource(Res.string.minecraft_uuid)
                    )
                },
                singleLine = true,
                modifier = Modifier
            )

            Text(
                text = "",
                fontSize = MaterialTheme.typography.overline.fontSize,
                lineHeight = MaterialTheme.typography.overline.lineHeight,
                modifier = Modifier
                    .padding(8.dp, 1.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
        ) {
            Button(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(Res.string.cancel)
                )
            }
            Button(
                onClick = {
                    registry()
                },
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(Res.string.register)
                )
            }
        }
    }
}
