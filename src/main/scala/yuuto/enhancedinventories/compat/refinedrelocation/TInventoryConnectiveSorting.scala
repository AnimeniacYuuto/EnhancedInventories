package yuuto.enhancedinventories.compat.refinedrelocation

import com.dynious.refinedrelocation.api.tileentity.ISortingInventory.Priority

import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.tile.traits.TInventoryConnective
import yuuto.enhancedinventories.tile.traits.TInventorySimple

/**
 * @author Jacob
 */
trait TInventoryConnectiveSorting extends TInventoryConnective with TInventorySorting{
  
  override def setInventoryConnective(){
    this.connectedInventory = new TileInventorySorting(this.asInstanceOf[ITileSorting], this.asInstanceOf[TInventorySimple], Array(this.asInstanceOf[TInventorySimple], this.getPartner().asInstanceOf[TInventorySimple]))
  }
  
  override def onFilterChanged(){
    super.onFilterChanged();
    if(this.isConnected()){
      val p:TInventoryConnectiveSorting = getPartner().asInstanceOf[TInventoryConnectiveSorting];
      val nbt:NBTTagCompound = new NBTTagCompound();
      filter.writeToNBT(nbt);
      p.getTileFilterGui().readFromNBT(nbt);
      this.getPartner().markDirty();
    }
  }
  
  override def setTilePriority(priority:Priority) {
    super.setTilePriority(priority)
    if(this.isConnected()){
      this.getPartner().asInstanceOf[TInventoryConnectiveSorting].priority = priority;
      this.getPartner().markDirty();
    }
  }
}