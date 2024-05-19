package ui

import Greeting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.AppContainer
import data.RoaForumThemes
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import roaforum.composeapp.generated.resources.Res
import roaforum.composeapp.generated.resources.baseline_dark_mode_24
import roaforum.composeapp.generated.resources.baseline_light_mode_24
import roaforum.composeapp.generated.resources.logo_roa_256x
import roaforum.composeapp.generated.resources.logo_roa_kawaii

@Composable
fun WelcomeScreen(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Surface(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
        ) {
            welcome(
                modifier = Modifier
            )

            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                login(
                    appContainer = appContainer,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp, 16.dp)
                )
            }
        }
    }

    FABChangeLightDarkTheme(
        appContainer = appContainer,
        modifier = Modifier
            .wrapContentSize()
            .padding(24.dp)
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FABChangeLightDarkTheme(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
        .wrapContentSize()
        .padding(24.dp)
) {
    FloatingActionButton(
        onClick = {
            appContainer.autoSwitchDarkTheme = false
            appContainer.appTheme =
                if (appContainer.appTheme == RoaForumThemes.LIGHT) {
                    RoaForumThemes.DARK
                } else {
                    RoaForumThemes.LIGHT
                }
        },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(
                if (appContainer.appTheme == RoaForumThemes.LIGHT) {
                    Res.drawable.baseline_dark_mode_24
                } else {
                    Res.drawable.baseline_light_mode_24
                }
            ),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun welcome(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val logoType by remember { mutableStateOf((0..10).random()) }
        val greeting = remember { Greeting().greet() }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(256.dp)
            ) {
                Image(
                    painter =  painterResource(
                        if (logoType == 0) {
                            Res.drawable.logo_roa_kawaii
                        } else {
                            Res.drawable.logo_roa_256x
                        }
                    ),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Text("Compose: $greeting")
        }
    }
}
