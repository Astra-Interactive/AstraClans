package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.response.InvitePlayerResponse
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.PendingInviteDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO

object InvitePlayerUseCase : UseCase<InvitePlayerResponse, InvitePlayerUseCase.Params>() {
    class Params(val leaderDTO: ClanMemberDTO, val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Params): InvitePlayerResponse? {
        val clanDTO = ClanDataSource.select(params.leaderDTO.minecraftUUID) ?: return InvitePlayerResponse.NotLeader
        if (clanDTO.leaderUUID == params.memberDTO.minecraftUUID) return InvitePlayerResponse.AlreadyInClan
        val isInvited = PendingInviteDataSource.select(params.memberDTO.minecraftUUID).any {
            it.clanID == clanDTO.id
        }
        if (isInvited) return InvitePlayerResponse.AlreadyInvited
        ClanMemberDataSource.select(params.memberDTO.minecraftUUID)?.let {
            return InvitePlayerResponse.AlreadyInClan
        }
        val pendingInviteDTO = PendingInviteDataSource.insert(
            PendingInviteDTO(
                minecraftUUID = params.memberDTO.minecraftUUID,
                minecraftName = params.memberDTO.minecraftName,
                clanID = clanDTO.id
            )
        ) ?: run {
            return InvitePlayerResponse.ErrorInDatabase
        }
        return InvitePlayerResponse.Success(pendingInviteDTO)
    }
}