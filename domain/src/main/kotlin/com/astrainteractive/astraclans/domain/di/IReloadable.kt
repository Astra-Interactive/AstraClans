package com.astrainteractive.astraclans.domain.di

abstract class IReloadable<T> {
    abstract fun initializer(): T
    var value: T = initializer()
        private set

    fun reload() {
        value = initializer()
    }

}