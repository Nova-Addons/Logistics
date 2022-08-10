package xyz.xenondevs.nova.logistics.item

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.cbf.Compound
import xyz.xenondevs.nova.data.serialization.persistentdata.get
import xyz.xenondevs.nova.item.behavior.ItemBehavior
import xyz.xenondevs.nova.tileentity.TileEntity
import xyz.xenondevs.nova.util.item.localizedName

object StorageUnitItemBehavior : ItemBehavior() {
    
    override fun getLore(itemStack: ItemStack): List<Array<BaseComponent>>? {
        val data: Compound = itemStack.itemMeta?.persistentDataContainer?.get(TileEntity.TILE_ENTITY_KEY) ?: return null
        val type = data.get<ItemStack>("type") ?: return null
        val amount = data.get<Int>("amount") ?: return null
        
        return listOf(
            ComponentBuilder()
                .append("${amount}x ")
                .color(ChatColor.GRAY)
                .append(TranslatableComponent(type.localizedName))
                .create()
        )
    }
    
}