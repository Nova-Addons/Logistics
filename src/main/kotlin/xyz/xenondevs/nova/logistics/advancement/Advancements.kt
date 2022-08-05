package xyz.xenondevs.nova.logistics.advancement

import net.md_5.bungee.api.chat.TranslatableComponent
import xyz.xenondevs.nmsutils.advancement.AdvancementLoader
import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.util.advancement
import xyz.xenondevs.nova.util.obtainNovaItemAdvancement

object Advancements {
    
    private val ROOT = advancement(Logistics, "root") {
        display {
            icon(Blocks.ULTIMATE_CABLE.clientsideProvider.get())
            title(TranslatableComponent("advancement.logistics.root.title"))
            description("")
            background("textures/block/tuff.png")
            
            showToast(false)
            announceToChat(false)
        }
        
        criteria { tick("tick") {} }
    }
    
    //<editor-fold desc="Cables" defaultstate="collapsed">
    private val BASIC_CABLE = obtainNovaItemAdvancement(Logistics, ROOT, Blocks.BASIC_CABLE)
    private val ADVANCED_CABLE = obtainNovaItemAdvancement(Logistics, BASIC_CABLE, Blocks.ADVANCED_CABLE)
    private val ELITE_CABLE = obtainNovaItemAdvancement(Logistics, ADVANCED_CABLE, Blocks.ELITE_CABLE)
    private val ULTIMATE_CABLE = obtainNovaItemAdvancement(Logistics, ELITE_CABLE, Blocks.ULTIMATE_CABLE)
    //</editor-fold>
    
    //<editor-fold desc="Power Cells" defaultstate="collapsed">
    private val BASIC_POWER_CELL = obtainNovaItemAdvancement(Logistics, ROOT, Blocks.BASIC_POWER_CELL)
    private val ADVANCED_POWER_CELL = obtainNovaItemAdvancement(Logistics, BASIC_POWER_CELL, Blocks.ADVANCED_POWER_CELL)
    private val ELITE_POWER_CELL = obtainNovaItemAdvancement(Logistics, ADVANCED_POWER_CELL, Blocks.ELITE_POWER_CELL)
    private val ULTIMATE_POWER_CELL = obtainNovaItemAdvancement(Logistics, ELITE_POWER_CELL, Blocks.ULTIMATE_POWER_CELL)
    //</editor-fold>
    
    //<editor-fold desc="Fluid Storage" defaultstate="collapsed">
    private val BASIC_FLUID_TANK = obtainNovaItemAdvancement(Logistics, ROOT, Blocks.BASIC_FLUID_TANK)
    private val ADVANCED_FLUID_TANK = obtainNovaItemAdvancement(Logistics, BASIC_FLUID_TANK, Blocks.ADVANCED_FLUID_TANK)
    private val ELITE_FLUID_TANK = obtainNovaItemAdvancement(Logistics, ADVANCED_FLUID_TANK, Blocks.ELITE_FLUID_TANK)
    private val ULTIMATE_FLUID_TANK = obtainNovaItemAdvancement(Logistics, ELITE_FLUID_TANK, Blocks.ULTIMATE_FLUID_TANK)
    private val FLUID_STORAGE_UNIT = obtainNovaItemAdvancement(Logistics, ULTIMATE_FLUID_TANK, Blocks.FLUID_STORAGE_UNIT)
    //</editor-fold>
    
    //<editor-fold desc="Items" defaultstate="collapsed">
    private val TRASH_CAN = obtainNovaItemAdvancement(Logistics, ROOT, Blocks.TRASH_CAN)
    private val VACUUM_CHEST = obtainNovaItemAdvancement(Logistics, TRASH_CAN, Blocks.VACUUM_CHEST)
    private val STORAGE_UNIT = obtainNovaItemAdvancement(Logistics, VACUUM_CHEST, Blocks.STORAGE_UNIT)
    //</editor-fold>
    
    fun register() {
        AdvancementLoader.registerAdvancements(
            ROOT, BASIC_CABLE, ADVANCED_CABLE, ELITE_CABLE, ULTIMATE_CABLE, BASIC_POWER_CELL, ADVANCED_POWER_CELL,
            ELITE_POWER_CELL, ULTIMATE_POWER_CELL, BASIC_FLUID_TANK, ADVANCED_FLUID_TANK, ELITE_FLUID_TANK,
            ULTIMATE_FLUID_TANK, FLUID_STORAGE_UNIT, TRASH_CAN, VACUUM_CHEST, STORAGE_UNIT
        )
    }
    
}