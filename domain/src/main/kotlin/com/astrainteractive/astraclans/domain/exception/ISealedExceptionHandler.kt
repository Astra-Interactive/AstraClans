package com.astrainteractive.astraclans.domain.exception

fun interface ISealedExceptionHandler<T : Exception> {
    fun handle(e: T)

    companion object {
        inline fun <reified T : Exception, reified L> handle(
            handler: ISealedExceptionHandler<T>,
            block: () -> L
        ) = runCatching(block).onFailure {
            (it as? T)?.let(handler::handle)
        }.getOrNull()
    }
}

inline fun <reified T : Exception, reified L> ISealedExceptionHandler<T>.handle(block: () -> L) {
    ISealedExceptionHandler.handle(this, block)

}