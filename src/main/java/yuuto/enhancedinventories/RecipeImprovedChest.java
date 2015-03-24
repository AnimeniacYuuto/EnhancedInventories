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

import yuuto.enhancedinventories.item.ItemBlockImprovedChest;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
    public boolean matches(InventoryCrafting inv, World world)
    {
        if(subType != 0)
        	return super.matches(inv, world);
        if(!super.matches(inv, world))
        	return false;
        ItemStack wood = null;
        for(int x = 0; x < 3; x++){
        	for(int y = 0; y < 3; y++){
            	if(!((x==1 && y != 1) || (y == 1 && x!=1)))
            		continue;
        		if(wood == null){
        			wood = inv.getStackInRowAndColumn(x, y);
        			continue;
        		}
    			ItemStack stack = inv.getStackInRowAndColumn(x, y);
    			if(wood.getItem() != stack.getItem())
    				return false;
    			if(wood.getItemDamage() != stack.getItemDamage())
    				return false;
    			if(wood.hasTagCompound() != stack.hasTagCompound())
    				return false;
    			if(wood.hasTagCompound() && !ItemStack.areItemStacksEqual(wood, stack))
    				return false;
            }
        }
        return true;
    }
	
	@Override
    public ItemStack getCraftingResult(InventoryCrafting var1){ 
		if(subType == 0)
			return getBasicResult(var1);
		if(subType == 1)
			return getDyeResult(var1);
		if(subType == 2)
			return getAltResult(var1);
		return null;
	}
	
	public ItemStack getDyeResult(InventoryCrafting var1) {
		ItemStack ret = super.getCraftingResult(var1);
		ItemStack dye = null;
		for(int i = 0; i < var1.getSizeInventory(); i++){
			if(var1.getStackInSlot(i) == null)
				continue;
			if(var1.getStackInSlot(i).getItem() != Item.getItemFromBlock(EnhancedInventories.improvedChest)){
				dye = var1.getStackInSlot(i);
				continue;
			}
			else if(var1.getStackInSlot(i).hasTagCompound())
				ret.setTagCompound((NBTTagCompound)var1.getStackInSlot(i).getTagCompound().copy());
			else
				ret.setTagCompound(generateNBT(ret));
		}
		int dyeColor = WoolUpgradeHelper.getDyeId(dye);
		dyeColor = WoolUpgradeHelper.getCollorValue(dyeColor);
		if(dyeColor < 0)
			dyeColor = 0;
		if(dyeColor > 15)
			dyeColor = 15;
		ret.getTagCompound().setByte("wool", (byte)dyeColor);
		return ret;
	}

	public ItemStack getBasicResult(InventoryCrafting var1){
		ItemStack ret = super.getCraftingResult(var1);
		if(ret.getItemDamage() == 0){
			String wood = WoodTypes.getId(var1.getStackInRowAndColumn(0, 1));
			int wool = var1.getStackInRowAndColumn(1, 1).getItemDamage();
			if(wood == null)
				return null;
			ret.setTagCompound(new NBTTagCompound());
			ret.getTagCompound().setString("woodType", wood);
			if(ret.getItem() instanceof ItemBlockImprovedChest)
				ret.getTagCompound().setByte("wool", (byte)wool);
			ret.getTagCompound().setBoolean("hopper", false);
			ret.getTagCompound().setBoolean("alt", false);
			ret.getTagCompound().setBoolean("redstone", false);
			return ret;
		}
		if(var1.getStackInRowAndColumn(1, 1).hasTagCompound())
			ret.setTagCompound((NBTTagCompound)var1.getStackInRowAndColumn(1, 1).getTagCompound().copy());
		else
			ret.setTagCompound(generateNBT(ret));
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
				ret.setTagCompound(generateNBT(ret));
		}
		ret.getTagCompound().setBoolean("alt", !ret.getTagCompound().getBoolean("alt"));
		return ret;
	}
	
	
	public NBTTagCompound generateNBT(ItemStack stack){
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("woodType", WoodTypes.DEFAULT_WOOD_ID);
		if(stack.getItem() instanceof ItemBlockImprovedChest)
			nbt.setByte("wool", (byte)0);
		nbt.setBoolean("hopper", false);
		nbt.setBoolean("alt", false);
		nbt.setBoolean("redstone", false);
		return nbt;
	}

}
