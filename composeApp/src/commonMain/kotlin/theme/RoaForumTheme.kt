package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import data.RoaForumThemes

@Composable
fun RoaForumTheme(
    theme: RoaForumThemes = RoaForumThemes.LIGHT,
    isSystemDark: Boolean = isSystemInDarkTheme(),
    syncWithSystemTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val appTheme =
        if (syncWithSystemTheme) {
            if (isSystemDark) {
                RoaForumThemes.DARK
            } else {
                RoaForumThemes.LIGHT
            }
        } else {
            theme
        }
    MaterialTheme(
        colors = if (appTheme == RoaForumThemes.LIGHT) lightColors() else darkColors(),
        content = content
    )
}
