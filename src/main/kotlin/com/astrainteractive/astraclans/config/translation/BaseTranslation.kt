package com.astrainteractive.astraclans.config.translation

import com.astrainteractive.astralibs.FileManager
import com.astrainteractive.astralibs.utils.HEX
import com.astrainteractive.astralibs.utils.getHEXString
import org.bukkit.configuration.file.FileConfiguration

abstract class BaseTranslation {

    object TranslationException : Exception("Default value and path cannot be empty")

    protected abstract val fileManager: FileManager
    protected val fileConfig: FileConfiguration
        get() = fileManager.getConfig()

    protected abstract fun onCreate()

    /**
     * This function will write non-existing translation into config file
     * So you don't need to create your config file by yourself
     * Just run plugin with this function and translation file will be generated automatically
     */
    protected fun getHEXString(path: String, default: String): String {
        val msg = fileConfig.getHEXString(path) ?: default.HEX()
        if (!fileConfig.contains(path)) {
            fileConfig.set(path, default)
            fileManager.saveConfig()
        }
        return msg
    }

    data class TranslationDetails(var path: String? = null, var default: String? = null)

    fun translationDetails(details: TranslationDetails.() -> Unit): String {
        val details = TranslationDetails().apply(details)
        val default = details.default
        val path = details.path
        if (default.isNullOrEmpty() || path.isNullOrEmpty()) throw TranslationException
        return getHEXString(path = path, default = default)
    }

    fun translationDetails(path: String, default: String) = translationDetails {
        this.path = path
        this.default = default
    }

    init {
        onCreate()
    }
}