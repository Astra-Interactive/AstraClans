package com.astrainteractive.astraclans.domain.datasource

import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.domain.dto.mapping.map
import com.astrainteractive.astraclans.domain.entities.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

object FlagDataSource {
    fun updateOrInsert(flagDTO: FlagDTO): FlagDTO {
        val result = transaction {
            FlagDAO.find(Flag.flag.eq(flagDTO.flag).and(Flag.clanID eq flagDTO.clanID) ).firstOrNull()?.apply {
                this.enabled = flagDTO.enabled
                this.flag = flagDTO.flag
            }?.map() ?: FlagDAO.new {
                this.clanID = EntityID(flagDTO.clanID, Clan)
                this.enabled = flagDTO.enabled
                this.flag = flagDTO.flag
            }.map()
        }
        return result
    }
}