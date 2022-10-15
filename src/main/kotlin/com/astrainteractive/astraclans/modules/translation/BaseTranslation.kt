package com.astrainteractive.astraclans.modules.translation

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.file_manager.FileManager
import ru.astrainteractive.astralibs.utils.HEX
import ru.astrainteractive.astralibs.utils.getHEXString

abstract class BaseTranslation {

    object TranslationException : Exception("Default value and path cannot be empty")

    protected abstract val fileManager: FileManager
    protected val fileConfig: FileConfiguration
        get() = fileManager.fileConfiguration

    /**
     * This function will write non-existing translation into config file
     * So you don't need to create your config file by yourself
     * Just run plugin with this function and translation file will be generated automatically
     */
    protected fun getHEXString(path: String, default: String): String {
        val msg = fileConfig.getHEXString(path) ?: default.HEX()
        if (!fileConfig.contains(path)) {
            fileConfig.set(path, default)
            fileManager.save()
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
}