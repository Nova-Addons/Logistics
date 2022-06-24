package xyz.xenondevs.nova.logistics

import xyz.xenondevs.nova.addon.Addon
import xyz.xenondevs.nova.logistics.advancement.Advancements
import xyz.xenondevs.nova.logistics.item.findCorrectFilterMaterial
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.logistics.registry.Items
import xyz.xenondevs.nova.tileentity.network.item.ItemFilter
import xyz.xenondevs.nova.tileentity.network.item.saveFilterConfig
import java.util.logging.Logger

lateinit var LOGGER: Logger

object Logistics : Addon() {
    
    override fun init() {
        LOGGER = logger
        
        Blocks.init()
        Items.init()
        Advancements.register()
        
        ItemFilter.creatorFun = { filter -> findCorrectFilterMaterial(filter).createItemStack().apply { saveFilterConfig(filter) } }
    }
    
    override fun onEnable() = Unit
    override fun onDisable() = Unit
    
}