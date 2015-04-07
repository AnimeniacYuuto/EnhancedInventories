package yuuto.enhancedinventories.tile;

import java.util.ArrayList;
import java.util.List;

import yuuto.enhancedinventories.tile.upgrades.EUpgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileBase extends TileEntity{
	
	protected boolean initialized;
	protected final List<EUpgrade> upgrades = new ArrayList<EUpgrade>();
	
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
		if(upgrades.size() < 1)
			return;
		byte[] ups = new byte[upgrades.size()];
		for(int i = 0; i < ups.length; i++){
			ups[i] = (byte)upgrades.get(i).ordinal();
		}
		nbt.setByteArray("upgrades", ups);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		super.onDataPacket(net, pkt);
		NBTTagCompound nbt = pkt.func_148857_g();
		readPacketNBT(nbt);
	}
	public void readPacketNBT(NBTTagCompound nbt){
		if(!nbt.hasKey("upgrades"))
			return;
		byte[] ups = nbt.getByteArray("upgrades");
		for(byte b : ups){
			EUpgrade u = EUpgrade.get(b);
			if(!upgrades.contains(u))
				upgrades.add(u);
		}
	}

	public boolean hasUpgrade(EUpgrade upgrade){
		return upgrades.contains(upgrade);
	}
	public void getUpgradeDrops(ItemStack blockDrop, List<ItemStack> drops){
		drops.add(blockDrop);
		for(EUpgrade u : upgrades){
			if(u.getDrop() == null)
				continue;
			drops.add(u.getDrop().copy());
		}
	}
	public boolean upgradesMatch(List<EUpgrade> target){
		if(target.size() != upgrades.size())
			return false;
		return true;
	}
	public boolean canApplyUpgrade(EUpgrade upgrade, ItemStack stack){
		return !upgrades.contains(upgrade);
	}
	public TileBase getUpgradedTile(EUpgrade upgrade, ItemStack stack){
		return new TileBase();
	}
	public void finalizeUpgrade(EUpgrade upgrade, ItemStack stack, 
			TileBase oldTile){}
}
