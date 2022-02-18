package xyz.xenondevs.nova.logistics.registry

import org.bukkit.Material.*
import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.tileentity.*
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultTileEntity
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerEnergyTileEntity
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerTileEntity

object Blocks {
    
    val BASIC_CABLE = registerTileEntity(LOGISTICS, "basic_cable", ::BasicCable, STRUCTURE_VOID, isDirectional = false)
    val ADVANCED_CABLE = registerTileEntity(LOGISTICS, "advanced_cable", ::AdvancedCable, STRUCTURE_VOID, isDirectional = false)
    val ELITE_CABLE = registerTileEntity(LOGISTICS, "elite_cable", ::EliteCable, STRUCTURE_VOID, isDirectional = false)
    val ULTIMATE_CABLE = registerTileEntity(LOGISTICS, "ultimate_cable", ::UltimateCable, STRUCTURE_VOID, isDirectional = false)
    val CREATIVE_CABLE = registerTileEntity(LOGISTICS, "creative_cable", ::CreativeCable, STRUCTURE_VOID, isDirectional = false)
    
    val BASIC_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "basic_power_cell", ::BasicPowerCell, IRON_BLOCK)
    val ADVANCED_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "advanced_power_cell", ::AdvancedPowerCell, IRON_BLOCK)
    val ELITE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "elite_power_cell", ::ElitePowerCell, IRON_BLOCK)
    val ULTIMATE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "ultimate_power_cell", ::UltimatePowerCell, IRON_BLOCK)
    val CREATIVE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "creative_power_cell", ::CreativePowerCell, IRON_BLOCK)
    
    val BASIC_FLUID_TANK = registerEnergyTileEntity(LOGISTICS, "basic_fluid_tank", ::BasicFluidTank, BARRIER)
    val ADVANCED_FLUID_TANK = registerEnergyTileEntity(LOGISTICS, "advanced_fluid_tank", ::AdvancedFluidTank, BARRIER)
    val ELITE_FLUID_TANK = registerEnergyTileEntity(LOGISTICS, "elite_fluid_tank", ::EliteFluidTank, BARRIER)
    val ULTIMATE_FLUID_TANK = registerEnergyTileEntity(LOGISTICS, "ultimate_fluid_tank", ::UltimateFluidTank, BARRIER)
    val CREATIVE_FLUID_TANK = registerEnergyTileEntity(LOGISTICS, "creative_fluid_tank", ::CreativeFluidTank, BARRIER)
    
    val STORAGE_UNIT = registerDefaultTileEntity(LOGISTICS, "storage_unit", ::StorageUnit, BARRIER)
    val FLUID_STORAGE_UNIT = registerDefaultTileEntity(LOGISTICS, "fluid_storage_unit", ::FluidStorageUnit, BARRIER)
    val VACUUM_CHEST = registerDefaultTileEntity(LOGISTICS, "vacuum_chest", ::VacuumChest, BARRIER)
    val TRASH_CAN = registerDefaultTileEntity(LOGISTICS, "trash_can", ::TrashCan, BARRIER)
    
    // Move these somewhere else?
    val TANK_WATER_LEVELS = registerDefaultItem(LOGISTICS, "tank_water_levels")
    val TANK_LAVA_LEVELS = registerDefaultItem(LOGISTICS, "tank_lava_levels")
    
    fun init() = Unit
    
}