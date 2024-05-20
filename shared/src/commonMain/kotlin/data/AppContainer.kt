package data

import DefaultHttpService
import HttpService

enum class RoaForumThemes {
    LIGHT, DARK
}

interface AppContainer {
    val httpService: HttpService
    var syncWithDeviceTheme: Boolean
    var onChangeSyncWithDeviceTheme: (syncWithDeviceTheme: Boolean) -> Unit
    var appTheme: RoaForumThemes
    var onChangeAppTheme: (theme: RoaForumThemes) -> Unit
    var enableBackHandler: Boolean
    var onChangeEnableBackHandler: (enableBackHandler: Boolean) -> Unit
    var onBackKey: () -> Unit
}

class DefaultAppContainer : AppContainer {
    override val httpService: HttpService = DefaultHttpService()

    override var onChangeSyncWithDeviceTheme: (syncWithDeviceTheme: Boolean) -> Unit = {}
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

    private var _syncWithDeviceTheme: Boolean = true
    override var syncWithDeviceTheme: Boolean
        get() {return _syncWithDeviceTheme}
        set(value) {
            _syncWithDeviceTheme = value
            onChangeSyncWithDeviceTheme(value)
        }

    private var _appTheme: RoaForumThemes = RoaForumThemes.LIGHT
    override var appTheme: RoaForumThemes
        get() {return _appTheme}
        set(value) {
            _appTheme = value
            onChangeAppTheme(value)
        }
}
