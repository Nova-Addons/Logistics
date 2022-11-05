package xyz.xenondevs.nova.logistics.tileentity

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.Reloadable
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.config.notReloadable
import xyz.xenondevs.nova.data.provider.Provider
import xyz.xenondevs.nova.data.world.block.state.NovaTileEntityState
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.tileentity.NetworkedTileEntity
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType.BUFFER
import xyz.xenondevs.nova.tileentity.network.energy.holder.BufferEnergyHolder
import xyz.xenondevs.nova.ui.EnergyBar
import xyz.xenondevs.nova.ui.config.side.OpenSideConfigItem
import xyz.xenondevs.nova.ui.config.side.SideConfigGUI

class PowerCell(
    creative: Boolean,
    maxEnergy: Provider<Long>,
    blockState: NovaTileEntityState
) : NetworkedTileEntity(blockState), Reloadable {
    
    override val energyHolder = BufferEnergyHolder(this, maxEnergy, creative) { createSideConfig(BUFFER) }
    
    override val gui = lazy(::PowerCellGUI)
    
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

private val BASIC_CAPACITY: Provider<Long> = configReloadable { NovaConfig[Blocks.BASIC_POWER_CELL].getLong("capacity") }
private val ADVANCED_CAPACITY: Provider<Long> = configReloadable { NovaConfig[Blocks.ADVANCED_POWER_CELL].getLong("capacity") }
private val ELITE_CAPACITY: Provider<Long> = configReloadable { NovaConfig[Blocks.ELITE_POWER_CELL].getLong("capacity") }
private val ULTIMATE_CAPACITY: Provider<Long> = configReloadable { NovaConfig[Blocks.ULTIMATE_POWER_CELL].getLong("capacity") }

fun createBasicPowerCell(blockState: NovaTileEntityState) = PowerCell(
    false,
    BASIC_CAPACITY,
    blockState
)

fun createAdvancedPowerCell(blockState: NovaTileEntityState) = PowerCell(
    false,
    ADVANCED_CAPACITY,
    blockState
)

fun createElitePowerCell(blockState: NovaTileEntityState) = PowerCell(
    false,
    ELITE_CAPACITY,
    blockState
)

fun createUltimatePowerCell(blockState: NovaTileEntityState) = PowerCell(
    false,
    ULTIMATE_CAPACITY,
    blockState
)

fun createCreativePowerCell(blockState: NovaTileEntityState) = PowerCell(
    true,
    notReloadable(Long.MAX_VALUE),
    blockState
)
