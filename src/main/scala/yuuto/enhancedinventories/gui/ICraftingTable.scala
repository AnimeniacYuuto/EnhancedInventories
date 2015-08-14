/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World;
import yuuto.yuutolib.inventory.IInventoryExtended

trait ICraftingTable {

  def onCraftMatrixChanged(inv:IInventory);
  
  def getCraftingMatrix():InventoryCrafting;
  
  def getCraftResult():IInventory;
  
  def findMatchingRecipe(inventoryCrafting:InventoryCrafting, world:World):ItemStack;
  
  def getCurrentRecipe():IRecipe;

  def getSubInventories():Array[IInventoryExtended];
  
  def isUsingSchematic():Boolean;
  
  def getSchematic():ItemStack;

}