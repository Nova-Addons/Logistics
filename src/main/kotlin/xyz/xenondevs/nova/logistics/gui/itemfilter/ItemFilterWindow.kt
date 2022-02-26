package xyz.xenondevs.nova.logistics.gui.itemfilter

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.virtualinventory.VirtualInventory
import de.studiocode.invui.virtualinventory.event.ItemUpdateEvent
import de.studiocode.invui.virtualinventory.event.UpdateReason
import de.studiocode.invui.window.Window
import de.studiocode.invui.window.impl.single.SimpleWindow
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.logistics.item.isItemFilter
import xyz.xenondevs.nova.logistics.registry.GUIMaterials
import xyz.xenondevs.nova.material.NovaMaterial
import xyz.xenondevs.nova.tileentity.network.item.getOrCreateFilterConfig
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
import xyz.xenondevs.nova.util.data.setLocalizedName
import xyz.xenondevs.nova.util.novaMaterial
import kotlin.math.ceil

class ItemFilterWindow(player: Player, material: NovaMaterial, size: Int, private val itemStack: ItemStack) {
    
    private val itemFilter = itemStack.getOrCreateFilterConfig(size)
    private val filterInventory = object : VirtualInventory(null, itemFilter.items.size, itemFilter.items, IntArray(itemFilter.items.size) { 1 }) {
        
        override fun addItem(updateReason: UpdateReason?, itemStack: ItemStack): Int {
            items.withIndex()
                .firstOrNull { it.value == null }
                ?.index
                ?.also { putItemStack(updateReason, it, itemStack) }
            
            return itemStack.amount
        }
        
        override fun setItemStack(updateReason: UpdateReason?, slot: Int, itemStack: ItemStack?): Boolean {
            return super.forceSetItemStack(updateReason, slot, itemStack)
        }
        
    }
    
    private val gui: GUI
    private val window: Window
    
    init {
        val rows = ceil(itemFilter.items.size / 7.0).toInt()
        
        if (rows > 3) {
            gui = GUIBuilder(GUIType.SCROLL_INVENTORY, 9, 6)
                .setStructure("" +
                    "1 - - - - - - - 2" +
                    "| # # m # n # # |" +
                    "| x x x x x x x u" +
                    "| x x x x x x x |" +
                    "| x x x x x x x d" +
                    "3 - - - - - - - 4")
                .addIngredient('m', SwitchModeItem())
                .addIngredient('n', SwitchNBTItem())
                .setInventory(filterInventory)
                .build()
        } else {
            gui = GUIBuilder(GUIType.NORMAL, 9, 3 + rows)
                .setStructure("" +
                    "1 - - - - - - - 2" +
                    "| # # m # n # # |" +
                    ("| # # # # # # # |").repeat(rows) +
                    "3 - - - - - - - 4")
                .addIngredient('m', SwitchModeItem())
                .addIngredient('n', SwitchNBTItem())
                .build()
            gui.fillRectangle(1, 2, 7, filterInventory, true)
        }
        
        window = SimpleWindow(player, arrayOf(TranslatableComponent(material.localizedName)), gui)
        filterInventory.setItemUpdateHandler(::handleInventoryUpdate)
        window.addCloseHandler(::saveFilterConfig)
        window.show()
    }
    
    private fun saveFilterConfig() {
        itemFilter.items = filterInventory.items
        itemStack.saveFilterConfig(itemFilter)
    }
    
    private fun handleInventoryUpdate(event: ItemUpdateEvent) {
        if (event.updateReason == null) return
        
        event.isCancelled = true
        if (event.newItemStack?.novaMaterial.isItemFilter()) return
        filterInventory.setItemStack(null, event.slot, event.newItemStack?.clone()?.apply { amount = 1 })
    }
    
    private inner class SwitchModeItem : BaseItem() {
        
        override fun getItemProvider(): ItemProvider =
            if (itemFilter.whitelist) GUIMaterials.WHITELIST_BTN.createBasicItemBuilder().setLocalizedName("menu.logistics.item_filter.whitelist")
            else GUIMaterials.BLACKLIST_BTN.createBasicItemBuilder().setLocalizedName("menu.logistics.item_filter.blacklist")
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            itemFilter.whitelist = !itemFilter.whitelist
            notifyWindows()
        }
        
    }
    
    private inner class SwitchNBTItem : BaseItem() {
        
        override fun getItemProvider(): ItemProvider {
            return (if (itemFilter.nbt) GUIMaterials.NBT_BTN_ON else GUIMaterials.NBT_BTN_OFF)
                .createBasicItemBuilder().setLocalizedName("menu.logistics.item_filter.nbt." + if (itemFilter.nbt) "on" else "off")
        }
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            itemFilter.nbt = !itemFilter.nbt
            notifyWindows()
        }
        
    }
    
}