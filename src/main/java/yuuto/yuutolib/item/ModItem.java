package yuuto.yuutolib.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModItem extends Item{
	
	public ModItem(CreativeTabs tab, String mod, String unlocName) {
		super();
		setUnlocalizedName(mod+":"+unlocName);
		setCreativeTab(tab);
	}
	
	@Override
    public Item setUnlocalizedName(String name)
    {
        super.setUnlocalizedName(name);
        this.setTextureName(name);
        return this;
    }

}
