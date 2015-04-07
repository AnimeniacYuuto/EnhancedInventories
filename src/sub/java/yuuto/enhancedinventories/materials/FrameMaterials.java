package yuuto.enhancedinventories.materials;

import java.util.List;
import java.util.Map;

import yuuto.enhancedinventories.ref.MaterialKeys;
import yuuto.enhancedinventories.tile.upgrades.ETier;

import net.minecraft.item.ItemStack;

public class FrameMaterials {
	static Map<String, FrameMaterial> mats;
	
	public static void addMaterial(FrameMaterial mat){
		addMaterial(mat, -1);
	}
	public static void addMaterial(FrameMaterial mat, int tier){
		mats.put(mat.getId(), mat);
		if(tier > -1){
			ETier.get(tier).getFrames().add(mat);
		}
	}
	public static FrameMaterial readFrameMaterial(ItemStack stack){
		String key = stack.getTagCompound().getString(MaterialKeys.FRAME);
		if(mats.containsKey(key))
			return mats.get(key);
		return null;
	}
	public static void writeToStack(ItemStack stack, FrameMaterial mat){
		stack.getTagCompound().setString(MaterialKeys.FRAME, mat.getId());
	}

}
