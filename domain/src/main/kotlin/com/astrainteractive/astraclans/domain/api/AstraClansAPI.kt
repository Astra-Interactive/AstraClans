package com.astrainteractive.astraclans.domain.api

import com.astrainteractive.astraclans.domain.datasource.ClanDataSource
import com.astrainteractive.astraclans.domain.dto.*
import com.astrainteractive.astraclans.domain.getFlagOrNull
import com.astrainteractive.astraclans.domain.isSame

object AstraClansAPI {
    private val _clansMap = HashMap<Int, ClanDTO>()
    val clansMap: Map<Int, ClanDTO>
        get() = _clansMap

    var playerStatusProvider: IPlayerStatusProvider? = null


    fun rememberClan(clanDTO: ClanDTO) {
        _clansMap[clanDTO.id] = clanDTO
    }

    fun forgetClan(clanDTO: ClanDTO) {
        _clansMap.remove(clanDTO.id)
    }

    suspend fun onLandChanged(_clanDTO: ClanDTO) {
        _clansMap[_clanDTO.id] = ClanDataSource.select(_clanDTO)
    }

    suspend fun onMemberChanged(_clanDTO: ClanDTO) {
        _clansMap[_clanDTO.id] = ClanDataSource.select(_clanDTO)
    }

    suspend fun onFlagChanged(_clanDTO: ClanDTO) {
        _clansMap[_clanDTO.id] = ClanDataSource.select(_clanDTO)
    }

    // TODO optimize
    fun getPlayerClan(player: ClanMemberDTO): ClanDTO? {
        return clansMap.values.firstOrNull {
            it.leaderUUID == player.minecraftUUID ||
                    it.clanMember.firstOrNull { it.minecraftUUID == player.minecraftUUID } != null
        }
    }

    // TODO optimize
    fun getChunkClan(chunk: LandDTO): ClanDTO? {
        return clansMap.values.firstOrNull {
            it.clanLands.firstOrNull { it.isSame(chunk) } != null
        }
    }

    /**
     * Проверяет, включен ли флаг на чанке
     * @return
     * null if no clan on chunk
     * true if flag enabled
     * false if flag disabled
     */
    fun isFlagEnabled(chunk: LandDTO, flag: FlagsEnum): Boolean? {
        val chunkClan = getChunkClan(chunk) ?: return null
        val clanFlag = chunkClan.getFlagOrNull(flag) ?: return true
        val isEnabled = clanFlag.enabled
        return isEnabled
    }

    /**
     * Проверяет, доступно ли игроку выполнение действия [FlagsEnum] на чанке
     */
    fun isFlagEnabledForPlayer(player: ClanMemberDTO, chunk: LandDTO, flag: FlagsEnum): Boolean? {
        val flagEnabled = isFlagEnabled(chunk, flag) ?: return null
        val playerClan = getPlayerClan(player)
        val chunkClan = getChunkClan(chunk) ?: return null
        if (playerClan?.id == chunkClan.id) return null
        val anyMemberOnline = playerStatusProvider?.isAnyMemberOnline(chunkClan)
        return anyMemberOnline?.not()?.and(flagEnabled) ?: flagEnabled
    }

}