package yuuto.enhancedinventories.tile;

import yuuto.enhancedinventories.tile.upgrades.EUpgrade;

import com.mojang.authlib.GameProfile;

import cofh.api.tileentity.ISecurable;
import cofh.api.tileentity.ISecurable.AccessMode;
import cofh.core.util.SocialRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileSecurable extends TileBase implements ISecurable{

	protected AccessMode accessMode;
	protected GameProfile owner;
	
	@Override
	public boolean canPlayerAccess(String playerName) {
		if(isAccessible())
			return true;
		if(owner == null)
			return true;
		if(accessMode == AccessMode.RESTRICTED)
			return SocialRegistry.playerHasAccess(playerName, owner);
		return playerName.equals(owner.getName());
	}
	public boolean isAccessible(){
		return !hasUpgrade(EUpgrade.Security) || (accessMode == AccessMode.PUBLIC);
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
		if(owner == null)
			return null;
		return owner.getName();
	}

	@Override
	public boolean setAccess(AccessMode mode) {
		this.accessMode = mode;
		worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.blockType, 1, this.accessMode.ordinal());
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
	public boolean setOwnerName(String ownerName) {
		if(this.owner != null)
			return false;
		EntityPlayer player = worldObj.getPlayerEntityByName(ownerName);
		if(player == null)
			return false;
		this.owner = player.getGameProfile();
		return true;
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		//nbt.setBoolean("secured", secured);
		if(hasUpgrade(EUpgrade.Security)){
			nbt.setByte("accessMode", (byte)accessMode.ordinal());
			if(owner != null)
				nbt.setString("owner", owner.getName());
		}
	}
	@Override
	public void writePacketNBT(NBTTagCompound nbt){
		super.writePacketNBT(nbt);
		//nbt.setBoolean("secured", secured);
		if(hasUpgrade(EUpgrade.Security)){
			nbt.setByte("accessMode", (byte)accessMode.ordinal());
			if(owner != null)
				nbt.setString("owner", owner.getName());
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		//this.secured = nbt.getBoolean("secured");
		if(hasUpgrade(EUpgrade.Security)){
			this.accessMode = AccessMode.values()[nbt.getInteger("accessMode")];
			this.setOwnerName(nbt.getString("owner"));
		}
	}
	@Override
	public void readPacketNBT(NBTTagCompound nbt){
		super.readPacketNBT(nbt);
		//this.secured = nbt.getBoolean("secured");
		if(hasUpgrade(EUpgrade.Security)){
			this.accessMode = AccessMode.values()[nbt.getInteger("accessMode")];
			this.setOwnerName(nbt.getString("owner"));
		}
	}
	
	@Override
    public boolean receiveClientEvent(int i, int j){
		switch(i){
		case 1:
			this.accessMode = AccessMode.values()[j];
			return true;
		default:
			return super.receiveClientEvent(i, j);
		}
    }

	

}

