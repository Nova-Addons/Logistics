package xyz.xenondevs.nova.logistics.item

import de.studiocode.invui.item.builder.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.item.NovaItem
import xyz.xenondevs.nova.logistics.gui.itemfilter.ItemFilterWindow
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig

object FilterItem : NovaItem() {
    
    override fun handleInteract(player: Player, itemStack: ItemStack, action: Action, event: PlayerInteractEvent) {
        if (action == Action.RIGHT_CLICK_AIR) {
            event.isCancelled = true
            ItemFilterWindow(player, itemStack)
        }
    }
    
    override fun modifyItemBuilder(itemBuilder: ItemBuilder): ItemBuilder =
        itemBuilder.addModifier {
            it.saveFilterConfig(ItemFilter())
            return@addModifier it
        }
    
}