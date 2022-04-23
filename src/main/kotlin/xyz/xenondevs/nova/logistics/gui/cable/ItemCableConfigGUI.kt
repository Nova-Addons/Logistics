package xyz.xenondevs.nova.logistics.gui.cable

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.SlotElement.VISlotElement
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.virtualinventory.VirtualInventory
import de.studiocode.invui.virtualinventory.event.ItemUpdateEvent
import org.bukkit.block.BlockFace
import xyz.xenondevs.nova.logistics.item.getLogisticsItemFilterConfig
import xyz.xenondevs.nova.logistics.item.isItemFilter
import xyz.xenondevs.nova.logistics.registry.GUIMaterials
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.NetworkManager
import xyz.xenondevs.nova.tileentity.network.item.ItemNetwork
import xyz.xenondevs.nova.tileentity.network.item.holder.ItemHolder
import xyz.xenondevs.nova.ui.item.AddNumberItem
import xyz.xenondevs.nova.ui.item.DisplayNumberItem
import xyz.xenondevs.nova.ui.item.RemoveNumberItem
import xyz.xenondevs.nova.util.item.novaMaterial
import xyz.xenondevs.nova.util.putOrRemove

class ItemCableConfigGUI(
    val itemHolder: ItemHolder,
    private val face: BlockFace
) : BaseCableConfigGUI(ItemNetwork.CHANNEL_AMOUNT) {
    
    val gui: GUI
    private val insertFilterInventory = VirtualInventory(null, 1, arrayOfNulls(1), intArrayOf(1))
        .apply { setItemUpdateHandler(::checkItem) }
    private val extractFilterInventory = VirtualInventory(null, 1, arrayOfNulls(1), intArrayOf(1))
        .apply { setItemUpdateHandler(::checkItem) }
    
    init {
        updateValues(false)
        
        gui = GUIBuilder(GUIType.NORMAL)
            .setStructure(
                "# p # # c # # P #",
                "# d # e # i # D #",
                "# m # E # I # M #")
            .addIngredient('i', InsertItem().also(updatableItems::add))
            .addIngredient('e', ExtractItem().also(updatableItems::add))
            .addIngredient('I', VISlotElement(insertFilterInventory, 0, GUIMaterials.ITEM_FILTER_PLACEHOLDER.createBasicItemBuilder()))
            .addIngredient('E', VISlotElement(extractFilterInventory, 0, GUIMaterials.ITEM_FILTER_PLACEHOLDER.createBasicItemBuilder()))
            .addIngredient('P', AddNumberItem({ 0..100 }, { insertPriority }, { insertPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('M', RemoveNumberItem({ 0..100 }, { insertPriority }, { insertPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('D', DisplayNumberItem({ insertPriority }, "menu.logistics.cable_config.insert_priority").also(updatableItems::add))
            .addIngredient('p', AddNumberItem({ 0..100 }, { extractPriority }, { extractPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('m', RemoveNumberItem({ 0..100 }, { extractPriority }, { extractPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('d', DisplayNumberItem({ extractPriority }, "menu.logistics.cable_config.extract_priority").also(updatableItems::add))
            .addIngredient('c', SwitchChannelItem().also(updatableItems::add))
            .build()
    }
    
    override fun updateValues(updateButtons: Boolean) {
        NetworkManager.execute { // TODO: queueSync / queueAsync ?
            val allowedConnections = itemHolder.allowedConnectionTypes[itemHolder.inventories[face]]!!
            allowsExtract = allowedConnections.extract
            allowsInsert = allowedConnections.insert
            
            insertPriority = itemHolder.insertPriorities[face]!!
            extractPriority = itemHolder.extractPriorities[face]!!
            insertState = itemHolder.connectionConfig[face]!!.insert
            extractState = itemHolder.connectionConfig[face]!!.extract
            channel = itemHolder.channels[face]!!
            
            insertFilterInventory.setItemStackSilently(0, itemHolder.insertFilters[face]?.createFilterItem())
            extractFilterInventory.setItemStackSilently(0, itemHolder.extractFilters[face]?.createFilterItem())
        }
        
        if (updateButtons) updateButtons()
    }
    
    override fun writeChanges() {
        itemHolder.insertPriorities[face] = insertPriority
        itemHolder.extractPriorities[face] = extractPriority
        itemHolder.channels[face] = channel
        itemHolder.connectionConfig[face] = NetworkConnectionType.of(insertState, extractState)
        itemHolder.insertFilters.putOrRemove(face, insertFilterInventory.getUnsafeItemStack(0)?.getLogisticsItemFilterConfig())
        itemHolder.extractFilters.putOrRemove(face, extractFilterInventory.getUnsafeItemStack(0)?.getLogisticsItemFilterConfig())
    }
    
    private fun checkItem(event: ItemUpdateEvent) {
        val newStack = event.newItemStack
        event.isCancelled = newStack != null && !newStack.novaMaterial.isItemFilter()
    }
    
}