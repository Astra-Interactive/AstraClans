package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.entities.ClanDAO

private interface IClanMapper : Mapper<ClanDAO, ClanDTO>

object ClanMapper : IClanMapper {
    override fun map(it: ClanDAO): ClanDTO = ClanDTO(
        it.id.value,
        it.leaderName,
        it.leaderUUID,
        it.clanName,
        it.clanTAG,
        it.lands.map { it.map() },
        it.members.map { it.map() },
        addMissingFlags(it.id.value, it.protectionFlags.map { it.map() })
    )
}

private fun addMissingFlags(clanID: Int, list: List<FlagDTO>): List<FlagDTO> {
    val missingFlags = FlagsEnum.values().toMutableList()
    list.toMutableList().apply {
        forEach { missingFlags.remove(it.flag) }
        addAll(
            missingFlags.map {
                FlagDTO(clanID = clanID, flag = it, enabled = false)
            }
        )
    }
    return list
}

fun ClanDAO.map(): ClanDTO = ClanMapper.map(this)