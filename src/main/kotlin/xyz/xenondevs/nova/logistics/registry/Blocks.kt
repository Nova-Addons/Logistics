package xyz.xenondevs.nova.logistics.registry

import org.bukkit.Material.*
import org.bukkit.Sound
import xyz.xenondevs.nova.logistics.LOGISTICS
import xyz.xenondevs.nova.logistics.tileentity.*
import xyz.xenondevs.nova.material.BlockOptions
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerDefaultItem
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerEnergyTileEntity
import xyz.xenondevs.nova.material.NovaMaterialRegistry.registerTileEntity
import xyz.xenondevs.nova.tileentity.network.fluid.holder.NovaFluidHolder
import xyz.xenondevs.nova.util.SoundEffect
import xyz.xenondevs.nova.util.item.ToolCategory
import xyz.xenondevs.nova.util.item.ToolLevel

object Blocks {
    
    private val CABLE = BlockOptions(0.0, null, null, false, STRUCTURE_VOID, SoundEffect(Sound.BLOCK_STONE_PLACE), SoundEffect(Sound.BLOCK_STONE_BREAK))
    private val POWER_CELL = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, BARRIER, SoundEffect(Sound.BLOCK_METAL_PLACE), SoundEffect(Sound.BLOCK_METAL_BREAK), IRON_BLOCK)
    private val TANK = BlockOptions(2.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, BARRIER, SoundEffect(Sound.BLOCK_GLASS_PLACE), SoundEffect(Sound.BLOCK_GLASS_BREAK), GLASS)
    private val OTHER = BlockOptions(4.0, ToolCategory.PICKAXE, ToolLevel.STONE, true, BARRIER, SoundEffect(Sound.BLOCK_STONE_PLACE), SoundEffect(Sound.BLOCK_STONE_BREAK), COBBLESTONE)
    
    val BASIC_CABLE = registerTileEntity(LOGISTICS, "basic_cable", CABLE, ::BasicCable, isInteractable = false, properties = emptyList())
    val ADVANCED_CABLE = registerTileEntity(LOGISTICS, "advanced_cable", CABLE, ::AdvancedCable, isInteractable = false, properties = emptyList())
    val ELITE_CABLE = registerTileEntity(LOGISTICS, "elite_cable", CABLE, ::EliteCable, isInteractable = false, properties = emptyList())
    val ULTIMATE_CABLE = registerTileEntity(LOGISTICS, "ultimate_cable", CABLE, ::UltimateCable, isInteractable = false, properties = emptyList())
    val CREATIVE_CABLE = registerTileEntity(LOGISTICS, "creative_cable", CABLE, ::CreativeCable, isInteractable = false, properties = emptyList())
    
    val BASIC_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "basic_power_cell", POWER_CELL, ::BasicPowerCell)
    val ADVANCED_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "advanced_power_cell", POWER_CELL, ::AdvancedPowerCell)
    val ELITE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "elite_power_cell", POWER_CELL, ::ElitePowerCell)
    val ULTIMATE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "ultimate_power_cell", POWER_CELL, ::UltimatePowerCell)
    val CREATIVE_POWER_CELL = registerEnergyTileEntity(LOGISTICS, "creative_power_cell", POWER_CELL, ::CreativePowerCell)
    
    val BASIC_FLUID_TANK = registerTileEntity(LOGISTICS, "basic_fluid_tank", TANK, ::BasicFluidTank, listOf(NovaFluidHolder::modifyItemBuilder))
    val ADVANCED_FLUID_TANK = registerTileEntity(LOGISTICS, "advanced_fluid_tank", TANK, ::AdvancedFluidTank, listOf(NovaFluidHolder::modifyItemBuilder))
    val ELITE_FLUID_TANK = registerTileEntity(LOGISTICS, "elite_fluid_tank", TANK, ::EliteFluidTank, listOf(NovaFluidHolder::modifyItemBuilder))
    val ULTIMATE_FLUID_TANK = registerTileEntity(LOGISTICS, "ultimate_fluid_tank", TANK, ::UltimateFluidTank, listOf(NovaFluidHolder::modifyItemBuilder))
    val CREATIVE_FLUID_TANK = registerTileEntity(LOGISTICS, "creative_fluid_tank", TANK, ::CreativeFluidTank, listOf(NovaFluidHolder::modifyItemBuilder))
    
    val STORAGE_UNIT = registerTileEntity(LOGISTICS, "storage_unit", OTHER, ::StorageUnit)
    val FLUID_STORAGE_UNIT = registerTileEntity(LOGISTICS, "fluid_storage_unit", OTHER, ::FluidStorageUnit)
    val VACUUM_CHEST = registerTileEntity(LOGISTICS, "vacuum_chest", OTHER, ::VacuumChest)
    val TRASH_CAN = registerTileEntity(LOGISTICS, "trash_can", OTHER, ::TrashCan)
    
    // Move these somewhere else?
    val TANK_WATER_LEVELS = registerDefaultItem(LOGISTICS, "tank_water_levels")
    val TANK_LAVA_LEVELS = registerDefaultItem(LOGISTICS, "tank_lava_levels")
    
    fun init() = Unit
    
}