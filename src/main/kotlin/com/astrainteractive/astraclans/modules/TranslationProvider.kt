package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.di.IReloadable
import com.astrainteractive.astraclans.modules.translation.PluginTranslation


object TranslationProvider : IReloadable<PluginTranslation>() {
    override fun initializer(): PluginTranslation = PluginTranslation()
}

