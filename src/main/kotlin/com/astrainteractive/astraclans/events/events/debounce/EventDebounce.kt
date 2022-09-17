package com.astrainteractive.astraclans.events.events.debounce

import com.astrainteractive.astralibs.utils.catching
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.concurrent.TimeUnit;



/*
 * WorldGuard, a suite of tools for Minecraft
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldGuard team and contributors
 */
class EventDebounce<K : IDebounce>(debounceTime: Long) {
    private val cache: LoadingCache<K, Entry> by lazy {
        CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(debounceTime, TimeUnit.MILLISECONDS)
            .concurrencyLevel(2)
            .build(object : CacheLoader<K, Entry>() {
                override fun load(key: K): Entry {
                    return Entry()
                }
            })
    }

    fun <T> getOrNull(
        key: K,
        originalEvent: T,
        cancellation: () -> Boolean?
    ): Entry? where T : Event?, T : Cancellable? {
        val entry: Entry? = catching { cache.getUnchecked(key) }
        val isCancelled = entry?.isCancelled ?: cancellation() ?: return entry
        if (isCancelled) {
            originalEvent?.isCancelled = isCancelled
            entry?.isCancelled = isCancelled
        }
        return entry

    }

    data class Entry(var isCancelled: Boolean? = null)

    companion object {
        fun <K : IDebounce> create(debounceTime: Long): EventDebounce<K> {
            return EventDebounce(debounceTime)
        }
    }
}
