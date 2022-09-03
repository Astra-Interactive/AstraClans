package com.astrainteractive.astraclans.events.events.debounce

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.domain.dto.LandDTO

interface IDebounce
interface IBlockDebounce : IDebounce {
    val chunk: LandDTO
}

interface IPlayerDebounce : IDebounce {
    val chunk: LandDTO
    val player: ClanMemberDTO
    val flag: FlagsEnum
}

class BlockRetractKey(
    override val chunk: LandDTO,
    val flag: FlagsEnum
) : IBlockDebounce

class PlayerRetractKey(
    override val chunk: LandDTO,
    override val player: ClanMemberDTO,
    override val flag: FlagsEnum
) : IPlayerDebounce