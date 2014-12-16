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

import java.util.List;

public enum EInventoryMaterial {
	Stone(27, 172, 172, 172), 
	Iron(54, 255, 255, 255, "ingotIron", 1), 
	Gold(81, 205, 178, 64, "ingotGold", 2), 
	Diamond(108, 105, 240, 218, "gemDiamond", 3), 
	Emerald(108, 77, 192, 97, "gemEmerald", 3), 
	Obsidian(108, 255, 255, 255, "obsidian", 4),
	
	Copper(54, 195, 125, 10, "ingotCopper", 1), 
	Tin(54, 122, 175, 196, "ingotTin", 1),
	
	Silver(81, 151, 160, 174, "ingotSilver", 2), 
	Bronze(81, 145, 79, 34, "ingotBronze", 2), 
	Steel(81, 79, 79, 79, "ingotSteel", 2),
	
	Platinum(54, 98, 153, 255, "ingotPlatinum", 3);
	
	int size;
	int rVal;
	int gVal;
	int bVal;
	int tier;
	String mat;
	String[] recipes;
	EInventoryMaterial(int size, int rV, int gV, int bV, String mat, int tier){
		this.size = size;
		this.rVal = rV;
		this.gVal = gV;
		this.bVal = bV;
		this.tier = tier;
		this.mat = mat;
	}
	EInventoryMaterial(int size, int rV, int gV, int bV){
		this.size = size;
		this.rVal = rV;
		this.gVal = gV;
		this.bVal = bV;
		this.tier = 0;
	}
	public int getTier(){
		return tier;
	}
	public String getMaterial(){
		return mat;
	}
	public int size(){
		return size;
	}
	public int doubleSize(){
		return size*2;
	}
	public float r(){
		return (rVal)/255f;
	}
	public float g(){
		return (gVal)/255f;
	}
	public float b(){
		return (bVal)/255f;
	}

	public static void registerRecipes(){
	}
}
