package com.astrainteractive.astraclans.domain.dto

import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID

data class ClanMemberDTO(
    val id: Int = NOT_EXISTS_ID,
    val clanID: Int = NOT_EXISTS_ID,
    val minecraftName: String,
    val minecraftUUID: String,
)