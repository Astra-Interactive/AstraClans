package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.utils.PluginTranslation
import ru.astrainteractive.astralibs.di.IReloadable


object TranslationProvider : IReloadable<PluginTranslation>() {
    override fun initializer(): PluginTranslation = PluginTranslation()
}

