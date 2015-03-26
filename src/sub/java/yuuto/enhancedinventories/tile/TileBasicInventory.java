package yuuto.enhancedinventories.tile;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.yuutolib.block.tile.IRotatable;

public abstract class TileBasicInventory extends TileEnhancedInventory implements IRotatable{

	protected ForgeDirection orientation = ForgeDirection.getOrientation(2);
	
	protected TileBasicInventory partner;
	protected ForgeDirection conectionSide;
	protected boolean initialized;
	
	
	public abstract List<ForgeDirection> getConnectableSides();
	public abstract List<ForgeDirection> getMasterSides();
	
	public abstract boolean canConnectTo(TileBasicInventory tile);
	public IInventory getInternalInventory(){
		return this.internalInventory;
	}
	public void connectTo(TileBasicInventory tile, ForgeDirection from, IInventory combined){
		
	}
	
	public void initialize(){
		this.initialized = true;
	}
	public void checkForConnections(){
		if(partner != null && !partner.isInvalid()){
			
		}else{
			for(ForgeDirection dir : getConnectableSides()){
				TileEntity tile = this.worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
				if(!(tile instanceof TileBasicInventory))
					continue;
				TileBasicInventory inv = (TileBasicInventory)tile;
				if(!inv.canConnectTo(this) || !this.canConnectTo(inv))
					continue;
			}
		}
	}
	
	@Override
	public void updateEntity(){
		if(!initialized && !this.isInvalid()){
			this.initialize();
		}
	}
	
	@Override
	public ForgeDirection getOrientation(){
		return orientation;
	}
	@Override
	public ForgeDirection setOrientation(ForgeDirection dir){
		orientation = dir;
		return orientation;
	}
	@Override
	public ForgeDirection rotateAround(ForgeDirection axis){
		orientation = orientation.getRotation(axis);
		return orientation;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		orientation = ForgeDirection.getOrientation(nbt.getInteger("Orientation"));	
	}
	@Override
	public void readNbtPacket(NBTTagCompound nbt){
		super.readNbtPacket(nbt);
		orientation = ForgeDirection.getOrientation(nbt.getInteger("Orientation"));
	}
	
	@Override 
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("Orientation", orientation.ordinal());
	}
	
	@Override
	public void writeNbtPacket(NBTTagCompound nbt){
		super.writeNbtPacket(nbt);
		nbt.setByte("Orientation", (byte)orientation.ordinal());
	}

}
