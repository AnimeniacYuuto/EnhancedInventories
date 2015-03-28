package yuuto.enhancedinventories.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class InventoryBase implements IInventory{
	
	@Override
	public int getInventoryStackLimit() { return 64; }
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = getStackInSlot(slot);
		if (stack == null) return null;
		amount = Math.min(amount, stack.stackSize);
		if (amount < stack.stackSize) {
			stack.stackSize -= amount;
			stack = stack.copy();
			stack.stackSize = amount;
			markDirty();
		} else setInventorySlotContents(slot, null);
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack == null) return null;
		setInventorySlotContents(slot, null);
		return stack;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) { return true; }

}
