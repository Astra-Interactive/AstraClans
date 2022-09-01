package com.astrainteractive.astraclans.commands

import CommandManager
import com.astrainteractive.astraclans.AstraClans
import com.astrainteractive.astraclans.utils.AstraPermission
import com.astrainteractive.astraclans.utils.Translation
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.utils.registerCommand

/**
 * Reload command handler
 */

/**
 * This function called only when atempreload being called
 *
 * Here you should also check for permission
 */
fun CommandManager.reload() = AstraLibs.registerCommand("aclanreload") { sender, args ->
    if (!AstraPermission.Reload.hasPermission(sender)) {
        sender.sendMessage(Translation.noPermission)
        return@registerCommand
    }
    sender.sendMessage(Translation.reload)
    AstraClans.instance.reloadPlugin()
    sender.sendMessage(Translation.reloadComplete)
}






