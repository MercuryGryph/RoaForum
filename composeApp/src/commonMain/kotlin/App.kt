import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_dark_mode_24
import roaforum.composeapp.generated.resources.baseline_light_mode_24
import theme.RoaForumTheme
import ui.WelcomeScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    val isDarkMode: Boolean = isSystemInDarkTheme()

    var theme by remember {
        mutableStateOf(
            if (isDarkMode) {
                RoaForumTheme.DARK
            } else {
                RoaForumTheme.LIGHT
            }
        )
    }

    RoaForumTheme(theme) {
        WelcomeScreen()

        FloatingActionButton(
            onClick = {
                theme = if (theme == RoaForumTheme.LIGHT) {
                    RoaForumTheme.DARK
                } else {
                    RoaForumTheme.LIGHT
                }
            },
            modifier = Modifier
                .wrapContentSize()
                .padding(24.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (theme == RoaForumTheme.LIGHT) {
                        Res.drawable.baseline_dark_mode_24
                    } else {
                        Res.drawable.baseline_light_mode_24
                    }
                ),
                contentDescription = null
            )
        }
    }
}
