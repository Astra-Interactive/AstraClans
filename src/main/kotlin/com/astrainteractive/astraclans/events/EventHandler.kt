package com.astrainteractive.astraclans.events

import com.astrainteractive.astraclans.events.events.ClanLandEvent
import ru.astrainteractive.astralibs.events.EventListener
import ru.astrainteractive.astralibs.events.EventManager


/**
 * Handler for all your events
 */
class EventHandler : EventManager {
    override val handlers: MutableList<EventListener> = mutableListOf()
    init {
        ClanLandEvent()
    }
}
