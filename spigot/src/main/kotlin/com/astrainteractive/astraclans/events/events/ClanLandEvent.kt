package com.astrainteractive.astraclans.events.events

import com.astrainteractive.astraclans.domain.api.AstraClansAPI
import com.astrainteractive.astraclans.domain.dto.FlagsEnum
import com.astrainteractive.astraclans.events.events.debounce.*
import com.astrainteractive.astraclans.utils.ClanExceptionHandler.toPlayer
import com.astrainteractive.astraclans.utils.sendTranslationMessage
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.*
import org.bukkit.event.vehicle.VehicleDamageEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent
import org.bukkit.event.world.StructureGrowEvent
import ru.astrainteractive.astralibs.events.DSLEvent

class ClanLandEvent {
    private val blockDebounce = EventDebounce.create<IDebounce>(5000)
    private fun <T> blockRetractEvent(key: BlockRetractKey, event: T) where T : Event?, T : Cancellable? {
        blockDebounce.getOrNull(key, event) {
            AstraClansAPI.isFlagEnabled(
                chunk = key.chunk,
                flag = key.flag
            )
        }
    }

    private fun <T> playerRetractEvent(
        key: IPlayerDebounce,
        event: T,
    ) where T : Event?, T : Cancellable? {
        blockDebounce.getOrNull(key, event) {
            AstraClansAPI.isFlagEnabledForPlayer(
                player = key.player,
                chunk = key.chunk,
                flag = key.flag
            )
        }
        if (event?.isCancelled == true){
            key.player.toPlayer()?.sendTranslationMessage { actionBannedInClanLand }

        }
    }

    //-------------------------------------------------------------------------
    // Block break / place
    //-------------------------------------------------------------------------

    val blockBreakEvent = DSLEvent.event(BlockBreakEvent::class.java) {
        playerRetractEvent(it.toRetractKey(), it)
    }

    val blockPlaceEvent = DSLEvent.event(BlockPlaceEvent::class.java) {
        if (it is BlockMultiPlaceEvent) return@event
        playerRetractEvent(it.toRetractKey(), it)
    }

    val onBlockMultiPlace = DSLEvent.event(BlockMultiPlaceEvent::class.java) {
        playerRetractEvent(it.toRetractKey(), it)
    }

    val onBlockBurn = DSLEvent.event(BlockBurnEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_IGNITE_DENY), it)
    }

    val onStructureGrowEvent = DSLEvent.event(StructureGrowEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.GROW_DENY), it)
    }

    val handleFallingBlock = DSLEvent.event(EntityChangeBlockEvent::class.java) {
    }

    val onEntityExplode = DSLEvent.event(EntityExplodeEvent::class.java) {
//        blockRetractEvent(it.toRetractKey(FlagsEnum.EXPLODE_DENY), it)
    }

    val onBlockPistonRetract = DSLEvent.event(BlockPistonRetractEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.PISTON_DENY), it)
    }

    val onBlockPistonExtend = DSLEvent.event(BlockPistonExtendEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.PISTON_DENY), it)
    }

    //-------------------------------------------------------------------------
    // Block external interaction
    //-------------------------------------------------------------------------
    val onBlockDamage = DSLEvent.event(BlockDamageEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_DAMAGE_DENY), it)
    }

    val onPlayerInteract = DSLEvent.event(PlayerInteractEvent::class.java) {
        val block = it.clickedBlock ?: return@event
        playerRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_INTERACT_DENY), it)
    }

    val onEntityBlockForm = DSLEvent.event(EntityBlockFormEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_FORM_DENY), it)
    }

    val onEntityInteract = DSLEvent.event(EntityInteractEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_INTERACT_DENY), it)
    }

    val onBlockFertilize = DSLEvent.event(BlockFertilizeEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_FERTILIZE_EVENT_DENY), it)
    }

    val onBlockIgnite = DSLEvent.event(BlockIgniteEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_IGNITE_DENY), it)
    }

    val onSignChange = DSLEvent.event(SignChangeEvent::class.java) {
        playerRetractEvent(it.toRetractKey(FlagsEnum.SIGN_CHANGE_DENY), it)
    }

    val onBedEnter = DSLEvent.event(PlayerBedEnterEvent::class.java) {

    }

    val onPlayerBucketEmpty = DSLEvent.event(PlayerBucketEmptyEvent::class.java) {
        playerRetractEvent(it.toRetractKey(), it)
    }

    val onPlayerBucketFill = DSLEvent.event(PlayerBucketFillEvent::class.java) {
        playerRetractEvent(it.toRetractKey(), it)
    }

    //-------------------------------------------------------------------------
    // Block self-interaction
    //-------------------------------------------------------------------------

    val onBlockFromTo = DSLEvent.event(BlockFromToEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.BLOCK_IGNITE_DENY), it)
    }
    val onCreatureSpawn = DSLEvent.event(CreatureSpawnEvent::class.java) {
        blockRetractEvent(it.toRetractKey(FlagsEnum.CREATURE_SPAWN_DENY), it)
    }
    val onHangingPlace = DSLEvent.event(HangingPlaceEvent::class.java) {
        it.player ?: return@event
        playerRetractEvent(it.toRetractKey(), it)
    }
    val onHangingBreak = DSLEvent.event(HangingBreakEvent::class.java) {
        blockRetractEvent(it.toRetractKey(), it)
    }
    val onVehicleDestroy = DSLEvent.event(VehicleDestroyEvent::class.java) {
        (it.attacker as? Player)?.let { player ->
            playerRetractEvent(it.toRetractKey(player), it)
        } ?: run {
            blockRetractEvent(it.toRetractKey(), it)
        }
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
    val onEntityDamage = DSLEvent.event(EntityDamageByEntityEvent::class.java) {
        playerRetractEvent(it.toRetractKey(it.damager as? Player?:return@event), it)
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
        it.toRetractKey().map { retractKey ->
            blockRetractEvent(retractKey, it)
        }
    }

    val onEntityExplode2 = DSLEvent.event(EntityExplodeEvent::class.java) {
        it.toRetractKey(FlagsEnum.EXPLODE_DENY).map { retractKey ->
            blockRetractEvent(retractKey, it)
        }
    }
    val onTakeLecternBook = DSLEvent.event(PlayerTakeLecternBookEvent::class.java) {
    }
}