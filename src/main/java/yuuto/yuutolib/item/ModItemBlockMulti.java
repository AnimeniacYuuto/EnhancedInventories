package yuuto.yuutolib.item;

import yuuto.yuutolib.block.IBlockMulti;
import yuuto.yuutolib.block.ModBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ModItemBlockMulti extends ItemBlock{

	protected IBlockMulti blockMulti;
	public ModItemBlockMulti(Block block) {
		super(block);
		if(block instanceof IBlockMulti)
			blockMulti = (IBlockMulti)block;
		this.hasSubtypes = true;
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata){
		return this.field_150939_a.getIcon(2, metadata);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack){
		return blockMulti.getUnlocalizedName(stack.getItemDamage());
	}
	
	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}
	
	

}
