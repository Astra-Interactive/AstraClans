package com.astrainteractive.astraclans.domain.dto

import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID

data class FlagDTO(
    val id: Int = NOT_EXISTS_ID,
    val clanID: Int,
    val flag: FlagsEnum,
    var enabled: Boolean
)

