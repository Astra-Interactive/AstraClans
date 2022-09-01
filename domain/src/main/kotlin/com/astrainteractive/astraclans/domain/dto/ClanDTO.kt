package com.astrainteractive.astraclans.domain.dto

import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID

data class ClanDTO(
    val id: Int = NOT_EXISTS_ID,
    val leaderName: String,
    val leaderUUID: String,
    val clanName: String,
    val clanTag: String,
    val clanLands: List<LandDTO> = emptyList(),
    val clanMember: List<ClanMemberDTO> = emptyList(),
    val flags: List<FlagDTO> = emptyList()
)