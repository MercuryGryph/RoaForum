package data

import DefaultHttpService
import HttpService

enum class RoaForumThemes {
    LIGHT, DARK
}

interface AppContainer {
    var isSystemInDarkTheme: Boolean
    val httpService: HttpService
    val onChangeAppTheme: (theme: RoaForumThemes) -> Unit
    fun setAppTheme(theme: RoaForumThemes): Unit
    fun appTheme(): RoaForumThemes
}

class DefaultAppContainer(
    override var isSystemInDarkTheme: Boolean,
    override val onChangeAppTheme: (theme: RoaForumThemes) -> Unit
) : AppContainer {
    override val httpService: HttpService = DefaultHttpService()

    private var _appTheme: RoaForumThemes =
        if (isSystemInDarkTheme) {
            RoaForumThemes.DARK
        } else {
            RoaForumThemes.LIGHT
        }

    override fun setAppTheme(theme: RoaForumThemes) {
        _appTheme = theme
        onChangeAppTheme(theme)
    }

    override fun appTheme(): RoaForumThemes {
        return _appTheme
    }
}
