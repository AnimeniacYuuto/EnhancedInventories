package yuuto.enhancedinventories;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WoolUpgradeHelper {
	
	static List<Integer> dyeIds = null;
	static List<String> dyeNames = null;
	
	public static void init(){
		dyeNames = Arrays.asList(
				"dyeWhite", "dyeOrange", "dyeMagenta", "dyeLightBlue", 
				"dyeYellow", "dyeLime", "dyePink", "dyeGray", 
				"dyeLightGray", "dyeCyan", "dyePurple", "dyeBlue",
				"dyeBrown", "dyeGreen", "dyeRed", "dyeBlack");
		dyeIds = Arrays.asList(
				OreDictionary.getOreID("dyeWhite"), OreDictionary.getOreID("dyeOrange"), OreDictionary.getOreID("dyeMagenta"), OreDictionary.getOreID("dyeLightBlue"), 
				OreDictionary.getOreID("dyeYellow"), OreDictionary.getOreID("dyeLime"), OreDictionary.getOreID("dyePink"), OreDictionary.getOreID("dyeGray"), 
				OreDictionary.getOreID("dyeLightGray"), OreDictionary.getOreID("dyeCyan"), OreDictionary.getOreID("dyePurple"), OreDictionary.getOreID("dyeBlue"),
				OreDictionary.getOreID("dyeBrown"), OreDictionary.getOreID("dyeGreen"), OreDictionary.getOreID("dyeRed"), OreDictionary.getOreID("dyeBlack")
			);
	}
	
	public static int getDyeId(ItemStack mat){
		if(dyeIds == null || dyeIds.size() < 16)
			return -1;
		int[] ids = OreDictionary.getOreIDs(mat);
    	for(int i = 0; i < ids.length; i++){
    		if(dyeIds.contains(ids[i])){
    			return ids[i];
    		}
    	}
    	return -1;
	}
	public static int getCollorValue(int dyeId){
		if(dyeIds == null || dyeIds.size() < 16)
			return -1;
		return dyeIds.indexOf(dyeId);
	}
	public static List<Integer> getDyeIDs(){
		return dyeIds;
	}
	public static List<String> getDyeNames(){
		return dyeNames;
	}

}
