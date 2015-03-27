package yuuto.enhancedinventories.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import yuuto.enhancedinventories.tile.TileEnhancedInventory;
import yuuto.enhancedinventories.tile.TileSecurable;

public class TileInventory extends InventoryBase{

	TileEnhancedInventory mainTile;
	TileEnhancedInventory[] tiles;
	IInventory inv;
	int usingPlayers;
	
	public TileInventory(TileEnhancedInventory mainTile, TileEnhancedInventory[] tiles, 
			IInventory inv){
		this.mainTile = mainTile;
		this.tiles = tiles;
		this.inv = inv;
	}
	public TileInventory(TileEnhancedInventory mainTile, TileEnhancedInventory... tiles){
		this(mainTile, tiles, new InventoryMerged(getContents(tiles)));
	}
	public TileInventory(TileEnhancedInventory tile){
		this(tile, new TileEnhancedInventory[]{tile}, new InventoryMerged(tile.getContents()));
	}
	public static ItemStack[][] getContents(TileEnhancedInventory[] tiles){
		ItemStack[][] mergedInvs = new ItemStack[tiles.length][];
		for(int i = 0; i < tiles.length; i++){
			mergedInvs[i] = tiles[i].getContents();
		}
		return mergedInvs;
	}

	@Override
	public int getSizeInventory() {
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return inv.getStackInSlot(p_70301_1_);
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		inv.setInventorySlotContents(p_70299_1_, p_70299_2_);		
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
	public void markDirty() {
		inv.markDirty();
		for (TileEnhancedInventory te : tiles)
			te.markDirty();
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		for (TileEnhancedInventory te : tiles)
			if(!te.canPlayerAccess(p_70300_1_.getGameProfile().getName()))
				return false;
		return true;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

}
