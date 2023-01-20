package xyz.xenondevs.nova.logistics.item

import de.studiocode.invui.item.builder.ItemBuilder
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.data.config.configReloadable
import xyz.xenondevs.nova.data.provider.Provider
import xyz.xenondevs.nova.data.serialization.cbf.NamespacedCompound
import xyz.xenondevs.nova.item.PacketItemData
import xyz.xenondevs.nova.item.behavior.ItemBehavior
import xyz.xenondevs.nova.logistics.gui.itemfilter.ItemFilterWindow
import xyz.xenondevs.nova.logistics.registry.Items
import xyz.xenondevs.nova.material.ItemNovaMaterial
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.getOrCreateFilterConfig
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
import xyz.xenondevs.nova.util.data.localized
import xyz.xenondevs.nova.util.item.localizedName
import xyz.xenondevs.nova.util.item.novaMaterial

private val FILTER_MATERIALS = hashMapOf(
    BasicItemFilterBehavior.size to Items.BASIC_ITEM_FILTER,
    AdvancedItemFilterBehavior.size to Items.ADVANCED_ITEM_FILTER,
    EliteItemFilterBehavior.size to Items.ELITE_ITEM_FILTER,
    UltimateItemFilterBehavior.size to Items.ULTIMATE_ITEM_FILTER
)

fun ItemStack.getItemFilterConfig(): ItemFilter? {
    return (this.novaMaterial?.novaItem?.getBehavior(ItemFilterBehavior::class))
        ?.getFilterConfig(this)
}

fun ItemNovaMaterial?.isItemFilter(): Boolean {
    return this == Items.BASIC_ITEM_FILTER
        || this == Items.ADVANCED_ITEM_FILTER
        || this == Items.ELITE_ITEM_FILTER
        || this == Items.ULTIMATE_ITEM_FILTER
}

fun findCorrectFilterMaterial(itemFilter: ItemFilter): ItemNovaMaterial {
    return FILTER_MATERIALS[itemFilter.size] ?: Items.BASIC_ITEM_FILTER
}

abstract class ItemFilterBehavior(size: Provider<Int>) : ItemBehavior() {
    
    val size by size
    
    override fun handleInteract(player: Player, itemStack: ItemStack, action: Action, event: PlayerInteractEvent) {
        if (action == Action.RIGHT_CLICK_AIR) {
            event.isCancelled = true
            ItemFilterWindow(player, itemStack.novaMaterial!!, size, itemStack)
        }
    }
    
    override fun modifyItemBuilder(itemBuilder: ItemBuilder): ItemBuilder {
        return itemBuilder.addModifier {
            it.saveFilterConfig(ItemFilter(size))
            return@addModifier it
        }
    }
    
    fun getFilterConfig(itemStack: ItemStack): ItemFilter =
        itemStack.getOrCreateFilterConfig(size)
    
    override fun updatePacketItemData(data: NamespacedCompound, itemData: PacketItemData) {
        val filterConfig = data.get<ItemFilter>(ItemFilter.ITEM_FILTER_KEY) ?: return
        val lines = ArrayList<Array<BaseComponent>>()
        
        val whitelist = filterConfig.whitelist
        lines += arrayOf(localized(
            ChatColor.GRAY,
            "item.logistics.item_filter.lore.type",
            localized(
                if (whitelist) ChatColor.GREEN else ChatColor.RED,
                "item.logistics.item_filter.lore.type.${if (whitelist) "whitelist" else "blacklist"}"
            )
        ))
        
        val nbt = filterConfig.nbt
        lines += arrayOf(localized(
            ChatColor.GRAY,
            "item.logistics.item_filter.lore.nbt",
            localized(
                if (nbt) ChatColor.GREEN else ChatColor.RED,
                "item.logistics.item_filter.lore.nbt.${if (nbt) "on" else "off"}"
            )
        ))
        
        lines += arrayOf(TextComponent())
        
        lines += arrayOf(localized(ChatColor.GRAY, "item.logistics.item_filter.lore.contents", filterConfig.items.count { it != null }))
        filterConfig.items.filterNotNull().forEach {
            lines += ComponentBuilder("- ")
                .color(ChatColor.DARK_GRAY)
                .append(TranslatableComponent(it.localizedName ?: "Unknown Name"))
                .create()
        }
        
        itemData.addLore(lines)
    }
    
}

object BasicItemFilterBehavior : ItemFilterBehavior(configReloadable { NovaConfig[Items.BASIC_ITEM_FILTER].getInt("size") })
object AdvancedItemFilterBehavior : ItemFilterBehavior(configReloadable { NovaConfig[Items.ADVANCED_ITEM_FILTER].getInt("size") })
object EliteItemFilterBehavior : ItemFilterBehavior(configReloadable { NovaConfig[Items.ELITE_ITEM_FILTER].getInt("size") })
object UltimateItemFilterBehavior : ItemFilterBehavior(configReloadable { NovaConfig[Items.ULTIMATE_ITEM_FILTER].getInt("size") })