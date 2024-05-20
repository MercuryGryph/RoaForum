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
import data.AppRoutes
import data.RoaForumThemes
import theme.RoaForumTheme
import ui.RegisterScreen
import ui.SettingsScreen
import ui.WelcomeScreen

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    appContainer: AppContainer
) {

    var theme by remember { mutableStateOf(RoaForumThemes.DARK) }
    var syncWithDeviceTheme by remember { mutableStateOf(false) }

    appContainer.onChangeAppTheme = {
        theme = it
    }
    appContainer.onChangeSyncWithDeviceTheme = {
        syncWithDeviceTheme = it
    }

    RoaForumTheme(
        theme = theme,
        syncWithSystemTheme = syncWithDeviceTheme
    ) {
        NavHost(
            navController = navController,
            startDestination = AppRoutes.WELCOME_SCREEN
        ) {
            composable(AppRoutes.WELCOME_SCREEN) {
                WelcomeScreen(
                    appContainer = appContainer,
                    navController = navController
                )
            }
            composable(AppRoutes.REGISTER_SCREEN) {
                RegisterScreen(
                    appContainer = appContainer,
                    navController = navController
                )
            }
            composable(AppRoutes.SETTINGS_SCREEN) {
                SettingsScreen(
                    appContainer = appContainer,
                    navController = navController
                )
            }
        }
    }
}
