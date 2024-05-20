package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import data.AppContainer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.setting

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsScreen(
    appContainer: AppContainer,
    navController: NavHostController,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Surface (
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(300.dp, 600.dp)
                .wrapContentWidth()
        ) {
            Text(
                text = stringResource(Res.string.setting),
                fontSize = MaterialTheme.typography.h1.fontSize
            )
            SettingTheme(
                appContainer = appContainer
            )
        }
    }
}

@Composable
fun SettingTheme(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {

}
