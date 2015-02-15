package yuuto.enhancedinventories;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import yuuto.enhancedinventories.EInventoryMaterial;

public class RecipeFunctionUpgrades implements IRecipe{

	Random rand = new Random();
	
	int type = 0;
	ItemStack result;
	
	public RecipeFunctionUpgrades(int type){
		this.type = type;
		switch(type){
		case 0:
		case 1:
		case 2:
		case 6:
			result = new ItemStack(EnhancedInventories.improvedChest);
			break;
		case 3:
		case 4:
		case 5:
			result = new ItemStack(EnhancedInventories.locker);
			break;
		}
	}
	
	@Override
    public boolean matches(InventoryCrafting inv, World world)
    {
		if(type == 0 || type == 3){
			for (int x = 0; x < 3; x++)
	        {
	            for (int y = 0; y < 3; ++y)
	            {
	            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
	            	int i = x*3+y;
	                if(i == 0 || i == 2 || i == 6 || i == 8){
	                	if(mat != null)
	                		return false;
	                }else if(i != 4){
	                	if(mat == null || mat.getItem() != Item.getItemFromBlock(Blocks.hopper))
	                		return false;
	                }else{
	                	if(mat == null || mat.getItem() != result.getItem())
	                		return false;
	                }
	            }
	        }
			return true;
		}
		if(type == 1 || type == 4){
			boolean chest = false;
			boolean tripwire = false;
			for (int x = 0; x < 3; x++)
	        {
	            for (int y = 0; y < 3; ++y)
	            {
	            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
	            	if(mat == null)
	            		continue;
	            	if(mat.getItem() == result.getItem()){
	            		if(chest)
	            			return false;
	            		chest = true;
	            		continue;
	            	}
	            	if(mat.getItem() == Item.getItemFromBlock(Blocks.tripwire_hook)){
	            		if(tripwire)
	            			return false;
	            		tripwire = true;
	            		continue;
	            	}
	            	return false;
	            }
	        }
			return chest && tripwire;
		}
		if(type == 2 || type == 5){
			boolean chest = false;
			for (int x = 0; x < 3; x++)
	        {
	            for (int y = 0; y < 3; ++y)
	            {
	            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
	            	if(mat == null)
	            		continue;
	            	if(chest)
	            		return false;
	            	if(mat.getItem() == result.getItem()){
	            		chest = true;
	            		continue;
	            	}
	            	return false;
	            }
	        }
			return chest;
		}
		if(type == 6){
			boolean chest = false;
			boolean dye = false;
			for (int x = 0; x < 3; x++)
	        {
	            for (int y = 0; y < 3; ++y)
	            {
	            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
	            	if(mat == null)
	            		continue;
	            	if(mat.getItem() == result.getItem()){
	            		if(chest)
	            			return false;
	            		chest = true;
	            		continue;
	            	}
	            	if(WoolUpgradeHelper.getDyeId(mat) >= 0){
	            		if(dye)
	            			return false;
	            		dye = true;
	            		continue;
	            	}
	            	
	            	
	            	return false;
	            }
	        }
			return chest && dye;
		}

        return false;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack ret = null;
		int dyeId = -1;
		if(type == 0 || type == 3){
			ret = inv.getStackInRowAndColumn(1, 1).copy();
		}else{
			for (int x = 0; x < 3; x++)
	        {
	            for (int y = 0; y < 3; ++y)
	            {
	            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
	            	if(mat == null || mat.getItem() != result.getItem()){
	            		if(type == 6){
		            		int id = WoolUpgradeHelper.getDyeId(mat);
		            		if(id >= 0)
		            			dyeId = id;
	            		}
	            		continue;
	            	}
	            	ret = mat.copy();
	            }
            }
		}
		if(ret == null)
			return null;
		ret.stackSize = 1;
		if(type == 0 || type == 3)
			ret.getTagCompound().setBoolean("hopper", true);
		else if(type == 1 || type == 4)
			ret.getTagCompound().setBoolean("redstone", true);
		else if(type == 2 || type == 5)
			ret.getTagCompound().setBoolean("alt", !ret.getTagCompound().getBoolean("alt"));
		else if(type == 6){
			if(dyeId < 0)
				return null;
			ret.getTagCompound().setByte("wool", (byte)WoolUpgradeHelper.getCollorValue(dyeId));
		}
		return ret;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack ret = result.copy();
		NBTTagCompound nbt = new NBTTagCompound();
		ret.setItemDamage(rand.nextInt(EInventoryMaterial.values().length));
		nbt.setString("woodType", WoodTypes.getWoodTypes().get(rand.nextInt(WoodTypes.getWoodTypes().size())).id());
		if(type == 0 || type == 1 || type == 2)
			nbt.setByte("wool", (byte)rand.nextInt(16));
		nbt.setBoolean("hopper", type == 0 || type == 3);
		nbt.setBoolean("redstone", type == 1 || type == 4);
		nbt.setBoolean("alt", type == 2 || type == 5);
		ret.setTagCompound(nbt);
		return ret;
	}

}
