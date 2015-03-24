package yuuto.enhancedinventories.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeHopperUpgrade extends EnhancedShapedRecipe{

	public int originalSlot = 0;
	
	public RecipeHopperUpgrade(ItemStack result, Object[] recipe, int originalSlot) {
		super(result, recipe);
		this.originalSlot = originalSlot;
	}
	
	 @Override
	 public ItemStack getCraftingResult(InventoryCrafting var1){
		 ItemStack ret = super.getCraftingResult(var1);
		 ItemStack original = var1.getStackInSlot(originalSlot);
		 NBTTagCompound nbt = original.getTagCompound();
		 if(nbt == null)
			 return null;
		 nbt.setBoolean("hopper", true);
		 ret.setTagCompound(nbt);
		 ret.setItemDamage(original.getItemDamage());
		 return ret;
	 }

}
