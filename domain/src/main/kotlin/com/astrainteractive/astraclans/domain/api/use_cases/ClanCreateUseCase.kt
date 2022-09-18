package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.ClanCreateResponse
import com.astrainteractive.astraclans.domain.config.IConfigProvider
import com.astrainteractive.astraclans.domain.config.PluginConfig
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astralibs.utils.Injector
import com.astrainteractive.astralibs.utils.economy.IEconomyProvider
import java.util.*


object ClanCreateUseCase : UseCase<ClanCreateResponse, ClanCreateUseCase.Params>() {
    private val configProvider: IConfigProvider by Injector.lazyInject()
    private val economyProvider: IEconomyProvider by Injector.lazyInject()
    val config: PluginConfig
        get() = configProvider.config

    class Params(
        val clanTag: String?,
        val clanName: String?,
        val player: ClanMemberDTO
    )

    override suspend fun run(params: Params): ClanCreateResponse? {
        val clanTag = params.clanTag
        val clanName = params.clanName
        val player = params.player
        if (clanTag == null) return ClanCreateResponse.EmptyClanTag
        if (clanName == null) return ClanCreateResponse.EmptyClanName
        ClanDataSource.select(player.minecraftUUID)?.let {
            return ClanCreateResponse.PlayerAlreadyInClan
        }
        ClanMemberDataSource.select(player.minecraftUUID)?.let {
            return ClanCreateResponse.PlayerAlreadyInClan
        }
        val playerUUID = UUID.fromString(player.minecraftUUID)
        val playerBalance = economyProvider.getBalance(playerUUID) ?: 0.0
        println("Balance: $playerBalance; required: ${config.economy.clanCreatePurchaseAmount}")
        if (playerBalance < config.economy.clanCreatePurchaseAmount)
            return ClanCreateResponse.NotEnoughMoney
        val takeMoneyResult = economyProvider.takeMoney(playerUUID, config.economy.clanCreatePurchaseAmount.toDouble())
        if (!takeMoneyResult) return ClanCreateResponse.NotEnoughMoney
        val _clanDTO = ClanDTO(
            clanTag = clanTag,
            clanName = clanName,
            leaderUUID = player.minecraftUUID,
            leaderName = player.minecraftName
        )
        val clanDTO = ClanDataSource.insert(_clanDTO)
        if (clanDTO == null)
            return ClanCreateResponse.ClanCreateError
        AstraClansAPI.rememberClan(clanDTO)
        return ClanCreateResponse.Success(clanDTO)

    }
}