package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import ru.astrainteractive.astralibs.di.IReloadable
import ru.astrainteractive.astralibs.di.getValue

object ClanCreateUseCaseModule:IReloadable<ClanCreateUseCase>() {
    override fun initializer(): ClanCreateUseCase {
        val config by ConfigProvider
        val economyProvider by VaultEconomyModule
        return ClanCreateUseCase(config, economyProvider)
    }
}