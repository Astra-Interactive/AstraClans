package com.astrainteractive.astraclans.events.events.debounce

import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.utils.toDTO
import org.bukkit.entity.Player
import org.bukkit.event.block.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.world.StructureGrowEvent

fun BlockBreakEvent.toRetractKey() = PlayerRetractKey(
    player = player.toDTO(), chunk = block.location.chunk.toDTO(), flag = FlagsEnum.BLOCK_BREAK_DENY
)

fun BlockPlaceEvent.toRetractKey() = PlayerRetractKey(
    player = player.toDTO(), chunk = block.location.chunk.toDTO(), flag = FlagsEnum.BLOCK_PLACE_DENY
)

fun BlockMultiPlaceEvent.toRetractKey() = (this as BlockPlaceEvent).toRetractKey()

fun BlockBurnEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun StructureGrowEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = location.chunk.toDTO(), flag = flag
)

fun EntityExplodeEvent.toRetractKey(flag: FlagsEnum) = this.blockList().map { it.chunk }.distinct().map {
    BlockRetractKey(
        chunk = it.toDTO(),
        flag = flag
    )
}

fun BlockPistonRetractEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun BlockPistonExtendEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun BlockDamageEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun PlayerInteractEvent.toRetractKey(flag: FlagsEnum) = PlayerRetractKey(
    player = player.toDTO(), chunk = clickedBlock!!.location.chunk.toDTO(), flag = flag
)

fun EntityBlockFormEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun EntityInteractEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun BlockFertilizeEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun BlockIgniteEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun SignChangeEvent.toRetractKey(flag: FlagsEnum) = PlayerRetractKey(
    player = player.toDTO(), chunk = block.location.chunk.toDTO(), flag = flag
)

fun PlayerBucketEmptyEvent.toRetractKey() = PlayerRetractKey(
    player = player.toDTO(), chunk = block.location.chunk.toDTO(), flag = FlagsEnum.BUCKET_EMPTY_DENY
)

fun PlayerBucketFillEvent.toRetractKey() = PlayerRetractKey(
    player = player.toDTO(), chunk = block.location.chunk.toDTO(), flag = FlagsEnum.BUCKET_FILL_DENY
)

fun BlockFromToEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = block.location.chunk.toDTO(), flag = flag
)

fun CreatureSpawnEvent.toRetractKey(flag: FlagsEnum) = BlockRetractKey(
    chunk = this.location.chunk.toDTO(), flag = flag
)

fun HangingPlaceEvent.toRetractKey() = PlayerRetractKey(
    player = player!!.toDTO(), chunk = block.location.chunk.toDTO(), flag = FlagsEnum.HANGING_PLACE_DENY
)

fun HangingBreakEvent.toRetractKey() = BlockRetractKey(
    chunk = this.entity.chunk.toDTO(), flag = FlagsEnum.HANGING_BREAK_DENY
)

fun VehicleDestroyEvent.toRetractKey(player: Player) = PlayerRetractKey(
    player = player.toDTO(),
    chunk = this.vehicle.chunk.toDTO(),
    flag = FlagsEnum.HANGING_PLACE_DENY
)

fun VehicleDestroyEvent.toRetractKey() = BlockRetractKey(
    chunk = this.vehicle.chunk.toDTO(),
    flag = FlagsEnum.HANGING_PLACE_DENY
)

fun BlockExplodeEvent.toRetractKey() = this.blockList()
    .toMutableList()
    .apply { add(this@toRetractKey.block) }
    .map { it.chunk }
    .distinct()
    .map {
        BlockRetractKey(
            chunk = it.toDTO(),
            flag = FlagsEnum.HANGING_PLACE_DENY
        )
    }

