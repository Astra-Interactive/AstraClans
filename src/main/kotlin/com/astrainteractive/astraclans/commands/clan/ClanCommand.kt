package com.astrainteractive.astraclans.commands.clan

import CommandManager
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.gui.flags.FlagInventory
import com.astrainteractive.astraclans.utils.AstraPermission
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.utils.registerCommand
import com.astrainteractive.astralibs.utils.valueOfOrNull
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.jetbrains.annotations.Async


fun CommandManager.ClanCommand() = AstraLibs.registerCommand("aclan") { sender, args ->

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
            AsyncHelper.launch {
                ClanCommandController.createClan(
                    clanTag = args.getOrNull(1),
                    clanName = args.getOrNull(2),
                    player = sender
                )
            }
        }

        "invite" -> {
            val playerName = args.getOrNull(1)
            val player = playerName?.let { Bukkit.getPlayer(it) }
            AsyncHelper.launch { ClanCommandController.invite(sender, player) }

        }

        "join" -> {
            val clanTag = args.getOrNull(1)
            AsyncHelper.launch { ClanCommandController.join(sender, clanTag) }

        }

        "update" -> {

        }

        "disband" -> {
            AsyncHelper.launch { ClanCommandController.disband(sender) }
        }

        "leave" -> {
            AsyncHelper.launch { ClanCommandController.leave(sender) }
        }

        "claim" -> {
            AsyncHelper.launch { ClanCommandController.clanClaim(player = sender) }
        }

        // aclan flag <flag> <bool>
        "flag" -> {
            val flag = valueOfOrNull<FlagsEnum>(args.getOrNull(1) ?: "")
            val value = args.getOrNull(2)?.toBoolean() ?: true
            AsyncHelper.launch { ClanCommandController.setFlag(player = sender, flag = flag, value = value) }
        }

        "flags" -> {
            FlagInventory(sender).open()
        }
    }
}

