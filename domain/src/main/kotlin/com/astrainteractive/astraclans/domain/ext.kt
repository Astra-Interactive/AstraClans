package com.astrainteractive.astraclans.domain

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.dto.LandDTO

fun LandDTO.isSame(another: LandDTO): Boolean {
    return this.x == another.x && this.worldName == another.worldName && this.z == another.z
}
fun ClanDTO.getFlagOrNull(flag: FlagsEnum) = flags.firstOrNull { it.flag == flag }