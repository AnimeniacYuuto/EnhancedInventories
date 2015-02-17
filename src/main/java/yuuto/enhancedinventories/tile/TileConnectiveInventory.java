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

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.gui.IConnectedContainer;
import yuuto.yuutolib.block.tile.TileRotatable;

public abstract class TileConnectiveInventory extends TileRotatable implements IInventory{
	
	protected int ticksSinceSync = -1;
    public float prevLidAngle;
    public float lidAngle;
    protected int numUsingPlayers;
    protected ItemStack[] chestContents;
    
    protected EInventoryMaterial type;
    
    protected ForgeDirection partnerDir = ForgeDirection.UNKNOWN;
    protected TileConnectiveInventory partnerTile;
    
    boolean initialized = false;
    boolean needsConnectionUpdate = false;
    boolean disconnected = false;
    
    public boolean redstone;
    
    public TileConnectiveInventory()
    {
        this(EInventoryMaterial.Stone);
    }

    public TileConnectiveInventory(EInventoryMaterial type)
    {
        super();
        this.type = type;
        this.chestContents = new ItemStack[getSizeInventory()];
    }
    
    public TileConnectiveInventory getPartner(){
    	return partnerTile;
    }
    public ForgeDirection getPartnerDir(){
    	return partnerDir;
    }
    public void connectTo(TileConnectiveInventory tile, ForgeDirection from){
    	if(!initialized)
    		return;
    	if(partnerTile != null && !partnerTile.isInvalid())
    		return;
    	partnerTile = tile;
    	partnerDir = from;
    	this.orientation = partnerTile.orientation;
    }
    public void disconnect(){
    	partnerTile = null;
    	partnerDir = ForgeDirection.UNKNOWN;
    	if(!this.worldObj.isRemote){
    		this.disconnected = true;
    		this.markDirty();
    	}
    }
    public abstract boolean isValidForConnection(ItemStack itemBlock);
    public abstract boolean isValidForConnection(TileConnectiveInventory tile);
    public boolean canConnectTo(TileConnectiveInventory tile, ForgeDirection from){
    	if(!initialized)
    		return false;
    	if(partnerTile != null && !partnerTile.isInvalid())
    		return false;
    	if(!isValidForConnection(tile))
    		return false;
    	return getValidConnectionSides().contains(from);
    }
    public abstract List<ForgeDirection> getValidConnectionSides();
    public abstract List<ForgeDirection> getTopSides();
    
    
    public ItemStack[] getContents()
    {
        return chestContents;
    }

    @Override
    public int getSizeInventory()
    {
        if(partnerTile == null)
        	return type.size();
        return type.doubleSize();
    }
    
    @Override
    public String getInventoryName()
    {
        return type.toString();
    }
    
    public EInventoryMaterial getType(){
    	return type;
    }
    
    @Override
    public ItemStack getStackInSlot(int i)
    {
        if(partnerTile == null)
        	return chestContents[i];
        if(this.getTopSides().contains(partnerDir))
        	return i < type.size() ? chestContents[i] : partnerTile.getContents()[i-type.size()];
        return i < type.size() ? partnerTile.getContents()[i]: chestContents[i-type.size()];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int j)
    {
    	boolean flag = false;
    	int i = slot;
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
	    	if (chestContents[i] != null)
	        {
	            if (chestContents[i].stackSize <= j)
	            {
	                ItemStack itemstack = chestContents[i];
	                chestContents[i] = null;
	                markDirty();
	                return itemstack;
	            }
	            ItemStack itemstack1 = chestContents[i].splitStack(j);
	            if (chestContents[i].stackSize == 0)
	            {
	                chestContents[i] = null;
	            }
	            markDirty();
	            return itemstack1;
	        }
	        else
	        {
	            return null;
	        }
    	}else{
    		ItemStack[] contents = partnerTile.getContents();
    		if (contents[i] != null)
	        {
	            if (contents[i].stackSize <= j)
	            {
	                ItemStack itemstack = contents[i];
	                contents[i] = null;
	                markDirty();
	                return itemstack;
	            }
	            ItemStack itemstack1 = contents[i].splitStack(j);
	            if (contents[i].stackSize == 0)
	            {
	            	contents[i] = null;
	            }
	            markDirty();
	            return itemstack1;
	        }
	        else
	        {
	            return null;
	        }
    	}
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack)
    {
    	boolean flag = false;
    	int i = slot;
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
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.type = EInventoryMaterial.values()[nbttagcompound.getInteger("MatType")];
        this.redstone = nbttagcompound.getBoolean("redstone");
        if(this.chestContents.length != getSizeInventory())
        	chestContents = new ItemStack[getSizeInventory()];
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        chestContents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < chestContents.length)
            {
                chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < chestContents.length; i++)
        {
            if (chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
        nbttagcompound.setInteger("MatType", type.ordinal());
        nbttagcompound.setBoolean("redstone", redstone);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj == null)
        {
            return true;
        }
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        if(partnerTile == null)
        	 return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
        return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D || 
        		entityplayer.getDistanceSq((double) partnerTile.xCoord + 0.5D, (double) partnerTile.yCoord + 0.5D, (double) partnerTile.zCoord + 0.5D) <= 64D;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if(!initialized && !this.isInvalid())
        	initialize();
        // Resynchronize clients with the server state
        if (worldObj != null && !this.worldObj.isRemote && this.numUsingPlayers != 0 && (this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            float var1 = 5.0F;
            @SuppressWarnings("unchecked")
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)((float)this.xCoord - var1), (double)((float)this.yCoord - var1), (double)((float)this.zCoord - var1), (double)((float)(this.xCoord + 1) + var1), (double)((float)(this.yCoord + 1) + var1), (double)((float)(this.zCoord + 1) + var1)));

            for (EntityPlayer var4 : var2) {
                if (var4.openContainer instanceof IConnectedContainer) {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (worldObj != null && !worldObj.isRemote && ticksSinceSync < 0)
        {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 3, ((numUsingPlayers << 3) & 0xF8) | (this.orientation.ordinal() & 0x7));
            //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            //this.ticksSinceSync = 20;
        }

        this.ticksSinceSync++;
        prevLidAngle = lidAngle;
        float f = 0.1F;
        if (getTotalUsingPlayers() > 0 && lidAngle == 0.0F && (this.partnerTile == null || this.getTopSides().contains(partnerDir)))
        {
            double d = (double) xCoord + 0.5D;
            double d1 = (double) zCoord + 0.5D;
            worldObj.playSoundEffect(d, (double) yCoord + 0.5D, d1, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }
        if (getTotalUsingPlayers() == 0 && lidAngle > 0.0F || getTotalUsingPlayers() > 0 && lidAngle < 1.0F)
        {
            float f1 = lidAngle;
            if (getTotalUsingPlayers() > 0)
            {
                lidAngle += f;
            }
            else
            {
                lidAngle -= f;
            }
            if (lidAngle > 1.0F)
            {
                lidAngle = 1.0F;
            }
            float f2 = 0.5F;
            if (lidAngle < f2 && f1 >= f2 && (this.partnerTile == null || this.getTopSides().contains(partnerDir)))
            {
                double d2 = (double) xCoord + 0.5D;
                double d3 = (double) zCoord + 0.5D;
                worldObj.playSoundEffect(d2, (double) yCoord + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }
            if (lidAngle < 0.0F)
            {
                lidAngle = 0.0F;
            }
            
            //this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j)
    {
    	if (i == 1)
        {
            numUsingPlayers = j;
        }
        else if (i == 2)
        {
            orientation = ForgeDirection.getOrientation(j);
        }
        else if (i == 3)
        {
        	orientation = ForgeDirection.getOrientation(j & 0x7);
            numUsingPlayers = (j & 0xF8) >> 3;
        }
        return true;
    }

    @Override
    public void openInventory()
    {
        if (worldObj == null) return;
        numUsingPlayers++;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 1, numUsingPlayers);
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
    }

    @Override
    public void closeInventory()
    {
        if (worldObj == null) return;
        numUsingPlayers--;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 1, numUsingPlayers);
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
    	boolean flag = false;
    	int i = slot;
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
	    	if (this.chestContents[i] != null)
	        {
	            ItemStack var2 = this.chestContents[i];
	            this.chestContents[i] = null;
	            return var2;
	        }
	        else
	        {
	            return null;
	        }
    	}else{
    		if (partnerTile.getContents()[i] != null)
	        {
	            ItemStack var2 = partnerTile.getContents()[i];
	            partnerTile.getContents()[i] = null;
	            return var2;
	        }
	        else
	        {
	            return null;
	        }
    	}
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }
    
    public void initialize(){
    	initialized = true;
    	checkConnections();
    }
    public void checkConnections(){
    	for(ForgeDirection dir : getValidConnectionSides()){
    		TileEntity tile = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileConnectiveInventory))
    			continue;
    		TileConnectiveInventory inv = (TileConnectiveInventory)tile;
    		if(!inv.isValidForConnection(this))
    			continue;
    		if(!inv.canConnectTo(this, dir.getOpposite()) || !canConnectTo(inv, dir))
    			continue;
    		if(this.orientation == dir || this.orientation == dir.getOpposite()){
    			for(ForgeDirection dir2 : getValidConnectionSides()){
    				if(dir2 == dir || dir2 == dir.getOpposite())
    					continue;
    				this.setOrientation(dir2);
    				//break;
    			}
    		}
    		inv.connectTo(this, dir.getOpposite());
    		connectTo(inv, dir);
    		break;
    	}
    }
    
    @Override
    public void invalidate(){
    	super.invalidate();
    	if(this.partnerTile != null){
    		this.partnerTile.disconnect();
    	}
    	this.disconnect();
    }
    
    public int getUsingPlayers(){
    	return this.numUsingPlayers;
    }
    public int getTotalUsingPlayers(){
    	if(partnerTile == null)
    		return this.numUsingPlayers;
    	return this.numUsingPlayers+partnerTile.getUsingPlayers();
    }
    
    @Override
    public void markDirty(){
    	super.markDirty();
    	this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public void markDirty(boolean connection){
    	this.needsConnectionUpdate = true;
    	markDirty();
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
    	NBTTagCompound tagCompound = new NBTTagCompound();
	    writeToNBT(tagCompound);
	    tagCompound.setFloat("pkt.prevLidAngle", this.prevLidAngle);
	    tagCompound.setFloat("pkt.lidAngle", this.lidAngle);
	    tagCompound.setBoolean("pkt.needConUpdate", this.needsConnectionUpdate);
	    tagCompound.setBoolean("pkt.disconnected", this.disconnected);
	    this.needsConnectionUpdate = false;
	    this.disconnected = false;
	    return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tagCompound);
    }
	
    @Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
		NBTTagCompound nbt = pkt.func_148857_g();
		readFromNBT(nbt);
		this.prevLidAngle = nbt.getFloat("pkt.prevLidAngle");
		this.lidAngle = nbt.getFloat("pkt.lidAngle");
		if(nbt.getBoolean("pkt.disconnected"))
			this.disconnect();
		if(nbt.getBoolean("pkt.needConUpdate")){
			this.checkConnections();
		}
		this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }
    
    public boolean canUpgrade(ItemStack stack){
    	if(this.getTotalUsingPlayers() > 0)
    		return false;
    	if(stack.getItem() == EnhancedInventories.sizeUpgrade){
    		return this.type != EInventoryMaterial.Obsidian && EInventoryMaterial.values()[stack.getItemDamage()+1].getTier() == this.getType().getTier()+1;
    	}
    	if(stack.getItem() == EnhancedInventories.functionUpgrade && stack.getItemDamage() == 2)
    		return !redstone;
    	return false;
    }
    
    public abstract TileConnectiveInventory getUpgradeTile(ItemStack stack);
    
    public void setContents(ItemStack[] origin, boolean removeOld){
    	for(int i = 0; i < origin.length; i++){
    		if(i > chestContents.length)
    			break;
    		chestContents[i] = origin[i];
    		if(removeOld)
    			origin[i] = null;
    	}
    }

}
