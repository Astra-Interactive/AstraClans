package com.astrainteractive.astraclans.domain.di

import com.astrainteractive.astraclans.domain.config.PluginConfig

abstract class IConfigProvider : IReloadable<PluginConfig>()