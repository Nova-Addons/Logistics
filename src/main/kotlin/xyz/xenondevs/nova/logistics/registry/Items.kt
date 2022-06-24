package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.logistics.item.AdvancedItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.BasicItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.EliteItemFilterBehavior
import xyz.xenondevs.nova.logistics.item.UltimateItemFilterBehavior
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem

object Items {
    
    val BASIC_ITEM_FILTER = registerDefaultItem(Logistics, "basic_item_filter", BasicItemFilterBehavior)
    val ADVANCED_ITEM_FILTER = registerDefaultItem(Logistics, "advanced_item_filter", AdvancedItemFilterBehavior)
    val ELITE_ITEM_FILTER = registerDefaultItem(Logistics, "elite_item_filter", EliteItemFilterBehavior)
    val ULTIMATE_ITEM_FILTER = registerDefaultItem(Logistics, "ultimate_item_filter", UltimateItemFilterBehavior)
    
    fun init() = Unit
    
}