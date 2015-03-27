package yuuto.enhancedinventories.block;

import yuuto.enhancedinventories.tile.TileBasicInventory;
import yuuto.yuutolib.block.tile.IRotatable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockBasicInventory extends BlockEnhancedInventory{

	public BlockBasicInventory(Material mat, String unlocName) {
		super(mat, unlocName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis){
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		if(!(tile instanceof IRotatable))
			return false;
		if(axis != ForgeDirection.UP && axis != ForgeDirection.DOWN)
			return false;
		((IRotatable)tile).rotateAround(axis);
		return true;
    }
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof TileBasicInventory))
			return;
		setTags((TileBasicInventory)tile, stack);
		setRotation((TileBasicInventory)tile, world, x, y, z, player, stack);
	}
	public abstract void setTags(TileBasicInventory tile, ItemStack stack);
	public void setRotation(TileBasicInventory tile, World world, int x, int y, int z, EntityLivingBase player, ItemStack stack){
		int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0)
			tile.setOrientation(ForgeDirection.getOrientation(2));
		else if(l == 1)
			tile.setOrientation(ForgeDirection.getOrientation(5));
		else if(l == 2)
			tile.setOrientation(ForgeDirection.getOrientation(3));
		else if(l == 3)
			tile.setOrientation(ForgeDirection.getOrientation(4));
	}
	
	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

}
