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
import cpw.mods.fml.common.FMLCommonHandler;
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
import yuuto.enhancedinventories.compat.modules.NEIModule;
import yuuto.enhancedinventories.compat.modules.TConstructModule;
import yuuto.enhancedinventories.compat.refinedrelocation.BlockImprovedSortingChest;
import yuuto.enhancedinventories.compat.refinedrelocation.BlockSortingLocker;
import yuuto.enhancedinventories.item.ItemChestConverter;
import yuuto.enhancedinventories.item.ItemFunctionUpgrade;
import yuuto.enhancedinventories.item.ItemSizeUpgrade;
import yuuto.enhancedinventories.proxy.InventoryRecipeRegister;
import yuuto.enhancedinventories.proxy.ProxyCommon;
import yuuto.yuutolib.IMod;

@Mod(modid = "EnhancedInventories", name = "Enhanced Inventories", version = "1.7.10-1.0.8")
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
		if(Loader.isModLoaded("NotEnoughItems") && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
			NEIModule.init();
	}
	
	public void registerRecipes(){
		
	}

}
