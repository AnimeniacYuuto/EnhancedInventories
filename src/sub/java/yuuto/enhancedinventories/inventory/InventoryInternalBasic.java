package yuuto.enhancedinventories.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import yuuto.enhancedinventories.tile.TileBasicInventory;

public class InventoryInternalBasic implements IInventory{
	
	TileBasicInventory tile;
	ItemStack[] inv;
	
	public InventoryInternalBasic(TileBasicInventory tile){
		this.tile = tile;
		this.inv = new ItemStack[tile.getBaseSize()*(tile.getTier()*tile.getBaseSize())];
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return this.inv[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (this.inv[p_70298_1_] != null)
        {
            ItemStack itemstack;

            if (this.inv[p_70298_1_].stackSize <= p_70298_2_)
            {
                itemstack = this.inv[p_70298_1_];
                this.inv[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.inv[p_70298_1_].splitStack(p_70298_2_);

                if (this.inv[p_70298_1_].stackSize == 0)
                {
                    this.inv[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (this.inv[p_70304_1_] != null)
        {
            ItemStack itemstack = this.inv[p_70304_1_];
            this.inv[p_70304_1_] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		this.inv[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return tile.getInventoryName();
	}

	@Override
	public boolean hasCustomInventoryName() {
		return tile.hasCustomInventoryName();
	}

	@Override
	public int getInventoryStackLimit() {
		return tile.getInventoryStackLimit();
	}

	@Override
	public void markDirty() {
		tile.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

	@Override
	public void openInventory() {
		tile.openInventory();
		
	}

	@Override
	public void closeInventory() {
		tile.openInventory();
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}
	
	public ItemStack[] getContents(){
		return inv;
	}

}
