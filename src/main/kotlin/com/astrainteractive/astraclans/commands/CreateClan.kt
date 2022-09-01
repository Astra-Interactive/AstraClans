package com.astrainteractive.astraclans.commands

import CommandManager
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.ClaimChunkResponse
import com.astrainteractive.astraclans.domain.api.ClanCreateResponse
import com.astrainteractive.astraclans.domain.api.SetClanFlagsResponse
import com.astrainteractive.astraclans.domain.api.use_cases.ClaimChunkUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.ClanCreateUseCase
import com.astrainteractive.astraclans.domain.api.use_cases.SetClanFlagUseCase
import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.datasource.FlagDataSource
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.gui.flags.FlagInventory
import com.astrainteractive.astraclans.utils.AstraPermission
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import com.astrainteractive.astraclans.utils.toDTO
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.utils.registerCommand
import com.astrainteractive.astralibs.utils.uuid
import com.astrainteractive.astralibs.utils.valueOfOrNull
import kotlinx.coroutines.launch
import org.bukkit.entity.Player

fun CommandManager.clanCommand() = AstraLibs.registerCommand("aclan") { sender, args ->

    if (sender !is Player) {
        sender.sendTranslationMessage { notPlayer }
        return@registerCommand
    }
    when (args.getOrNull(0)) {
        "create" -> {
            if (!AstraPermission.CreateClan.hasPermission(sender)) {
                sender.sendTranslationMessage { noPermission }
                return@registerCommand
            }
            ClanCommandController.createClan(clanTag = args.getOrNull(1), clanName = args.getOrNull(2), player = sender)
        }

        "update" -> {

        }

        "disband" -> {

        }

        "claim" -> {
            ClanCommandController.clanClaim(player = sender)
        }

        // aclan flag <flag> <bool>
        "flag" -> {
            val flag = valueOfOrNull<FlagsEnum>(args.getOrNull(1) ?: "")
            val value = args.getOrNull(2)?.toBoolean() ?: true
            ClanCommandController.setFlag(player = sender, flag = flag, value = value)
        }

        "flags" -> {
            FlagInventory(sender).open()
        }
    }
}

object ClanCommandController {
    // clan create <tag> <name>
    fun createClan(clanTag: String?, clanName: String?, player: Player) {
        AsyncHelper.launch {
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
    }

    fun clanClaim(player: Player) {
        AsyncHelper.launch {
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
    }

    fun setFlag(player: Player, flag: FlagsEnum?, value: Boolean): FlagDTO? {
        flag ?: run {
            player.sendTranslationMessage { noFlagProvided }
            return null
        }

        AsyncHelper.launch {
            val result = SetClanFlagUseCase.Params(player.toDTO(), flag.toDTO(value)).run {
                val params = this
                SetClanFlagUseCase(params)
            }
            when(result){
                SetClanFlagsResponse.ErrorInDatabase -> player.sendTranslationMessage { databaseError }
                SetClanFlagsResponse.NotLeader -> player.sendTranslationMessage { youAreNotLeader }
                is SetClanFlagsResponse.Success -> {

                }
                null -> player.sendTranslationMessage { databaseError }
            }
        }
        val clan = ClanDataSource.select(player.uuid) ?: run {
            player.sendTranslationMessage { youAreNotLeader }
            return null
        }
        val flagDTO = FlagDataSource.updateOrInsert(FlagDTO(clanID = clan.id, flag = flag, enabled = value))
        AsyncHelper.launch { AstraClansAPI.onFlagChanged(clan) }
        return flagDTO
    }
}