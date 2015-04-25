package yuuto.enhancedinventories;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeConverter extends ShapedOreRecipe{
	
	public RecipeConverter(ItemStack result, Object[] recipe) {
		super(result, recipe);
	}
	@Override
    public ItemStack getCraftingResult(InventoryCrafting var1){
		ItemStack chest = null;
		for(int i = 0; i < var1.getSizeInventory(); i++){
			if(var1.getStackInSlot(i) == null)
				continue;
			if(var1.getStackInSlot(i).getItem() == Item.getItemFromBlock(EnhancedInventories.improvedChest)){
				chest = var1.getStackInSlot(i);
				break;
			}
		}
		if(chest == null)
			return null;
		ItemStack result = this.getRecipeOutput().copy();
		result.setTagCompound((NBTTagCompound)chest.getTagCompound().copy());
		result.stackSize = 1;
		return result;
	}

}
