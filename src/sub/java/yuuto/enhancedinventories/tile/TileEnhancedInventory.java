package yuuto.enhancedinventories.tile;

import cofh.api.tileentity.ISecurable.AccessMode;
import yuuto.enhancedinventories.inventory.TileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;

public class TileEnhancedInventory extends TileSecurable implements IInventory{

	protected ItemStack[] inv;
	protected TileInventory invHandler;
	protected int tier;
	
	protected int numUsingPlayers;
	protected float prevLidAngle;
	protected float lidAngle;
	
	protected boolean redstone;
	
	public TileEnhancedInventory(){
		this(0);
	}
	public TileEnhancedInventory(int tier){
		this.tier = tier;
		this.resetInventory();
	}
	public void resetInventory(){
		int size = getBaseSize();
		this.inv = new ItemStack[size*(this.tier+1)];
		setInventory();
	}
	public void setInventory(){
		if(inv == null)
			return;
		invHandler = new TileInventory(this);
	}
	public ItemStack[] getContents(){
		return inv;
	}
	
	public int getTier(){
		return tier;
	}
	public int getBaseSize(){
		return 27;
	}
	public TileInventory getInventory(){
		return invHandler;
	}
	public void setUsingPlayers(int amount){
		this.numUsingPlayers = amount;
		worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.blockType, 2, this.numUsingPlayers);
	}
	public int countUsingPlayers(){
		return this.numUsingPlayers;
	}
	public int getRedstonePower(){
		return this.redstone ? MathHelper.clamp_int(this.numUsingPlayers, 0, 15) : 0;
	}
	public int getComparatorInputOverride(){
		return Container.calcRedstoneFromInventory(getInventory());
	}
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		prevLidAngle = lidAngle;
		if (numUsingPlayers > 0) {
			if (lidAngle < 1.0F) lidAngle = Math.min(1.0F, lidAngle + 0.1f);
		} else if (lidAngle > 0.0F) lidAngle = Math.max(0.0F, lidAngle - 0.1f);
	}
	
	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return this.isAccessible() ? this.getInventory().getSizeInventory() : 0;
	}
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return this.isAccessible() ? this.getInventory().getStackInSlot(p_70301_1_) : null;
	}
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return this.isAccessible() ? this.getInventory().decrStackSize(p_70298_1_, p_70298_2_) : null;
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return this.isAccessible() ? this.getInventory().getStackInSlotOnClosing(p_70304_1_) : null;
	}
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if(this.isAccessible())
			this.getInventory().setInventorySlotContents(p_70299_1_, p_70299_2_);
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(!(entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D))
			return false;
		return this.canPlayerAccess(entityplayer.getGameProfile().getName());
	}
	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return this.isAccessible() ? this.getInventory().isItemValidForSlot(p_94041_1_, p_94041_2_) : false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setByte("tier", (byte) tier);
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < this.inv.length; i++){
			if(this.inv[i] == null)
				continue;
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte) i);
            inv[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
		}
		nbt.setBoolean("redstone", redstone);
	}
	@Override
	public void writePacketNBT(NBTTagCompound nbt){
		super.writePacketNBT(nbt);
		nbt.setInteger("usingPlayers", numUsingPlayers);
		nbt.setBoolean("redstone", redstone);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		this.tier = nbt.getInteger("tier");
		this.resetInventory();
		NBTTagList nbttaglist = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < inv.length)
            {
                inv[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        this.redstone = nbt.getBoolean("redstone");
		
	}
	@Override
	public void readPacketNBT(NBTTagCompound nbt){
		super.readPacketNBT(nbt);
		this.numUsingPlayers = nbt.getInteger("usingPlayers");
		this.redstone = nbt.getBoolean("redstone");
	}
	
	@Override
    public boolean receiveClientEvent(int i, int j){
		switch(i){
		case 2:
			this.numUsingPlayers = j;
			return true;
		default:
			return super.receiveClientEvent(i, j);
		}
    }

}
