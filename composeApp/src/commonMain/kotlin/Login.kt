import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import roaforum.composeapp.generated.resources.show_password
import roaforum.composeapp.generated.resources.user_name
import roaforum.composeapp.generated.resources.welcome_user
import java.net.ConnectException

@OptIn(ExperimentalResourceApi::class)
@Composable
fun login(
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
    var userData
        by remember { mutableStateOf(UserData()) }

    fun loginUser() {
        stateLoadingLogin = LoadingState.Loading
        runBlocking {
            try {
                HttpService.setHttpClientWithUserLogin(
                    userName = userName,
                    password = password,
                    targetUrl = "http://${SERVER_IP}:${SERVER_PORT}/login"
                )
                val response = HttpService.get("http://${SERVER_IP}:${SERVER_PORT}/user")
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val responseText = response.bodyAsText()
                        userData = Json.decodeFromString<UserData>(responseText)
                        stateLoadingLogin = LoadingState.Loaded(State.Succeed)
                    }
                    HttpStatusCode.Unauthorized -> {
                        stateLoadingLogin = LoadingState.Loaded(
                            State.Failed(response.bodyAsText())
                        )
                    }
                }
            } catch (e: ConnectException) {
                stateLoadingLogin = LoadingState.Loaded(when (e.message) {
                    "Connection refused" ->
                        State.ErrorConnectionRefused
                    else ->
                        State.ErrorUnknown(e.message ?: "")
                })
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
        Text(text = stringResource(Res.string.login))

        OutlinedTextField(
            label = {
                Text(
                    stringResource(Res.string.user_name)
                )
            },
            value = userName,
            onValueChange = {
                userName = it
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.baseline_person_24),
                    contentDescription = stringResource(Res.string.user_name)
                )
            },
            singleLine = true,
            modifier = Modifier
        )

        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(Res.string.password)
                )
            },
            value = passwordLiteral,
            onValueChange = {
                passwordLiteral = it
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
            visualTransformation = if (doPasswordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            modifier = Modifier
        )

        Button(
            onClick = {
                password = passwordLiteral.sha256()
                loginUser()
            },
            enabled = stateLoadingLogin != LoadingState.Loading
        ) {
            Text(
                text = stringResource(Res.string.login)
            )
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
