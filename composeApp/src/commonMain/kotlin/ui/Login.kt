package ui

import SERVER_IP
import SERVER_PORT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
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
import data.AppRoutes
import data.LoadingState
import data.State
import data.StringLegalState
import data.UserData
import data.isPasswordLegal
import data.isUserNameLegal
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_password_24
import roaforum.composeapp.generated.resources.baseline_person_24
import roaforum.composeapp.generated.resources.baseline_visibility_24
import roaforum.composeapp.generated.resources.baseline_visibility_off_24
import roaforum.composeapp.generated.resources.error
import roaforum.composeapp.generated.resources.error_connection_refused
import roaforum.composeapp.generated.resources.error_unknown
import roaforum.composeapp.generated.resources.hide_password
import roaforum.composeapp.generated.resources.login
import roaforum.composeapp.generated.resources.login_failed
import roaforum.composeapp.generated.resources.login_success
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
import roaforum.composeapp.generated.resources.welcome_user
import sha256
import java.net.ConnectException

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Login(
    appContainer: AppContainer,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var doPasswordHidden
        by remember { mutableStateOf(true) }
    var userName
        by remember { mutableStateOf("") }
    var passwordLiteral
        by remember { mutableStateOf("") }
    var password
        by remember { mutableStateOf("") }

    var stateLoadingLogin: LoadingState
        by remember { mutableStateOf(LoadingState.Waiting) }
    var stateUserNameLegal
        by remember { mutableStateOf(StringLegalState.Unchecked) }
    var statePasswordLegal
        by remember { mutableStateOf(StringLegalState.Unchecked) }

    var userData
        by remember { mutableStateOf(UserData()) }

    fun loginUser() {
        stateLoadingLogin = LoadingState.Loading
        runBlocking {
            try {
                appContainer.httpService.resetHttpClient()
                appContainer.httpService.updateHttpClientWithLogin(
                    userName = userName,
                    password = password,
                    targetHost = SERVER_IP,
                    targetRealm = "user"
                )
                val response = appContainer.httpService
                    .get("http://${SERVER_IP}:${SERVER_PORT}/user")
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val responseText = response.bodyAsText()
                        val data = Json.decodeFromString<UserData>(responseText)
                        if (data.userName == userName) {
                            userData = data
                            stateLoadingLogin = LoadingState.Loaded(State.Succeed)
                        } else {
                            stateLoadingLogin = LoadingState.Loaded(State.Failed(null))
                        }
                    }
                    HttpStatusCode.Unauthorized -> {
                        stateLoadingLogin = LoadingState.Loaded(
                            State.Failed(response.bodyAsText())
                        )
                    }
                }
            } catch (e: ConnectException) {
                stateLoadingLogin = LoadingState.Loaded(
                    when (e.message) {
                        "Connection refused" ->
                            State.ErrorConnectionRefused

                        else ->
                            State.ErrorUnknown(e.message ?: "")
                    }
                )
            } catch (e: Exception) {
                stateLoadingLogin = LoadingState.Loaded(
                    State.ErrorUnknown(e.message ?: "")
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.login),
            fontSize = MaterialTheme.typography.h5.fontSize,
            lineHeight = MaterialTheme.typography.h5.lineHeight,
            fontWeight = MaterialTheme.typography.h5.fontWeight
        )

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
                    stateUserNameLegal = isUserNameLegal(userName)
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
                    statePasswordLegal = isPasswordLegal(passwordLiteral)
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

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentSize()
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN)
                },
                enabled = stateLoadingLogin == LoadingState.Waiting,
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(16.dp, 0.dp)
            ) {
                Text(stringResource(Res.string.register))
            }

            Button(
                onClick = {
                    password = passwordLiteral.sha256()
                    loginUser()
                },
                enabled = (
                    stateLoadingLogin == LoadingState.Waiting
                    && stateUserNameLegal == StringLegalState.Legal
                    && statePasswordLegal == StringLegalState.Legal
                ),
                modifier = Modifier
                    .widthIn(min = 150.dp)
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(Res.string.login)
                )
            }
        }

        if (stateLoadingLogin is LoadingState.Loaded) {
            DialogLoginFeedback(
                stateLoadingLogin as LoadingState.Loaded,
                userData
            ) {
                stateLoadingLogin = LoadingState.Waiting
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun DialogLoginFeedback(
    stateLoadingLogin: LoadingState.Loaded,
    userData: UserData,
    onDismissRequest: () -> Unit
): () -> Unit {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(stringResource(Res.string.login))
        },
        text = {
            Column {
                Text(stringResource(when (
                    stateLoadingLogin.state
                ) {
                    State.Succeed ->
                        Res.string.login_success
                    is State.Failed ->
                        Res.string.login_failed
                    else ->
                        Res.string.error
                }))
                Text(when (
                    val state = stateLoadingLogin.state
                ) {
                    State.Succeed ->
                        stringResource(Res.string.welcome_user)
                            .replace("{{userName}}", userData.userName!!)
                    State.ErrorConnectionRefused ->
                        stringResource(Res.string.error_connection_refused)
                    else ->
                        stringResource(Res.string.error_unknown) + "\n" + state.toString()
                })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("ok")
            }
        }
    )
    return onDismissRequest
}
