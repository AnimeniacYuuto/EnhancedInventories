package yuuto.enhancedinventories.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.RecipeImprovedChest;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.block.BlockConnectiveInventory;
import yuuto.enhancedinventories.block.BlockImprovedChest;
import yuuto.enhancedinventories.block.BlockLocker;
import cpw.mods.fml.common.registry.GameRegistry;

public class InventoryRecipeRegister {
	
	public static void registerRecipes(){
		BlockImprovedChest improvedChest = EnhancedInventories.improvedChest; 
		BlockLocker locker = EnhancedInventories.locker;
		BlockImprovedChest improvedSortingChest = EnhancedInventories.improvedSortingChest; 
		BlockLocker sortingLocker = EnhancedInventories.sortingLocker;
		WoodTypes.init();
		/*ArrayList<WoodType> woodTypes = WoodTypes.getWoodTypes();
		for(int i = 0; i < woodTypes.size(); i++){
			GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(improvedChest, 1, 0), new Object[]{
				"sps", "pwp", "sps", 's', "cobblestone", 'p', woodTypes.get(i).getPlanksStack(),
				'w', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(locker, 1, 0), new Object[]{
				"sps", "ptp", "sps", 's', "cobblestone", 'p', woodTypes.get(i).getPlanksStack(),
				't', new ItemStack(Blocks.trapdoor, 1),
			}));
		}*/
		/*GameRegistry.addRecipe(new RecipeStoneCrafting(0));
		GameRegistry.addRecipe(new RecipeStoneCrafting(1));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(0));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(1));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(2));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(3));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(4));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(5));
		GameRegistry.addRecipe(new RecipeFunctionUpgrades(6));*/
		GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(improvedChest, 1, 0), new Object[]{
			"sps", "pwp", "sps", 's', "cobblestone", 'p', "plankWood",
			'w', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
		}));
		GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(locker, 1, 0), new Object[]{
			"sps", "ptp", "sps", 's', "cobblestone", 'p', "plankWood",
			't', new ItemStack(Blocks.trapdoor, 1),
		}));
		OreDictionary.registerOre("obsidian", Blocks.obsidian);
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			ItemStack stack = new ItemStack(improvedChest, 1, mat.ordinal());
			ItemStack stack2 = new ItemStack(locker, 1, mat.ordinal());
			switch(mat.getTier()){
			case 0:
				if(EnhancedInventories.refinedRelocation){
					registerSortingUpgrades(stack, new ItemStack(improvedSortingChest, 1, 0), mat);
					registerSortingUpgrades(stack2, new ItemStack(sortingLocker, 1, 0), mat);
				}
				break;
			case 3:
				registerUpgradeRecipesAlt(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
				registerUpgradeRecipesAlt(locker, stack2, mat.getTier()-1, mat.getMaterial());
				if(EnhancedInventories.refinedRelocation){
					registerSortingUpgrades(stack, new ItemStack(improvedSortingChest, 1, mat.ordinal()), mat);
					registerSortingUpgrades(stack2, new ItemStack(sortingLocker, 1, mat.ordinal()), mat);
				}
				break;
			default:
				registerUpgradeRecipes(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
				registerUpgradeRecipes(locker, stack2, mat.getTier()-1, mat.getMaterial());
				if(EnhancedInventories.refinedRelocation){
					registerSortingUpgrades(stack, new ItemStack(improvedSortingChest, 1, mat.ordinal()), mat);
					registerSortingUpgrades(stack2, new ItemStack(sortingLocker, 1, mat.ordinal()), mat);
				}
				break;
			}
			GameRegistry.addRecipe(new RecipeImprovedChest(1, stack, new Object[]{
					"d","c", 'd', "dye", 'c', stack
			}));
			GameRegistry.addRecipe(new RecipeImprovedChest(2, stack, new Object[]{
					"c", 'c', stack 
			}));
			
			GameRegistry.addRecipe(new RecipeImprovedChest(2, stack2, new Object[]{
					"c", 'c', stack2
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
			if(mat == EInventoryMaterial.Obsidian)
				continue;
			GameRegistry.addRecipe(new RecipeImprovedChest(0, output, new Object[]{
				"ggg", "mpm", "ggg", 'm', upgMat, 'p', new ItemStack(improvedChest, 1, mat.ordinal()),
				'g', "blockGlass"
			}));
		}
	}
	
	public static void registerSortingUpgrades(ItemStack input, ItemStack output, EInventoryMaterial mat){
		GameRegistry.addRecipe(new RecipeImprovedChest(0, output, new Object[]{
				"g g", " i ", "m m", 'g', "ingotGold", 'i', input, 'm', mat.getMaterial()
		}));
		GameRegistry.addRecipe(new RecipeImprovedChest(0, input, new Object[]{
				"i", 'i', output
		}));
	}

}
