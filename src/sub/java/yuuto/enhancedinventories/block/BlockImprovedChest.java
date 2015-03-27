package yuuto.enhancedinventories.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import yuuto.enhancedinventories.tile.TileBasicInventory;
import yuuto.enhancedinventories.tile.TileImprovedChest;
import yuuto.enhancedinventories.tile.TileImprovedChestOld;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockImprovedChest extends BlockBasicInventory{

	public BlockImprovedChest(String unlocName) {
		super(Material.wood, unlocName);
	}
	
	@Override
	public int getRenderType(){
        return 22;
    }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
		
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileImprovedChest(p_149915_2_);
	}

	@Override
	public void setTags(TileBasicInventory tile, ItemStack stack) {		
	}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                ItemStack stack = new ItemStack(item, 1, damageDropped(metadata));
                TileImprovedChest chest = (TileImprovedChest)world.getTileEntity(x, y, z);
            	ret.add(stack);
            }
        }
        return ret;
    }
	
	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return super.getPickBlock(target, world, x, y, z);
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        TileImprovedChestOld tile = (TileImprovedChestOld)p_149719_1_.getTileEntity(p_149719_2_, p_149719_3_, p_149719_4_);
    	if(tile.getPartner() == null){
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		return;
    	}
    	switch(tile.getPartnerDir()){
    	case NORTH:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	case SOUTH:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
    		break;
    	case WEST:
    		this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	case EAST:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
    		break;
    	default:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	}
    }

}
