package xyz.xenondevs.nova.logistics.tileentity

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.Reloadable
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.fluid.FluidType
import xyz.xenondevs.nova.tileentity.network.fluid.holder.NovaFluidHolder
import xyz.xenondevs.nova.ui.FluidBar
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGUI
import xyz.xenondevs.nova.util.center
import xyz.xenondevs.nova.world.armorstand.FakeArmorStand
import kotlin.math.roundToInt
import net.minecraft.world.entity.EquipmentSlot as NMSEquipmentSlot

private const val MAX_STATE = 99

@Suppress("LeakingThis")
open class FluidTank(
    capacity: Long,
    blockState: NovaTileEntityState,
    val onReload: (FluidTank) -> Unit = {}
) : NetworkedTileEntity(blockState), Reloadable {
    
    override val gui = lazy(::FluidTankGUI)
    
    val fluidContainer = getFluidContainer("tank", hashSetOf(FluidType.WATER, FluidType.LAVA), capacity, 0, ::handleFluidUpdate)
    override val fluidHolder = NovaFluidHolder(this, fluidContainer to NetworkConnectionType.BUFFER) { createSideConfig(NetworkConnectionType.BUFFER) }
    private lateinit var fluidLevel: FakeArmorStand
    
    init {
        NovaConfig.reloadables.add(this)
    }
    
    override fun reload() {
        onReload(this)
        updateFluidLevel()
    }
    
    override fun handleInitialized(first: Boolean) {
        super.handleInitialized(first)
        fluidLevel = FakeArmorStand(pos.location.center()) { _, data -> data.invisible = true; data.marker = true }
        updateFluidLevel()
    }
    
    private fun handleFluidUpdate() {
        // Creative Fluid Tank
        if (fluidContainer.capacity == Long.MAX_VALUE && fluidContainer.hasFluid() && !fluidContainer.isFull())
            fluidContainer.addFluid(fluidContainer.type!!, fluidContainer.capacity - fluidContainer.amount)
        
        updateFluidLevel()
    }
    
    fun updateFluidLevel() {
        val stack = if (fluidContainer.hasFluid()) {
            val state = (fluidContainer.amount.toDouble() / fluidContainer.capacity.toDouble() * MAX_STATE.toDouble()).roundToInt()
            when (fluidContainer.type) {
                FluidType.LAVA -> Blocks.TANK_LAVA_LEVELS
                FluidType.WATER -> Blocks.TANK_WATER_LEVELS
                else -> throw IllegalStateException()
            }.item.createClientsideItemStack(state)
        } else null
        
        val shouldGlow = fluidContainer.type == FluidType.LAVA
        fluidLevel.updateEntityData(true) { onFire = shouldGlow }
        fluidLevel.setEquipment(NMSEquipmentSlot.HEAD, stack, true)
    }
    
    override fun handleRemoved(unload: Boolean) {
        super.handleRemoved(unload)
        fluidLevel.remove()
    }
    
    inner class FluidTankGUI : TileEntityGUI() {
        
        private val sideConfigGUI = SideConfigGUI(
            this@FluidTank,
            fluidContainerNames = listOf(fluidContainer to "container.nova.fluid_tank"),
            openPrevious = ::openWindow
        )
        
        override val gui: GUI = GUIBuilder(GUIType.NORMAL)
            .setStructure(
                "1 - - - - - - - 2",
                "| s # # f # # # |",
                "| # # # f # # # |",
                "| # # # f # # # |",
                "3 - - - - - - - 4")
            .addIngredient('s', OpenSideConfigItem(sideConfigGUI))
            .addIngredient('f', FluidBar(3, fluidHolder, fluidContainer))
            .build()
        
    }
    
}

private val BASIC_CAPACITY by configReloadable { NovaConfig[Blocks.BASIC_FLUID_TANK].getLong("capacity") }
private val ADVANCED_CAPACITY by configReloadable { NovaConfig[Blocks.ADVANCED_FLUID_TANK].getLong("capacity") }
private val ELITE_CAPACITY by configReloadable { NovaConfig[Blocks.ELITE_FLUID_TANK].getLong("capacity") }
private val ULTIMATE_CAPACITY by configReloadable { NovaConfig[Blocks.ULTIMATE_FLUID_TANK].getLong("capacity") }

class BasicFluidTank(blockState: NovaTileEntityState) : FluidTank(BASIC_CAPACITY, blockState, { it.fluidContainer.capacity = BASIC_CAPACITY })

class AdvancedFluidTank(blockState: NovaTileEntityState) : FluidTank(ADVANCED_CAPACITY, blockState, { it.fluidContainer.capacity = ADVANCED_CAPACITY })

class EliteFluidTank(blockState: NovaTileEntityState) : FluidTank(ELITE_CAPACITY, blockState, { it.fluidContainer.capacity = ELITE_CAPACITY })

class UltimateFluidTank(blockState: NovaTileEntityState) : FluidTank(ULTIMATE_CAPACITY, blockState, { it.fluidContainer.capacity = ULTIMATE_CAPACITY })

class CreativeFluidTank(blockState: NovaTileEntityState) : FluidTank(Long.MAX_VALUE, blockState)