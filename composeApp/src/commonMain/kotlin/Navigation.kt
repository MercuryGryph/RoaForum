import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import data.AppContainer
import data.DefaultAppContainer
import data.RoaForumThemes
import theme.RoaForumTheme
import ui.WelcomeScreen

object RouteConfig {
    const val WELCOME_SCREEN = "WelcomeScreen"
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val isDarkMode: Boolean = isSystemInDarkTheme()

    var theme by remember { mutableStateOf(
        if (isDarkMode) {
            RoaForumThemes.DARK
        } else {
            RoaForumThemes.LIGHT
        }
    ) }

    val appContainer: AppContainer by remember { mutableStateOf(
        DefaultAppContainer()
    ) }

    appContainer.isSystemInDarkTheme = isDarkMode

    appContainer.onChangeAppTheme = {
        theme = it
    }

    RoaForumTheme(
        theme = theme
    ) {
        NavHost(
            navController = navController,
            startDestination = RouteConfig.WELCOME_SCREEN
        ) {
            composable(RouteConfig.WELCOME_SCREEN) {
                WelcomeScreen(
                    appContainer = appContainer
                )
            }
        }
    }
}