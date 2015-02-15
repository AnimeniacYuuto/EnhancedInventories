package yuuto.enhancedinventories.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;

import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.tile.TileLocker;

public class TileSortingLocker extends TileLocker implements ISortingInventory, IFilterTileGUI{

	public ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);
	public IFilterGUI filter = APIUtils.createStandardFilter(this);
	public Priority priority = Priority.NORMAL;
	
	public TileSortingLocker()
    {
        super();
        this.sortingChest = true;
    }

    public TileSortingLocker(EInventoryMaterial type)
    {
        super(type);
        this.sortingChest = true;
    }
    
    @Override
    public void initialize(){
    	super.initialize();
    	sortingInventoryHandler.onTileAdded();
    }
    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }    
	
	@Override
	public IFilterGUI getFilter() {
		return filter;
	}

	@Override
	public TileEntity getTileEntity() {
		/*if(this.getPartner() != null && !this.getTopSides().contains(this.getPartnerDir()))
			return this.getPartner();*/
		return this;
	}

	@Override
	public void onFilterChanged() {
		this.markDirty();
	}

	@Override
	public ISortingInventoryHandler getHandler() {
		return sortingInventoryHandler;
	}

	/*@Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < chestContents.length)
        {
            chestContents[slotIndex] = itemStack;
            return true;
        }
        return false;
    }*/
	@Override
	public final boolean putStackInSlot(ItemStack itemstack, int slot){
		boolean flag = false;
		//boolean flag1 = true;
    	int i = slot;
    	if(slot < 0 || slot > this.getSizeInventory())
    		return false;
    	if(partnerTile == null){
    		flag = true;
    	}else if(this.getTopSides().contains(partnerDir)){
    		if(i < type.size()){
    			flag = true;
    		}else{
    			i-= type.size();
    		}
    	}else if(i >= type.size()){
    		flag = true;
    		i-= type.size();
    	}
    	if(flag){
	    	chestContents[i] = itemstack;
	        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
	        {
	            itemstack.stackSize = getInventoryStackLimit();
	        }
    	}else{
    		partnerTile.getContents()[i] = itemstack;
	        if (itemstack != null && itemstack.stackSize > partnerTile.getInventoryStackLimit())
	        {
	            itemstack.stackSize = partnerTile.getInventoryStackLimit();
	        }
    	}
    	markDirty();
    	return true;
	}

    @Override
    public ItemStack putInInventory(ItemStack itemStack, boolean simulate)
    {
        int emptySlot = -1;
        for (int slot = 0; slot < getSizeInventory() && itemStack != null && itemStack.stackSize > 0; ++slot)
        {
            if (isItemValidForSlot(slot, itemStack))
            {
                ItemStack itemstack1 = getStackInSlot(slot);

                if (itemstack1 == null)
                {
                    if (simulate)
                        return null;
                    if (emptySlot == -1)
                        emptySlot = slot;
                }
                else if (areItemStacksEqual(itemstack1, itemStack))
                {
                    int max = Math.min(itemStack.getMaxStackSize(), getInventoryStackLimit());
                    if (max > itemstack1.stackSize)
                    {
                        int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                        itemStack.stackSize -= l;
                        if (!simulate)
                        {
                            itemstack1.stackSize += l;
                            markDirty();
                        }
                    }
                }
            }
        }

        if (itemStack != null && itemStack.stackSize != 0 && emptySlot != -1)
        {
            //chestContents[emptySlot] = itemStack;
            if(this.putStackInSlot(itemStack, emptySlot))
            	itemStack = null;
            markDirty();
        }

        return itemStack;
    }

	@Override
	public Priority getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	@Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.getHandler().setInventorySlotContents(i, itemstack);
    }
	
	public static boolean areItemStacksEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        return itemStack1 == null && itemStack2 == null || (!(itemStack1 == null || itemStack2 == null) && (itemStack1.getItem() == itemStack2.getItem() && (itemStack1.getItemDamage() == itemStack2.getItemDamage() && (!(itemStack1.stackTagCompound == null && itemStack2.stackTagCompound != null) && (itemStack1.stackTagCompound == null || itemStack1.stackTagCompound.equals(itemStack2.stackTagCompound))))));
    }
	
	@Override
	public TileSortingLocker getUpgradeTile(ItemStack stack){
		if(stack.getItem() == EnhancedInventories.functionUpgrade){
			TileSortingLocker ret = new TileSortingLocker(this.getType());
			ret.woodType = this.woodType;
			ret.alt = this.alt;
			ret.hopper = this.hopper;
			ret.redstone = this.redstone;
			ret.reversed = this.reversed;
			ret.setOrientation(this.orientation);
			ret.setPriority(priority);
			ret.filter = this.filter;
			
			switch(stack.getItemDamage()){
			case 1:
				if(ret.hopper)
					return this;
				ret.hopper = true;
				break;
			case 2:
				if(ret.redstone)
					return this;
				ret.redstone = true;
				break;
			default:
				return this;
			}
			return ret;
		}
		return this;
	}
	
	@Override
	public boolean canUpgrade(ItemStack stack){
		if(stack.getItem() == SortingUpgradeHelper.getUpgradeItem()){
			return false; 
		}
		if(stack.getItem() == EnhancedInventories.sizeUpgrade)
			return false;
		return super.canUpgrade(stack);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
        if (compound.hasKey("priority"))
        {
            setPriority(Priority.values()[compound.getByte("priority")]);
        }
        else
        {
            setPriority(filter.isBlacklisting() ? Priority.LOW : Priority.NORMAL);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
        compound.setByte("priority", (byte) priority.ordinal());
    }

}
