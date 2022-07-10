package xyz.xenondevs.nova.logistics.registry

import org.bukkit.Material.*
import org.bukkit.Sound
import xyz.xenondevs.nova.data.world.block.property.Directional
import xyz.xenondevs.nova.logistics.Logistics
import xyz.xenondevs.nova.logistics.tileentity.*
import xyz.xenondevs.nova.material.BlockOptions
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerTileEntity
import xyz.xenondevs.nova.util.SoundEffect
import xyz.xenondevs.nova.util.item.ToolCategory
import xyz.xenondevs.nova.util.item.ToolLevel

object Blocks {
    
    private val CABLE = BlockOptions(0.0, null, null, false, SoundEffect(Sound.BLOCK_STONE_PLACE), SoundEffect(Sound.BLOCK_STONE_BREAK))
    private val POWER_CELL = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundEffect(Sound.BLOCK_METAL_PLACE), SoundEffect(Sound.BLOCK_METAL_BREAK), IRON_BLOCK)
    private val TANK = BlockOptions(2.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundEffect(Sound.BLOCK_GLASS_PLACE), SoundEffect(Sound.BLOCK_GLASS_BREAK), GLASS)
    private val OTHER = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, SoundEffect(Sound.BLOCK_STONE_PLACE), SoundEffect(Sound.BLOCK_STONE_BREAK), COBBLESTONE)
    
    val BASIC_CABLE = registerTileEntity(Logistics, "basic_cable", CABLE, ::BasicCable, isInteractive = false)
    val ADVANCED_CABLE = registerTileEntity(Logistics, "advanced_cable", CABLE, ::AdvancedCable, isInteractive = false)
    val ELITE_CABLE = registerTileEntity(Logistics, "elite_cable", CABLE, ::EliteCable, isInteractive = false)
    val ULTIMATE_CABLE = registerTileEntity(Logistics, "ultimate_cable", CABLE, ::UltimateCable, isInteractive = false)
    val CREATIVE_CABLE = registerTileEntity(Logistics, "creative_cable", CABLE, ::CreativeCable, isInteractive = false)
    
    val BASIC_POWER_CELL = registerTileEntity(Logistics, "basic_power_cell", POWER_CELL, ::BasicPowerCell, properties = listOf(Directional.NORMAL))
    val ADVANCED_POWER_CELL = registerTileEntity(Logistics, "advanced_power_cell", POWER_CELL, ::AdvancedPowerCell, properties = listOf(Directional.NORMAL))
    val ELITE_POWER_CELL = registerTileEntity(Logistics, "elite_power_cell", POWER_CELL, ::ElitePowerCell, properties = listOf(Directional.NORMAL))
    val ULTIMATE_POWER_CELL = registerTileEntity(Logistics, "ultimate_power_cell", POWER_CELL, ::UltimatePowerCell, properties = listOf(Directional.NORMAL))
    val CREATIVE_POWER_CELL = registerTileEntity(Logistics, "creative_power_cell", POWER_CELL, ::CreativePowerCell, properties = listOf(Directional.NORMAL))
    
    val BASIC_FLUID_TANK = registerTileEntity(Logistics, "basic_fluid_tank", TANK, ::BasicFluidTank, properties = listOf(Directional.NORMAL))
    val ADVANCED_FLUID_TANK = registerTileEntity(Logistics, "advanced_fluid_tank", TANK, ::AdvancedFluidTank, properties = listOf(Directional.NORMAL))
    val ELITE_FLUID_TANK = registerTileEntity(Logistics, "elite_fluid_tank", TANK, ::EliteFluidTank, properties = listOf(Directional.NORMAL))
    val ULTIMATE_FLUID_TANK = registerTileEntity(Logistics, "ultimate_fluid_tank", TANK, ::UltimateFluidTank, properties = listOf(Directional.NORMAL))
    val CREATIVE_FLUID_TANK = registerTileEntity(Logistics, "creative_fluid_tank", TANK, ::CreativeFluidTank, properties = listOf(Directional.NORMAL))
    
    val STORAGE_UNIT = registerTileEntity(Logistics, "storage_unit", OTHER, ::StorageUnit, properties = listOf(Directional.NORMAL))
    val FLUID_STORAGE_UNIT = registerTileEntity(Logistics, "fluid_storage_unit", OTHER, ::FluidStorageUnit, properties = listOf(Directional.NORMAL))
    val VACUUM_CHEST = registerTileEntity(Logistics, "vacuum_chest", OTHER, ::VacuumChest, properties = listOf(Directional.NORMAL))
    val TRASH_CAN = registerTileEntity(Logistics, "trash_can", OTHER, ::TrashCan, properties = listOf(Directional.NORMAL))
    
    // Move these somewhere else?
    val TANK_WATER_LEVELS = registerDefaultItem(Logistics, "tank_water_levels")
    val TANK_LAVA_LEVELS = registerDefaultItem(Logistics, "tank_lava_levels")
    
    fun init() = Unit
    
}