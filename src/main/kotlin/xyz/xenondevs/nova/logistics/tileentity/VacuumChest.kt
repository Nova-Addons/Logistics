package xyz.xenondevs.nova.logistics.tileentity

import org.bukkit.entity.Item
import xyz.xenondevs.invui.gui.SlotElement.VISlotElement
import xyz.xenondevs.invui.gui.builder.GuiBuilder
import xyz.xenondevs.invui.gui.builder.guitype.GuiType
import xyz.xenondevs.invui.virtualinventory.VirtualInventory
import xyz.xenondevs.invui.virtualinventory.event.ItemUpdateEvent
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.integration.protection.ProtectionManager
import xyz.xenondevs.nova.logistics.item.getItemFilterConfig
import xyz.xenondevs.nova.logistics.item.isItemFilter
import xyz.xenondevs.nova.logistics.registry.Blocks.VACUUM_CHEST
import xyz.xenondevs.nova.logistics.registry.GuiMaterials
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.holder.NovaItemHolder
import xyz.xenondevs.nova.tileentity.upgrade.Upgradable
import xyz.xenondevs.nova.ui.OpenUpgradesItem
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGui
import xyz.xenondevs.nova.util.item.novaMaterial
import xyz.xenondevs.nova.util.serverTick
import xyz.xenondevs.simpleupgrades.registry.UpgradeTypes

private val MIN_RANGE = configReloadable { NovaConfig[VACUUM_CHEST].getInt("range.min") }
private val MAX_RANGE = configReloadable { NovaConfig[VACUUM_CHEST].getInt("range.max") }
private val DEFAULT_RANGE by configReloadable { NovaConfig[VACUUM_CHEST].getInt("range.default") }

class VacuumChest(blockState: NovaTileEntityState) : NetworkedTileEntity(blockState), Upgradable {
    
    private val inventory: VirtualInventory = getInventory("inventory", 9)
    private val filterInventory: VirtualInventory = VirtualInventory(null, 1, arrayOfNulls(1), intArrayOf(1))
    override val itemHolder: NovaItemHolder = NovaItemHolder(
        this,
        inventory to NetworkConnectionType.BUFFER
    ) { createSideConfig(NetworkConnectionType.EXTRACT) }
    override val gui = lazy { VacuumChestGui() }
    override val upgradeHolder = getUpgradeHolder(UpgradeTypes.RANGE)
    
    private var filter: ItemFilter? by storedValue("itemFilter")
    private val region = getUpgradableRegion(UpgradeTypes.RANGE, MIN_RANGE, MAX_RANGE, DEFAULT_RANGE) { getSurroundingRegion(it) }
    
    private val items = ArrayList<Item>()
    
    init {
        filter?.let { filterInventory.setItemStack(SELF_UPDATE_REASON, 0, it.createFilterItem()) }
        
        filterInventory.setItemUpdateHandler(::handleFilterInventoryUpdate)
        filterInventory.guiShiftPriority = 1
    }
    
    override fun handleTick() {
        items
            .forEach {
                if (it.isValid) {
                    val itemStack = it.itemStack
                    val remaining = inventory.addItem(null, itemStack)
                    if (remaining != 0) it.itemStack = itemStack.apply { amount = remaining }
                    else it.remove()
                }
            }
        
        items.clear()
        
        if (serverTick % 10 == 0) {
            world.entities.forEach {
                if (it is Item
                    && it.location in region
                    && filter?.allowsItem(it.itemStack) != false
                    && ProtectionManager.canInteractWithEntity(this, it, null).get()
                ) {
                    items += it
                    it.velocity = location.clone().subtract(it.location).toVector()
                }
            }
        }
    }
    
    private fun handleFilterInventoryUpdate(event: ItemUpdateEvent) {
        val newStack = event.newItemStack
        if (newStack?.novaMaterial.isItemFilter())
            filter = newStack.getItemFilterConfig()
        else if (newStack != null) event.isCancelled = true
    }
    
    inner class VacuumChestGui : TileEntityGui() {
        
        private val sideConfigGui = SideConfigGui(
            this@VacuumChest,
            listOf(itemHolder.getNetworkedInventory(inventory) to "inventory.nova.default")
        ) { openWindow(it) }
        
        override val gui = GuiBuilder(GuiType.NORMAL)
            .setStructure(
                "1 - - - - - - - 2",
                "| s u # i i i p |",
                "| r # # i i i d |",
                "| f # # i i i m |",
                "3 - - - - - - - 4")
            .addIngredient('i', inventory)
            .addIngredient('s', OpenSideConfigItem(sideConfigGui))
            .addIngredient('u', OpenUpgradesItem(upgradeHolder))
            .addIngredient('r', region.visualizeRegionItem)
            .addIngredient('f', VISlotElement(filterInventory, 0, GuiMaterials.ITEM_FILTER_PLACEHOLDER.clientsideProvider))
            .addIngredient('p', region.increaseSizeItem)
            .addIngredient('m', region.decreaseSizeItem)
            .addIngredient('d', region.displaySizeItem)
            .build()
        
    }
    
}