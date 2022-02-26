package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.item.AdvancedItemFilter
import xyz.xenondevs.nova.logistics.item.BasicItemFilter
import xyz.xenondevs.nova.logistics.item.EliteItemFilter
import xyz.xenondevs.nova.logistics.item.UltimateItemFilter
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem

object Items {
    
    val BASIC_ITEM_FILTER = registerDefaultItem(LOGISTICS, "basic_item_filter", BasicItemFilter)
    val ADVANCED_ITEM_FILTER = registerDefaultItem(LOGISTICS, "advanced_item_filter", AdvancedItemFilter)
    val ELITE_ITEM_FILTER = registerDefaultItem(LOGISTICS, "elite_item_filter", EliteItemFilter)
    val ULTIMATE_ITEM_FILTER = registerDefaultItem(LOGISTICS, "ultimate_item_filter", UltimateItemFilter)
    
    fun init() = Unit
    
}