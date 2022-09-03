package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.SetClanFlagsResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.FlagDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO

object SetClanFlagUseCase : UseCase<SetClanFlagsResponse, SetClanFlagUseCase.Params>() {
    class Params(val memberDTO: ClanMemberDTO, val flagDTO: FlagDTO)

    override suspend fun run(params: Params): SetClanFlagsResponse? {
        val clanLeaderDTO = params.memberDTO
        val clanDTO = ClanDataSource.select(clanLeaderDTO.minecraftUUID)
        if (clanDTO == null || clanLeaderDTO.minecraftUUID != clanDTO.leaderUUID) return SetClanFlagsResponse.NotLeader
        val flagDTO = params.flagDTO.copy(clanID = clanDTO.id)
        val result = FlagDataSource.updateOrInsert(flagDTO) ?: return SetClanFlagsResponse.ErrorInDatabase
        AstraClansAPI.onFlagChanged(clanDTO)
        return SetClanFlagsResponse.Success(result)
    }
}