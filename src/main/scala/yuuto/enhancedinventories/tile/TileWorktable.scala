package yuuto.enhancedinventories.tile

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import yuuto.enhancedinventories.inventory.IInventoryParent
import yuuto.enhancedinventories.inventory.InventorySimple
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.tile.base.TileCrafter
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.yuutolib.inventory.IInventoryExtended
import yuuto.yuutolib.inventory.InventoryWrapper

object TileWorktable{
  private val schematicSlot:Int = 27;
}
class TileWorktable extends TileCrafter with IInventoryParent{
  
  protected val internalInventory:IInventory = new InventorySimple(28, this);
  protected var usingSchematic:Boolean = false;
  var syncronizing:Boolean=false;
  
  def getInternalInventory():IInventory=internalInventory;
  override def getSubInventories():Array[IInventoryExtended]=Array(InventoryWrapper.getWrapper(internalInventory))
  override def onCraftMatrixChanged(inv:IInventory) {
    if(syncronizing){
      return;
    }
    this.markDirty();
    if(isCraftingMatrixEmpty() && getSchematic() != null){
      usingSchematic = true;
      this.craftResult.setInventorySlotContents(0, ItemSchematic.getResult(getSchematic()));
    }else{
      usingSchematic = false;
      this.craftResult.setInventorySlotContents(0, findMatchingRecipe(this.craftingMatrix, this.worldObj));
    }
  }

  override def isUsingSchematic():Boolean=usingSchematic;

  override def getSchematic():ItemStack={
    return this.internalInventory.getStackInSlot(TileWorktable.schematicSlot);
  }
  
  def saveSchematic(){
    if(this.getSchematic() == null)
      return;
    val result:ItemStack = findMatchingRecipe(this.craftingMatrix, this.worldObj);
    this.onCraftMatrixChanged(craftingMatrix);
    if(result == null){
      return;
    }
    ItemSchematic.writeSchematic(this.getSchematic(), this.craftingMatrix, result);   
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    val nbttaglist:NBTTagList = new NBTTagList();
    for(i <-0 until this.internalInventory.getSizeInventory() if(this.internalInventory.getStackInSlot(i) != null)){
      val nbttagcompound1:NBTTagCompound = new NBTTagCompound();
      nbttagcompound1.setByte("Slot", i.asInstanceOf[Byte]);
      this.internalInventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
      nbttaglist.appendTag(nbttagcompound1);
    }
    nbt.setTag("inv", nbttaglist);
    super.writeToNBT(nbt);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    val nbttaglist:NBTTagList = nbt.getTagList("inv", Constants.NBT.TAG_COMPOUND);
    this.syncronizing=true;
    for (i <- 0 until nbttaglist.tagCount()){
        val nbttagcompound1:NBTTagCompound = nbttaglist.getCompoundTagAt(i);
        val j:Int = nbttagcompound1.getByte("Slot") & 0xff;
        if (j >= 0 && j < internalInventory.getSizeInventory())
        {
          internalInventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }
    super.readFromNBT(nbt);
    this.syncronizing=false;
    this.onCraftMatrixChanged(getCraftingMatrix());
  }

  override def onInventoryChanged(inventorySimple:IInventory)=this.onCraftMatrixChanged(inventorySimple);

  override def getItemStack():ItemStack={
    return new ItemStack(ProxyCommon.blockWorktable);
  }

}