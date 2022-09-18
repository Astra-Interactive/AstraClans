package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.config.translation.Translation
import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.utils.toItemStack
import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.events.EventManager
import com.astrainteractive.astralibs.menu.AstraMenuSize
import com.astrainteractive.astralibs.menu.AstraPlayerMenuUtility
import com.astrainteractive.astralibs.menu.PaginatedMenu
import com.astrainteractive.astralibs.utils.setDisplayName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack




class FlagInventory(player: Player) : PaginatedMenu(), IFlagView {
    override val playerMenuUtility: AstraPlayerMenuUtility = AstraPlayerMenuUtility(player)
    var presenter: FlagInventoryPresenter? = null
    override val backButtonIndex: Int
        get() = 54 - 5
    override val backPageButton: ItemStack = ItemStack(Material.PAPER).apply {
        editMeta {
            it.setDisplayName(Translation.back)
        }
    }
    override val maxItemsAmount: Int
        get() = presenter?.flagList?.size ?: 0
    override var menuName: String = "Флаги вашего клана"
    override val menuSize: AstraMenuSize
        get() = AstraMenuSize.XL
    override val nextButtonIndex: Int
        get() = 54 - 1
    override val nextPageButton: ItemStack = ItemStack(Material.PAPER).apply {
        editMeta {
            it.setDisplayName(Translation.next)
        }
    }
    override var page: Int = 0
    override val prevButtonIndex: Int = 54 - 9
    override val prevPageButton: ItemStack = ItemStack(Material.PAPER).apply {
        editMeta {
            it.setDisplayName(Translation.previous)
        }
    }

    override fun close() {
        inventory.close()
    }

    override fun showFlags(flags: List<FlagDTO>) {
        flags.forEachIndexed { i, flag ->
            val index = getIndex(i)
            if (index > flags.size) return@forEachIndexed
            inventory.setItem(i, flag.flag.toItemStack(flag.enabled))
        }
    }

    override fun handleMenu(e: InventoryClickEvent) {
        super.handleMenu(e)
        if (e.slot < menuSize.size) {
            presenter?.onFlagClicked(e.slot, page, 54 - 9)
        }
    }

    override fun onDestroy(it: InventoryCloseEvent, manager: EventManager) {
    }

    override fun setMenuItems() {
        showFlags(presenter?.flagList ?: return)
    }

    init {
        AsyncHelper.launch(Dispatchers.IO) {
            while (!isInventoryInitialized())
                delay(10)
            presenter = FlagInventoryPresenter(playerMenuUtility, this@FlagInventory)

        }
    }
}