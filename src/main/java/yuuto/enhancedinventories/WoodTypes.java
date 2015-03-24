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

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.Loader;

import yuuto.enhancedinventories.compat.modules.*;
import yuuto.enhancedinventories.proxy.ConfigHandler;

import net.minecraft.item.ItemStack;

public final class WoodTypes {
	
	public static final String DEFAULT_WOOD_ID = "wood:minecraft:planks:0";
	
	static ArrayList<WoodType> woodTypes = new ArrayList<WoodType>();
	static HashMap<String, WoodType> woodTypeMap = new HashMap<String, WoodType>();

	public static void init(){
		VanillaModule.init();
		if(ConfigHandler.chisel && Loader.isModLoaded("chisel"))
			ChiselModule.init();
		if(ConfigHandler.natura && Loader.isModLoaded("Natura"))
			NaturaModule.init();
		if(ConfigHandler.forestry && Loader.isModLoaded("Forestry")){
			ForestryModule.init();
			if(ConfigHandler.extraTrees && Loader.isModLoaded("ExtraTrees"))
				ExtraTreesModule.init();
		}
		if(ConfigHandler.botania && Loader.isModLoaded("Botania"))
			BotaniaModule.init();
		if(ConfigHandler.thaumcraft && Loader.isModLoaded("Thaumcraft"))
			ThaumcraftModule.init();
		if(ConfigHandler.biomesOPlenty && Loader.isModLoaded("BiomesOPlenty"))
			BiomesOPlentyModule.init();
		if(ConfigHandler.extrabiomes && Loader.isModLoaded("ExtrabiomesXL"))
			ExtrabiomesModule.init();
	}
	public static void addWoodType(WoodType wood){
		woodTypes.add(wood);
		woodTypeMap.put(wood.id(), wood);
	}
	public static String getId(ItemStack stack){
		for(WoodType wood : woodTypes){
			if(wood.matches(stack))
				return wood.id();
		}
		return DEFAULT_WOOD_ID;
	}
	public static WoodType getWoodType(String id){
		if(woodTypeMap.containsKey(id))
			return woodTypeMap.get(id);
		return null;
	}
	public static ArrayList<WoodType> getWoodTypes(){
		return woodTypes;
	}
}
