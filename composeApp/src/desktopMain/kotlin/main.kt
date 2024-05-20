import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.DefaultAppContainer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.app_name

val appContainer = DefaultAppContainer()

@OptIn(ExperimentalResourceApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            size = DpSize(1200.dp, 675.dp)
        ),
        title = stringResource(Res.string.app_name),
        onKeyEvent = {
            if (it.key == Key.Escape) {
                if (appContainer.enableBackHandler) {
                    appContainer.onBackKey()
                }
                true
            } else false
        }
    ) {
        App(
            appContainer = appContainer
        )
    }
}
