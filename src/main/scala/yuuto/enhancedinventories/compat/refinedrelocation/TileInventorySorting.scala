package yuuto.enhancedinventories.compat.refinedrelocation

import yuuto.enhancedinventories.tile.traits.TInventorySimple
import net.minecraft.inventory.IInventory
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler
import com.dynious.refinedrelocation.api.filter.IFilterGUI
import yuuto.enhancedinventories.inventory.InventoryMerged
import yuuto.enhancedinventories.inventory.TileInventoryBasic
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory.Priority

/**
 * @author Jacob
 */
object TileInventorySorting{
  def areItemStacksEqual(itemStack1:ItemStack, itemStack2:ItemStack):Boolean={
      return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.getItem() == itemStack2.getItem() && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
  }
}
class TileInventorySorting(sorter:ITileSorting, mainTile:TInventorySimple, 
    tiles:Array[TInventorySimple], 
    inv:IInventory) extends TileInventoryBasic(mainTile, tiles, inv) with ISortingInventory with IFilterTileGUI{
  
  def this(sorter:ITileSorting, mainTile:TInventorySimple, tiles:Array[TInventorySimple])=
    this(sorter, mainTile, tiles, new InventoryMerged(TileInventoryBasic.getContents(tiles)));
  def this(sorter:ITileSorting, tile:TInventorySimple)=
    this(sorter, tile, Array(tile), new InventoryMerged(Seq(tile.getContents())));
  
  override def getFilter():IFilterGUI=sorter.getTileFilterGui();
  override def getTileEntity():TileEntity=this.getMainTile();
  override def onFilterChanged()=sorter.onFilterChanged();
  override def getHandler():ISortingInventoryHandler=sorter.getTileInvHandler();
  override def getPriority():Priority=sorter.getTilePriority();
  override def putInInventory(itemStack:ItemStack, simulate:Boolean):ItemStack={
    var emptySlot:Int = -1;
    if(itemStack == null)
      return itemStack;
    for (slot <- 0 until getSizeInventory() if(itemStack.stackSize > 0))
    {
        if (isItemValidForSlot(slot, itemStack))
        {
            val itemstack1:ItemStack = getStackInSlot(slot);

            if (itemstack1 == null)
            {
                if (simulate)
                    return null;
                if (emptySlot == -1)
                    emptySlot = slot;
            }
            else if (TileInventorySorting.areItemStacksEqual(itemstack1, itemStack))
            {
                val max:Int = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                if (max > itemstack1.stackSize)
                {
                    val l:Int = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                    itemStack.stackSize -= l;
                    if (!simulate)
                    {
                        itemstack1.stackSize += l;
                        markDirty();
                    }
                }
            }
        }
    }

    if (itemStack != null && itemStack.stackSize != 0 && emptySlot != -1)
    {
        //chestContents[emptySlot] = itemStack;
      if(simulate)
        return null;
      if(this.putStackInSlot(itemStack, emptySlot))
        return null;
        markDirty();
    }

    return itemStack;
  }
  override def putStackInSlot(stack:ItemStack, slot:Int):Boolean={
    super.setInventorySlotContents(slot, stack);
    if(stack == null && this.getStackInSlot(slot) == null)
      return true;
    if(stack != null && this.getStackInSlot(slot) == null)
      return false;
    if(stack == null && this.getStackInSlot(slot) != null)
      return false;
    return this.getStackInSlot(slot).stackSize == stack.stackSize;
  }
  override def setInventorySlotContents(slot:Int, stack:ItemStack){
    this.getHandler().setInventorySlotContents(slot, stack);
  }
  override def setPriority(priority:Priority) {
    sorter.setTilePriority(priority);
  }
}