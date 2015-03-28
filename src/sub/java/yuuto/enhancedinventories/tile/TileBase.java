package yuuto.enhancedinventories.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBase extends TileEntity{
	
	protected boolean initialized;
	
	@Override
	public void updateEntity(){
		if(!this.initialized && !this.isInvalid()){
			this.initialize();
		}
	}
	public void initialize(){
		this.initialized = true;
	}
	
	@Override
    public Packet getDescriptionPacket()
    {
		NBTTagCompound nbt = new NBTTagCompound();
		writePacketNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }
	public void writePacketNBT(NBTTagCompound nbt){
		
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		super.onDataPacket(net, pkt);
		NBTTagCompound nbt = pkt.func_148857_g();
		readPacketNBT(nbt);
	}
	public void readPacketNBT(NBTTagCompound nbt){
		
	}

}
