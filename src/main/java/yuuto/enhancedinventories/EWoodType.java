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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public enum EWoodType {
	//Vanilla Wood
	OAK("minecraft", "planks", 0),
	SPRUCE("minecraft", "planks", 1),
	BIRCH("minecraft", "planks", 2),
	JUNGLE("minecraft", "planks", 3),
	ACACIA("minecraft", "planks", 4),
	DARK_OAK("minecraft", "planks", 5);
	
	
	String modId;
	String itemId;
	int meta;
	ResourceLocation singleChestLoc;
	ResourceLocation doubleChestLoc;
	ResourceLocation singleLockerLoc;
	ResourceLocation doubleLockerLoc;
	EWoodType(String modId, String itemId, int meta){
		this.modId = modId;
		this.itemId = itemId;
		this.meta = meta;
		singleChestLoc = new ResourceLocation("enhancedinventories", "/textures/uvs/normalChestWood/"+modId+"/"+this.name().toLowerCase()+".png");
		doubleChestLoc = new ResourceLocation("enhancedinventories", "/textures/uvs/doubleChestWood/"+modId+"/"+this.name().toLowerCase()+".png");
		//singleLockerLoc = new ResourceLocation("enhancedinventories", "/textures/uvs/normalLockerWood/"+this.toString().toLowerCase());
		//doubleLockerLoc = new ResourceLocation("enhancedinventories", "/textures/uvs/doubleLockerWood/"+this.toString().toLowerCase());
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
	public ResourceLocation getSingleChestTexture(){
		return singleChestLoc;
	}
	public ResourceLocation getDoubleChestTexture(){
		return doubleChestLoc;
	}
	public ResourceLocation getSingleLockerTexture(){
		return singleLockerLoc;
	}
	public ResourceLocation getDoubleLockerTexture(){
		return doubleLockerLoc;
	}
	
	@Override
	public String toString(){
		String ret = super.toString();
		String[] strings = ret.split("_");
		ret = "";
		for(int i = 0; i < strings.length; i++){
			String s1 = strings[i].substring(0, 1);
			String s2 = strings[i].substring(1).toLowerCase();
			ret+= (s1+s2);
			if(i != strings.length-1)
				ret+= " ";
		}
		
		return ret;
	}
	
}
