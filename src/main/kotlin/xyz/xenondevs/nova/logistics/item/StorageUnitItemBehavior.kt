package xyz.xenondevs.nova.logistics.item

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.cbf.Compound
import xyz.xenondevs.nova.data.serialization.cbf.NamespacedCompound
import xyz.xenondevs.nova.item.PacketItemData
import xyz.xenondevs.nova.item.behavior.ItemBehavior
import xyz.xenondevs.nova.tileentity.TileEntity
import xyz.xenondevs.nova.util.item.localizedName

object StorageUnitItemBehavior : ItemBehavior() {
    
    override fun updatePacketItemData(data: NamespacedCompound, itemData: PacketItemData) {
        val tileEntityData: Compound = data[TileEntity.TILE_ENTITY_DATA_KEY] ?: return
        val type: ItemStack = tileEntityData["type"] ?: return
        val amount: Int = tileEntityData["amount"] ?: return
        
        itemData.addLore(
            ComponentBuilder()
                .append("${amount}x ")
                .color(ChatColor.GRAY)
                .append(TranslatableComponent(type.localizedName))
                .create()
        )
    }
    
}