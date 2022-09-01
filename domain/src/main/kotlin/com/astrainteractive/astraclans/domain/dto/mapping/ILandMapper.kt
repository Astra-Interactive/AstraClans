package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO
import com.astrainteractive.astraclans.domain.entities.ClanDAO
import com.astrainteractive.astraclans.domain.entities.LandDAO

private interface ILandMapper : Mapper<LandDAO, LandDTO>

object LandMapper : ILandMapper {
    override fun map(it: LandDAO): LandDTO = LandDTO(it.id.value, it.clanID.value, it.x, it.z, it.worldName)
}

fun LandDAO.map(): LandDTO = LandMapper.map(this)