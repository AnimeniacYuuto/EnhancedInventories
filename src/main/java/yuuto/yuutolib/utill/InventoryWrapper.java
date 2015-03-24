package yuuto.yuutolib.utill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public class InventoryWrapper implements ISidedInventory{

	public static ISidedInventory getWrapper(IInventory inv){
		if(inv instanceof ISidedInventory)
			return (ISidedInventory)inv;
		return new InventoryWrapper(inv);
	}
	IInventory inv;
	int[] sides;
	private InventoryWrapper(IInventory inv){
		this.inv = inv;
		this.sides = new int[inv.getSizeInventory()]; 
		for(int i = 0; i < inv.getSizeInventory(); i++){
			sides[i] = i;
		}
	}
	
	@Override
	public int getSizeInventory() {
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return inv.decrStackSize(p_70298_1_, p_70298_2_);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return inv.getStackInSlotOnClosing(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		inv.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}

	@Override
	public String getInventoryName() {
		return inv.getInventoryName();
	}
	@Override
	public boolean hasCustomInventoryName() {
		return inv.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return inv.getInventoryStackLimit();
	}

	@Override
	public void markDirty() {
		inv.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return inv.isUseableByPlayer(p_70300_1_);
	}

	@Override
	public void openInventory() {
		inv.openInventory();
	}

	@Override
	public void closeInventory() {
		inv.closeInventory();
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return inv.isItemValidForSlot(p_94041_1_, p_94041_2_);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return sides;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		return true;
	}
	
	

}
