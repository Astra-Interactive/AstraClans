package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.domain.dto.FlagDTO
import ru.astrainteractive.astralibs.architecture.IBaseView

interface IFlagView: IBaseView {
    fun close()
    fun showFlags(flags: List<FlagDTO>)
}