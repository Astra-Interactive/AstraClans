package com.astrainteractive.astraclans.utils

import com.astrainteractive.astraclans.config.translation.Translation
import com.astrainteractive.astraclans.domain.dto.*
import com.astrainteractive.astraclans.domain.dto.FlagsEnum.*
import com.astrainteractive.astraclans.domain.dto.mapping.NOT_EXISTS_ID
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.astrainteractive.astralibs.utils.uuid
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

fun Player.chunkDTO(clanID: Int = NOT_EXISTS_ID) = LandDTO(
    clanID = clanID,
    x = chunk.x,
    z = chunk.z,
    worldName = chunk.world.name
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

fun ItemStack.withMeta(block: ItemMeta.() -> Unit): ItemStack = apply {
    editMeta {
        block(it)
    }
}

fun FlagsEnum.toItemStack(enabled: Boolean): ItemStack {
    return when (this) {
        BLOCK_BREAK_DENY -> ItemStack(Material.BEDROCK).withMeta {
            setDisplayName(Translation.flagBlockBreak)
        }

        BLOCK_PLACE_DENY -> ItemStack(Material.GRASS_BLOCK).withMeta {
            setDisplayName(Translation.flagBlockPlace)
        }

        BLOCK_IGNITE_DENY -> ItemStack(Material.FLINT_AND_STEEL).withMeta {
            setDisplayName(Translation.flagBlockIgnite)
        }

        CREATURE_SPAWN_DENY -> ItemStack(Material.HORSE_SPAWN_EGG).withMeta {
            setDisplayName(Translation.flagCreatureSpawn)
        }

        GROW_DENY -> ItemStack(Material.GRASS_BLOCK).withMeta {
            setDisplayName(Translation.flagBlockGrowDeny)
        }

        EXPLODE_DENY -> ItemStack(Material.TNT).withMeta {
            setDisplayName(Translation.flagBlockExplode)
        }

        PISTON_DENY -> ItemStack(Material.PISTON).withMeta {
            setDisplayName(Translation.flagBlockPiston)
        }

        BLOCK_DAMAGE_DENY -> ItemStack(Material.DIAMOND_PICKAXE).withMeta {
            setDisplayName(Translation.flagBlockDamage)
        }

        BLOCK_INTERACT_DENY -> ItemStack(Material.REDSTONE_TORCH).withMeta {
            setDisplayName(Translation.flagBlockInteract)
        }

        BLOCK_FORM_DENY -> ItemStack(Material.BLUE_ICE).withMeta {
            setDisplayName(Translation.flagBlockForm)
        }

        BLOCK_FERTILIZE_EVENT_DENY -> ItemStack(Material.SAND).withMeta {
            setDisplayName(Translation.flagBlockFertilize)
        }

        SIGN_CHANGE_DENY -> ItemStack(Material.ACACIA_SIGN).withMeta {
            setDisplayName(Translation.flagSignChange)
        }

        BUCKET_EMPTY_DENY -> ItemStack(Material.BUCKET).withMeta {
            setDisplayName(Translation.flagBucketEmpty)
        }

        BUCKET_FILL_DENY -> ItemStack(Material.WATER_BUCKET).withMeta {
            setDisplayName(Translation.flagBucketFill)
        }

        HANGING_PLACE_DENY -> ItemStack(Material.ITEM_FRAME).withMeta {
            setDisplayName(Translation.flagHangingPlace)
        }

        HANGING_BREAK_DENY -> ItemStack(Material.GLOW_ITEM_FRAME).withMeta {
            setDisplayName(Translation.flagHangingBreak)
        }
    }.withMeta {
        lore = listOf(
            Translation.flagIsEnabled + if (enabled) Translation.flagTrue else Translation.flagFalse
        )
    }

}