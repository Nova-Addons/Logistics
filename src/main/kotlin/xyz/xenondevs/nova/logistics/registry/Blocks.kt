package xyz.xenondevs.nova.logistics.registry

import org.bukkit.Material.*
import xyz.xenondevs.nova.data.world.block.property.Directional
import xyz.xenondevs.nova.data.world.block.property.LegacyDirectional
import xyz.xenondevs.nova.item.tool.ToolCategory
import xyz.xenondevs.nova.item.tool.ToolLevel
import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.logistics.item.StorageUnitItemBehavior
import xyz.xenondevs.nova.logistics.tileentity.AdvancedCable
import xyz.xenondevs.nova.logistics.tileentity.AdvancedFluidTank
import xyz.xenondevs.nova.logistics.tileentity.BasicCable
import xyz.xenondevs.nova.logistics.tileentity.BasicFluidTank
import xyz.xenondevs.nova.logistics.tileentity.CreativeCable
import xyz.xenondevs.nova.logistics.tileentity.CreativeFluidTank
import xyz.xenondevs.nova.logistics.tileentity.EliteCable
import xyz.xenondevs.nova.logistics.tileentity.EliteFluidTank
import xyz.xenondevs.nova.logistics.tileentity.FluidStorageUnit
import xyz.xenondevs.nova.logistics.tileentity.StorageUnit
import xyz.xenondevs.nova.logistics.tileentity.TrashCan
import xyz.xenondevs.nova.logistics.tileentity.UltimateCable
import xyz.xenondevs.nova.logistics.tileentity.UltimateFluidTank
import xyz.xenondevs.nova.logistics.tileentity.VacuumChest
import xyz.xenondevs.nova.logistics.tileentity.createAdvancedPowerCell
import xyz.xenondevs.nova.logistics.tileentity.createBasicPowerCell
import xyz.xenondevs.nova.logistics.tileentity.createCreativePowerCell
import xyz.xenondevs.nova.logistics.tileentity.createElitePowerCell
import xyz.xenondevs.nova.logistics.tileentity.createUltimatePowerCell
import xyz.xenondevs.nova.material.BlockOptions
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerUnnamedItem
import xyz.xenondevs.nova.material.NovaMaterialRegistry.tileEntity
import xyz.xenondevs.nova.world.block.sound.SoundGroup

object Blocks {
    
    private val CABLE = BlockOptions(0.0, null, null, false, SoundGroup.STONE)
    private val POWER_CELL = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundGroup.METAL, IRON_BLOCK)
    private val TANK = BlockOptions(2.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundGroup.GLASS, GLASS)
    private val OTHER = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundGroup.STONE, COBBLESTONE)
    
    val BASIC_CABLE = tileEntity(Logistics, "basic_cable", ::BasicCable).blockOptions(CABLE).interactive(false).register()
    val ADVANCED_CABLE = tileEntity(Logistics, "advanced_cable", ::AdvancedCable).blockOptions(CABLE).interactive(false).register()
    val ELITE_CABLE = tileEntity(Logistics, "elite_cable", ::EliteCable).blockOptions(CABLE).interactive(false).register()
    val ULTIMATE_CABLE = tileEntity(Logistics, "ultimate_cable", ::UltimateCable).blockOptions(CABLE).interactive(false).register()
    val CREATIVE_CABLE = tileEntity(Logistics, "creative_cable", ::CreativeCable).blockOptions(CABLE).interactive(false).register()
    
    val BASIC_POWER_CELL = tileEntity(Logistics, "basic_power_cell", ::createBasicPowerCell).blockOptions(POWER_CELL).properties(LegacyDirectional).register()
    val ADVANCED_POWER_CELL = tileEntity(Logistics, "advanced_power_cell", ::createAdvancedPowerCell).blockOptions(POWER_CELL).properties(LegacyDirectional).register()
    val ELITE_POWER_CELL = tileEntity(Logistics, "elite_power_cell", ::createElitePowerCell).blockOptions(POWER_CELL).properties(LegacyDirectional).register()
    val ULTIMATE_POWER_CELL = tileEntity(Logistics, "ultimate_power_cell", ::createUltimatePowerCell).blockOptions(POWER_CELL).properties(LegacyDirectional).register()
    val CREATIVE_POWER_CELL = tileEntity(Logistics, "creative_power_cell", ::createCreativePowerCell).blockOptions(POWER_CELL).properties(LegacyDirectional).register()
    
    val BASIC_FLUID_TANK = tileEntity(Logistics, "basic_fluid_tank", ::BasicFluidTank).blockOptions(TANK).properties(LegacyDirectional).register()
    val ADVANCED_FLUID_TANK = tileEntity(Logistics, "advanced_fluid_tank", ::AdvancedFluidTank).blockOptions(TANK).properties(LegacyDirectional).register()
    val ELITE_FLUID_TANK = tileEntity(Logistics, "elite_fluid_tank", ::EliteFluidTank).blockOptions(TANK).properties(LegacyDirectional).register()
    val ULTIMATE_FLUID_TANK = tileEntity(Logistics, "ultimate_fluid_tank", ::UltimateFluidTank).blockOptions(TANK).properties(LegacyDirectional).register()
    val CREATIVE_FLUID_TANK = tileEntity(Logistics, "creative_fluid_tank", ::CreativeFluidTank).blockOptions(TANK).properties(LegacyDirectional).register()
    
    val STORAGE_UNIT = tileEntity(Logistics, "storage_unit", ::StorageUnit).itemBehaviors(StorageUnitItemBehavior).blockOptions(OTHER).properties(LegacyDirectional).register()
    val FLUID_STORAGE_UNIT = tileEntity(Logistics, "fluid_storage_unit", ::FluidStorageUnit).blockOptions(OTHER).properties(LegacyDirectional).register()
    val VACUUM_CHEST = tileEntity(Logistics, "vacuum_chest", ::VacuumChest).blockOptions(OTHER).properties(LegacyDirectional).register()
    val TRASH_CAN = tileEntity(Logistics, "trash_can", ::TrashCan).blockOptions(OTHER).properties(Directional.NORMAL).register()
    
    // Move these somewhere else?
    val TANK_WATER_LEVELS = registerUnnamedItem(Logistics, "tank_water_levels")
    val TANK_LAVA_LEVELS = registerUnnamedItem(Logistics, "tank_lava_levels")
    
    fun init() = Unit
    
}