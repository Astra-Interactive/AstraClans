package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.ClanCreateResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO


object ClanCreateUseCase : UseCase<ClanCreateResponse, ClanCreateUseCase.Params>() {
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
        println("All clans: ${ClanDataSource.selectAll()}")
        println("Player: ${player}")
        ClanDataSource.select(player.minecraftUUID)?.let {
            println("Found leader: $it")
            return ClanCreateResponse.PlayerAlreadyInClan
        }
        ClanMemberDataSource.select(player.minecraftUUID)?.let {
            return ClanCreateResponse.PlayerAlreadyInClan
        }
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