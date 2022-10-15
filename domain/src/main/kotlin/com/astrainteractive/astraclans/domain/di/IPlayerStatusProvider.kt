package com.astrainteractive.astraclans.domain.di

import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO

interface IPlayerStatusProvider {
    fun isPlayerOnline(playerDTO: ClanMemberDTO): Boolean
    fun isAnyMemberOnline(clanDTO: ClanDTO): Boolean
}