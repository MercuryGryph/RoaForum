package cn.mercury9.roa.forum

import App
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import data.DefaultAppContainer

class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            val appContainer by remember { mutableStateOf(DefaultAppContainer()) }

            App(
                appContainer = appContainer
            )

            var enableBackHandler by remember { mutableStateOf(false) }
            appContainer.onChangeEnableBackHandler = {
                enableBackHandler = it
            }
            BackHandler(
                enabled = enableBackHandler
            ) {
                appContainer.onBackKey()
            }
        }
    }

    private fun requestPermissions() {
        val requestList: MutableList<String> = mutableListOf()

        for (permission in permissions) {
            if (
                ContextCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_DENIED
            ) {
                requestList.add(permission)
            }
        }

        if (requestList.isEmpty()) return

        ActivityCompat.requestPermissions(
            this, requestList.toTypedArray(), 1
        )
    }
}
