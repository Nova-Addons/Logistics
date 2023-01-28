package xyz.xenondevs.nova.logistics.tileentity

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.SlotElement.VISlotElement
import xyz.xenondevs.invui.gui.builder.GuiBuilder
import xyz.xenondevs.invui.gui.builder.guitype.GuiType
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.BaseItem
import xyz.xenondevs.invui.virtualinventory.VirtualInventory
import xyz.xenondevs.invui.virtualinventory.event.ItemUpdateEvent
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.logistics.registry.Blocks.STORAGE_UNIT
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.item.holder.NovaItemHolder
import xyz.xenondevs.nova.tileentity.network.item.inventory.NetworkedInventory
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGui
import xyz.xenondevs.nova.util.data.localized
import xyz.xenondevs.nova.util.item.takeUnlessEmpty
import xyz.xenondevs.nova.util.runTaskLater
import kotlin.math.min

private val MAX_ITEMS by configReloadable { NovaConfig[STORAGE_UNIT].getInt("max_items") }

class StorageUnit(blockState: NovaTileEntityState) : NetworkedTileEntity(blockState) {
    
    override val gui = lazy { StorageUnitGui() }
    private val inventory = StorageUnitInventory(retrieveDataOrNull("type"), retrieveDataOrNull("amount") ?: 0)
    private val inputInventory = VirtualInventory(null, 1).apply { setItemUpdateHandler(::handleInputInventoryUpdate) }
    private val outputInventory = VirtualInventory(null, 1).apply { setItemUpdateHandler(::handleOutputInventoryUpdate) }
    override val itemHolder = NovaItemHolder(
        this,
        uuid to (inventory to NetworkConnectionType.BUFFER)
    ) { createSideConfig(NetworkConnectionType.BUFFER) }
    
    private fun handleInputInventoryUpdate(event: ItemUpdateEvent) {
        if (event.isAdd && inventory.type != null && !inventory.type!!.isSimilar(event.newItemStack))
            event.isCancelled = true
    }
    
    private fun handleOutputInventoryUpdate(event: ItemUpdateEvent) {
        if (event.updateReason == SELF_UPDATE_REASON)
            return
        
        if (event.isAdd || event.isSwap) {
            event.isCancelled = true
        } else if (event.isRemove && inventory.type != null) {
            inventory.amount -= event.removedAmount
            if (inventory.amount == 0) inventory.type = null
            
            runTaskLater(1) { if (gui.isInitialized()) gui.value.update() }
        }
    }
    
    private fun updateOutputSlot() {
        if (inventory.type == null)
            outputInventory.setItemStack(SELF_UPDATE_REASON, 0, null)
        else
            outputInventory.setItemStack(SELF_UPDATE_REASON, 0, inventory.type!!.apply { amount = min(type.maxStackSize, inventory.amount) })
    }
    
    override fun handleTick() {
        val item = inputInventory.getItemStack(0)
        if (item != null) {
            val remaining = inventory.addItem(item)
            inputInventory.setItemStack(null, 0, item.apply { amount = remaining }.takeUnless { it.amount <= 0 })
        }
    }
    
    override fun saveData() {
        super.saveData()
        storeData("type", inventory.type, true)
        storeData("amount", inventory.amount, true)
    }
    
    inner class StorageUnitGui : TileEntityGui() {
        
        private val sideConfigGui = SideConfigGui(
            this@StorageUnit,
            listOf(inventory to "inventory.nova.default"),
            ::openWindow
        )
        
        private val storageUnitDisplay = StorageUnitDisplay()
        
        override val gui = GuiBuilder(GuiType.NORMAL)
            .setStructure(
                "1 - - - - - - - 2",
                "| # i # c # o s |",
                "3 - - - - - - - 4")
            .addIngredient('c', storageUnitDisplay)
            .addIngredient('i', VISlotElement(inputInventory, 0))
            .addIngredient('o', VISlotElement(outputInventory, 0))
            .addIngredient('s', OpenSideConfigItem(sideConfigGui))
            .build()
        
        init {
            update()
        }
        
        fun update() {
            storageUnitDisplay.notifyWindows()
            updateOutputSlot()
        }
        
        private inner class StorageUnitDisplay : BaseItem() {
            
            override fun getItemProvider(): ItemProvider {
                val type = inventory.type ?: return ItemBuilder(Material.BARRIER).setDisplayName("§r")
                val amount = inventory.amount
                val component = localized(ChatColor.GRAY,
                    "menu.logistics.storage_unit.item_display_" + if (amount > 1) "plural" else "singular",
                    TextComponent(amount.toString()).apply { color = ChatColor.GREEN }
                )
                return ItemBuilder(type).setDisplayName(component).setAmount(1)
            }
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) = Unit
            
        }
        
    }
    
    
    @Suppress("LiftReturnOrAssignment")
    inner class StorageUnitInventory(var type: ItemStack? = null, var amount: Int = 0) : NetworkedInventory {
        
        override val size: Int
            get() = 1
        
        override val items: Array<ItemStack?>
            get() {
                type ?: return emptyArray()
                return arrayOf(type!!.clone().also { it.amount = amount })
            }
        
        init {
            // Fix corrupted inventories
            if (type?.type?.isAir == true) type = null
        }
        
        override fun addItem(item: ItemStack): Int {
            val remaining: Int
            
            if (item.type.isAir) return 0
            
            if (type == null) { // Storage unit is empty
                type = item.clone()
                amount = item.amount
                remaining = 0
            } else if (type!!.isSimilar(item)) { // The item is the same as the one stored in the unit
                val leeway = MAX_ITEMS - amount
                if (leeway >= item.amount) { // The whole stack fits into the storage unit
                    amount += item.amount
                    remaining = 0
                } else remaining = item.amount - leeway  // Not all items fit so a few will remain
            } else remaining = item.amount // The item isn't the same as the one stored in the unit
            
            if (gui.isInitialized()) gui.value.update()
            return remaining
        }
        
        override fun setItem(slot: Int, item: ItemStack?): Boolean {
            amount = item?.takeUnlessEmpty()?.amount ?: 0
            type = if (amount != 0) item else null
            if (gui.isInitialized()) gui.value.update()
            
            return true
        }
        
        override fun decrementByOne(slot: Int) {
            if (amount > 1) {
                amount -= 1
            } else {
                amount = 0
                type = null
            }
            
            if (gui.isInitialized()) gui.value.update()
        }
        
        override fun isFull(): Boolean {
            return amount >= MAX_ITEMS
        }
        
    }
    
}