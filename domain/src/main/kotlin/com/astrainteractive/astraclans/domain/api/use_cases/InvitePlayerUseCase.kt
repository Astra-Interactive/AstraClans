package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.response.InvitePlayerResponse
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.PendingInviteDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO

object InvitePlayerUseCase : UseCase<InvitePlayerResponse, InvitePlayerUseCase.Params>() {
    class Params(val clanDTO: ClanDTO, val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Params): InvitePlayerResponse? {
        if (params.clanDTO.leaderUUID==params.memberDTO.minecraftUUID) return InvitePlayerResponse.AlreadyInClan
        val isInvited = PendingInviteDataSource.select(params.memberDTO.minecraftUUID).any {
            it.clanID == params.clanDTO.id
        }
        if (isInvited) return InvitePlayerResponse.AlreadyInvited
        ClanMemberDataSource.select(params.memberDTO.minecraftUUID)?.let {
            return InvitePlayerResponse.AlreadyInClan
        }
        val pendingInviteDTO = PendingInviteDataSource.insert(
            PendingInviteDTO(
                minecraftUUID = params.memberDTO.minecraftUUID,
                minecraftName = params.memberDTO.minecraftName,
                clanID = params.clanDTO.id
            )
        ) ?: run {
            return InvitePlayerResponse.ErrorInDatabase
        }
        return InvitePlayerResponse.Success(pendingInviteDTO)
    }
}