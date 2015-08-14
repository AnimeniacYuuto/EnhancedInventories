package yuuto.enhancedinventories.tile.traits

import net.minecraft.item.ItemStack
import net.minecraft.inventory.IInventory
import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import yuuto.enhancedinventories.inventory.TileInventoryBasic

/**
 * @author Jacob
 */
trait TInventorySimple extends TileBaseEI with IInventory{
  var inv:Array[ItemStack]=null;
  var invHandler:IInventory=null;
  
  def getContents():Array[ItemStack]=inv;
  
  def resetInventory(){
    this.inv = new Array[ItemStack](getArraySize());
    setInventory();
  }
  def setInventory(){
    if(inv == null)
      return;
    invHandler = new TileInventoryBasic(this);
  }
  def getInventory():IInventory=this.invHandler;
  
  def getArraySize():Int=27;
  
  override def invalidate(){
    super.invalidate();
    this.invHandler = null;
  }

  override def getSizeInventory():Int=getInventory.getSizeInventory();
  
  override def getStackInSlot(slot:Int):ItemStack=getInventory().getStackInSlot(slot);
  override def decrStackSize(slot:Int, amt:Int):ItemStack=getInventory().decrStackSize(slot, amt);
  override def getStackInSlotOnClosing(slot:Int):ItemStack=getInventory().getStackInSlotOnClosing(slot);
  override def setInventorySlotContents(slot:Int, stack:ItemStack)=getInventory().setInventorySlotContents(slot, stack);
  override def getInventoryStackLimit():Int=return getInventory().getInventoryStackLimit();
  override def isUseableByPlayer(player:EntityPlayer):Boolean={
    return false;
  }
  override def isItemValidForSlot(slot:Int, stack:ItemStack):Boolean=return getInventory().isItemValidForSlot(slot, stack);
  
  override def getInventoryName():String=null;
  override def hasCustomInventoryName():Boolean=false;
  override def openInventory() {}
  override def closeInventory() {}
  def onOpenInventory() {}
  def onCloseInventory() {}

  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    val nbttaglist:NBTTagList = new NBTTagList();
    for(i<-0 until this.inv.length if(this.inv(i) != null)){
      val nbttagcompound1:NBTTagCompound = new NBTTagCompound();
            if(i <= Byte.MaxValue){
              nbttagcompound1.setByte("Slot", i.asInstanceOf[Byte])
            }else{
              nbttagcompound1.setShort("Slot", i.asInstanceOf[Short]);
            }
            inv(i).writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
    }
    nbt.setTag("Items", nbttaglist);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    val nbttaglist:NBTTagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (i<-0 until nbttaglist.tagCount())
        {
            val nbttagcompound1:NBTTagCompound = nbttaglist.getCompoundTagAt(i);
            val j:Int = nbttagcompound1.getInteger("Slot");
            if (j >= 0 && j < inv.length)
            {
                inv(j) = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
  }
}