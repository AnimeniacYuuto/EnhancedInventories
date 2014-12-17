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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeImprovedChest extends ShapedOreRecipe{

	int subType;
	
	public RecipeImprovedChest(int subType, ItemStack result, Object[] recipe) {
		super(result, recipe);
		this.subType = subType;
	}
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting var1){ 
		switch(subType){
		case 0:
			return getBasicResult(var1);
		case 1:
			return getHopperResult(var1);
		case 2:
			return getAltResult(var1);
		case 3:
			return getRedstoneResult(var1);
		default:
			return super.getCraftingResult(var1);
		}
	}
	
	public ItemStack getRedstoneResult(InventoryCrafting var1) {
		ItemStack ret = super.getCraftingResult(var1);
		for(int i = 0; i < var1.getSizeInventory(); i++){
			if(var1.getStackInSlot(i) == null)
				continue;
			if(var1.getStackInSlot(i).getItem() != Item.getItemFromBlock(EnhancedInventories.improvedChest))
				continue;
			if(var1.getStackInSlot(i).hasTagCompound())
				ret.setTagCompound((NBTTagCompound)var1.getStackInSlot(i).getTagCompound().copy());
			else
				ret.setTagCompound(generateNBT());
		}
		ret.getTagCompound().setBoolean("redstone", true);
		return ret;
	}

	public ItemStack getBasicResult(InventoryCrafting var1){
		ItemStack ret = super.getCraftingResult(var1);
		if(ret.getItemDamage() == 0){
			int wood = WoodTypes.getIdOf(var1.getStackInRowAndColumn(0, 1));
			int wool = var1.getStackInRowAndColumn(1, 1).getItemDamage();
			if(wood < 0)
				wood = 0;
			ret.setTagCompound(new NBTTagCompound());
			ret.getTagCompound().setByte("wood", (byte)wood);
			ret.getTagCompound().setByte("wool", (byte)wool);
			ret.getTagCompound().setBoolean("hopper", false);
			ret.getTagCompound().setBoolean("alt", false);
			ret.getTagCompound().setBoolean("redstone", false);
			return ret;
		}
		if(var1.getStackInRowAndColumn(1, 1).hasTagCompound())
			ret.setTagCompound((NBTTagCompound)var1.getStackInRowAndColumn(1, 1).getTagCompound().copy());
		else
			ret.setTagCompound(generateNBT());
		return ret;
	}
	public ItemStack getHopperResult(InventoryCrafting var1){
		ItemStack ret = super.getCraftingResult(var1);
		if(var1.getStackInRowAndColumn(1, 1).hasTagCompound())
			ret.setTagCompound((NBTTagCompound)var1.getStackInRowAndColumn(1, 1).getTagCompound().copy());
		else
			ret.setTagCompound(generateNBT());
		ret.getTagCompound().setBoolean("hopper", true);
		return ret;
	}
	public ItemStack getAltResult(InventoryCrafting var1){
		ItemStack ret = super.getCraftingResult(var1);
		for(int i = 0; i < var1.getSizeInventory(); i++){
			if(var1.getStackInSlot(i) == null)
				continue;
			if(var1.getStackInSlot(i).hasTagCompound())
				ret.setTagCompound((NBTTagCompound)var1.getStackInSlot(i).getTagCompound().copy());
			else
				ret.setTagCompound(generateNBT());
		}
		ret.getTagCompound().setBoolean("alt", !ret.getTagCompound().getBoolean("alt"));
		return ret;
	}
	
	
	public NBTTagCompound generateNBT(){
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setByte("wood", (byte)0);
		nbt.setByte("wool", (byte)0);
		nbt.setBoolean("hopper", false);
		nbt.setBoolean("alt", false);
		nbt.setBoolean("redstone", false);
		return nbt;
	}

}
