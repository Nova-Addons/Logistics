package xyz.xenondevs.nova.logistics.gui.cable

import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.builder.GuiBuilder
import xyz.xenondevs.invui.gui.builder.GuiType
import xyz.xenondevs.invui.window.builder.WindowType
import xyz.xenondevs.nova.material.CoreGuiMaterial
import xyz.xenondevs.nova.tileentity.network.NetworkEndPoint
import xyz.xenondevs.nova.tileentity.network.NetworkManager
import xyz.xenondevs.nova.tileentity.network.fluid.holder.FluidHolder
import xyz.xenondevs.nova.tileentity.network.item.holder.ItemHolder
import xyz.xenondevs.nova.ui.item.ClickyTabItem

class CableConfigGui(
    val endPoint: NetworkEndPoint,
    val itemHolder: ItemHolder?,
    val fluidHolder: FluidHolder?,
    private val face: BlockFace
) {
    
    private val gui: Gui
    
    private val itemConfigGui = itemHolder?.let { ItemCableConfigGui(it, face) }
    private val fluidConfigGui = fluidHolder?.let { FluidCableConfigGui(it, face) }
    
    init {
        require(itemConfigGui != null || fluidConfigGui != null)
        
        gui = GuiBuilder(GuiType.TAB)
            .setStructure(
                "# # # i # f # # #",
                "- - - - - - - - -",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x"
            )
            .addIngredient('i', ClickyTabItem(0) {
                (if (itemConfigGui != null) {
                    if (it.currentTab == 0)
                        CoreGuiMaterial.ITEM_BTN_SELECTED
                    else CoreGuiMaterial.ITEM_BTN_ON
                } else CoreGuiMaterial.ITEM_BTN_OFF).clientsideProvider
            })
            .addIngredient('f', ClickyTabItem(1) {
                (if (fluidConfigGui != null) {
                    if (it.currentTab == 1)
                        CoreGuiMaterial.FLUID_BTN_SELECTED
                    else CoreGuiMaterial.FLUID_BTN_ON
                } else CoreGuiMaterial.FLUID_BTN_OFF).clientsideProvider
            })
            .setContent(listOf(itemConfigGui?.gui, fluidConfigGui?.gui))
            .build()
    }
    
    fun openWindow(player: Player) {
        WindowType.NORMAL.createWindow {
            it.setViewer(player)
            it.setTitle(arrayOf(TranslatableComponent("menu.logistics.cable_config")))
            it.setGui(gui)
            it.addCloseHandler(::writeChanges)
        }.show()
    }
    
    fun closeForAllViewers() {
        gui.closeForAllViewers()
    }
    
    fun updateValues(updateButtons: Boolean = true) {
        itemConfigGui?.updateValues(updateButtons)
        fluidConfigGui?.updateValues(updateButtons)
    }
    
    private fun writeChanges() {
        NetworkManager.queueAsync {
            it.removeEndPoint(endPoint, false)
            
            itemConfigGui?.writeChanges()
            fluidConfigGui?.writeChanges()
            
            it.addEndPoint(endPoint, false).thenRun { endPoint.updateNearbyBridges() }
        }
    }
    
}