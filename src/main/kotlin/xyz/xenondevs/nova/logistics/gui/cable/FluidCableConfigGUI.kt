package xyz.xenondevs.nova.logistics.gui.cable

import org.bukkit.block.BlockFace
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.builder.GuiBuilder
import xyz.xenondevs.invui.gui.builder.guitype.GuiType
import xyz.xenondevs.nova.tileentity.network.fluid.FluidNetwork
import xyz.xenondevs.nova.tileentity.network.fluid.holder.FluidHolder
import xyz.xenondevs.nova.ui.item.AddNumberItem
import xyz.xenondevs.nova.ui.item.DisplayNumberItem
import xyz.xenondevs.nova.ui.item.RemoveNumberItem

class FluidCableConfigGui(
     holder: FluidHolder,
     face: BlockFace
) : BaseCableConfigGui<FluidHolder>(holder, face, FluidNetwork.CHANNEL_AMOUNT) {
    
    val gui: Gui
    
    init {
        updateValues(false)
        
        gui = GuiBuilder(GuiType.NORMAL)
            .setStructure(
                "# p # # # # # P #",
                "# d # e c i # D #",
                "# m # # # # # M #")
            .addIngredient('i', InsertItem().also(updatableItems::add))
            .addIngredient('e', ExtractItem().also(updatableItems::add))
            .addIngredient('P', AddNumberItem({ 0..100 }, { insertPriority }, { insertPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('M', RemoveNumberItem({ 0..100 }, { insertPriority }, { insertPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('D', DisplayNumberItem({ insertPriority }, "menu.logistics.cable_config.insert_priority").also(updatableItems::add))
            .addIngredient('p', AddNumberItem({ 0..100 }, { extractPriority }, { extractPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('m', RemoveNumberItem({ 0..100 }, { extractPriority }, { extractPriority = it; updateButtons() }).also(updatableItems::add))
            .addIngredient('d', DisplayNumberItem({ extractPriority }, "menu.logistics.cable_config.extract_priority").also(updatableItems::add))
            .addIngredient('c', SwitchChannelItem().also(updatableItems::add))
            .build()
    }
    
    override fun updateValues(updateButtons: Boolean) {
        updateCoreValues()
        if (updateButtons) updateButtons()
    }
    
}