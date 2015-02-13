package yuuto.enhancedinventories.proxy;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EWoodType;
import yuuto.enhancedinventories.RecipeImprovedChest;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.tile.BlockConnectiveInventory;
import yuuto.enhancedinventories.tile.BlockImprovedChest;
import yuuto.enhancedinventories.tile.BlockLocker;
import cpw.mods.fml.common.registry.GameRegistry;

public class InventoryRecipeRegister {
	
	public static void registerRecipes(BlockImprovedChest improvedChest, BlockLocker locker){
		WoodTypes.init();
		ArrayList<EWoodType> woodTypes = WoodTypes.getWoodTypes();
		for(int i = 0; i < woodTypes.size(); i++){
			GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(improvedChest, 1, 0), new Object[]{
				"sps", "pwp", "sps", 's', "cobblestone", 'p', woodTypes.get(i).getPlanksStack(),
				'w', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(locker, 1, 0), new Object[]{
				"sps", "ptp", "sps", 's', "cobblestone", 'p', woodTypes.get(i).getPlanksStack(),
				't', new ItemStack(Blocks.trapdoor, 1),
			}));
		}
		OreDictionary.registerOre("obsidian", Blocks.obsidian);
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			ItemStack stack = new ItemStack(improvedChest, 1, mat.ordinal());
			ItemStack stack2 = new ItemStack(locker, 1, mat.ordinal());
			switch(mat.getTier()){
			case 0:
				break;
			case 3:
				registerUpgradeRecipesAlt(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
				registerUpgradeRecipesAlt(locker, stack2, mat.getTier()-1, mat.getMaterial());
				break;
			default:
				registerUpgradeRecipes(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
				registerUpgradeRecipes(locker, stack2, mat.getTier()-1, mat.getMaterial());
				break;
			}
			GameRegistry.addRecipe(new RecipeImprovedChest(1, stack, new Object[]{
					" h ", "hch", " h ", 'h', new ItemStack(Blocks.hopper), 'c', stack
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(2, stack, new Object[]{
					"c", 'c', stack 
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(3, stack, new Object[]{
					"tc", 't', new ItemStack(Blocks.tripwire_hook), 'c', stack 
			}));
			
			GameRegistry.addRecipe(new RecipeImprovedChest(1, stack2, new Object[]{
					" h ", "hch", " h ", 'h', new ItemStack(Blocks.hopper), 'c', stack2
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(2, stack2, new Object[]{
					"c", 'c', stack2 
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(3, stack2, new Object[]{
					"tc", 't', new ItemStack(Blocks.tripwire_hook), 'c', stack2 
			}));
		}
	}
	public static void registerUpgradeRecipes(BlockConnectiveInventory improvedChest,  ItemStack output, int previousTier, String upgMat){
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			if(mat.getTier() != previousTier)
				continue;
			GameRegistry.addRecipe(new RecipeImprovedChest(0, output, new Object[]{
				"mmm", "mpm", "mmm", 'm', upgMat, 'p', new ItemStack(improvedChest, 1, mat.ordinal())
			}));
		}
	}
	public static void registerUpgradeRecipesAlt(BlockConnectiveInventory improvedChest, ItemStack output, int previousTier, String upgMat){
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			if(mat.getTier() != previousTier)
				continue;
			GameRegistry.addRecipe(new RecipeImprovedChest(0, output, new Object[]{
				"ggg", "mpm", "ggg", 'm', upgMat, 'p', new ItemStack(improvedChest, 1, mat.ordinal()),
				'g', "blockGlass"
			}));
		}
	}

}
