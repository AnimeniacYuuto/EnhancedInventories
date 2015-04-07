package yuuto.enhancedinventories.materials;

import com.google.common.base.Strings;

import yuuto.enhancedinventories.ref.MaterialKeys;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CoreHelper {
	
	static CoreMaterial defaultCore;
	
	public static boolean isValidMaterial(ItemStack stack){
		return getCore(stack) == null;
	}
	public static CoreMaterial readMaterial(ItemStack stack){
		String name = stack.getTagCompound().getString(MaterialKeys.CORE_ID);
		if(Strings.isNullOrEmpty(name))
			return null;
		Block block = (Block)Block.blockRegistry.getObject(name);
		int meta = stack.getTagCompound().getInteger(MaterialKeys.CORE_META);
		return new CoreMaterial(block, meta);
	}
	public static void writeToStack(ItemStack out, CoreMaterial mat){
		String name = Block.blockRegistry.getNameForObject(mat.block);
		if(name == null || name.trim().isEmpty())
			return;
		out.getTagCompound().setString(MaterialKeys.CORE_ID, name);
		out.getTagCompound().setByte(MaterialKeys.CORE_META, (byte)mat.metadata);
	}
	
	public static CoreMaterial getCore(ItemStack stack){
		if(stack == null)
			return null;
		if(!(stack.getItem() instanceof ItemBlock))
			return null;
		Block b = ((ItemBlock)stack.getItem()).field_150939_a;
		if(b instanceof ITileEntityProvider || b.hasTileEntity(stack.getItemDamage()))
			return null;
		if( !b.isOpaqueCube() || !(b.getMaterial().isOpaque() && b.renderAsNormalBlock()))
			return null;
		return new CoreMaterial(b, stack.getItemDamage());
	}
	public static CoreMaterial getDefault(){
		if(defaultCore == null)
			defaultCore = new CoreMaterial(Blocks.planks, 0);
		return defaultCore;
	}

}
