package yuuto.enhancedinventories.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CoreMaterialHelper {
	
	static ArrayList<String> blackList = new ArrayList<String>();
	static List<ItemStack> woodStacks;
	static Random random = new Random();
	
	public static void resetWoodStacks(){
		woodStacks = (List<ItemStack>)OreDictionary.getOres("plankWood").clone();
		for(ItemStack s : OreDictionary.getOres("plankWood")){
			Block b = Block.getBlockFromItem(s.getItem());
			
		}
	}
	public static void addToBlackList(String s){
		blackList.add(s);
	}
	public static boolean isBlackListed(){
		return false;
	}
	
	public static String getRandomCoreMaterial(){
		if(woodStacks == null)
			resetWoodStacks();
		ItemStack stack = woodStacks.get(random.nextInt(woodStacks.size()));
		UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(stack.getItem()));
		//String ret = id.modId+":"+id.name+":"+stack.getItemDamage();
		return id.modId+":"+id.name+":"+stack.getItemDamage();
	}

}
