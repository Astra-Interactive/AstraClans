package com.astrainteractive.astraclans.domain.api.use_cases

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import ru.astrainteractive.astralibs.domain.IUseCase

object ClanRenameUseCase : IUseCase<ClanDTO, ClanRenameUseCase.Param> {
    class Param(val clanMemberDTO: ClanMemberDTO, val newName: String)

    override suspend fun run(params: Param): ClanDTO {
        val sender = params.clanMemberDTO
        val clanDTO = ClanDataSource.select(params.clanMemberDTO.minecraftUUID)
            ?: throw ClanOperationException.PlayerNotClanLeader(sender)
        val updatedClanDTO = clanDTO.copy(clanName = params.newName).let { ClanDataSource.update(it) }
        updatedClanDTO ?: throw ClanOperationException.ErrorInDatabase(sender)
        return updatedClanDTO.also(AstraClansAPI::rememberClan)
    }
}