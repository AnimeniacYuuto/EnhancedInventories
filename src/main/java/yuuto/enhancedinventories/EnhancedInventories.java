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
package yuuto.enhancedinventories;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import yuuto.enhancedinventories.block.BlockImprovedChest;
import yuuto.enhancedinventories.block.BlockLocker;
import yuuto.enhancedinventories.compat.modules.TConstructModule;
import yuuto.enhancedinventories.compat.refinedrelocation.BlockImprovedSortingChest;
import yuuto.enhancedinventories.compat.refinedrelocation.BlockSortingLocker;
import yuuto.enhancedinventories.item.ItemChestConverter;
import yuuto.enhancedinventories.item.ItemFunctionUpgrade;
import yuuto.enhancedinventories.item.ItemSizeUpgrade;
import yuuto.enhancedinventories.proxy.InventoryRecipeRegister;
import yuuto.enhancedinventories.proxy.ProxyCommon;
import yuuto.yuutolib.IMod;

@Mod(modid = "EnhancedInventories", name = "Enhanced Inventories", version = "1.7.10-1.0.11")
public class EnhancedInventories implements IMod{

	@Instance("EnhancedInventories")
	public static EnhancedInventories instance;
	
	@SidedProxy(clientSide = "yuuto.enhancedinventories.proxy.ProxyClient", serverSide = "yuuto.enhancedinventories.proxy.ProxyCommon")
	public static ProxyCommon proxy;
	
	public static CreativeTabs tab = new CreativeTabs("EnhancedInventories"){

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Item.getItemFromBlock(improvedChest);
		}
		
	};
	
	public static boolean refinedRelocation = false;
	
	public static final BlockImprovedChest improvedChest = new BlockImprovedChest();
	public static final BlockLocker locker = new BlockLocker();
	public static BlockImprovedChest improvedSortingChest;
	public static BlockLocker sortingLocker;
	public static final ItemChestConverter chestConverter = new ItemChestConverter();
	public static final ItemSizeUpgrade sizeUpgrade = new ItemSizeUpgrade();
	public static final ItemFunctionUpgrade functionUpgrade = new ItemFunctionUpgrade();
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		if(Loader.isModLoaded("RefinedRelocation")){
			refinedRelocation = true;
			improvedSortingChest = new BlockImprovedSortingChest();
			sortingLocker = new BlockSortingLocker();
		}
		proxy.preInit(event);
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		WoolUpgradeHelper.init();
		if(Loader.isModLoaded("TConstruct"))
			TConstructModule.init();
		InventoryRecipeRegister.registerRecipes();	
		registerRecipes();
	}
	
	public void registerRecipes(){
		ItemStack base = new ItemStack(EnhancedInventories.functionUpgrade, 1, 0);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(EnhancedInventories.functionUpgrade, 1, 0), new Object[]{
			" c ", "cpc", " c ", 'c', "cobblestone", 'p', Items.paper 
		}));
		GameRegistry.addShapedRecipe(new ItemStack(EnhancedInventories.functionUpgrade, 1, 1), new Object[]{
			" h ", "hmh", " h ", 'h', Blocks.hopper, 'm', base
		});
		GameRegistry.addShapelessRecipe(new ItemStack(EnhancedInventories.functionUpgrade, 1, 2), 
			Blocks.tripwire_hook, base);
		GameRegistry.addRecipe(new RecipeConverter(new ItemStack(chestConverter, 1, 0), new Object[]{
			"c","b", 
			'c', new ItemStack(improvedChest, 1, 0), 
			'b', new ItemStack(functionUpgrade, 1, 0)
		}));
		for(int i = 1; i < EInventoryMaterial.values().length; i++){
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(EnhancedInventories.sizeUpgrade, 1, i-1), new Object[]{
				"mmm", "mbm", "mmm", 'm', EInventoryMaterial.values()[i].getMaterial(), 'b', base
			}));
			GameRegistry.addRecipe(new RecipeConverter(new ItemStack(chestConverter, 1, i), new Object[]{
				"c","b", 
				'c', new ItemStack(improvedChest, 1, i), 
				'b', new ItemStack(functionUpgrade, 1, 0)
			}));
		}
	}

}
