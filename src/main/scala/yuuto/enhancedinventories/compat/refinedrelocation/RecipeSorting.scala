/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.refinedrelocation

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.proxy.ProxyCommon;
import yuuto.yuutolib.recipe.ExtendedShapelessOreRecipe

class RecipeSorting(result:ItemStack, recipe:Object*) extends ExtendedShapelessOreRecipe(result, recipe.toArray){
  def this(result:Block, recipe:Object*)=this(new ItemStack(result), recipe);
  def this(result:Item, recipe:Object*)=this(new ItemStack(result), recipe);
  
  override def getCraftingResult(inv:InventoryCrafting):ItemStack={
    val res:ItemStack = this.getRecipeOutput().copy();
    if(res == null)
      return null;
    val original:ItemStack = findOriginal(inv);
    if(original == null || !original.hasTagCompound()){
      res.setTagCompound(defaultTagCompound());
      return res;
    }
    res.setTagCompound(original.getTagCompound().copy().asInstanceOf[NBTTagCompound]);
    return res;
  }
  private def defaultTagCompound():NBTTagCompound={
    val ret:NBTTagCompound = new NBTTagCompound();
    DecorationHelper.setCoreBlock(ret, Blocks.planks, 0);
    DecorationHelper.setFrame(ret, FrameMaterials.Stone);
    DecorationHelper.setWool(ret, 4);
    return ret;
  }
  def findOriginal(inv:InventoryCrafting):ItemStack={
    for( i <- 0 until inv.getSizeInventory() if(inv.getStackInSlot(i) != null && upgradable(inv.getStackInSlot(i)))){
      return inv.getStackInSlot(i);
    }
    return null;
  }
  def upgradable(stack:ItemStack):Boolean={
    if(stack.getItem() == Item.getItemFromBlock(ProxyCommon.blockImprovedChest))
      return true;
    if(stack.getItem() == Item.getItemFromBlock(ProxyCommon.blockLocker))
      return true;
    if(stack.getItem() == Item.getItemFromBlock(ProxyCommon.blockCabinet))
      return true;
    return false;
  }

}