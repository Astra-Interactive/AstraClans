package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.utils.getPlugin
import github.scarsz.discordsrv.DiscordSRV
import ru.astrainteractive.astralibs.di.IReloadable

object DiscordSRVModule : IReloadable<DiscordSRV?>() {
    override fun initializer(): DiscordSRV? = getPlugin<DiscordSRV>("DiscordSRV")
}