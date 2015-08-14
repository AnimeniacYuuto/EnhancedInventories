/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui.slot

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import yuuto.enhancedinventories.proxy.ProxyCommon;

class SlotSchematic(inv:IInventory, slotIndex:Int, x:Int, y:Int) extends Slot(inv, slotIndex, x,y){

  override def isItemValid(stack:ItemStack):Boolean={
      return stack.getItem() == ProxyCommon.schematic;
  }

}