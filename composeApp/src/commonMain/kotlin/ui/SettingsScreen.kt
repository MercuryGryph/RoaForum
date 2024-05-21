package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import data.AppContainer
import data.RoaForumThemes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_arrow_back_24
import roaforum.composeapp.generated.resources.setting
import roaforum.composeapp.generated.resources.theme
import roaforum.composeapp.generated.resources.theme_dark
import roaforum.composeapp.generated.resources.theme_light
import roaforum.composeapp.generated.resources.theme_select_one
import roaforum.composeapp.generated.resources.theme_sync_with_device

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
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp, 16.dp)
        ) {
            SettingTheme(
                appContainer = appContainer,
                modifier = Modifier
                    .widthIn(300.dp, 600.dp)
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
            )
        }
    }
}

@Composable
fun SettingSwitchItem(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = text)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun SettingRadioItem(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(text = text)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SettingTheme(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    var syncDeviceTheme by remember { mutableStateOf(appContainer.syncWithDeviceTheme) }
    Column(
        modifier
    ) {
        Text(
            text = stringResource(Res.string.theme),
            fontSize = MaterialTheme.typography.h4.fontSize
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .wrapContentWidth()
        ) {
            SettingSwitchItem(
                text = stringResource(Res.string.theme_sync_with_device),
                checked = syncDeviceTheme
            ) {
                syncDeviceTheme = it
                appContainer.syncWithDeviceTheme = it
            }

            AnimatedVisibility(
                visible = !syncDeviceTheme
            ) {
                val appThemeList = mapOf(
                    stringResource(Res.string.theme_light) to RoaForumThemes.LIGHT,
                    stringResource(Res.string.theme_dark) to RoaForumThemes.DARK
                )
                var appTheme by remember { mutableStateOf(appContainer.appTheme) }
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text(
                        text = stringResource(Res.string.theme_select_one),
//                        fontSize = MaterialTheme.typography.h5.fontSize
                    )
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .wrapContentWidth()
                    ) {
                        appThemeList.forEach {
                            SettingRadioItem(
                                text = it.key,
                                selected = appTheme == it.value
                            ) {
                                appTheme = it.value
                                appContainer.appTheme = it.value
                            }
                        }
                    }
                }
            }
        }
        Divider()
    }
}
