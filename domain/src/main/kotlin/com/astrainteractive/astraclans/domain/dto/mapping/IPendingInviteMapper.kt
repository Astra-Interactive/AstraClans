package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO
import com.astrainteractive.astraclans.domain.dto.PendingInviteDTO
import com.astrainteractive.astraclans.domain.entities.ClanDAO
import com.astrainteractive.astraclans.domain.entities.LandDAO
import com.astrainteractive.astraclans.domain.entities.PendingInviteDAO

private interface IPendingInviteMapper : Mapper<PendingInviteDAO, PendingInviteDTO>

object PendingInviteMapper : IPendingInviteMapper {
    override fun map(it: PendingInviteDAO): PendingInviteDTO =
        PendingInviteDTO(it.id.value, it.minecraftName, it.minecraftUUID, it.clanID.value)
}

fun PendingInviteDAO.map(): PendingInviteDTO = PendingInviteMapper.map(this)