package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.domain.dto.*
import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID
import com.astrainteractive.astralibs.utils.uuid
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

val ClanDTO.offlineLeader: OfflinePlayer
    get() = Bukkit.getOfflinePlayer(UUID.fromString(this.leaderUUID))
val ClanDTO.onlineLeader: Player?
    get() = Bukkit.getPlayer(UUID.fromString(this.leaderUUID))
val LandDTO.chunkKey: Long
    get() = Chunk.getChunkKey(this.x, this.z)
val LandDTO.chunk: Chunk?
    get() = Bukkit.getWorld(this.worldName)?.getChunkAt(chunkKey)

fun LandDTO.isSame(chunk: Chunk?): Boolean {
    chunk ?: return false
    return this.worldName == chunk.world.name && this.x == chunk.x && this.z == chunk.z
}

fun Player.toDTO(clanID: Int = NOT_EXISTS_ID) = ClanMemberDTO(
    clanID = clanID,
    minecraftName = name,
    minecraftUUID = uuid
)

fun FlagsEnum.toDTO(value: Boolean, clanID: Int = NOT_EXISTS_ID) = FlagDTO(
    clanID = clanID,
    flag = this,
    enabled = value
)

fun OfflinePlayer.toDTO(clanID: Int = NOT_EXISTS_ID) = ClanMemberDTO(
    clanID = clanID,
    minecraftUUID = uuid,
    minecraftName = name ?: "NO_NAME_PROVIDED"
)

fun Chunk.toDTO(clanID: Int = NOT_EXISTS_ID) = LandDTO(
    clanID = clanID,
    x = x,
    z = z,
    worldName = world.name
)