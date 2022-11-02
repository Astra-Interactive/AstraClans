package com.astrainteractive.astraclans.commands

import CommandManager
import com.astrainteractive.astraclans.AstraClans
import com.astrainteractive.astraclans.modules.TranslationProvider
import com.astrainteractive.astraclans.utils.AstraPermission
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
    val translation = TranslationProvider.value
    if (!AstraPermission.Reload.hasPermission(sender)) {
        sender.sendMessage(translation.noPermission)
        return@registerCommand
    }
    sender.sendMessage(translation.reload)
    AstraClans.instance.reload()
    sender.sendMessage(translation.reloadComplete)
}






