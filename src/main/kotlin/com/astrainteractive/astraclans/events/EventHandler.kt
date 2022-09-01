package com.astrainteractive.astraclans.events

import com.astrainteractive.astraclans.events.events.ClanLandEvent
import com.astrainteractive.astralibs.events.EventListener
import com.astrainteractive.astralibs.events.EventManager
import com.astrainteractive.astralibs.menu.MenuListener


/**
 * Handler for all your events
 */
class EventHandler : EventManager {
    override val handlers: MutableList<EventListener> = mutableListOf()
    private val handler: EventHandler = this
    val menuListener = MenuListener().apply { onEnable(handler) }

    init {
        ClanLandEvent()
    }
}
