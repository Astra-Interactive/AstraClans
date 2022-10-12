package com.astrainteractive.astraclans.domain.exception

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

object ExceptionHandler {
    var handler: IClanExceptionHandler? = null
    fun catch(block: () -> Unit) = try {
        block()
    } catch (e: Exception) {
        if (e is ClanOperationException)
            handler?.handle(e)
        else handler?.handle(e)
    }

    suspend fun catchSuspend(block: suspend CoroutineScope.() -> Unit) = try {
        coroutineScope(block)
    } catch (e: Exception) {
        if (e is ClanOperationException)
            handler?.handle(e)
        else handler?.handle(e)
    }
}