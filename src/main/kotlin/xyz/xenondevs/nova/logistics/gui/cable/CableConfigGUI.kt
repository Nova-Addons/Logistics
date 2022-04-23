package xyz.xenondevs.nova.logistics.gui.cable

import de.studiocode.invui.gui.GUI
import de.studiocode.invui.gui.builder.GUIBuilder
import de.studiocode.invui.gui.builder.guitype.GUIType
import de.studiocode.invui.window.impl.single.SimpleWindow
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import xyz.xenondevs.nova.material.CoreGUIMaterial
import xyz.xenondevs.nova.tileentity.network.NetworkEndPoint
import xyz.xenondevs.nova.tileentity.network.NetworkManager
import xyz.xenondevs.nova.tileentity.network.fluid.holder.FluidHolder
import xyz.xenondevs.nova.tileentity.network.item.holder.ItemHolder
import xyz.xenondevs.nova.ui.item.ClickyTabItem

class CableConfigGUI(
    val endPoint: NetworkEndPoint,
    val itemHolder: ItemHolder?,
    val fluidHolder: FluidHolder?,
    private val face: BlockFace
) {
    
    private val gui: GUI
    
    private val itemConfigGUI = itemHolder?.let { ItemCableConfigGUI(it, face) }
    private val fluidConfigGUI = fluidHolder?.let { FluidCableConfigGUI(it, face) }
    
    init {
        require(itemConfigGUI != null || fluidConfigGUI != null)
        
        gui = GUIBuilder(GUIType.TAB)
            .setStructure(
                "# # # i # f # # #" ,
                "- - - - - - - - -" ,
                "x x x x x x x x x" ,
                "x x x x x x x x x" ,
                "x x x x x x x x x"
            )
            .addIngredient('i', ClickyTabItem(0) {
                (if (itemConfigGUI != null) {
                    if (it.currentTab == 0)
                        CoreGUIMaterial.ITEM_BTN_SELECTED
                    else CoreGUIMaterial.ITEM_BTN_ON
                } else CoreGUIMaterial.ITEM_BTN_OFF).itemProvider
            })
            .addIngredient('f', ClickyTabItem(1) {
                (if (fluidConfigGUI != null) {
                    if (it.currentTab == 1)
                        CoreGUIMaterial.FLUID_BTN_SELECTED
                    else CoreGUIMaterial.FLUID_BTN_ON
                } else CoreGUIMaterial.FLUID_BTN_OFF).itemProvider
            })
            .setGUIs(listOf(itemConfigGUI?.gui, fluidConfigGUI?.gui))
            .build()
    }
    
    fun openWindow(player: Player) {
        SimpleWindow(player, arrayOf(TranslatableComponent("menu.logistics.cable_config")), gui)
            .also { it.addCloseHandler(::writeChanges) }
            .show()
    }
    
    fun closeForAllViewers() {
        gui.closeForAllViewers()
    }
    
    fun updateValues(updateButtons: Boolean = true) {
        itemConfigGUI?.updateValues(updateButtons)
        fluidConfigGUI?.updateValues(updateButtons)
    }
    
    private fun writeChanges() {
        NetworkManager.queueAsync {
            it.removeEndPoint(endPoint, false)
    
            itemConfigGUI?.writeChanges()
            fluidConfigGUI?.writeChanges()
            
            it.addEndPoint(endPoint, false).thenRun { endPoint.updateNearbyBridges() }
        }
    }
    
}