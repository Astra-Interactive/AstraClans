package com.astrainteractive.astraclans.events.events

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.api.canBreak
import com.astrainteractive.astraclans.domain.api.canPlace
import com.astrainteractive.astraclans.events.events.debounce.BlockRetractKey
import com.astrainteractive.astraclans.events.events.debounce.EventDebounce
import com.astrainteractive.astraclans.utils.toDTO
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astralibs.utils.uuid
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.*
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.world.StructureGrowEvent

class ClanLandEvent {
    private val blockDebounce = EventDebounce.create<BlockRetractKey>(5000)

    //-------------------------------------------------------------------------
    // Block break / place
    //-------------------------------------------------------------------------

    val blockBreakEvent = DSLEvent.event(BlockBreakEvent::class.java) {
        val key = BlockRetractKey(it.block.location, it.player.uuid)
        blockDebounce.getOrNull(key, it) {
            val player = it.player
            val chunk = it.block.chunk
            val canBreak = AstraClansAPI.canBreak(player.toDTO(), chunk.toDTO())?.not()
            canBreak
        }
    }
    val blockPlaceEvent = DSLEvent.event(BlockPlaceEvent::class.java) {
        if (it is BlockMultiPlaceEvent) return@event
        val key = BlockRetractKey(it.block.location, it.player.uuid)
        blockDebounce.getOrNull(key, it) {
            val player = it.player
            val chunk = it.block.chunk
            val canBreak = AstraClansAPI.canPlace(player.toDTO(), chunk.toDTO())?.not()
            canBreak
        }
    }
    val onBlockMultiPlace = DSLEvent.event(BlockMultiPlaceEvent::class.java) {
    }
    val onBlockBurn = DSLEvent.event(BlockBurnEvent::class.java) {
    }
    val onStructureGrowEvent = DSLEvent.event(StructureGrowEvent::class.java) {
    }
    val handleFallingBlock = DSLEvent.event(EntityChangeBlockEvent::class.java) {
    }
    val onEntityExplode = DSLEvent.event(EntityExplodeEvent::class.java) {
    }
    val onBlockPistonRetract = DSLEvent.event(BlockPistonRetractEvent::class.java) {
    }
    val onBlockPistonExtend = DSLEvent.event(BlockPistonExtendEvent::class.java) {
    }

    //-------------------------------------------------------------------------
    // Block external interaction
    //-------------------------------------------------------------------------
    val onBlockDamage = DSLEvent.event(BlockDamageEvent::class.java) {
    }

    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) {
    }
    val onEntityBlockForm = DSLEvent.event(EntityBlockFormEvent::class.java) {
    }
    val onEntityInteract = DSLEvent.event(EntityInteractEvent::class.java) {
    }
    val onBlockFertilize = DSLEvent.event(BlockFertilizeEvent::class.java) {
    }
    val onBlockIgnite = DSLEvent.event(BlockIgniteEvent::class.java) {
    }
    val onSignChange = DSLEvent.event(SignChangeEvent::class.java) {
    }
    val onBedEnter = DSLEvent.event(PlayerBedEnterEvent::class.java) {
    }
    val onPlayerBucketEmpty = DSLEvent.event(PlayerBucketEmptyEvent::class.java) {
    }
    val onPlayerBucketFill = DSLEvent.event(PlayerBucketFillEvent::class.java) {
    }

    //-------------------------------------------------------------------------
    // Block self-interaction
    //-------------------------------------------------------------------------

    val onBlockFromTo = DSLEvent.event(BlockFromToEvent::class.java) {
    }
    val onCreatureSpawn = DSLEvent.event(CreatureSpawnEvent::class.java) {
    }
    val onHangingPlace = DSLEvent.event(HangingPlaceEvent::class.java) {
    }
    val onHangingBreak = DSLEvent.event(HangingBreakEvent::class.java) {
    }
    val onVehicleDestroy = DSLEvent.event(VehicleDestroyEvent::class.java) {
    }
    val onBlockExp = DSLEvent.event(BlockExpEvent::class.java) {
    }
    val onPlayerFish = DSLEvent.event(PlayerFishEvent::class.java) {
    }
    val onEntityDeath = DSLEvent.event(EntityDeathEvent::class.java) {
    }
    //-------------------------------------------------------------------------
    // Entity external interaction
    //-------------------------------------------------------------------------

    val onPlayerInteractEntity = DSLEvent.event(PlayerInteractEntityEvent::class.java) {
    }
    val onEntityDamage = DSLEvent.event(EntityDamageEvent::class.java) {
    }
    val onEntityCombust = DSLEvent.event(EntityCombustEvent::class.java) {
    }
    val onEntityUnleash = DSLEvent.event(EntityUnleashEvent::class.java) {
    }
    val onEntityTame = DSLEvent.event(EntityTameEvent::class.java) {
    }
    val onPlayerShearEntity = DSLEvent.event(PlayerShearEntityEvent::class.java) {
    }
    val onVehicleDamage = DSLEvent.event(VehicleDamageEvent::class.java) {
    }

    //-------------------------------------------------------------------------
    // Composite events
    //-------------------------------------------------------------------------

    val onPotionSplash = DSLEvent.event(PotionSplashEvent::class.java) {
    }
    val onBlockDispense = DSLEvent.event(BlockDispenseEvent::class.java) {
    }
    val onLingeringSplash = DSLEvent.event(LingeringPotionSplashEvent::class.java) {
    }

    val onLingeringApply = DSLEvent.event(AreaEffectCloudApplyEvent::class.java) {
    }
    val onBlockExplode = DSLEvent.event(BlockExplodeEvent::class.java) {
    }
    val onTakeLecternBook = DSLEvent.event(PlayerTakeLecternBookEvent::class.java) {
    }
}