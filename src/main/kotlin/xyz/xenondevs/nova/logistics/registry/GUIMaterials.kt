package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerItem

object GUIMaterials {
    
    val ITEM_FILTER_PLACEHOLDER = registerItem(LOGISTICS, "gui_item_filter_placeholder")
    val TRASH_CAN_PLACEHOLDER = registerItem(LOGISTICS, "gui_trash_can_placeholder")
    val NBT_BTN_OFF = registerItem(LOGISTICS, "gui_nbt_btn_off")
    val NBT_BTN_ON = registerItem(LOGISTICS, "gui_nbt_btn_on")
    val WHITELIST_BTN = registerItem(LOGISTICS, "gui_whitelist_btn")
    val BLACKLIST_BTN = registerItem(LOGISTICS, "gui_blacklist_btn")
    
}