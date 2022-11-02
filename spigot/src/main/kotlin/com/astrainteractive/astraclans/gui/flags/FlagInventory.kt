package com.astrainteractive.astraclans.gui.flags

import com.astrainteractive.astraclans.domain.dto.FlagDTO
import com.astrainteractive.astraclans.modules.TranslationProvider
import com.astrainteractive.astraclans.utils.toItemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.di.getValue
import ru.astrainteractive.astralibs.menu.AstraMenuSize
import ru.astrainteractive.astralibs.menu.DefaultPlayerHolder
import ru.astrainteractive.astralibs.menu.IInventoryButton
import ru.astrainteractive.astralibs.menu.PaginatedMenu


class FlagInventory(player: Player) : PaginatedMenu(), IFlagView {
    override val playerMenuUtility = DefaultPlayerHolder(player)
    private val presenter: FlagInventoryPresenter = FlagInventoryPresenter(playerMenuUtility, this)
    val translation by TranslationProvider

    override val maxItemsAmount: Int
        get() = presenter.flagList.size
    override val menuSize: AstraMenuSize
        get() = AstraMenuSize.XL
    override var menuTitle: String = "Флаги вашего клана"

    override val nextPageButton: IInventoryButton = object : IInventoryButton {
        override val index: Int = 54 - 1
        override val item: ItemStack = ItemStack(Material.PAPER).apply {
            editMeta {
                it.setDisplayName(translation.next)
            }
        }
        override val onClick: (e: InventoryClickEvent) -> Unit = {}
    }
    override val prevPageButton: IInventoryButton = object : IInventoryButton {
        override val index: Int = 54 - 9
        override val item: ItemStack = ItemStack(Material.PAPER).apply {
            editMeta {
                it.setDisplayName(translation.previous)
            }
        }
        override val onClick: (e: InventoryClickEvent) -> Unit = {}
    }
    override val backPageButton: IInventoryButton = object : IInventoryButton {
        override val index: Int = 54 - 5
        override val item: ItemStack = ItemStack(Material.PAPER).apply {
            editMeta {
                it.setDisplayName(translation.back)
            }
        }
        override val onClick: (e: InventoryClickEvent) -> Unit = {}
    }
    override var page: Int = 0
    override fun onCreated() {
        presenter.onBinded()
    }

    override fun onInventoryClose(it: InventoryCloseEvent) {
        presenter.clear()
    }

    override fun onPageChanged() {
        showFlags(presenter.flagList)
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

    override fun onInventoryClicked(e: InventoryClickEvent) {
        super.onInventoryClicked(e)
        e.isCancelled = true
        if (e.slot < menuSize.size) {
            presenter?.onFlagClicked(e.slot, page, 54 - 9)
        }
    }
}