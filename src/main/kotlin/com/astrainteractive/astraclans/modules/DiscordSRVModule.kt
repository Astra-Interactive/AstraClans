package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.di.IReloadable
import com.astrainteractive.astraclans.utils.getPlugin
import github.scarsz.discordsrv.DiscordSRV

object DiscordSRVModule : IReloadable<DiscordSRV?>() {
    override fun initializer(): DiscordSRV? = getPlugin<DiscordSRV>("DiscordSRV")
}