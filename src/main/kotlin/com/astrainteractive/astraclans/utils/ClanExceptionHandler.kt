package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.exception.ClanOperationException
import com.astrainteractive.astraclans.domain.exception.IClanExceptionHandler
import com.astrainteractive.astraclans.modules.translation.sendTranslationMessage
import org.bukkit.Bukkit
import java.util.*

object ClanExceptionHandler : IClanExceptionHandler {
    fun ClanMemberDTO.toPlayer() = Bukkit.getPlayer(UUID.fromString(minecraftUUID))
    override fun handle(e: ClanOperationException.AlreadyInClan) {
        e.sender.toPlayer()?.sendTranslationMessage { this.playerAlreadyInClan }
    }

    override fun handle(e: ClanOperationException.AlreadyInvited) {
        e.sender.toPlayer()?.sendTranslationMessage { this.playerAlreadyInvited }
    }

    override fun handle(e: ClanOperationException.EmptyClanName) {
        e.sender.toPlayer()?.sendTranslationMessage { this.noClanNameProvided }
    }

    override fun handle(e: ClanOperationException.EmptyClanTag) {
        e.sender.toPlayer()?.sendTranslationMessage { this.noClanTagProvided }
    }

    override fun handle(e: ClanOperationException.ErrorInDatabase) {
        e.sender.toPlayer()?.sendTranslationMessage { this.databaseError }
    }

    override fun handle(e: ClanOperationException.LandAlreadyClaimed) {
        e.sender.toPlayer()?.sendTranslationMessage { this.chunkAlreadyClaimed }
    }

    override fun handle(e: ClanOperationException.NoClaimedChunkNearby) {
        e.sender.toPlayer()?.sendTranslationMessage { this.chunkClaimError }
    }

    override fun handle(e: ClanOperationException.NotEnoughMoney) {
        e.sender.toPlayer()?.sendTranslationMessage { this.notEnoguhMoney }
    }

    override fun handle(e: ClanOperationException.NotInClan) {
        e.sender.toPlayer()?.sendTranslationMessage { this.notClanMember }
    }

    override fun handle(e: ClanOperationException.NotInvited) {
        e.sender.toPlayer()?.sendTranslationMessage { this.youNotInvited }
    }

    override fun handle(e: ClanOperationException.PlayerAlreadyInClan) {
        e.sender.toPlayer()?.sendTranslationMessage { this.playerAlreadyInClan }
    }

    override fun handle(e: ClanOperationException.PlayerNotClanLeader) {
        e.sender.toPlayer()?.sendTranslationMessage { this.youAreNotLeader }
    }

    override fun handle(e: ClanOperationException.TagNotFound) {
        e.sender.toPlayer()?.sendTranslationMessage { this.noClanTagProvided }
    }

    override fun handle(e: ClanOperationException.YouAreLeader) {
        e.sender.toPlayer()?.sendTranslationMessage { this.youAreLeader }
    }

    override fun handle(e: Exception) {
        println("Thread.setDefaultUncaughtExceptionHandler: ${e?.cause} ${e?.localizedMessage ?: e?.message}")
    }
}