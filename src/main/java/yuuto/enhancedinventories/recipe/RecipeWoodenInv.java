package yuuto.enhancedinventories.recipe;

import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.WoolUpgradeHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeWoodenInv extends EnhancedShapedRecipe{

	public boolean woolCrafting = false;
	public int[] woodSlots;
	public int[] woolSlots;
	
	public RecipeWoodenInv(ItemStack result, Object[] recipe, int[] woodSlots) {
		super(result, recipe);
		this.woodSlots = woodSlots;
	}
	public RecipeWoodenInv(ItemStack result, Object[] recipe, int[] woodSlots, int[] woolSlots) {
		super(result, recipe);
		this.woolCrafting = true;
		this.woodSlots = woodSlots;
		this.woolSlots = woolSlots;
	}
	
	 @Override
	 public ItemStack getCraftingResult(InventoryCrafting var1){
		 ItemStack ret = super.getCraftingResult(var1);
		 NBTTagCompound nbt = getTagCompound(var1);
		 if(nbt == null)
			 return null;
		 ret.setTagCompound(nbt);
		 return ret;
	 }
	 
	 NBTTagCompound getTagCompound(InventoryCrafting var1){
		 String woodType = null;
		 for(int i = 0; i < woodSlots.length; i++){
			 ItemStack stack = var1.getStackInSlot(woodSlots[i]);
			 if(stack == null)
				 return null;
			 String s = WoodTypes.getId(stack);
			 if(woodType == null){
				 woodType = s;
				 continue;
			 }
			 if(!woodType.matches(s))
				 return null;
		 }
		 NBTTagCompound nbt = new NBTTagCompound();
		 nbt.setString("woodType", woodType);
		 if(woolCrafting){
			 int wool = -1;
			 for(int i = 0; i < woolSlots.length; i++){
				 ItemStack stack = var1.getStackInSlot(woolSlots[i]);
				 if(stack == null)
					 return null;
				 int woolType = stack.getItemDamage();
				 if(wool == -1){
					 wool = woolType;
					 continue;
				 }
				 if(wool != woolType)
					 return null;
			 }
			 nbt.setByte("wool", (byte)wool);
		 }
		 return nbt;
	 }

}
