package cn.mercury9.roa.forum

import App
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val permissions = arrayOf(
        Manifest.permission.INTERNET
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            App()
        }
    }

    private fun requestPermissions() {
        val requestList: MutableList<String> = mutableListOf()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                requestList.add(permission)
            }
        }

        if (requestList.isEmpty()) return

        ActivityCompat.requestPermissions(
            this, requestList.toTypedArray(), 1
        )
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}