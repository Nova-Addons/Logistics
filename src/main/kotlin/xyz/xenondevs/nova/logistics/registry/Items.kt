package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.item.AdvancedItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.BasicItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.EliteItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.UltimateItemFilterBehavior
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem

object Items {
    
    val BASIC_ITEM_FILTER = registerDefaultItem(LOGISTICS, "basic_item_filter", BasicItemFilterBehavior)
    val ADVANCED_ITEM_FILTER = registerDefaultItem(LOGISTICS, "advanced_item_filter", AdvancedItemFilterBehavior)
    val ELITE_ITEM_FILTER = registerDefaultItem(LOGISTICS, "elite_item_filter", EliteItemFilterBehavior)
    val ULTIMATE_ITEM_FILTER = registerDefaultItem(LOGISTICS, "ultimate_item_filter", UltimateItemFilterBehavior)
    
    fun init() = Unit
    
}