package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import data.AppContainer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_arrow_back_24
import roaforum.composeapp.generated.resources.setting

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsScreen(
    appContainer: AppContainer,
    navController: NavHostController,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Scaffold(
        topBar = {
            SettingsScreenTopBar(
                appContainer = appContainer,
                navController = navController
            )
        },
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(300.dp, 600.dp)
                .wrapContentWidth()
        ) {
            SettingTheme(
                appContainer = appContainer
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingsScreenTopBar(
    appContainer: AppContainer,
    navController: NavHostController,
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.baseline_arrow_back_24),
                    contentDescription = null
                )
            }

            Text(
                text = stringResource(Res.string.setting),
                fontSize = MaterialTheme.typography.h1.fontSize
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
