package yuuto.enhancedinventories.tile.traits

import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer

/**
 * @author Jacob
 */
trait TInventorySecurable extends TInventorySimple with TSecurable{
  
  override def getSizeInventory():Int=if(this.getAccess().isPublic()){getInventory.getSizeInventory();}else{9};
  
  override def getStackInSlot(slot:Int):ItemStack=if(this.getAccess().isPublic()){getInventory().getStackInSlot(slot)}else{null};
  override def decrStackSize(slot:Int, amt:Int):ItemStack=if(this.getAccess().isPublic()){getInventory().decrStackSize(slot, amt)}else{null};
  override def getStackInSlotOnClosing(slot:Int):ItemStack=if(this.getAccess().isPublic()){getInventory().getStackInSlotOnClosing(slot)}else{null};
  override def setInventorySlotContents(slot:Int, stack:ItemStack)=if(this.getAccess().isPublic()){getInventory().setInventorySlotContents(slot, stack)};
  override def getInventoryStackLimit():Int=if(this.getAccess().isPublic()){getInventory().getInventoryStackLimit()}else{0};
  override def isUseableByPlayer(player:EntityPlayer):Boolean=this.canPlayerAccess(player);
  override def isItemValidForSlot(slot:Int, stack:ItemStack):Boolean=if(this.getAccess().isPublic()){getInventory().isItemValidForSlot(slot, stack)}else{false};
  
  
}