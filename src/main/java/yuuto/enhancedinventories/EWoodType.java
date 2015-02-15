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

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public enum EWoodType {
	//Vanilla Wood
	/*OAK("minecraft", "planks", 0),
	SPRUCE("minecraft", "planks", 1),
	BIRCH("minecraft", "planks", 2),
	JUNGLE("minecraft", "planks", 3),
	ACACIA("minecraft", "planks", 4),
	DARK_OAK("minecraft", "planks", 5),
	
	//Chisel Wood
	//Oak
	OAK_CLEAN("chisel", "oak_planks", 1),
	OAK_SHORT("chisel", "oak_planks", 2),
	OAK_VERTICAL("chisel", "oak_planks", 3),
	OAK_VERTICAL_UNEVEN("chisel", "oak_planks", 4),
	OAK_PARQUET("chisel", "oak_planks", 5),
	OAK_FANCY("chisel", "oak_planks", 6),
	OAK_BLINDS("chisel", "oak_planks", 7),
	OAK_PANEL_NAILS("chisel", "oak_planks", 8),
	OAK_DOUBLE("chisel", "oak_planks", 9),
	OAK_CRATE("chisel", "oak_planks", 10),
	OAK_CRATE_FANCY("chisel", "oak_planks", 11),
	OAK_CRATEEX("chisel", "oak_planks", 12),
	OAK_LARGE("chisel", "oak_planks", 13),
	OAK_CHAOTIC_HOR("chisel", "oak_planks", 14),
	OAK_CHAOTIC("chisel", "oak_planks", 15),
	//Birch
	BIRCH_CLEAN("chisel", "birch_planks", 1),
	BIRCH_SHORT("chisel", "birch_planks", 2),
	BIRCH_VERTICAL("chisel", "birch_planks", 3),
	BIRCH_VERTICAL_UNEVEN("chisel", "birch_planks", 4),
	BIRCH_PARQUET("chisel", "birch_planks", 5),
	BIRCH_FANCY("chisel", "birch_planks", 6),
	BIRCH_BLINDS("chisel", "birch_planks", 7),
	BIRCH_PANEL_NAILS("chisel", "birch_planks", 8),
	BIRCH_DOUBLE("chisel", "birch_planks", 9),
	BIRCH_CRATE("chisel", "birch_planks", 10),
	BIRCH_CRATE_FANCY("chisel", "birch_planks", 11),
	BIRCH_CRATEEX("chisel", "birch_planks", 12),
	BIRCH_LARGE("chisel", "birch_planks", 13),
	BIRCH_CHAOTIC_HOR("chisel", "birch_planks", 14),
	BIRCH_CHAOTIC("chisel", "birch_planks", 15),
	//Jungle
	JUNGLE_CLEAN("chisel", "jungle_planks", 1),
	JUNGLE_SHORT("chisel", "jungle_planks", 2),
	JUNGLE_VERTICAL("chisel", "jungle_planks", 3),
	JUNGLE_VERTICAL_UNEVEN("chisel", "jungle_planks", 4),
	JUNGLE_PARQUET("chisel", "jungle_planks", 5),
	JUNGLE_FANCY("chisel", "jungle_planks", 6),
	JUNGLE_BLINDS("chisel", "jungle_planks", 7),
	JUNGLE_PANEL_NAILS("chisel", "jungle_planks", 8),
	JUNGLE_DOUBLE("chisel", "jungle_planks", 9),
	JUNGLE_CRATE("chisel", "jungle_planks", 10),
	JUNGLE_CRATE_FANCY("chisel", "jungle_planks", 11),
	JUNGLE_CRATEEX("chisel", "jungle_planks", 12),
	JUNGLE_LARGE("chisel", "jungle_planks", 13),
	JUNGLE_CHAOTIC_HOR("chisel", "jungle_planks", 14),
	JUNGLE_CHAOTIC("chisel", "jungle_planks", 15),
	//Acacia
	ACACIA_CLEAN("chisel", "acacia_planks", 1),
	ACACIA_SHORT("chisel", "acacia_planks", 2),
	ACACIA_VERTICAL("chisel", "acacia_planks", 3),
	ACACIA_VERTICAL_UNEVEN("chisel", "acacia_planks", 4),
	ACACIA_PARQUET("chisel", "acacia_planks", 5),
	ACACIA_FANCY("chisel", "acacia_planks", 6),
	ACACIA_BLINDS("chisel", "acacia_planks", 7),
	ACACIA_PANEL_NAILS("chisel", "acacia_planks", 8),
	ACACIA_DOUBLE("chisel", "acacia_planks", 9),
	ACACIA_CRATE("chisel", "acacia_planks", 10),
	ACACIA_CRATE_FANCY("chisel", "acacia_planks", 11),
	ACACIA_CRATEEX("chisel", "acacia_planks", 12),
	ACACIA_LARGE("chisel", "acacia_planks", 13),
	ACACIA_CHAOTIC_HOR("chisel", "acacia_planks", 14),
	ACACIA_CHAOTIC("chisel", "acacia_planks", 15),
	//Dark Oak
	DARK_OAK_CLEAN("chisel", "dark_oak_planks", 1),
	DARK_OAK_SHORT("chisel", "dark_oak_planks", 2),
	DARK_OAK_VERTICAL("chisel", "dark_oak_planks", 3),
	DARK_OAK_VERTICAL_UNEVEN("chisel", "dark_oak_planks", 4),
	DARK_OAK_PARQUET("chisel", "dark_oak_planks", 5),
	DARK_OAK_FANCY("chisel", "dark_oak_planks", 6),
	DARK_OAK_BLINDS("chisel", "dark_oak_planks", 7),
	DARK_OAK_PANEL_NAILS("chisel", "dark_oak_planks", 8),
	DARK_OAK_DOUBLE("chisel", "dark_oak_planks", 9),
	DARK_OAK_CRATE("chisel", "dark_oak_planks", 10),
	DARK_OAK_CRATE_FANCY("chisel", "dark_oak_planks", 11),
	DARK_OAK_CRATEEX("chisel", "dark_oak_planks", 12),
	DARK_OAK_LARGE("chisel", "dark_oak_planks", 13),
	DARK_OAK_CHAOTIC_HOR("chisel", "dark_oak_planks", 14),
	DARK_OAK_CHAOTIC("chisel", "dark_oak_planks", 15),
	//Spruce
	SPRUCE_CLEAN("chisel", "spruce_planks", 1),
	SPRUCE_SHORT("chisel", "spruce_planks", 2),
	SPRUCE_VERTICAL("chisel", "spruce_planks", 3),
	SPRUCE_VERTICAL_UNEVEN("chisel", "spruce_planks", 4),
	SPRUCE_PARQUET("chisel", "spruce_planks", 5),
	SPRUCE_FANCY("chisel", "spruce_planks", 6),
	SPRUCE_BLINDS("chisel", "spruce_planks", 7),
	SPRUCE_PANEL_NAILS("chisel", "spruce_planks", 8),
	SPRUCE_DOUBLE("chisel", "spruce_planks", 9),
	SPRUCE_CRATE("chisel", "spruce_planks", 10),
	SPRUCE_CRATE_FANCY("chisel", "spruce_planks", 11),
	SPRUCE_CRATEEX("chisel", "spruce_planks", 12),
	SPRUCE_LARGE("chisel", "spruce_planks", 13),
	SPRUCE_CHAOTIC_HOR("chisel", "spruce_planks", 14),
	SPRUCE_CHAOTIC("chisel", "spruce_planks", 15);
	
	
	String modId;
	String itemId;
	int meta;
	ResourceLocation[] textures;
	EWoodType(String modId, String itemId, int meta){
		this.modId = modId;
		this.itemId = itemId;
		this.meta = meta;
		textures = new ResourceLocation[3];
		textures[0] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/64x64/"+modId+"/"+this.name().toLowerCase()+".png");
		textures[1] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/128x64/"+modId+"/"+this.name().toLowerCase()+".png");
		textures[2] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/128x128/"+modId+"/"+this.name().toLowerCase()+".png");
	}
	public String getModID(){
		return modId;
	}
	public String getItemID(){
		return itemId;
	}
	public int getMetaData(){
		return meta;
	}
	public boolean matches(ItemStack target){
		ItemStack woodStack = getPlanksStack();
		if(woodStack == null)
			return false;
		if(woodStack.getItem() == target.getItem() && woodStack.getItemDamage() == target.getItemDamage()){
			return true;
		}
		return false;
	}
	public ItemStack getPlanksStack(){
		ItemStack ret = GameRegistry.findItemStack(modId, itemId, 1);
		if(ret == null){
			return null;
		}
		ret = new ItemStack(ret.getItem(), 1, meta);
		return ret;
	}
	public ResourceLocation getTexture(int index){
		return textures[index];
	}*/
	
}
