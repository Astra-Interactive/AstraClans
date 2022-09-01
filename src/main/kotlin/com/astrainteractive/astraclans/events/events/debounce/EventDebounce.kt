package com.astrainteractive.astraclans.events.events.debounce

import com.astrainteractive.astralibs.utils.catching
import com.sk89q.worldguard.bukkit.util.Events;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.kotlin.gradle.utils.`is`

import java.util.concurrent.TimeUnit;

/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 */
class EventDebounce<K : Any>(debounceTime: Long) {
    private val cache: LoadingCache<K, Entry>

    init {
        cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(debounceTime, TimeUnit.MILLISECONDS)
            .concurrencyLevel(2)
            .build(object : CacheLoader<K, Entry>() {
                override fun load(key: K): Entry {
                    return Entry()
                }
            })
    }

    fun <T> fireToCancel(originalEvent: Cancellable, firedEvent: T, key: K) where T : Event?, T : Cancellable? {
        val entry: Entry = cache.getUnchecked(key)
        if (entry.isCancelled != null) {
            if (entry.isCancelled!!) {
                originalEvent.isCancelled = true
            }
        } else {
            val cancelled: Boolean = Events.fireAndTestCancel(firedEvent)
            if (cancelled) {
                originalEvent.isCancelled = true
            }
            entry.isCancelled = cancelled
        }
    }

    fun <T> getOrNull(
        key: K,
        originalEvent: T,
        cancellation: () -> Boolean?
    ): Entry? where T : Event?, T : Cancellable? {
        val entry: Entry? = catching { cache.getUnchecked(key) }
        val isCancelled = entry?.isCancelled ?: cancellation() ?: return entry
        originalEvent?.isCancelled = isCancelled
        entry?.isCancelled = isCancelled
        return entry

    }

    data class Entry(var isCancelled: Boolean? = null)

    companion object {
        fun <K : Any> create(debounceTime: Long): EventDebounce<K> {
            return EventDebounce(debounceTime)
        }
    }
}
