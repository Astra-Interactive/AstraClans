package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import com.astrainteractive.astraclans.domain.exception.ISealedExceptionHandler
import org.bukkit.Bukkit
import java.util.*

object ClanExceptionHandler: ISealedExceptionHandler<ClanOperationException>{
    fun ClanMemberDTO.toPlayer() = Bukkit.getPlayer(UUID.fromString(minecraftUUID))
    override fun handle(e: ClanOperationException) {
        val player = e.sender.toPlayer() ?: return
        when(e){
            is ClanOperationException.AlreadyInClan -> player.sendTranslationMessage { this.playerAlreadyInClan }
            is ClanOperationException.AlreadyInvited -> player.sendTranslationMessage { this.playerAlreadyInvited }
            is ClanOperationException.EmptyClanName -> player.sendTranslationMessage { this.noClanNameProvided }
            is ClanOperationException.EmptyClanTag -> player.sendTranslationMessage { this.noClanTagProvided }
            is ClanOperationException.ErrorInDatabase -> player.sendTranslationMessage { this.databaseError }
            is ClanOperationException.LandAlreadyClaimed -> player.sendTranslationMessage { this.chunkAlreadyClaimed }
            is ClanOperationException.NoClaimedChunkNearby -> player.sendTranslationMessage { this.chunkClaimError }
            is ClanOperationException.NotEnoughMoney -> player.sendTranslationMessage { this.notEnoguhMoney }
            is ClanOperationException.NotInClan -> player.sendTranslationMessage { this.notClanMember }
            is ClanOperationException.NotInvited -> player.sendTranslationMessage { this.youNotInvited }
            is ClanOperationException.PlayerAlreadyInClan -> player.sendTranslationMessage { this.playerAlreadyInClan }
            is ClanOperationException.PlayerNotClanLeader -> player.sendTranslationMessage { this.youAreNotLeader }
            is ClanOperationException.TagNotFound -> player.sendTranslationMessage { this.noClanTagProvided }
            is ClanOperationException.YouAreLeader -> player.sendTranslationMessage { this.youAreLeader }
        }
    }

}