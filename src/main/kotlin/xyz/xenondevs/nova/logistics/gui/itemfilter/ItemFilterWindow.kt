package xyz.xenondevs.nova.logistics.gui.itemfilter

import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.impl.BaseItem
import de.studiocode.invui.virtualinventory.VirtualInventory
import de.studiocode.invui.virtualinventory.event.ItemUpdateEvent
import de.studiocode.invui.virtualinventory.event.UpdateReason
import de.studiocode.invui.window.impl.single.SimpleWindow
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.logistics.registry.GUIMaterials
import xyz.xenondevs.nova.tileentity.network.item.getOrCreateFilterConfig
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
import xyz.xenondevs.nova.util.data.setLocalizedName

class ItemFilterWindow(player: Player, private val itemStack: ItemStack) {
    
    private val itemFilter = itemStack.getOrCreateFilterConfig()
    private val filterInventory = object : VirtualInventory(null, 7, itemFilter.items, IntArray(7) { 1 }) {
        
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
    
    private val gui = GUIBuilder(GUIType.NORMAL, 9, 4)
        .setStructure("" +
            "1 - - - - - - - 2" +
            "| # # m # n # # |" +
            "| i i i i i i i |" +
            "3 - - - - - - - 4")
        .addIngredient('m', SwitchModeItem())
        .addIngredient('n', SwitchNBTItem())
        .addIngredient('i', filterInventory)
        .build()
    
    private val window = SimpleWindow(player, arrayOf(TranslatableComponent("menu.logistics.item_filter")), gui)
    
    init {
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