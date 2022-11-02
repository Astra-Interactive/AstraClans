package com.astrainteractive.astraclans.commands.clan

import CommandManager
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.gui.flags.FlagInventory
import com.astrainteractive.astraclans.utils.AstraPermission
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.utils.registerCommand
import ru.astrainteractive.astralibs.utils.valueOfOrNull


fun CommandManager.ClanCommand(clanCommandController: ClanCommandController) =
    AstraLibs.registerCommand("aclan") { sender, args ->
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
                PluginScope.launch {
                    clanCommandController.createClan(
                        clanTag = args.getOrNull(1),
                        clanName = args.getOrNull(2),
                        player = sender
                    )
                }
            }

            "invite" -> {
                val playerName = args.getOrNull(1)
                val player = playerName?.let { Bukkit.getPlayer(it) }
                PluginScope.launch { clanCommandController.invite(sender, player) }

            }

            "join" -> {
                val clanTag = args.getOrNull(1)
                PluginScope.launch { clanCommandController.join(sender, clanTag) }

            }


            "update" -> {

            }

            "disband" -> {
                PluginScope.launch { clanCommandController.disband(sender) }
            }

            "leave" -> {
                PluginScope.launch { clanCommandController.leave(sender) }
            }

            "claim" -> {
                PluginScope.launch { clanCommandController.clanClaim(player = sender) }
            }

            // aclan flag <flag> <bool>
            "flag" -> {
                val flag = valueOfOrNull<FlagsEnum>(args.getOrNull(1) ?: "")
                val value = args.getOrNull(2)?.toBoolean() ?: true
                PluginScope.launch { clanCommandController.setFlag(player = sender, flag = flag, value = value) }
            }

            "flags" -> {
                PluginScope.launch(Dispatchers.IO) { FlagInventory(sender).open() }
            }
        }
    }

