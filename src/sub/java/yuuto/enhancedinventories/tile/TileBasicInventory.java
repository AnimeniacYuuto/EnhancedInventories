package yuuto.enhancedinventories.tile;

import java.util.List;

import com.mojang.authlib.GameProfile;

import cofh.api.tileentity.ISecurable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.inventory.TileInventory;
import yuuto.enhancedinventories.tile.upgrades.EUpgrade;
import yuuto.yuutolib.block.tile.IRotatable;
import yuuto.yuutolib.utill.InventoryWrapper;

public abstract class TileBasicInventory extends TileEnhancedInventory implements IRotatable{

	protected ForgeDirection orientation = ForgeDirection.NORTH;
	protected ForgeDirection connection;
	
	protected TileInventory combinedInventory;
	protected TileBasicInventory partner;
	
	public TileBasicInventory(){
		super();
	}
	public TileBasicInventory(int tier){
		super(tier);
	}
	
	@Override
	public void initialize(){
		super.initialize();
		checkConnections();
	}
	
	public void checkConnections(){
		if(worldObj.isRemote)
			return;
		ForgeDirection foundDirection = ForgeDirection.UNKNOWN;
		TileBasicInventory foundConnection = null;
		for(ForgeDirection dir : getValidConnections()){
			TileEntity te = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
			if(!(te instanceof TileBasicInventory))
				continue;
			TileBasicInventory potentialConnection = (TileBasicInventory)te;
			if(!canConnect(potentialConnection, dir) || !potentialConnection.canConnect(this, dir.getOpposite()))
				continue;
			foundConnection = potentialConnection;
			foundDirection = dir;
			if(foundConnection != null)
				break;
		}
		if(foundConnection == null)
			return;
		this.connectTo(foundConnection, foundDirection);
		foundConnection.connectTo(this, foundDirection.getOpposite());
		
		this.markDirty();
		foundConnection.markDirty();
	}
	public boolean canConnect(TileBasicInventory tile, ForgeDirection dir){
		if(!initialized)
			return false;
		if(!isValidForConnection(tile))
			return false;
		if(!tile.upgradesMatch(upgrades))
			return false;
		return true;
	}
	public boolean isValidForConnection(TileBasicInventory tile){
		if(this.tier != tile.getTier())
			return false;
		if(this.hasUpgrade(EUpgrade.Alternate) != tile.hasUpgrade(EUpgrade.Alternate))
			return false;
		return true;
	}
	public boolean isValidForConnection(ItemStack tileStack){
		if(tileStack.getItem() != this.getItem())
			return false;
		if(this.tier != tileStack.getItemDamage())
			return false;
		if(hasUpgrade(EUpgrade.Alternate) != tileStack.getTagCompound().getBoolean("alt"))
			return false;
		return true;
	}
	
	public abstract Item getItem();
	
	public void connectTo(TileBasicInventory tile, ForgeDirection dir){
		this.connection = dir;
		combinedInventory = new TileInventory(this, 
				(isMain() ? this : tile),
                (isMain() ? tile : this));
	}
	public void disconnect(){
		if(!isConnected())
			return;
		this.connection = ForgeDirection.UNKNOWN;
		TileBasicInventory oldPartner = this.partner;
		this.partner = null;
		this.combinedInventory = null;
		oldPartner.disconnect();
		this.markDirty();
	}
	
	public abstract List<ForgeDirection> getValidConnections();
	public abstract List<ForgeDirection> getMainConnections();
	public boolean isMain(){
		if(!isConnected())
			return true;
		return getMainConnections().contains(connection);
	}
	public boolean isConnected(){
		return connection != ForgeDirection.UNKNOWN && combinedInventory != null && partner != null;
	}
	public TileBasicInventory getPartner(){
		return this.partner;
	}
	public ForgeDirection getPartnerDir(){
		return connection;
	}
	
	public void updateHopper(){
		suckItems(getSuckDirection());
		pushItems(getPushDirection());
	}
	public void suckItems(ForgeDirection dir){
		TileEntity tile = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
		if(tile == null || !(tile instanceof IInventory))
			return;
		ISidedInventory inv = InventoryWrapper.getWrapper((IInventory)tile);
		if(inv.getSizeInventory() < 1)
			return;
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
	public void pushItems(ForgeDirection dir){
		TileEntity tile = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
		if(tile == null || !(tile instanceof IInventory))
			return;
		ISidedInventory tar = InventoryWrapper.getWrapper((IInventory)tile);
		if(tar.getSizeInventory() < 1)
			return;
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
	public abstract ForgeDirection getSuckDirection();
	public abstract ForgeDirection getPushDirection();
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
	
	@Override
	public void updateEntity(){
		super.updateEntity();
		
		if(!isMain())
			return;
		
		double x = xCoord + 0.5;
		double y = yCoord + 0.5;
		double z = zCoord + 0.5;
		
		if(isConnected())
			this.lidAngle = Math.max(lidAngle, partner.lidAngle);
		
		float pitch = worldObj.rand.nextFloat() * 0.1F + 0.9F;
		
		// Play sound when opening
		if ((lidAngle > 0.0F) && (prevLidAngle == 0.0F))
			worldObj.playSoundEffect(x, y, z, "random.chestopen", 0.5F, pitch);
		// Play sound when closing
		if ((lidAngle < 0.5F) && (prevLidAngle >= 0.5F))
			worldObj.playSoundEffect(x, y, z, "random.chestclosed", 0.5F, pitch);
		
		if(hasUpgrade(EUpgrade.Hopper))
			updateHopper();
	}
	
	@Override
	public IInventory getInventory(){
		if(isConnected())
			return combinedInventory;
		return invHandler;
	}
	@Override
	public int getRedstonePower(){
		if(!hasUpgrade(EUpgrade.Redstone))
			return 0;
		int power = this.numUsingPlayers;
		if(isConnected())
			power += this.partner.countUsingPlayers();
		return MathHelper.clamp_int(power, 0, 15);
	}
	
	@Override
	public ForgeDirection getOrientation() {
		return orientation;
	}
	@Override
	public ForgeDirection setOrientation(ForgeDirection dir) {
		this.orientation = dir;
		if(this.worldObj.isRemote)
			worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.blockType, 3, this.orientation.ordinal());
		return this.orientation;
	}
	@Override
	public ForgeDirection rotateAround(ForgeDirection axis) {
		this.orientation = this.orientation.getRotation(axis);
		if(this.worldObj.isRemote)
			worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.blockType, 3, this.orientation.ordinal());
		return this.orientation;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setByte("orientation", (byte)orientation.ordinal());
	}
	@Override
	public void writePacketNBT(NBTTagCompound nbt){
		super.writePacketNBT(nbt);
		nbt.setByte("orientation", (byte)orientation.ordinal());
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);	
		this.setOrientation(ForgeDirection.getOrientation(nbt.getInteger("orientation")));
	}
	@Override
	public void readPacketNBT(NBTTagCompound nbt){
		super.readPacketNBT(nbt);
		this.setOrientation(ForgeDirection.getOrientation(nbt.getInteger("orientation")));
	}
	
	@Override
    public boolean receiveClientEvent(int i, int j){
		switch(i){
		case 3:
			this.setOrientation(ForgeDirection.getOrientation(j));
			return true;
		default:
			return super.receiveClientEvent(i, j);
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
		return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord-1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
    }
	
}