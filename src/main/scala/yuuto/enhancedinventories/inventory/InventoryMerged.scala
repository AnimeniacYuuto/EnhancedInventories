package yuuto.enhancedinventories.inventory

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import yuuto.yuutolib.inventory.InventoryBase

class InventoryMerged(inv:Seq[Array[ItemStack]]) extends InventoryBase{
	protected var totalLength:Int=0;
  protected var tempSlot:Int=0;
  for(s <-inv){
    totalLength+=s.length;
  }
	/*public InventoryMerged(ItemStack[]... stacks){
		this.inv = stacks;
		for(ItemStack[] s : stacks){
			totalLength += s.length;
		}
	}*/
	override def getSizeInventory():Int=totalLength;
	def getInvAndSlot(slot:Int):Array[ItemStack]={
		tempSlot = slot;
		for(i <- 0 until inv.length){
			if(tempSlot < inv.apply(i).length){
				return inv.apply(i);
			}
			tempSlot -= inv.apply(i).length;
		}
		return null;
	}
	override def getStackInSlot(slot:Int):ItemStack=getInvAndSlot(slot)(tempSlot);
	override def setInventorySlotContents(slot:Int , stack:ItemStack)=getInvAndSlot(slot)(tempSlot) = stack;
	override def getInventoryName():String=null;
	override def hasCustomInventoryName():Boolean=false;
	override def markDirty(){}
	override def isUseableByPlayer(player:EntityPlayer):Boolean=false;
	override def openInventory(){}
	override def closeInventory(){}
	
	

}