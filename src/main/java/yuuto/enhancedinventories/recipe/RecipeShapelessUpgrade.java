package yuuto.enhancedinventories.recipe;

import yuuto.enhancedinventories.WoolUpgradeHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeShapelessUpgrade extends EnhancedShapelessRecipe{

	int type;
	
	public RecipeShapelessUpgrade(ItemStack result, Object[] recipe, int type) {
		super(result, recipe);
		this.type = type;
	}
	
	 @Override
	 public ItemStack getCraftingResult(InventoryCrafting var1){
		 ItemStack ret = super.getCraftingResult(var1);
		 ItemStack original = null;
		 for(int i = 0; i < var1.getSizeInventory(); i++){
			 ItemStack stack = var1.getStackInSlot(i);
			 if(stack == null)
				 continue;
			 if(stack.getItem() == ret.getItem()){
				 original = stack;
				 break;
			 }
		 }
		 ret.setItemDamage(original.getItemDamage());
		 NBTTagCompound nbt = getTagCompound(var1, (NBTTagCompound)original.getTagCompound().copy());
		 if(nbt == null)
			 return null;
		 ret.setTagCompound(nbt);
		 return ret;
	 }

	NBTTagCompound getTagCompound(InventoryCrafting var1, NBTTagCompound copy) {
		if(type == 0){
			copy.setBoolean("redstone", true);
			return copy;
		}
		if(type == 1){
			copy.setBoolean("alt", true);
			return copy;
		}
		if(type == 2){
			int wool = getWoolColor(var1);
			if(wool < 0)
				return null;
			if(wool == copy.getInteger("wool"))
				return null;
			copy.setByte("wool", (byte)wool);
			return copy;
		}
		return null;
	}
	int getWoolColor(InventoryCrafting var1) {
		for(int i = 0; i < var1.getSizeInventory(); i++){
			ItemStack stack = var1.getStackInSlot(i);
			if(stack == null)
				continue;
			int woolID = WoolUpgradeHelper.getDyeId(stack);
			if(woolID < 0)
				continue;
			return WoolUpgradeHelper.getCollorValue(woolID);
		}
		return -1;
	}

}
