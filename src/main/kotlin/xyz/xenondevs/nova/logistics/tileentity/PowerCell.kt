package xyz.xenondevs.nova.logistics.tileentity

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import xyz.xenondevs.nova.data.config.NovaConfig
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
    blockState: NovaTileEntityState
) : NetworkedTileEntity(blockState) {
    
    final override val energyHolder = BufferEnergyHolder(this, maxEnergy, creative) { createSideConfig(BUFFER) }
    
    override val gui = lazy(::PowerCellGUI)
    
    override fun handleTick() = Unit
    
    inner class PowerCellGUI : TileEntityGUI() {
        
        private val sideConfigGUI = SideConfigGUI(this@PowerCell, ::openWindow)
        
        override val gui: GUI = GUIBuilder(GUIType.NORMAL, 9, 5)
            .setStructure("" +
                "1 - - - - - - - 2" +
                "| s # # e # # # |" +
                "| # # # e # # # |" +
                "| # # # e # # # |" +
                "3 - - - - - - - 4")
            .addIngredient('s', OpenSideConfigItem(sideConfigGUI))
            .addIngredient('e', EnergyBar(3, energyHolder))
            .build()
        
    }
    
}

private val BASIC_CAPACITY = NovaConfig[Blocks.BASIC_POWER_CELL].getLong("capacity")!!
private val ADVANCED_CAPACITY = NovaConfig[Blocks.ADVANCED_POWER_CELL].getLong("capacity")!!
private val ELITE_CAPACITY = NovaConfig[Blocks.ELITE_POWER_CELL].getLong("capacity")!!
private val ULTIMATE_CAPACITY = NovaConfig[Blocks.ULTIMATE_POWER_CELL].getLong("capacity")!!

class BasicPowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    BASIC_CAPACITY,
    blockState
)

class AdvancedPowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ADVANCED_CAPACITY,
    blockState
)

class ElitePowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ELITE_CAPACITY,
    blockState
)

class UltimatePowerCell(blockState: NovaTileEntityState) : PowerCell(
    false,
    ULTIMATE_CAPACITY,
    blockState
)

class CreativePowerCell(blockState: NovaTileEntityState) : PowerCell(
    true,
    Long.MAX_VALUE,
    blockState
)
