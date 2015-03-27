package yuuto.enhancedinventories.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryMerged extends InventoryBase{
	
	ItemStack[][] inv;
	int totalLength;
	int tempSlot;
	public InventoryMerged(ItemStack[]... stacks){
		this.inv = stacks;
		for(ItemStack[] s : stacks){
			totalLength += s.length;
		}
	}
	@Override
	public int getSizeInventory() {
		return totalLength;
	}
	public ItemStack[] getInvAndSlot(int slot){
		tempSlot = slot;
		for(int i = 0; i < inv.length; i++){
			if(slot < inv[i].length)
				return inv[i];
			tempSlot -= inv[i].length;
		}
		return null;
	}
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return getInvAndSlot(p_70301_1_)[tempSlot];
	}
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		getInvAndSlot(p_70299_1_)[tempSlot] = p_70299_2_;
	}
	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}
	
	

}
