package com.astrainteractive.astraclans.commands

import CommandManager
import com.astrainteractive.astraclans.AstraClans
import com.astrainteractive.astraclans.config.AstraPermission
import com.astrainteractive.astraclans.config.translation.Translation
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.registerCommand

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
    AstraClans.instance.reload()
    sender.sendMessage(Translation.reloadComplete)
}






