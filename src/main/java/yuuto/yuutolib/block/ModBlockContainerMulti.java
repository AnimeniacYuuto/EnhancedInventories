package yuuto.yuutolib.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class ModBlockContainerMulti extends ModBlockContainer implements IBlockMulti{

	protected String[] subNames;
	protected IIcon[] icons;
	public ModBlockContainerMulti(Material mat, CreativeTabs tab, String mod, String unlocName, String ... subNames) {
		super(mat, tab, mod, unlocName);
		this.subNames = subNames;
		this.icons = new IIcon[subNames.length];
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
        	icons[i] = reg.registerIcon(this.getTextureName(i));
        }
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if(meta < 0 || meta >= icons.length)
			return this.blockIcon;
		return icons[meta];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < subNames.length; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}

}
