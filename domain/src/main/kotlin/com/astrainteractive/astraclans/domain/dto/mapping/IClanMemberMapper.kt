package com.astrainteractive.astraclans.domain.dto.mapping

import com.astrainteractive.astraclans.domain.dto.ClanMemberDTO
import com.astrainteractive.astraclans.domain.entities.ClanMemberDAO

private interface IClanMemberMapper : Mapper<ClanMemberDAO, ClanMemberDTO>

object ClanMemberMapper : IClanMemberMapper {
    override fun map(it: ClanMemberDAO): ClanMemberDTO =
        ClanMemberDTO(it.id.value, it.clanID.value, it.minecraftName, it.minecraftUUID)
}

fun ClanMemberDAO.map(): ClanMemberDTO = ClanMemberMapper.map(this)