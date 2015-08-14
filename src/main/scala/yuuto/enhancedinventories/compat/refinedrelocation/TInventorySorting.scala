package yuuto.enhancedinventories.compat.refinedrelocation

import com.dynious.refinedrelocation.api.APIUtils
import com.dynious.refinedrelocation.api.filter.IFilterGUI
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory.Priority
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import yuuto.enhancedinventories.tile.base.TileBaseEI
import yuuto.enhancedinventories.tile.traits.TInventorySimple

/**
 * @author Jacob
 */
trait TInventorySorting extends TileBaseEI with TInventorySimple with ITileSorting{
  val sortingInventoryHandler:ISortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
  var filter:IFilterGUI = APIUtils.createStandardFilter(this);
  var priority:Priority = Priority.NORMAL;
  
  override def initialize(){
    super.initialize();
    this.sortingInventoryHandler.onTileAdded();
  }
  override def invalidate(){
    super.invalidate();
    this.sortingInventoryHandler.onTileRemoved();
  }
  
  override def setInventory(){
    if(this.inv != null){
      this.invHandler = new TileInventorySorting(this.asInstanceOf[ITileSorting], this.asInstanceOf[TInventorySimple]);
    }
  }
  
  def getSortingInventory():TileInventorySorting=this.getInventory().asInstanceOf[TileInventorySorting];
  override def getFilter():IFilterGUI=this.getSortingInventory().getFilter();
  override def getTileEntity():TileEntity=this.getSortingInventory().getTileEntity();
  override def onFilterChanged()=this.markDirty();
  override def getHandler():ISortingInventoryHandler=this.getSortingInventory().getHandler();
  override def getPriority():Priority=this.getSortingInventory().getPriority();
  override def putInInventory(stack:ItemStack, simulate:Boolean):ItemStack=this.getSortingInventory().putInInventory(stack, simulate);
  override def putStackInSlot(stack:ItemStack, slot:Int):Boolean=this.getSortingInventory().putStackInSlot(stack, slot);
  override def setPriority(priority:Priority)=this.getSortingInventory().setPriority(priority);
  
  override def getTileInvHandler():ISortingInventoryHandler= sortingInventoryHandler;
  
  override def getTileFilterGui():IFilterGUI=filter;
  override def setTileFilterGui(gui:IFilterGUI)=this.filter = gui;
  override def getTilePriority():Priority=priority;
  override def setTilePriority(priority:Priority) {
    this.priority = priority;
    this.markDirty();
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    val filterTag:NBTTagCompound = new NBTTagCompound();
    this.filter.writeToNBT(filterTag);
    nbt.setTag("sortingFilter", filterTag);
    nbt.setByte("sortingPriority", priority.ordinal().asInstanceOf[Byte]);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    this.filter.readFromNBT(nbt.getCompoundTag("sortingFilter"));
    this.priority = Priority.values()(nbt.getInteger("sortingPriority"));
  }
  
}