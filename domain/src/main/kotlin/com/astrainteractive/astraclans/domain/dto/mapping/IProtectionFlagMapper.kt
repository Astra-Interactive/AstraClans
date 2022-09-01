package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.LandDTO
import com.astrainteractive.astraclans.domain.entities.ClanDAO
import com.astrainteractive.astraclans.domain.entities.FlagDAO
import com.astrainteractive.astraclans.domain.entities.LandDAO

private interface IFlagMapper : Mapper<FlagDAO, FlagDTO>

object FlagMapper : IFlagMapper {
    override fun map(it: FlagDAO): FlagDTO = FlagDTO(it.id.value, it.clanID.value, it.flag, it.enabled)
}

fun FlagDAO.map(): FlagDTO = FlagMapper.map(this)