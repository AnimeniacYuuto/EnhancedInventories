package yuuto.enhancedinventories.inventory


import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

abstract class InventoryBase extends IInventory{
  
  override def getInventoryStackLimit():Int=64;
  
  override def decrStackSize(slot:Int, amount:Int):ItemStack={
    markDirty();
    var stack:ItemStack = getStackInSlot(slot);
    if (stack == null) return null;
    val amt = Math.min(amount, stack.stackSize);
    if (amount < stack.stackSize) {
      stack.stackSize -= amount;
      stack = stack.copy();
      stack.stackSize = amount;
    } else setInventorySlotContents(slot, null);
    return stack;
  }
  
  override def getStackInSlotOnClosing(slot:Int):ItemStack={
    val stack:ItemStack = getStackInSlot(slot);
    if (stack == null) return null;
    setInventorySlotContents(slot, null);
    return stack;
  }
  
  override def isItemValidForSlot(slot:Int, stack:ItemStack):Boolean=true;

}
