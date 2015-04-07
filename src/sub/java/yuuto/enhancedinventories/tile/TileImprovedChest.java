package yuuto.enhancedinventories.tile;

import java.util.ArrayList;
import java.util.List;

import yuuto.enhancedinventories.materials.CoreMaterial;
import yuuto.enhancedinventories.materials.FrameMaterial;
import yuuto.enhancedinventories.materials.MaterialHelper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileImprovedChest extends TileBasicInventory{

	static List<ForgeDirection> validDirections;
	static List<ForgeDirection> topDirections;
	
	public CoreMaterial coreMat;
	public FrameMaterial frameMat;
	public int woolColor;
	public boolean isPainted;
	
	static{
		validDirections = new ArrayList<ForgeDirection>();
		topDirections = new ArrayList<ForgeDirection>();
		
		validDirections.add(ForgeDirection.NORTH);
		validDirections.add(ForgeDirection.SOUTH);
		validDirections.add(ForgeDirection.EAST);
		validDirections.add(ForgeDirection.WEST);
		topDirections.add(ForgeDirection.NORTH);
		topDirections.add(ForgeDirection.WEST);
	}
	
	public TileImprovedChest(){
		super();
	}
	public TileImprovedChest(int tier){
		super(tier);
	}
	
	@Override
	public boolean isValidForConnection(TileBasicInventory tile) {
		if(!(tile instanceof TileImprovedChest))
			return false;
		TileImprovedChest chest = (TileImprovedChest)tile;
		if(super.isValidForConnection(tile))
			return false;
		if(!this.coreMat.matches(chest.coreMat))
			return false;
		if(this.frameMat != chest.frameMat)
			return false;
		if(this.woolColor != chest.woolColor)
			return false;
		return true;
	}

	@Override
	public boolean isValidForConnection(ItemStack tileStack) {
		if(!super.isValidForConnection(tileStack))
			return false;
		if(!this.coreMat.matches(MaterialHelper.readCoreMaterial(tileStack)))
			return false;
		if(this.frameMat != MaterialHelper.readFrameMaterial(tileStack))
			return false;
		if(this.woolColor != MaterialHelper.readColor(tileStack))
			return false;
		return true;
	}
	@Override
	public boolean canConnect(TileBasicInventory tile, ForgeDirection dir) {
		if(!(tile instanceof TileImprovedChest))
			return false;
		return super.canConnect(tile, dir);
	}
	@Override
	public Item getItem() {
		return null;
	}

	@Override
	public List<ForgeDirection> getValidConnections() {
		return validDirections;
	}

	@Override
	public List<ForgeDirection> getMainConnections() {
		return topDirections;
	}

	@Override
	public ForgeDirection getSuckDirection() {
		return ForgeDirection.UP;
	}

	@Override
	public ForgeDirection getPushDirection() {
		return ForgeDirection.DOWN;
	}



}
