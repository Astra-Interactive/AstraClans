package com.astrainteractive.astraclans.commands.clan

import com.astrainteractive.astraclans.config.translation.sendTranslationMessage
import com.astrainteractive.astraclans.domain.api.use_cases.*
import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.exception.ExceptionHandler
import com.astrainteractive.astraclans.utils.DiscordController
import com.astrainteractive.astraclans.utils.toDTO
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.utils.uuid

class ClanCommandController(private val discordController: DiscordController?) {
    fun Player.toMemberDTO() = ClanMemberDTO(
        minecraftUUID = uuid,
        minecraftName = name
    )

    //aclan leave
    suspend fun leave(sender: Player) = ExceptionHandler.catchSuspend {
        val clanDTO = ClanLeaveUseCase.Param(sender.toDTO()).run {
            ClanLeaveUseCase(this@run)
        }
        discordController?.onMemberLeave(clanDTO, sender.uniqueId)
        sender.sendTranslationMessage { successLeave }
    }

    // aclan disband
    suspend fun disband(sender: Player) = ExceptionHandler.catchSuspend {
        val clanDTO = ClanDisbandUseCase.Param(sender.toDTO()).run {
            ClanDisbandUseCase(this@run)
        }
        discordController?.onClanDisbanded(clanDTO)
        sender.sendTranslationMessage { successDisband }
    }

    // aclan create <tag> <name>
    suspend fun createClan(clanTag: String?, clanName: String?, player: Player) = ExceptionHandler.catchSuspend {
        val clanDTO = ClanCreateUseCase.Params(clanTag, clanName, player.toDTO()).run {
            val params = this
            ClanCreateUseCase(params)
        }
        discordController?.onClanCreated(clanDTO)
        player.sendTranslationMessage("%tag%" to clanTag!!) { successClanCreate }
    }

    // aclan claim
    suspend fun clanClaim(player: Player) = ExceptionHandler.catchSuspend {
        val result = ClaimChunkUseCase.Params(player.toDTO(), player.chunk.toDTO()).run {
            val params = this
            ClaimChunkUseCase(params)
        }
        player.sendTranslationMessage { chunkClaimSuccess }
    }

    // aclan invite <player>
    suspend fun invite(sender: Player, player: Player?) = ExceptionHandler.catchSuspend {
        player ?: run {
            sender.sendTranslationMessage { playerNotOnline }
            return@catchSuspend
        }
        val leaderDTO = sender.toMemberDTO()
        val memberDTO = player.toMemberDTO()
        val result = InvitePlayerUseCase.Params(leaderDTO, memberDTO).run {
            val params = this
            InvitePlayerUseCase(params)
        }
        sender.sendTranslationMessage("%player%" to player.name) { playerInvited }
    }


    // aclan join <tag>
    suspend fun join(sender: Player, clan: String?) = ExceptionHandler.catchSuspend {
        clan ?: run {
            sender.sendTranslationMessage("%clan%" to (clan ?: "-")) { clanNotFound }
            return@catchSuspend
        }
        val memberDTO = sender.toMemberDTO()
        val result = ClanJoinUseCase.Params(clan, memberDTO).run {
            val params = this
            ClanJoinUseCase(params)
        }
        discordController?.onMemberJoined(result.clanDTO, result.memberDTO)
        sender.sendTranslationMessage("%clan%" to clan) { joinedClan }
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
        player.sendTranslationMessage("%flag%" to flag.name, "%value%" to value) { flagChanged }
        return result
    }
}