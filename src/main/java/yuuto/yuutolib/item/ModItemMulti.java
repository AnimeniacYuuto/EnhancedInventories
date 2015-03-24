package yuuto.yuutolib.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ModItemMulti extends ModItem{

	protected String[] subNames;
	protected IIcon[] icons;
	public ModItemMulti(CreativeTabs tab, String mod, String unlocName, String ... subNames) {
		super(tab, mod, unlocName);
		this.hasSubtypes = true;
		this.subNames = subNames;
		this.icons = new IIcon[subNames.length];
	}
	
	public String getUnlocalizedName(ItemStack stack){
		return this.getUnlocalizedName(stack.getItemDamage());
	}
	public String getUnlocalizedName(int meta){
		return this.getUnlocalizedName()+subNames[meta];
	}
	public String[] getUnlocalizedNames(){
		String[] ret = new String[subNames.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = getUnlocalizedName(i);
		}
		return ret;
	}
	public String[] getSubNames(){
		return subNames;
	}
	public String getIconString(int meta){
		return this.getIconString()+subNames[meta];
	}
	public String[] getIconStrings(){
		String[] ret = new String[subNames.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = getIconString(i);
		}
		return ret;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg)
    {
        this.itemIcon = reg.registerIcon(this.getIconString());
        for(int i = 0; i < subNames.length; i++){
        	icons[i] = reg.registerIcon(this.getIconString(i));
        }
    }
	
	@Override
	public IIcon getIconFromDamage(int metadata){
		if(metadata < 0 || metadata >= icons.length)
			return this.itemIcon;
		return this.icons[metadata];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for (int ix = 0; ix < subNames.length; ix++) {
			subItems.add(new ItemStack(this, 1, ix));
		}
	}

}
