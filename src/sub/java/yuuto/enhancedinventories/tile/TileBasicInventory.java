package yuuto.enhancedinventories.tile;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;

import cofh.api.tileentity.ISecurable;
import cofh.core.util.SocialRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.inventory.InventoryInternalBasic;
import yuuto.yuutolib.block.tile.TileRotatable;

public class TileBasicInventory extends TileRotatable implements IInventory, ISecurable{

	//protected TileBasicInventory partner;
	//protected ForgeDirection partnerDirection;
	protected IInventory internalInventory;
	protected IInventory combinedInventory;
	protected int tier;
	
	protected boolean secured;
	protected GameProfile owner;
	protected AccessMode accessMode;
	
	
	public TileBasicInventory(int tier){
		super();
		this.tier = tier;
		this.internalInventory = new InventoryInternalBasic(this);
	}
	public TileBasicInventory(){
		super();
	}
	
	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}
	
	public int getBaseSize(){
		return 27;
	}
	public int getTier(){
		return tier;
	}
	
	@Override
	public boolean canPlayerAccess(String name) {
		if(accessMode == AccessMode.PUBLIC)
			return true;
		if(accessMode == AccessMode.RESTRICTED)
			return SocialRegistry.playerHasAccess(name, owner);
		return name.equals(owner.getName()); 
	}

	@Override
	public AccessMode getAccess() {
		return accessMode;
	}

	@Override
	public GameProfile getOwner() {
		return owner;
	}

	@Override
	public String getOwnerName() {
		return owner.getName();
	}

	@Override
	public boolean setAccess(AccessMode mode) {
		accessMode = mode;
		return true;
	}

	@Override
	public boolean setOwner(GameProfile owner) {
		if(this.owner != null)
			return false;
		this.owner = owner;
		return true;
	}

	@Override
	public boolean setOwnerName(String arg0) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		if(secured)
			return 0;
		if(combinedInventory != null)
			return combinedInventory.getSizeInventory();
		return internalInventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		if(secured)
			return null;
		if(combinedInventory != null)
			return combinedInventory.getStackInSlot(p_70301_1_);
		return internalInventory.getStackInSlot(p_70301_1_);
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if(secured)
			return null;
		if(combinedInventory != null)
			return combinedInventory.decrStackSize(p_70298_1_, p_70298_2_);
		return internalInventory.decrStackSize(p_70298_1_, p_70298_2_);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if(secured)
			return null;
		if(combinedInventory != null)
			return combinedInventory.getStackInSlotOnClosing(p_70304_1_);
		return internalInventory.getStackInSlotOnClosing(p_70304_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if(secured)
			return;
		if(combinedInventory != null)
			combinedInventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
		else
			internalInventory.setInventorySlotContents(p_70299_1_, p_70299_2_);
	}

	@Override
	public String getInventoryName() {
		return "container";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if(!(this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D))
			return false;
		if(!secured)
			return true;
		return canPlayerAccess(player.getGameProfile().getName());
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return !secured;
	}
	

}
