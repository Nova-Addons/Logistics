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
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType.BUFFER
import xyz.xenondevs.nova.tileentity.network.energy.holder.BufferEnergyHolder
import xyz.xenondevs.nova.ui.EnergyBar
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGUI

@Suppress("LeakingThis")
open class PowerCell(
    creative: Boolean,
    maxEnergy: Long,
    blockState: NovaTileEntityState,
    val onReload: (PowerCell) -> Unit = {}
) : NetworkedTileEntity(blockState), Reloadable {
    
    final override val energyHolder = BufferEnergyHolder(this, maxEnergy, creative) { createSideConfig(BUFFER) }
    
    override val gui = lazy(::PowerCellGUI)
    
    init {
        NovaConfig.reloadables.add(this)
    }
    
    override fun reload() {
        onReload(this)
    }
    
    override fun handleTick() = Unit
    
    inner class PowerCellGUI : TileEntityGUI() {
        
        private val sideConfigGUI = SideConfigGUI(this@PowerCell, ::openWindow)
        
        override val gui: GUI = GUIBuilder(GUIType.NORMAL)
            .setStructure(
                "1 - - - - - - - 2",
                "| s # # e # # # |",
                "| # # # e # # # |",
                "| # # # e # # # |",
                "3 - - - - - - - 4")
            .addIngredient('s', OpenSideConfigItem(sideConfigGUI))
            .addIngredient('e', EnergyBar(3, energyHolder))
            .build()
        
    }
    
}

//TODO: Make capacities reloadable
private val BASIC_CAPACITY by configReloadable { NovaConfig[Blocks.BASIC_POWER_CELL].getLong("capacity") }
private val ADVANCED_CAPACITY by configReloadable { NovaConfig[Blocks.ADVANCED_POWER_CELL].getLong("capacity") }
private val ELITE_CAPACITY by configReloadable { NovaConfig[Blocks.ELITE_POWER_CELL].getLong("capacity") }
private val ULTIMATE_CAPACITY by configReloadable { NovaConfig[Blocks.ULTIMATE_POWER_CELL].getLong("capacity") }

class BasicPowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    BASIC_CAPACITY,
    blockState,
    { it.energyHolder.defaultMaxEnergy = BASIC_CAPACITY }
)

class AdvancedPowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ADVANCED_CAPACITY,
    blockState,
    { it.energyHolder.defaultMaxEnergy = ADVANCED_CAPACITY }
)

class ElitePowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ELITE_CAPACITY,
    blockState,
    { it.energyHolder.defaultMaxEnergy = ELITE_CAPACITY }
)

class UltimatePowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ULTIMATE_CAPACITY,
    blockState,
    { it.energyHolder.defaultMaxEnergy = ULTIMATE_CAPACITY }
)

class CreativePowerCell(blockState: NovaTileEntityState) : PowerCell(
    true,
    Long.MAX_VALUE,
    blockState
)
