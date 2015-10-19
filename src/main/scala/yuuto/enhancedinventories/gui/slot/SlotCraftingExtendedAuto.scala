/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui.slot

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import yuuto.enhancedinventories.gui.ICraftingTable
import net.minecraft.item.ItemStack
import net.minecraft.inventory.InventoryCrafting
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.yuutolib.inventory.InventorySimple
import net.minecraftforge.oredict.OreDictionary
import yuuto.yuutolib.inventory.InventoryHelper
import yuuto.yuutolib.inventory.IInventoryExtended

class SlotCraftingExtendedAuto(craftingTable:ICraftingTable, player:EntityPlayer, craftMatrix:IInventory, result:IInventory, slotIndex:Int, x:Int, y:Int) 
  extends SlotCraftingExtended(craftingTable, player, craftMatrix, result, slotIndex, x, y) {
  
  override def canTakeStack(player:EntityPlayer):Boolean={
    //If not using a schematic then this can pull as normal for sure
    var craftMatrix:InventoryCrafting=craftingTable.getCraftingMatrix();
    //Get the crafting matrix and recipe for the schematic
    if(craftingTable.isUsingSchematic())
      craftMatrix = ItemSchematic.getCraftingMatrix(craftingTable.getSchematic());
    if(craftMatrix != null){
      val result:ItemStack = craftingTable.findMatchingRecipe(craftMatrix, player.worldObj);
      if(result == null || !OreDictionary.itemMatches(result, this.getStack(), true))
        return false;
    }
    //make copies of sub-inventories
    val subInventories:Array[IInventoryExtended] = craftingTable.getSubInventories();
    if(subInventories == null || subInventories.length < 1)
      return false;
    val copyInvs:Array[IInventory] = new Array[IInventory](subInventories.length);
    for(i <-0 until subInventories.length){
      val invOrg:IInventory = subInventories(i);
      val copy:IInventory = new InventorySimple(invOrg.getSizeInventory());
      for(j <-0 until copy.getSizeInventory() if(invOrg.getStackInSlot(j) != null)){
        copy.setInventorySlotContents(j, invOrg.getStackInSlot(j).copy());
      }
      copyInvs(i) = copy;
    }
    return pullItems(craftMatrix, craftingTable.getCurrentRecipe(), copyInvs, subInventories, player, false, true);
  }
  override def pullFromCraftingMatrix(craftMatrix:InventoryCrafting, index:Int):ItemStack={
    return null;
  }
  override def returnItem(stack:ItemStack, containerItem:ItemStack, player:EntityPlayer, matrix:InventoryCrafting, slot:Int, simulate:Boolean):Boolean={
    return false;
  }
  override def returnItem(containerItem:ItemStack, player:EntityPlayer, inv:IInventory, inv2:IInventoryExtended, simulate:Boolean):Boolean={
    if(simulate)
      return mergeStack(containerItem, inv, inv2)
    return InventoryHelper.mergeStack(containerItem, inv2, simulate);
  }
  
  def mergeStack(stack:ItemStack, inv:IInventory, inv2:IInventoryExtended):Boolean={
    if(stack.stackSize < 1)
      return false;
    var max:Int= Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
    for(i<-0 until inv.getSizeInventory() if(inv2.canInsertItem(i, stack))){
      var stack2=inv.getStackInSlot(i);
      if(stack2 == null){
        val m2=Math.min(stack.stackSize, max);
        if(m2 == stack.stackSize){
          stack2=stack.copy();
          stack.stackSize=0;
        }else{
          stack2=stack.splitStack(m2);
        }
        inv.setInventorySlotContents(i, stack2);
        if(stack.stackSize < 1)
          return true;
      }
      else if(!stack.isItemEqual(stack2)){}
      else if(!ItemStack.areItemStackTagsEqual(stack, stack2)){}
      else if(stack2.stackSize < max){
        val m2=Math.min(stack.stackSize, max-stack2.stackSize);
        stack2.stackSize+=m2;
        inv.setInventorySlotContents(i, stack2);
        stack.stackSize-=m2;
        if(stack.stackSize < 1)
          return true;
      }
    }
    return false;
  }
}