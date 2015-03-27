package yuuto.enhancedinventories.materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;

public class FrameMaterials {
	
	public static final String DEFAULT_MAT_ID = "frame:ore:cobblestone"; 
	
	static ArrayList<FrameMaterial> materials = new ArrayList<FrameMaterial>();
	static HashMap<String, FrameMaterial> materialMap = new HashMap<String, FrameMaterial>();
	
	public static void addMaterial(FrameMaterial mat){
		materials.add(mat);
		materialMap.put(mat.id, mat);
	}
	public static void addMaterial(FrameMaterial mat, int tier){
		addMaterial(mat);
		StorageTiers.getTier(tier).addMat(mat);
	}
	
	public static String getId(ItemStack stack){
		for(FrameMaterial mat : materials){
			if(mat.matches(stack))
				return mat.getId();
		}
		return DEFAULT_MAT_ID;
	}
	public static FrameMaterial getMaterial(String id){
		if(materialMap.containsKey(id))
			return materialMap.get(id);
		return materialMap.get(DEFAULT_MAT_ID);
	}
	public static List<FrameMaterial> getMaterials(){
		return materials;
	}

}
