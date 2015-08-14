/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui.slot

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

class SlotPaintResult(result:IInventory, inv:IInventory, slot:Int, posX:Int, posY:Int) extends Slot(result, slot, posX, posY){
  
  override def isItemValid(stack:ItemStack):Boolean={
      return false;
  }
  
  override def onPickupFromSlot(player:EntityPlayer, stack:ItemStack){
    inv.decrStackSize(0, stack.stackSize);
   }

}