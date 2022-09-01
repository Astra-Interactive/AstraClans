package com.astrainteractive.astraclans.domain.dto

import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID

data class LandDTO(
    val id: Int = NOT_EXISTS_ID,
    val clanID: Int = NOT_EXISTS_ID,
    val x: Int,
    val z: Int,
    val worldName: String
)