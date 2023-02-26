package xyz.xenondevs.nova.logistics.tileentity

import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.BaseItem
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.Reloadable
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.logistics.registry.Blocks.FLUID_STORAGE_UNIT
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.fluid.FluidType
import xyz.xenondevs.nova.tileentity.network.fluid.holder.NovaFluidHolder
import xyz.xenondevs.nova.ui.FluidBar
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGui
import xyz.xenondevs.nova.util.center
import xyz.xenondevs.nova.world.fakeentity.impl.FakeArmorStand

private val MAX_CAPACITY = configReloadable { NovaConfig[FLUID_STORAGE_UNIT].getLong("max_capacity") }

class FluidStorageUnit(blockState: NovaTileEntityState) : NetworkedTileEntity(blockState), Reloadable {
    
    override val gui = lazy(::FluidStorageUnitGui)
    private val fluidTank = getFluidContainer("fluid", setOf(FluidType.LAVA, FluidType.WATER), MAX_CAPACITY, 0, ::handleFluidUpdate)
    private val fluidLevel = FakeArmorStand(pos.location.center()) { _, data -> data.isInvisible = true; data.isMarker = true }
    override val fluidHolder = NovaFluidHolder(this, fluidTank to NetworkConnectionType.BUFFER) { createSideConfig(NetworkConnectionType.BUFFER) }
    
    init {
        handleFluidUpdate()
    }
    
    private fun handleFluidUpdate() {
        val stack = if (fluidTank.hasFluid()) {
            when (fluidTank.type) {
                FluidType.LAVA -> Blocks.TANK_LAVA_LEVELS
                FluidType.WATER -> Blocks.TANK_WATER_LEVELS
                else -> throw IllegalStateException()
            }.clientsideProviders[10].get()
        } else null
        
        fluidLevel.setEquipment(EquipmentSlot.HEAD, stack, true)
    }
    
    override fun handleRemoved(unload: Boolean) {
        super.handleRemoved(unload)
        fluidLevel.remove()
    }
    
    inner class FluidStorageUnitGui : TileEntityGui() {
        
        private val sideConfigGui = SideConfigGui(
            this@FluidStorageUnit,
            fluidContainerNames = listOf(fluidTank to "container.nova.fluid_tank"),
            openPrevious = ::openWindow
        )
        
        override val gui = Gui.normal()
            .setStructure(
                "1 - - - - - - - 2",
                "| s # # # # # f |",
                "| # # # d # # f |",
                "| # # # # # # f |",
                "3 - - - - - - - 4")
            .addIngredient('s', OpenSideConfigItem(sideConfigGui))
            .addIngredient('d', FluidStorageUnitDisplay())
            .addIngredient('f', FluidBar(3, fluidHolder, fluidTank))
            .build()
        
        private inner class FluidStorageUnitDisplay : BaseItem() {
            
            init {
                fluidTank.updateHandlers += { notifyWindows() }
            }
            
            override fun getItemProvider(): ItemProvider {
                val type = fluidTank.type?.bucket
                    ?: return ItemBuilder(Material.BARRIER).setDisplayName("§r")
                val amount = fluidTank.amount
                return ItemBuilder(type).setDisplayName("§a$amount §7mB").setAmount(1)
            }
            
            override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) = Unit
            
        }
    }
    
}