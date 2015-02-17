package yuuto.enhancedinventories.item;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;
import yuuto.yuutolib.item.ModItemMulti;

public class ItemFunctionUpgrade extends ModItemMulti{

	public ItemFunctionUpgrade() {
		super(EnhancedInventories.tab, "EnhancedInventories", "functionUpgrade", ".base", ".hopper", ".redstone");
	}
	
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
		if(world.isRemote || stack.getItemDamage() == 0 || !player.isSneaking())
			return false;
		TileEntity tile = world.getTileEntity(X, Y, Z);
		if(tile == null || !(tile instanceof TileConnectiveInventory))
			return false;
		TileConnectiveInventory oldTile = (TileConnectiveInventory) tile;
		if(!oldTile.canUpgrade(stack))
			return false;
		TileConnectiveInventory newTile = oldTile.getUpgradeTile(stack);
		if(newTile == null || newTile == oldTile ||!canUpgrade(oldTile, newTile, world, X, Y, Z))
			return false;
		newTile.setContents(oldTile.getContents(), true);
		oldTile.invalidate();
		world.setTileEntity(X, Y, Z, newTile);
		world.setBlockMetadataWithNotify(X, Y, Z, world.getBlockMetadata(X, Y, Z), 3);
		oldTile.invalidate();
		newTile.disconnect();
		newTile.checkConnections();
		newTile.markDirty(true);
		if(!player.capabilities.isCreativeMode){
			stack.stackSize--;
			player.inventoryContainer.detectAndSendChanges();
		}
		return true;
    }
	
	
	public boolean canUpgrade(TileConnectiveInventory oldTile, TileConnectiveInventory newTile, World world, int x, int y, int z){
		List<ForgeDirection> dirs = newTile.getValidConnectionSides();
    	int chests = 0;
    	for(ForgeDirection dir : dirs){
    		TileEntity tile = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileConnectiveInventory))
    			continue;
    		TileConnectiveInventory inv = (TileConnectiveInventory)tile;
    		if(!inv.isValidForConnection(newTile))
    			continue;
    		if(inv.getPartner() != null)
    			return false;
    		chests++;
    	}
    	if(chests > 1)
    		return false;
    	return true;
	}

}
