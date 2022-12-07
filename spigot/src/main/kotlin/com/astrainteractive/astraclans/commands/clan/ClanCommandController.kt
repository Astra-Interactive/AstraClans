package com.astrainteractive.astraclans.commands.clan

import com.astrainteractive.astraclans.domain.api.use_cases.*
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.exception.handle
import com.astrainteractive.astraclans.modules.ClanCreateUseCaseModule
import com.astrainteractive.astraclans.utils.ClanExceptionHandler
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import com.astrainteractive.astraclans.utils.toDTO
import com.astrainteractive.astraclans.utils.toMemberDTO
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.di.getValue

object ClanCommandController {
    private val discordController: DiscordController?
        get() = DiscordController

    private val clanCreateUseCase by ClanCreateUseCaseModule



    //aclan leave
    suspend fun leave(sender: Player) = ClanExceptionHandler.handle {
        val clanDTO = ClanLeaveUseCase.Param(sender.toDTO()).run {
            ClanLeaveUseCase(this@run)
        }
        discordController?.onMemberLeave(clanDTO, sender.uniqueId)
        sender.sendTranslationMessage { successLeave }
    }

    // aclan disband
    suspend fun disband(sender: Player) = ClanExceptionHandler.handle {
        val clanDTO = ClanDisbandUseCase.Param(sender.toDTO()).run {
            ClanDisbandUseCase(this@run)
        }
        discordController?.onClanDisbanded(clanDTO)
        sender.sendTranslationMessage { successDisband }
    }

    // aclan create <tag> <name>
    suspend fun createClan(clanTag: String?, clanName: String?, player: Player) = ClanExceptionHandler.handle {
        val clanDTO = ClanCreateUseCase.Params(clanTag, clanName, player.toDTO()).run {
            val params = this
            clanCreateUseCase(params)
        }
        discordController?.onClanCreated(clanDTO)
        player.sendTranslationMessage { successClanCreate(clanTag ?: "-") }
    }

    // aclan rename
    suspend fun clanRename(player: Player,name:String) = ClanExceptionHandler.handle{
        ClanRenameUseCase.Param(player.toDTO(),name).run {
            val params = this
            ClanRenameUseCase(params)
        }
        player.sendTranslationMessage { clanRenameSuccess }
    }
    // aclan claim
    suspend fun clanClaim(player: Player) = ClanExceptionHandler.handle {
        val result = ClaimChunkUseCase.Params(player.toDTO(), player.chunk.toDTO()).run {
            val params = this
            ClaimChunkUseCase(params)
        }
        player.sendTranslationMessage { chunkClaimSuccess }
    }

    // aclan invite <player>
    suspend fun invite(sender: Player, player: Player?) = ClanExceptionHandler.handle {
        player ?: run {
            sender.sendTranslationMessage { playerNotOnline }
            return@handle
        }
        val leaderDTO = sender.toMemberDTO()
        val memberDTO = player.toMemberDTO()
        val result = InvitePlayerUseCase.Params(leaderDTO, memberDTO).run {
            val params = this
            InvitePlayerUseCase(params)
        }
        sender.sendTranslationMessage{ playerInvited(player.name) }
    }


    // aclan join <tag>
    suspend fun join(sender: Player, clan: String?) = ClanExceptionHandler.handle {
        clan ?: run {
            sender.sendTranslationMessage{ clanNotFound(clan ?: "-") }
            return@handle
        }
        val memberDTO = sender.toMemberDTO()
        val result = ClanJoinUseCase.Params(clan, memberDTO).run {
            val params = this
            ClanJoinUseCase(params)
        }
        discordController?.onMemberJoined(result.clanDTO, result.memberDTO)
        sender.sendTranslationMessage { joinedClan(clan) }
    }

    // aclan setflag <flag> [value]
    suspend fun setFlag(player: Player, flag: FlagsEnum?, value: Boolean): FlagDTO? {
        flag ?: run {
            player.sendTranslationMessage { noFlagProvided }
            return null
        }

        val result = SetClanFlagUseCase.Params(player.toDTO(), flag.toDTO(value)).run {
            val params = this
            SetClanFlagUseCase(params)
        }
        player.sendTranslationMessage { flagChanged(flag.name,value) }
        return result
    }
}