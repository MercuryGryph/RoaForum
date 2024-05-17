package theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

enum class RoaForumTheme {
    LIGHT, DARK
}

@Composable
fun RoaForumTheme(
    theme: RoaForumTheme = RoaForumTheme.LIGHT,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (theme == RoaForumTheme.LIGHT) lightColors() else darkColors(),
        content = content
    )
}
