package xyz.xenondevs.nova.logistics.gui.cable

import de.studiocode.invui.item.Item
import de.studiocode.invui.item.ItemProvider
import de.studiocode.invui.item.impl.BaseItem
import net.md_5.bungee.api.chat.TranslatableComponent
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.nova.material.CoreGUIMaterial
import xyz.xenondevs.nova.tileentity.network.ContainerEndPointDataHolder
import xyz.xenondevs.nova.tileentity.network.NetworkConnectionType
import xyz.xenondevs.nova.tileentity.network.NetworkManager
import xyz.xenondevs.nova.ui.item.BUTTON_COLORS
import xyz.xenondevs.nova.util.data.setLocalizedName
import xyz.xenondevs.nova.util.notifyWindows

abstract class BaseCableConfigGUI<H : ContainerEndPointDataHolder<*>>(
    val holder: H,
    val face: BlockFace,
    private val channelAmount: Int
) {
    
    protected val updatableItems = ArrayList<Item>()
    
    protected var allowsExtract = false
    protected var allowsInsert = false
    
    protected var insertPriority = -1
    protected var extractPriority = -1
    protected var insertState = false
    protected var extractState = false
    protected var channel = -1
    
    protected fun updateButtons() = updatableItems.notifyWindows()
    
    abstract fun updateValues(updateButtons: Boolean)
    
    protected fun updateCoreValues() {
        NetworkManager.execute { // TODO: queueSync / queueAsync ?
            val allowedConnections = holder.allowedConnectionTypes[holder.containerConfig[face]]!!
            allowsExtract = allowedConnections.extract
            allowsInsert = allowedConnections.insert
    
            insertPriority = holder.insertPriorities[face]!!
            extractPriority = holder.extractPriorities[face]!!
            insertState = holder.connectionConfig[face]!!.insert
            extractState = holder.connectionConfig[face]!!.extract
            channel = holder.channels[face]!!
        }
    }
    
    open fun writeChanges() {
        holder.insertPriorities[face] = insertPriority
        holder.extractPriorities[face] = extractPriority
        holder.channels[face] = channel
        holder.connectionConfig[face] = NetworkConnectionType.of(insertState, extractState)
    }
    
    protected inner class InsertItem : BaseItem() {
        
        override fun getItemProvider(): ItemProvider =
            (if (insertState) CoreGUIMaterial.GREEN_BTN else CoreGUIMaterial.GRAY_BTN)
                .createClientsideItemBuilder().setLocalizedName("menu.logistics.cable_config.insert")
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (!allowsInsert) return
            
            insertState = !insertState
            notifyWindows()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }
        
    }
    
    protected inner class ExtractItem : BaseItem() {
        
        override fun getItemProvider(): ItemProvider =
            (if (extractState) CoreGUIMaterial.GREEN_BTN else CoreGUIMaterial.GRAY_BTN)
                .createClientsideItemBuilder().setLocalizedName("menu.logistics.cable_config.extract")
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (!allowsExtract) return
            
            extractState = !extractState
            notifyWindows()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
        }
        
    }
    
    protected inner class SwitchChannelItem : BaseItem() {
        
        override fun getItemProvider(): ItemProvider {
            return BUTTON_COLORS[channel].createClientsideItemBuilder()
                .setDisplayName(TranslatableComponent("menu.logistics.cable_config.channel", channel + 1))
        }
        
        override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
            if (clickType == ClickType.RIGHT || clickType == ClickType.LEFT) {
                if (clickType == ClickType.LEFT) channel++ else channel--
                if (channel >= channelAmount) channel = 0
                else if (channel < 0) channel = channelAmount - 1
                
                notifyWindows()
                player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
            }
        }
        
    }
    
}