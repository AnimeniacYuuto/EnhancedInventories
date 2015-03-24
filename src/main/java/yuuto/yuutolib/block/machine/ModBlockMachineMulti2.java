package yuuto.yuutolib.block.machine;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import yuuto.yuutolib.block.IBlockMulti;
import yuuto.yuutolib.block.ModBlockContainer;
import yuuto.yuutolib.block.tile.IRotatable;
import yuuto.yuutolib.block.tile.TileMachine;

public abstract class ModBlockMachineMulti2 extends ModBlockContainer implements IBlockMulti{

	protected String[] subNames;
	protected IIcon[][] icons;
	public ModBlockMachineMulti2(Material mat, CreativeTabs tab, String mod, String unlocName, String ... subNames) {
		super(mat, tab, mod, unlocName);
		this.subNames = subNames;
		this.icons = new IIcon[subNames.length][3];
		//this.onBlockPlacedBy(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_, p_149689_6_)
	}
	
	@Override
	public String getUnlocalizedName(int meta){
		return this.getUnlocalizedName()+subNames[meta];
	}
	@Override
	public String[] getUnlocalizedNames(){
		String[] ret = new String[subNames.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = getUnlocalizedName(i);
		}
		return ret;
	}
	@Override
	public String[] getSubNames(){
		return subNames;
	}
	@Override
	public String getTextureName(int meta){
		return this.getTextureName()+subNames[meta];
	}
	@Override
	public String[] getTextureNames(){
		String[] ret = new String[subNames.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = getTextureName(i);
		}
		return ret;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName());
        for(int i = 0; i < subNames.length; i++){
        	icons[i][0] = reg.registerIcon(this.getTextureName(i));
        	icons[i][1] = reg.registerIcon(this.getTextureName(i)+"FrontInactive");
        	icons[i][2] = reg.registerIcon(this.getTextureName(i)+"FrontActive");
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess bAcess, int x, int y, int z, int side){
		TileEntity tile = bAcess.getTileEntity(x, y, z);
		if(tile instanceof TileMachine){
			TileMachine m = (TileMachine) tile;
			if(m.getOrientation().ordinal() != side)
				return getPlacedIcon(0, bAcess.getBlockMetadata(x, y, z));
			if(m.isActive())
				return getPlacedIcon(2, bAcess.getBlockMetadata(x, y, z));
			return getPlacedIcon(1, bAcess.getBlockMetadata(x, y, z));
		}
		return getIcon(side, bAcess.getBlockMetadata(x, y, z));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if(meta < 0 || meta >= icons.length)
			return this.blockIcon;
		if(side == 4)
			return icons[meta][1];
		return icons[meta][0];
	}
	@SideOnly(Side.CLIENT)
	public IIcon getPlacedIcon(int side, int meta){
		if(meta < 0 || meta >= icons.length)
			return this.blockIcon;
		if(side < 0 || side >= icons[meta].length)
			return this.blockIcon;
		return icons[meta][side];
	}
	
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
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < subNames.length; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof IRotatable))
			return;
		int l = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0)
			((IRotatable)tile).setOrientation(ForgeDirection.getOrientation(2));
		else if(l == 1)
			((IRotatable)tile).setOrientation(ForgeDirection.getOrientation(5));
		else if(l == 2)
			((IRotatable)tile).setOrientation(ForgeDirection.getOrientation(3));
		else if(l == 3)
			((IRotatable)tile).setOrientation(ForgeDirection.getOrientation(4));
			
	}
}
