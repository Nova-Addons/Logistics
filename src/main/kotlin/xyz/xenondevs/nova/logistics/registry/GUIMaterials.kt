package xyz.xenondevs.nova.logistics.registry

import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerUnnamedHiddenItem

object GuiMaterials {
    
    val ITEM_FILTER_PLACEHOLDER = registerUnnamedHiddenItem(Logistics, "gui_item_filter_placeholder")
    val TRASH_CAN_PLACEHOLDER = registerUnnamedHiddenItem(Logistics, "gui_trash_can_placeholder")
    val NBT_BTN_OFF = registerUnnamedHiddenItem(Logistics, "gui_nbt_btn_off")
    val NBT_BTN_ON = registerUnnamedHiddenItem(Logistics, "gui_nbt_btn_on")
    val WHITELIST_BTN = registerUnnamedHiddenItem(Logistics, "gui_whitelist_btn")
    val BLACKLIST_BTN = registerUnnamedHiddenItem(Logistics, "gui_blacklist_btn")
    
}