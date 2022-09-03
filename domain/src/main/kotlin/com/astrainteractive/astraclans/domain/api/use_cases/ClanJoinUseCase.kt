package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.response.ClanJoinResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.PendingInviteDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

object ClanJoinUseCase : UseCase<ClanJoinResponse, ClanJoinUseCase.Params>() {
    class Params(val clanTAG: String, val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Params): ClanJoinResponse? {
        val clanDTO = ClanDataSource.selectByTag(params.clanTAG) ?: return ClanJoinResponse.TagNotFound

        val memberDTO = params.memberDTO
        if (memberDTO.minecraftUUID == clanDTO.leaderUUID) return ClanJoinResponse.AlreadyInClan
        ClanMemberDataSource.select(memberDTO.minecraftUUID)?.let {
            return ClanJoinResponse.AlreadyInClan
        }
        PendingInviteDataSource.select(memberDTO.minecraftUUID).firstOrNull { it.clanID == clanDTO.id }
            ?: return ClanJoinResponse.NotInvited
        PendingInviteDataSource.removeAll(memberDTO.minecraftUUID)
        val result = ClanMemberDataSource.insert(clanDTO, memberDTO) ?: return ClanJoinResponse.ErrorInDatabase

        return ClanJoinResponse.Success(result)
    }
}