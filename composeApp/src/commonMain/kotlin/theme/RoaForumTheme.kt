package theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import data.RoaForumThemes

@Composable
fun RoaForumTheme(
    theme: RoaForumThemes = RoaForumThemes.LIGHT,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (theme == RoaForumThemes.LIGHT) lightColors() else darkColors(),
        content = content
    )
}
