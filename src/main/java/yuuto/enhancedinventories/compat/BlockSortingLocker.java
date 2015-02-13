package yuuto.enhancedinventories.compat;

import com.dynious.refinedrelocation.api.APIUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.tile.BlockLocker;

public class BlockSortingLocker extends BlockLocker{
	
	public BlockSortingLocker() {
		super("sortingLocker");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSortingLocker(EInventoryMaterial.values()[meta]);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
        if (world.isRemote)
        {
            return true;
        }

        if (player.isSneaking())
        {
        	APIUtils.openFilteringGUI(player, world, x, y, z);
            return true;
        }

        return super.onBlockActivated(world, x, y, z, player, i1, f1, f2, f3);
    }
    

}
