package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerUnnamedItem

object GUIMaterials {
    
    val ITEM_FILTER_PLACEHOLDER = registerUnnamedItem(Logistics, "gui_item_filter_placeholder")
    val TRASH_CAN_PLACEHOLDER = registerUnnamedItem(Logistics, "gui_trash_can_placeholder")
    val NBT_BTN_OFF = registerUnnamedItem(Logistics, "gui_nbt_btn_off")
    val NBT_BTN_ON = registerUnnamedItem(Logistics, "gui_nbt_btn_on")
    val WHITELIST_BTN = registerUnnamedItem(Logistics, "gui_whitelist_btn")
    val BLACKLIST_BTN = registerUnnamedItem(Logistics, "gui_blacklist_btn")
    
}