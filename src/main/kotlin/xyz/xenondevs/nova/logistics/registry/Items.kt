package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.item.FilterItem
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem

object Items {
    
    val ITEM_FILTER = registerDefaultItem(LOGISTICS, "item_filter", FilterItem)
    
    fun init() = Unit
    
}