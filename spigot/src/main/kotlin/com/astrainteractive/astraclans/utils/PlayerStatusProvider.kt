package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain.di.IPlayerStatusProvider
import com.astrainteractive.astraclans.domain.dto.ClanDTO
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import org.bukkit.Bukkit
import java.util.*

object PlayerStatusProvider : IPlayerStatusProvider {
    override fun isPlayerOnline(playerDTO: ClanMemberDTO): Boolean {
        return Bukkit.getPlayer(UUID.fromString(playerDTO.minecraftUUID))?.isOnline == true
    }

    override fun isAnyMemberOnline(clanDTO: ClanDTO): Boolean {
        val isMemberOnline = clanDTO.clanMember.any(::isPlayerOnline)
        val isLeaderOnline = isPlayerOnline(
            ClanMemberDTO(
                clanID = clanDTO.id,
                minecraftUUID = clanDTO.leaderUUID,
                minecraftName = clanDTO.leaderName
            )
        )
        return isMemberOnline || isLeaderOnline
    }
}