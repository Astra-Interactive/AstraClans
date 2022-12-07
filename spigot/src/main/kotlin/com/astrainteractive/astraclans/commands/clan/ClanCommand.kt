package com.astrainteractive.astraclans.commands.clan

import CommandManager
import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.gui.flags.FlagInventory
import com.astrainteractive.astraclans.utils.AstraPermission
import com.astrainteractive.astraclans.utils.isSame
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import com.astrainteractive.astraclans.utils.toMemberDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.async.PluginScope
import ru.astrainteractive.astralibs.utils.HEX
import ru.astrainteractive.astralibs.utils.registerCommand
import ru.astrainteractive.astralibs.utils.valueOfOrNull


fun CommandManager.ClanCommand(
    clanCommandController: ClanCommandController
) = AstraLibs.registerCommand("aclan") { sender, args ->
    if (sender !is Player) {
        sender.sendTranslationMessage { notPlayer }
        return@registerCommand
    }
    when (val argument = args.getOrNull(0)) {
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

        "rename" -> {
            val newName = args.toList().subList(1, args.size).joinToString(" ")
            if (newName.isBlank()) {
                sender.sendTranslationMessage { wrongUsage }
                return@registerCommand
            }
            PluginScope.launch { clanCommandController.clanRename(player = sender, newName) }
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

        "region" -> {

            PluginScope.launch(Dispatchers.IO){
                val clan = AstraClansAPI.getPlayerClan(sender.toMemberDTO()) ?: run {
                    sender.sendTranslationMessage { notClanMember }
                    return@launch
                }
                val centerChunk = sender.location.chunk
                val xSize = 7
                val ySize = 7
                val chunkArray = Array(xSize) { i ->
                    Array(ySize) { j ->
                        val x = centerChunk.x - (xSize/2) + i
                        val y = centerChunk.z - (ySize/2) + j
                        val chunk = sender.location.world.getChunkAt(x, y)
                        clan.clanLands.firstOrNull { it.isSame(chunk) } != null
                    }
                }
                val chunkArrayString = chunkArray.joinToString("\n") {
                    it.joinToString("") {
                        if (it) "#03fc77■".HEX() else "#fc0303■".HEX()
                    }
                }.HEX()

                sender.sendMessage("----------------------")
                sender.sendMessage(chunkArrayString)
            }
        }
    }
}


