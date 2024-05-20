import androidx.compose.runtime.Composable
import data.AppContainer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    appContainer: AppContainer
) {
    Navigation(
        appContainer = appContainer
    )
}
