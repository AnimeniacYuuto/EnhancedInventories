package yuuto.enhancedinventories.inventory

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

class InventorySimple(private val inv:Array[ItemStack], private val parent:IInventoryParent) extends InventoryBase{
  
  def this(size:Int)=this(new Array[ItemStack](size), null);
  def this(size:Int, parrent:IInventoryParent)=this(new Array[ItemStack](size), parrent);
  
  def getContents():Array[ItemStack]=inv;
  
  override def getSizeInventory():Int=inv.length;

  override def getStackInSlot(slot:Int):ItemStack=inv(slot);

  override def setInventorySlotContents(slot:Int, stack:ItemStack){
    inv(slot) = stack;
    this.markDirty();
  }

  override def getInventoryName():String=null;
  override def hasCustomInventoryName():Boolean=false;

  override def markDirty() {
    if(parent != null)
      parent.onInventoryChanged(this);
  }

  override def isUseableByPlayer(player:EntityPlayer):Boolean=false;

  override def openInventory() {}
  override def closeInventory() {}

}