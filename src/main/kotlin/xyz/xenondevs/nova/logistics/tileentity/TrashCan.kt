package xyz.xenondevs.nova.logistics.tileentity

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.SlotElement.VISlotElement
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import xyz.xenondevs.nova.data.serialization.cbf.element.CompoundElement
import xyz.xenondevs.nova.logistics.registry.GUIMaterials
import xyz.xenondevs.nova.material.NovaMaterial
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.fluid.FluidType
import xyz.xenondevs.nova.tileentity.network.fluid.container.FluidContainer
import xyz.xenondevs.nova.tileentity.network.fluid.holder.NovaFluidHolder
import xyz.xenondevs.nova.tileentity.network.item.holder.NovaItemHolder
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGUI
import xyz.xenondevs.nova.util.CUBE_FACES
import xyz.xenondevs.nova.util.VoidingVirtualInventory
import xyz.xenondevs.nova.util.associateWithToEnumMap
import xyz.xenondevs.nova.world.armorstand.FakeArmorStand
import java.util.*

private val ALL_INSERT_CONFIG = { CUBE_FACES.associateWithToEnumMap { NetworkConnectionType.INSERT } }

class TrashCan(
    uuid: UUID,
    data: CompoundElement,
    material: NovaMaterial,
    ownerUUID: UUID,
    armorStand: FakeArmorStand
) : NetworkedTileEntity(uuid, data, material, ownerUUID, armorStand) {
    
    private val inventory = VoidingVirtualInventory(1)
    override val gui: Lazy<TileEntityGUI> = lazy(::TrashCanGUI)
    override val itemHolder = NovaItemHolder(
        this,
        inventory to NetworkConnectionType.INSERT,
        defaultConnectionConfig = ALL_INSERT_CONFIG
    )
    override val fluidHolder = NovaFluidHolder(this,
        VoidingFluidContainer to NetworkConnectionType.INSERT,
        defaultConnectionConfig = ALL_INSERT_CONFIG
    )
    
    override fun handleTick() = Unit
    
    private inner class TrashCanGUI : TileEntityGUI() {
        
        private val sideConfigGUI = SideConfigGUI(
            this@TrashCan,
            null,
            listOf(itemHolder.getNetworkedInventory(inventory) to "inventory.nova.default"),
            listOf(VoidingFluidContainer to "container.nova.fluid_tank"),
            ::openWindow
        )
        
        override val gui: GUI = GUIBuilder(GUIType.NORMAL, 9, 3)
            .setStructure("" +
                "1 - - - - - - - 2" +
                "| s # # i # # # |" +
                "3 - - - - - - - 4")
            .addIngredient('i', VISlotElement(inventory, 0, GUIMaterials.TRASH_CAN_PLACEHOLDER.itemProvider))
            .addIngredient('s', OpenSideConfigItem(sideConfigGUI))
            .build()
        
    }
    
}

object VoidingFluidContainer : FluidContainer(UUID(0, 1L), hashSetOf(FluidType.WATER, FluidType.LAVA), FluidType.NONE, 0, Long.MAX_VALUE) {
    override fun addFluid(type: FluidType, amount: Long) = Unit
    override fun tryAddFluid(type: FluidType, amount: Long) = amount
    override fun takeFluid(amount: Long) = Unit
    override fun tryTakeFluid(amount: Long) = 0L
    override fun accepts(type: FluidType, amount: Long) = true
    override fun clear() = Unit
    override fun isFull() = false
    override fun hasFluid() = false
    override fun isEmpty() = true
}