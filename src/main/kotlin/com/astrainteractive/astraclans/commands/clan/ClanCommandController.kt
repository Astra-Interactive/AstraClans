package com.astrainteractive.astraclans.commands.clan

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.response.*
import com.astrainteractive.astraclans.domain.api.use_cases.*
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.FlagDataSource
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import com.astrainteractive.astraclans.utils.toDTO
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.utils.uuid
import kotlinx.coroutines.launch
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object ClanCommandController {

    //aclan leave
    suspend fun leave(sender: Player) {
        val result = ClanLeaveUseCase.Param(sender.toDTO()).run {
            ClanLeaveUseCase(this@run)
        }
        when (result) {
            ClanLeaveResponse.ErrorInDatabase -> sender.sendTranslationMessage { errorInClanCreating }
            ClanLeaveResponse.NotInClan -> sender.sendTranslationMessage { notClanMember }
            ClanLeaveResponse.Success -> sender.sendTranslationMessage { successLeave }
            ClanLeaveResponse.YouAreLeader -> sender.sendTranslationMessage { leaderCantLeaveClan }
            null -> sender.sendTranslationMessage { errorInClanCreating }
        }
    }

    // aclan disband
    suspend fun disband(sender: Player) {
        val result = ClanDisbandUseCase.Param(sender.toDTO()).run {
            ClanDisbandUseCase(this@run)
        }
        when (result) {
            ClanDisbandResponse.ErrorInDatabase -> sender.sendTranslationMessage { errorInClanCreating }
            ClanDisbandResponse.NotLeader -> sender.sendTranslationMessage { youAreNotLeader }
            ClanDisbandResponse.Success -> sender.sendTranslationMessage { successDisband }
            null -> sender.sendTranslationMessage { errorInClanCreating }
        }
    }

    // aclan create <tag> <name>
    suspend fun createClan(clanTag: String?, clanName: String?, player: Player) {
        val result = ClanCreateUseCase.Params(clanTag, clanName, player.toDTO()).run {
            val params = this
            ClanCreateUseCase(params)
        }
        when (result) {
            ClanCreateResponse.ClanCreateError -> player.sendTranslationMessage { errorInClanCreating }
            ClanCreateResponse.EmptyClanName -> player.sendTranslationMessage { noClanNameProvided }
            ClanCreateResponse.EmptyClanTag -> player.sendTranslationMessage { noClanTagProvided }
            ClanCreateResponse.PlayerAlreadyInClan -> player.sendTranslationMessage { playerAlreadyInClan }
            is ClanCreateResponse.Success -> {
                player.sendTranslationMessage("%tag%" to clanTag!!) { successClanCreate }
            }

            null -> player.sendTranslationMessage { errorInClanCreating }
        }
    }

    // aclan claim
    suspend fun clanClaim(player: Player) {
        val result = ClaimChunkUseCase.Params(player.toDTO(), player.chunk.toDTO()).run {
            val params = this
            ClaimChunkUseCase(params)
        }
        when (result) {
            ClaimChunkResponse.AlreadyClaimed -> player.sendTranslationMessage { chunkAlreadyClaimed }
            ClaimChunkResponse.ErrorInDatabase -> player.sendTranslationMessage { chunkClaimError }
            ClaimChunkResponse.NoClaimedChunkNearby -> TODO()
            ClaimChunkResponse.NotLeader -> player.sendTranslationMessage { youAreNotLeader }
            is ClaimChunkResponse.Success -> {
                player.sendTranslationMessage { chunkClaimSuccess }
            }

            null -> player.sendTranslationMessage { chunkClaimError }
        }
    }

    // aclan invite <player>
    suspend fun invite(sender: Player, player: Player?) {
        player ?: run {
            sender.sendTranslationMessage { playerNotOnline }
            return
        }
        val leaderDTO = ClanMemberDTO(
            minecraftName = sender.name,
            minecraftUUID = sender.uuid
        )
        val memberDTO = ClanMemberDTO(
            minecraftName = player.name,
            minecraftUUID = player.uuid
        )
        val result = InvitePlayerUseCase.Params(leaderDTO, memberDTO).run {
            val params = this
            InvitePlayerUseCase(params)
        }
        when (result) {
            InvitePlayerResponse.AlreadyInClan -> sender.sendTranslationMessage { playerAlreadyInClan }
            InvitePlayerResponse.AlreadyInvited -> sender.sendTranslationMessage { playerAlreadyInvited }
            InvitePlayerResponse.ErrorInDatabase -> sender.sendTranslationMessage { databaseError }
            InvitePlayerResponse.NotLeader -> sender.sendTranslationMessage { youAreNotLeader }
            is InvitePlayerResponse.Success -> {
                sender.sendTranslationMessage("%player%" to player.name) { playerInvited }
            }

            null -> sender.sendTranslationMessage { databaseError }
        }
    }

    // aclan join <tag>
    suspend fun join(sender: Player, clan: String?) {
        clan ?: run {
            sender.sendTranslationMessage("%clan%" to (clan ?: "-")) { clanNotFound }
            return
        }
        val memberDTO = ClanMemberDTO(
            minecraftUUID = sender.uuid,
            minecraftName = sender.name
        )
        val result = ClanJoinUseCase.Params(clan, memberDTO).run {
            val params = this
            ClanJoinUseCase(params)
        }
        when (result) {
            ClanJoinResponse.AlreadyInClan -> sender.sendTranslationMessage { playerAlreadyInClan }
            ClanJoinResponse.ErrorInDatabase -> sender.sendTranslationMessage { databaseError }
            ClanJoinResponse.NotInvited -> sender.sendTranslationMessage { youNotInvited }
            is ClanJoinResponse.Success -> {
                sender.sendTranslationMessage("%clan%" to clan) { joinedClan }
            }

            ClanJoinResponse.TagNotFound -> sender.sendTranslationMessage("%clan%" to (clan ?: "-")) { clanNotFound }
            null -> sender.sendTranslationMessage { databaseError }
        }
    }

    // aclan setflag <flag> [value]
    suspend fun setFlag(player: Player, flag: FlagsEnum?, value: Boolean) {
        flag ?: run {
            player.sendTranslationMessage { noFlagProvided }
            return
        }

        val result = SetClanFlagUseCase.Params(player.toDTO(), flag.toDTO(value)).run {
            val params = this
            SetClanFlagUseCase(params)
        }
        when (result) {
            SetClanFlagsResponse.ErrorInDatabase -> player.sendTranslationMessage { databaseError }
            SetClanFlagsResponse.NotLeader -> player.sendTranslationMessage { youAreNotLeader }
            is SetClanFlagsResponse.Success -> {
                player.sendTranslationMessage("%flag%" to flag.name, "%value%" to value) { flagChanged }
            }

            null -> player.sendTranslationMessage { databaseError }
        }
    }
}