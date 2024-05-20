package data

import DefaultHttpService
import HttpService

enum class RoaForumThemes {
    LIGHT, DARK
}

interface AppContainer {
    var isSystemInDarkTheme: Boolean
    var autoSwitchDarkTheme: Boolean
    val httpService: HttpService
    var appTheme: RoaForumThemes
    var onChangeAppTheme: (theme: RoaForumThemes) -> Unit
    var enableBackHandler: Boolean
    var onChangeEnableBackHandler: (enableBackHandler: Boolean) -> Unit
    var onBackKey: () -> Unit
}

class DefaultAppContainer : AppContainer {
    override val httpService: HttpService = DefaultHttpService()
    override var onChangeAppTheme: (RoaForumThemes) -> Unit = {}
    override var onChangeEnableBackHandler: (enableBackHandler: Boolean) -> Unit = {}
    override var onBackKey: () -> Unit = {}

    private var _enableBackHandler: Boolean = false
    override var enableBackHandler: Boolean
        get() {return _enableBackHandler}
        set(value) {
            _enableBackHandler = value
            onChangeEnableBackHandler(value)
        }

    private var _autoSwitchDarkTheme: Boolean = true
    override var autoSwitchDarkTheme: Boolean
        get() {return _autoSwitchDarkTheme}
        set(value) {
            _autoSwitchDarkTheme = value
            if (value) {
                isSystemInDarkTheme = _isSystemInDarkTheme
            }
        }

    private var _isSystemInDarkTheme: Boolean = false
    override var isSystemInDarkTheme: Boolean
        get() {return _isSystemInDarkTheme}
        set(value) {
            _isSystemInDarkTheme = value
            if (_autoSwitchDarkTheme) {
                appTheme =
                    if (value) RoaForumThemes.DARK
                    else RoaForumThemes.LIGHT
            }
        }

    private var _appTheme: RoaForumThemes = RoaForumThemes.LIGHT
    override var appTheme: RoaForumThemes
        get() {
            return _appTheme
        }
        set(value) {
            _appTheme = value
            onChangeAppTheme(value)
        }
}
