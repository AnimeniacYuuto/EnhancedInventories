package yuuto.yuutolib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModBlock extends Block{

	/**
	 * A quick constructor for a mod block
	 * @param mat the material of the block
	 * @param tab the creativeTab for the block
	 * @param mod the mod adding the block, used for texture and naming
	 * @param unlocName the base name of the block
	 */
	public ModBlock(Material mat, CreativeTabs tab, String mod, String unlocName) {
		super(mat);
		setBlockName(mod+":"+unlocName);
		setCreativeTab(tab);
	}
	
	@Override
    public Block setBlockName(String name)
    {
        super.setBlockName(name);
        this.setBlockTextureName(name);
        return this;
    }

}
