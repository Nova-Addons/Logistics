package xyz.xenondevs.nova.logistics.item

import de.studiocode.invui.item.builder.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.item.behavior.ItemBehavior
import xyz.xenondevs.nova.logistics.gui.itemfilter.ItemFilterWindow
import xyz.xenondevs.nova.logistics.registry.Items
import xyz.xenondevs.nova.material.ItemNovaMaterial
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.getOrCreateFilterConfig
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
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

abstract class ItemFilterBehavior(size: Lazy<Int>) : ItemBehavior() {
    
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
    
}

object BasicItemFilterBehavior : ItemFilterBehavior(lazy { NovaConfig[Items.BASIC_ITEM_FILTER].getInt("size")!! })
object AdvancedItemFilterBehavior : ItemFilterBehavior(lazy { NovaConfig[Items.ADVANCED_ITEM_FILTER].getInt("size")!! })
object EliteItemFilterBehavior : ItemFilterBehavior(lazy { NovaConfig[Items.ELITE_ITEM_FILTER].getInt("size")!! })
object UltimateItemFilterBehavior : ItemFilterBehavior(lazy { NovaConfig[Items.ULTIMATE_ITEM_FILTER].getInt("size")!! })