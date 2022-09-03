package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.ClanLeaveResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

object ClanLeaveUseCase : UseCase<ClanLeaveResponse, ClanLeaveUseCase.Param>() {
    class Param(val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Param): ClanLeaveResponse? {
        ClanDataSource.select(params.memberDTO.minecraftUUID)?.let {
            return ClanLeaveResponse.YouAreLeader
        }

        val realMemberDTO =
            ClanMemberDataSource.select(params.memberDTO.minecraftUUID) ?: return ClanLeaveResponse.NotInClan
        ClanMemberDataSource.delete(realMemberDTO) ?: return ClanLeaveResponse.ErrorInDatabase
        ClanDataSource.selectByID(realMemberDTO.clanID)?.let { clanDTO ->
            AstraClansAPI.onMemberChanged(clanDTO)
        }
        return ClanLeaveResponse.Success
    }
}