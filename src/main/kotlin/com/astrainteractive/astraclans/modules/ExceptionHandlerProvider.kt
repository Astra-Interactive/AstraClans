package com.astrainteractive.astraclans.modules

import com.astrainteractive.astraclans.utils.ClanExceptionHandler
import ru.astrainteractive.astralibs.di.IModule

object ExceptionHandlerProvider : IModule<ClanExceptionHandler>() {
    override fun initializer(): ClanExceptionHandler = ClanExceptionHandler
}