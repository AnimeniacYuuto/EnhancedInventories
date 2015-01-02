/*******************************************************************************
 * Copyright (c) 2014 Yuuto.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * 	   cpw - src reference from Iron Chests
 * 	   doku/Dokucraft staff - base chest texture
 *     Yuuto - initial API and implementation
 ******************************************************************************/
package yuuto.enhancedinventories.tile;

import java.util.ArrayList;
import java.util.List;

import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.yuutolib.utill.InventoryWrapper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileLocker extends TileConnectiveInventory{
	
	public static ArrayList<ForgeDirection> conDirs = new ArrayList<ForgeDirection>();
	static ArrayList<ForgeDirection> topDirs = new ArrayList<ForgeDirection>();
	static{
		conDirs.add(ForgeDirection.UP);
		conDirs.add(ForgeDirection.DOWN);
		topDirs.add(ForgeDirection.DOWN);
	}
	
	public int woodType = 0;
	public int woolType = 0;
	public boolean hopper;
	public boolean alt;
	public boolean redstone;
	public boolean reversed;
	int timer = 20;
	
	public TileLocker()
    {
        super();
    }

    public TileLocker(EInventoryMaterial type)
    {
        super(type);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound){
    	super.readFromNBT(nbttagcompound);
    	woodType = nbttagcompound.getInteger("wood");
    	woolType = nbttagcompound.getInteger("wool");
    	hopper = nbttagcompound.getBoolean("hopper");
    	alt = nbttagcompound.getBoolean("alt");
    	redstone = nbttagcompound.getBoolean("redstone");
    	reversed = nbttagcompound.getBoolean("reversed");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound){
    	super.writeToNBT(nbttagcompound);
    	nbttagcompound.setByte("wood", (byte)woodType);
    	nbttagcompound.setByte("wool", (byte)woolType);
    	nbttagcompound.setBoolean("hopper", hopper);
    	nbttagcompound.setBoolean("alt", alt);
    	nbttagcompound.setBoolean("redstone", redstone);
    	nbttagcompound.setBoolean("reversed", reversed);
    }
    @Override
	public boolean isValidForConnection(ItemStack itemBlock) {
		if(itemBlock.getItemDamage() != this.getType().ordinal())
			return false;
		if(this.woodType != itemBlock.getTagCompound().getInteger("wood"))
			return false;
		if(this.woolType != itemBlock.getTagCompound().getInteger("wool"))
			return false;
		if(this.hopper != itemBlock.getTagCompound().getBoolean("hopper"))
			return false;
		if(this.alt != itemBlock.getTagCompound().getBoolean("alt"))
			return false;
		if(this.redstone != itemBlock.getTagCompound().getBoolean("redstone"))
			return false;
		return true;
	}
    @Override
	public boolean isValidForConnection(TileConnectiveInventory tile) {
		if(!(tile instanceof TileLocker))
			return false;
		TileLocker locker = (TileLocker)tile;
		if(locker.getType() != this.getType())
			return false;
		if(this.woodType != locker.woodType)
			return false;
		if(this.woolType != locker.woolType)
			return false;
		if(this.hopper != locker.hopper)
			return false;
		if(this.alt != locker.alt)
			return false;
		if(this.redstone != locker.redstone)
			return false;
		return true;
	}
	@Override
	public List<ForgeDirection> getValidConnectionSides() {
		return conDirs;
	}

	@Override
	public List<ForgeDirection> getTopSides() {
		return topDirs;
	}
	
	@Override
    public void updateEntity(){
		super.updateEntity();
		if(worldObj.isRemote)
			return;
		timer -= 1;
		if(timer > 0)
			return;
		timer = 20;
		if(hopper){
			suckItems();
			pushItems();
		}
    }
	public void suckItems(){
		TileEntity tile = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		if(tile == null || !(tile instanceof IInventory))
			return;
		ISidedInventory inv = InventoryWrapper.getWrapper((IInventory)tile);
		ISidedInventory tar = InventoryWrapper.getWrapper(this);
		int[] slots = inv.getAccessibleSlotsFromSide(ForgeDirection.DOWN.ordinal());
		for(int i = 0; i < slots.length; i++){
			if(inv.getStackInSlot(slots[i]) == null || inv.getStackInSlot(slots[i]).stackSize < 1)
				continue;
			if(!inv.canExtractItem(slots[i], inv.getStackInSlot(slots[i]), ForgeDirection.DOWN.ordinal()))
				continue;
			if(mergeStack(inv, i,tar)){
				inv.markDirty();
				tar.markDirty();
				break;
			}
		}
	}
	public void pushItems(){
		TileEntity tile = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
		if(tile == null || !(tile instanceof IInventory))
			return;
		ISidedInventory tar = InventoryWrapper.getWrapper((IInventory)tile);
		ISidedInventory inv = InventoryWrapper.getWrapper(this);
		for(int i = 0; i < inv.getSizeInventory(); i++){
			if(inv.getStackInSlot(i) == null || inv.getStackInSlot(i).stackSize < 1)
				continue;
			if(mergeStack(inv, i, tar)){
				inv.markDirty();
				tar.markDirty();
				break;
			}
		}
	}
	public boolean mergeStack(ISidedInventory src, int srcSlot, ISidedInventory target){
		ItemStack stack = src.getStackInSlot(srcSlot);
		int[] slots = target.getAccessibleSlotsFromSide(ForgeDirection.UP.ordinal());
		for(int i = 0; i < slots.length; i++){
			ItemStack tStack = target.getStackInSlot(slots[i]);
			if(tStack != null && 
					(tStack.stackSize == tStack.getMaxStackSize() || 
					tStack.stackSize == target.getInventoryStackLimit()))
				continue;
			if(tStack == null && target.canInsertItem(slots[i], stack, ForgeDirection.UP.ordinal())){
				target.setInventorySlotContents(slots[i], src.decrStackSize(srcSlot, stack.stackSize));
				return true;
			}
			if(tStack.getItem() != stack.getItem())
				continue;
			if(tStack.getItemDamage() != stack.getItemDamage())
				continue;
			if(!ItemStack.areItemStackTagsEqual(stack, tStack))
				continue;
			int max = stack.getMaxStackSize();
			if(target.getInventoryStackLimit() < max)
				max = target.getInventoryStackLimit();
			max -= tStack.stackSize;
			if(stack.stackSize < max)
				max = stack.stackSize;
			if(max == 0)
				continue;
			tStack.stackSize += src.decrStackSize(srcSlot, max).stackSize;
			return true;
		}
		return false;
	}

}
