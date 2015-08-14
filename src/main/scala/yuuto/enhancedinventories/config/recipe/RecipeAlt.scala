/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.recipe

import net.minecraft.item.ItemStack
import net.minecraft.item.Item
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.nbt.NBTTagCompound
import yuuto.yuutolib.recipe.ExtendedShapelessOreRecipe
import yuuto.enhancedinventories.materials.DecorationHelper

class RecipeAlt(result:ItemStack, recipe:Array[Object]) extends ExtendedShapelessOreRecipe(result, recipe){
  def this(result:ItemStack, recipe:Object*)=this(result, recipe.toArray)
  def this(result:Block, recipe:Object)=this(new ItemStack(result), recipe);
  def this(result:Item , recipe:Object)=this(new ItemStack(result), recipe);
  
  override def getCraftingResult(var1:InventoryCrafting):ItemStack={ 
    val ret=this.getRecipeOutput.copy();
    val original=this.getOriginal(var1);
    if(original != null && original.hasTagCompound()){
      ret.setTagCompound(original.getTagCompound().copy().asInstanceOf[NBTTagCompound]);
    }
    if(ret.hasTagCompound()){
      if(ret.getTagCompound().getBoolean(DecorationHelper.KEY_ALTERNATE)){
        ret.getTagCompound.removeTag(DecorationHelper.KEY_ALTERNATE);
      }else{
        ret.getTagCompound().setBoolean(DecorationHelper.KEY_ALTERNATE, true)
      }
    }
    return ret;
  }
  def getOriginal(var1:InventoryCrafting):ItemStack={
    for(i<-0 until var1.getSizeInventory()){
      if(var1.getStackInSlot(i) != null){
        return var1.getStackInSlot(i)
      }
    }
    return null;
  }
}