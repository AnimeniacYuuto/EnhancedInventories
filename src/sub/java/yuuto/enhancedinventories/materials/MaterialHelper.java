package yuuto.enhancedinventories.materials;

import java.util.List;
import java.util.Random;

import yuuto.enhancedinventories.ref.MaterialKeys;
import yuuto.enhancedinventories.tile.upgrades.ETier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialHelper {
	
	static Random rand = new Random();
	
	public static CoreMaterial readCoreMaterial(ItemStack stack){
		return CoreHelper.readMaterial(stack);
	}
	public static FrameMaterial readFrameMaterial(ItemStack stack){
		return FrameMaterials.readFrameMaterial(stack);
	}
	public static int readColor(ItemStack stack){
		return stack.getTagCompound().getInteger(MaterialKeys.COLOR);
	}
	public static boolean isPainted(ItemStack stack){
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean(MaterialKeys.PAINTED);
	}
	
	public static void writeToStack(ItemStack out, CoreMaterial core, FrameMaterial frame, boolean painted){
		writeToStack(out, core, frame, -1, painted);
	}
	public static void writeToStack(ItemStack out, CoreMaterial core, FrameMaterial frame, int color, boolean painted){
		if(out == null)
			return;
		if(!out.hasTagCompound()){
			out.setTagCompound(new NBTTagCompound());
		}
		CoreHelper.writeToStack(out, core);
		FrameMaterials.writeToStack(out, frame);
		if(color > -1 && color < 16)
			out.getTagCompound().setByte(MaterialKeys.COLOR, (byte) color);
		if(painted)
			out.getTagCompound().setBoolean(MaterialKeys.PAINTED, true);
	}
	
	public static CoreMaterial getRandomCore(){
		List<ItemStack> planks = OreDictionary.getOres("planksWood");
		ItemStack stack = planks.get(rand.nextInt(planks.size()));
		if(stack == null || !CoreHelper.isValidMaterial(stack))
			return CoreHelper.getDefault();
		return CoreHelper.getCore(stack);
	}
	public static int getRandomColor(){
		return rand.nextInt(16);
	}
	public static FrameMaterial getRandomFrame(int tier){
		List<FrameMaterial> mats = ETier.get(tier).getFrames();
		if(mats.size() < 1)
			return null;
		return mats.get(rand.nextInt(mats.size()));
	}

}
