package xyz.xenondevs.nova.logistics.item

import de.studiocode.invui.item.builder.ItemBuilder
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.data.config.NovaConfig
import xyz.xenondevs.nova.item.NovaItem
import xyz.xenondevs.nova.item.behavior.ItemBehavior
import xyz.xenondevs.nova.logistics.gui.itemfilter.ItemFilterWindow
import xyz.xenondevs.nova.logistics.registry.Items
import xyz.xenondevs.nova.material.ItemNovaMaterial
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.getOrCreateFilterConfig
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
import xyz.xenondevs.nova.util.item.novaMaterial

private val FILTER_MATERIALS = hashMapOf(
    BasicItemFilter.size to Items.BASIC_ITEM_FILTER,
    AdvancedItemFilter.size to Items.ADVANCED_ITEM_FILTER,
    EliteItemFilter.size to Items.ELITE_ITEM_FILTER,
    UltimateItemFilter.size to Items.ULTIMATE_ITEM_FILTER
)

fun ItemStack.getLogisticsItemFilterConfig(): ItemFilter? {
    return (this.novaMaterial?.novaItem as? FilterItem)?.getFilterConfig(this)
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

abstract class FilterItem(size: Lazy<Int>) : NovaItem() {
    
    val size by size
    
    init {
        behaviors += ItemFilterBehavior()
    }
    
    fun getFilterConfig(itemStack: ItemStack): ItemFilter =
        itemStack.getOrCreateFilterConfig(size)
    
    private inner class ItemFilterBehavior : ItemBehavior() {
    
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
        
    }
    
}

object BasicItemFilter : FilterItem(lazy { NovaConfig[Items.BASIC_ITEM_FILTER].getInt("size")!! })
object AdvancedItemFilter : FilterItem(lazy { NovaConfig[Items.ADVANCED_ITEM_FILTER].getInt("size")!! })
object EliteItemFilter : FilterItem(lazy { NovaConfig[Items.ELITE_ITEM_FILTER].getInt("size")!! })
object UltimateItemFilter : FilterItem(lazy { NovaConfig[Items.ULTIMATE_ITEM_FILTER].getInt("size")!! })