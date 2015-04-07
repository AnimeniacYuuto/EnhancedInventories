package yuuto.enhancedinventories.tile.upgrades;

import yuuto.enhancedinventories.tile.TileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UpgradeHelper {
	
	public static void addUpgrade(ItemStack stack, EUpgrade upgrade){}
	public static EUpgrade getUpgrade(ItemStack stack){
		return EUpgrade.Unknown;
	}
	
	public static boolean upgradeTile(ItemStack stack, TileBase oldTile, 
			World world, int x, int y, int z){
		EUpgrade upgrade = getUpgrade(stack);
		if(upgrade == EUpgrade.Unknown)
			return false;
		if(!oldTile.canApplyUpgrade(upgrade, stack))
			return false;
		TileBase retTile = oldTile.getUpgradedTile(upgrade, stack);
		if(retTile == null || retTile == oldTile || !canPlaceTile(retTile, world, x, y, z))
			return false;
		retTile.finalizeUpgrade(upgrade, stack, oldTile);
		oldTile.invalidate();
		world.setTileEntity(x, y, z, retTile);
		world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z), 3);
		retTile.markDirty();
		return true;
	}
	public static boolean canPlaceTile(TileBase retTile, 
			World world, int x, int y, int z){
		return true;
	}

}
