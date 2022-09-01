package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.domain.dto.FlagDTO

interface IFlagView {
    fun close()
    fun showFlags(flags: List<FlagDTO>)
}