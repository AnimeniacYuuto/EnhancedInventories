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

import net.minecraft.util.ResourceLocation;

public enum EInventoryMaterial {
	Stone(27, new String[]{"normalChestFrameStone.png", "doubleChestFrameStone.png",
			"normalLockerFrameStone.png", "doubleLockerFrameStone.png"}, "cobblestone", 0), 
	Iron(54, 255, 255, 255, "ingotIron", 1), 
	Gold(81, 230, 180, 40, "ingotGold", 2), 
	Diamond(108, 105, 240, 218, "gemDiamond", 3), 
	Emerald(108, 77, 192, 97, "gemEmerald", 3), 
	Obsidian(108, new String[]{"normalChestFrameObsidian.png", "doubleChestFrameObsidian.png",
			"normalLockerFrameObsidian.png", "doubleLockerFrameObsidian.png"}, "obsidian", 4),
	
	Copper(54, 195, 125, 10, "ingotCopper", 1), 
	Tin(54, 117, 148, 191, "ingotTin", 1),
	
	Silver(81, 147, 178, 201, "ingotSilver", 2), 
	Bronze(81, 225, 155, 50, "ingotBronze", 2), 
	Steel(81, 128, 128, 128, "ingotSteel", 2),
	
	Platinum(108, 98, 153, 255, "ingotPlatinum", 3);
	
	int size;
	int rVal;
	int gVal;
	int bVal;
	int tier;
	String mat;
	String[] recipes;
	ResourceLocation[] textures;
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
	EInventoryMaterial(int size, String[] textures, String mat, int tier){
		this.size = size;
		this.textures = new ResourceLocation[textures.length];
		for(int i = 0; i < textures.length; i++){
			this.textures[i] = new ResourceLocation("enhancedinventories", "textures/uvs/"+textures[i]);
		}
		this.mat = mat;
		this.tier = tier;
	}
	EInventoryMaterial(int size, String[] textures){
		this.size = size;
		this.textures = new ResourceLocation[textures.length];
		for(int i = 0; i < textures.length; i++){
			this.textures[i] = new ResourceLocation("enhancedinventories", "textures/uvs/"+textures[i]);
		}
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
		if(this == Tin)
			return 117f/255f;
		return (rVal)/255f;
	}
	public float g(){
		if(this == Tin)
			return 148f/255f;
		return (gVal)/255f;
	}
	public float b(){
		if(this == Tin)
			return 191f/255f;
		return (bVal)/255f;
	}
	public boolean hasTexture(){
		return textures != null;
	}
	public ResourceLocation getTexture(int index){
		return textures[index];
	}

	public static void registerRecipes(){
	}
}
