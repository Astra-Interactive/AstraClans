package com.astrainteractive.astraclans.events.events.debounce

import org.bukkit.Location

data class BlockRetractKey(
    val location: Location,
    val playerUUID: String,
) : IDebounce

interface IDebounce