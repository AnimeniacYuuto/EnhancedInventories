/*******************************************************************************
 * Copyright (c) 2014 Yuuto.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * 	   cpw - src reference from Iron Chests
 * 	   doku/Dokucraft staff - base chest texture
 *     Yuuto - initial API and implementation
 ******************************************************************************/
package yuuto.enhancedinventories.proxy;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EWoodType;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.RecipeImprovedChest;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.gui.GuiHandler;
import yuuto.enhancedinventories.tile.BlockImprovedChest;
import yuuto.enhancedinventories.tile.ItemBlockImprovedChest;
import yuuto.enhancedinventories.tile.TileImprovedChest;
import yuuto.yuutolib.IProxy;
import yuuto.yuutolib.item.ModItemBlockMulti;

public class ProxyCommon implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		BlockImprovedChest improvedChest = EnhancedInventories.improvedChest;
		GameRegistry.registerBlock(improvedChest, ItemBlockImprovedChest.class, "improvedChest");
		GameRegistry.registerTileEntity(TileImprovedChest.class, "container.ImprovedChests:ImprovedChest");
		registerRecipes(improvedChest);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(EnhancedInventories.instance, new GuiHandler());

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public void registerRecipes(BlockImprovedChest improvedChest){
		System.out.println("loading recipes");
		WoodTypes.init();
		ArrayList<EWoodType> woodTypes = WoodTypes.getWoodTypes();
		System.out.println("loading recipes for "+woodTypes.size()+" woodtypes");
		for(int i = 0; i < woodTypes.size(); i++){
			GameRegistry.addRecipe(new RecipeImprovedChest(0, new ItemStack(improvedChest, 1, 0), new Object[]{
				"sps", "pwp", "sps", 's', "cobblestone", 'p', woodTypes.get(i).getPlanksStack(),
				'w', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
			}));
		}
		OreDictionary.registerOre("obsidian", Blocks.obsidian);
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			if(mat.getTier() == 4)
				System.out.println("registering tier 4 chest");
			ItemStack stack = new ItemStack(improvedChest, 1, mat.ordinal());
			switch(mat.getTier()){
			case 0:
				break;
			case 3:
				registerUpgradeRecipesAlt(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
				break;
			default:
				registerUpgradeRecipes(improvedChest, stack, mat.getTier()-1, mat.getMaterial());
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
		}
	}
	public void registerUpgradeRecipes(BlockImprovedChest improvedChest, ItemStack output, int previousTier, String upgMat){
		for(EInventoryMaterial mat : EInventoryMaterial.values()){
			if(mat.getTier() != previousTier)
				continue;
			GameRegistry.addRecipe(new RecipeImprovedChest(0, output, new Object[]{
				"mmm", "mpm", "mmm", 'm', upgMat, 'p', new ItemStack(improvedChest, 1, mat.ordinal())
			}));
		}
	}
	public void registerUpgradeRecipesAlt(BlockImprovedChest improvedChest, ItemStack output, int previousTier, String upgMat){
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
