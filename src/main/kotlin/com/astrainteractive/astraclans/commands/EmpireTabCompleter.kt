package com.astrainteractive.astraclans.commands


import CommandManager
import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.utils.registerTabCompleter
import com.astrainteractive.astralibs.utils.withEntry

/**
 * Tab completer for your plugin which is called when player typing commands
 */
fun CommandManager.tabCompleter() = AstraLibs.registerTabCompleter("aclan") { sender, args ->
    if (args.size <= 1)
        return@registerTabCompleter listOf("create", "update", "disband", "claim").withEntry(args.last())
    return@registerTabCompleter listOf<String>()
}



