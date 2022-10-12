package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.ClanMemberDataSource
import com.astrainteractive.astraclans.domain.datasource.PendingInviteDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException

object InvitePlayerUseCase : UseCase<PendingInviteDTO, InvitePlayerUseCase.Params>() {
    class Params(val leaderDTO: ClanMemberDTO, val memberDTO: ClanMemberDTO)

    override suspend fun run(params: Params): PendingInviteDTO {
        val sender = params.leaderDTO
        val clanDTO =
            ClanDataSource.select(params.leaderDTO.minecraftUUID) ?: throw ClanOperationException.PlayerNotClanLeader(
                sender
            )
        if (clanDTO.leaderUUID == params.memberDTO.minecraftUUID) throw ClanOperationException.AlreadyInClan(sender)
        val isInvited = PendingInviteDataSource.select(params.memberDTO.minecraftUUID).any {
            it.clanID == clanDTO.id
        }
        if (isInvited) throw ClanOperationException.AlreadyInvited(sender)
        ClanMemberDataSource.select(params.memberDTO.minecraftUUID)?.let {
            throw ClanOperationException.AlreadyInClan(sender)
        }
        val pendingInviteDTO = PendingInviteDataSource.insert(
            PendingInviteDTO(
                minecraftUUID = params.memberDTO.minecraftUUID,
                minecraftName = params.memberDTO.minecraftName,
                clanID = clanDTO.id
            )
        ) ?: throw ClanOperationException.ErrorInDatabase(sender)
        return pendingInviteDTO
    }
}