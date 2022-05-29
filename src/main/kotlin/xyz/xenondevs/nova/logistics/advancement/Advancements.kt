package xyz.xenondevs.nova.logistics.advancement

import net.md_5.bungee.api.chat.TranslatableComponent
import xyz.xenondevs.kadvancements.adapter.version.AdvancementLoader
import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.registry.Blocks
import xyz.xenondevs.nova.util.advancement
import xyz.xenondevs.nova.util.obtainNovaItemAdvancement

object Advancements {
    
    private val ROOT = advancement(LOGISTICS, "root") {
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
    private val BASIC_CABLE = obtainNovaItemAdvancement(LOGISTICS, ROOT, Blocks.BASIC_CABLE)
    private val ADVANCED_CABLE = obtainNovaItemAdvancement(LOGISTICS, BASIC_CABLE, Blocks.ADVANCED_CABLE)
    private val ELITE_CABLE = obtainNovaItemAdvancement(LOGISTICS, ADVANCED_CABLE, Blocks.ELITE_CABLE)
    private val ULTIMATE_CABLE = obtainNovaItemAdvancement(LOGISTICS, ELITE_CABLE, Blocks.ULTIMATE_CABLE)
    //</editor-fold>
    
    //<editor-fold desc="Power Cells" defaultstate="collapsed">
    private val BASIC_POWER_CELL = obtainNovaItemAdvancement(LOGISTICS, ROOT, Blocks.BASIC_POWER_CELL)
    private val ADVANCED_POWER_CELL = obtainNovaItemAdvancement(LOGISTICS, BASIC_POWER_CELL, Blocks.ADVANCED_POWER_CELL)
    private val ELITE_POWER_CELL = obtainNovaItemAdvancement(LOGISTICS, ADVANCED_POWER_CELL, Blocks.ELITE_POWER_CELL)
    private val ULTIMATE_POWER_CELL = obtainNovaItemAdvancement(LOGISTICS, ELITE_POWER_CELL, Blocks.ULTIMATE_POWER_CELL)
    //</editor-fold>
    
    //<editor-fold desc="Fluid Storage" defaultstate="collapsed">
    private val BASIC_FLUID_TANK = obtainNovaItemAdvancement(LOGISTICS, ROOT, Blocks.BASIC_FLUID_TANK)
    private val ADVANCED_FLUID_TANK = obtainNovaItemAdvancement(LOGISTICS, BASIC_FLUID_TANK, Blocks.ADVANCED_FLUID_TANK)
    private val ELITE_FLUID_TANK = obtainNovaItemAdvancement(LOGISTICS, ADVANCED_FLUID_TANK, Blocks.ELITE_FLUID_TANK)
    private val ULTIMATE_FLUID_TANK = obtainNovaItemAdvancement(LOGISTICS, ELITE_FLUID_TANK, Blocks.ULTIMATE_FLUID_TANK)
    private val FLUID_STORAGE_UNIT = obtainNovaItemAdvancement(LOGISTICS, ULTIMATE_FLUID_TANK, Blocks.FLUID_STORAGE_UNIT)
    //</editor-fold>
    
    //<editor-fold desc="Items" defaultstate="collapsed">
    private val TRASH_CAN = obtainNovaItemAdvancement(LOGISTICS, ROOT, Blocks.TRASH_CAN)
    private val VACUUM_CHEST = obtainNovaItemAdvancement(LOGISTICS, TRASH_CAN, Blocks.VACUUM_CHEST)
    private val STORAGE_UNIT = obtainNovaItemAdvancement(LOGISTICS, VACUUM_CHEST, Blocks.STORAGE_UNIT)
    //</editor-fold>
    
    fun register() {
        AdvancementLoader.registerAdvancements(
            ROOT, BASIC_CABLE, ADVANCED_CABLE, ELITE_CABLE, ULTIMATE_CABLE, BASIC_POWER_CELL, ADVANCED_POWER_CELL,
            ELITE_POWER_CELL, ULTIMATE_POWER_CELL, BASIC_FLUID_TANK, ADVANCED_FLUID_TANK, ELITE_FLUID_TANK,
            ULTIMATE_FLUID_TANK, FLUID_STORAGE_UNIT, TRASH_CAN, VACUUM_CHEST, STORAGE_UNIT
        )
    }
    
}