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

import net.minecraft.item.ItemStack;

public class WoodTypes {
	static ArrayList<EWoodType> woodTypes = new ArrayList<EWoodType>();
	

	public static void init(){
		System.out.println("initializing woodTypes");
		ArrayList<String> loadedMods = new ArrayList<String>();
		loadedMods.add("minecraft");
		
		for(EWoodType type : EWoodType.values()){
			if(loadedMods.contains(type.getModID()))
				woodTypes.add(type);
		}
	}
	public static int getIdOf(ItemStack stack){
		for(EWoodType wood : woodTypes){
			if(wood.matches(stack))
				return wood.ordinal();
		}
		return -1;
	}
	public static ArrayList<EWoodType> getWoodTypes(){
		return woodTypes;
	}
}
