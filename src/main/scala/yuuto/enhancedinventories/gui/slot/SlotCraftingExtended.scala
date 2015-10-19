/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui.slot

import java.util.ArrayList
import java.util.List
import java.util.Iterator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.inventory.SlotCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraftforge.oredict.ShapelessOreRecipe
import yuuto.enhancedinventories.gui.ICraftingTable
import yuuto.yuutolib.inventory.InventorySimple
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.util.LogHelperEI
import yuuto.yuutolib.inventory.InventoryHelper
import yuuto.yuutolib.inventory.IInventoryExtended
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent
import net.minecraftforge.common.MinecraftForge

class SlotCraftingExtended(val craftingTable:ICraftingTable, player:EntityPlayer, craftMatrix:IInventory, result:IInventory, slotIndex:Int, x:Int, y:Int) 
  extends SlotCrafting(player, craftMatrix, result, slotIndex, x, y){
  
  override def canTakeStack(player:EntityPlayer):Boolean={
    //If not using a schematic then this can pull as normal for sure
    if(!craftingTable.isUsingSchematic())
      return super.canTakeStack(player);
    //Get the crafting matrix and recipe for the schematic
    val craftMatrix:InventoryCrafting = ItemSchematic.getCraftingMatrix(craftingTable.getSchematic());
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
      val invOrg:IInventoryExtended = subInventories(i);
      val copy:IInventory = new InventorySimple(invOrg.getSizeInventory());
      for(j <-0 until copy.getSizeInventory() if(invOrg.getStackInSlot(j) != null)){
        copy.setInventorySlotContents(j, invOrg.getStackInSlot(j).copy());
      }
      copyInvs(i) = copy;
    }
    return pullItems(craftMatrix, craftingTable.getCurrentRecipe(), copyInvs, subInventories, player, false, true);
  }
  def pullItems(craftMatrix:InventoryCrafting, recipe:IRecipe, invs:Array[IInventory], invs2:Array[IInventoryExtended], player:EntityPlayer, useMatrix:Boolean, simulate:Boolean):Boolean={
    //vars for checking
    var index:Int = -1; //the xth stack in a recipe
    var found:Boolean = false;
    //confirm recipe contents exist in the inventories
    for(i <-0 until 9){
      found = false;
      val stack1:ItemStack = craftMatrix.getStackInSlot(i);
      if(stack1 == null){}
      else{
        var containerItem:ItemStack = null; //the container item of the item used in crafting
        index+=1;
        //Gets possible ores to check against
        val ores:ArrayList[ItemStack] = getOresFromRecipe(recipe, index);
        var stack2:ItemStack=null;
        var invIndex= -1;
        for(j <-0 until invs.length if(!found)){
          stack2=pullFromInventory(stack1, ores, invs(j), invs2(j));
          if(stack2 != null && stack2.stackSize > 0){
             found = true;
             invIndex=j;
             if(stack2.getItem().hasContainerItem(stack2)){
               containerItem=stack2.getItem.getContainerItem(stack2);
             }
          }
        }
        if(!found && useMatrix){
          stack2=pullFromCraftingMatrix(craftMatrix, i)
          if(stack2 != null && stack2.stackSize > 0){
             found = true;
             if(stack2.getItem().hasContainerItem(stack2)){
               containerItem=stack2.getItem.getContainerItem(stack2);
             }
          }
        }
        if(simulate && !found){
          return false;
        }else if(found && containerItem != null){
          LogHelperEI.Debug("Returning container item");
          if(containerItem.isItemStackDamageable() && containerItem.getItemDamage() > containerItem.getMaxDamage()){
            LogHelperEI.Debug("Destroying container item");
            if(!simulate){
              MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, containerItem));
            }
          }else if(invIndex == -1){
            LogHelperEI.Debug("Returning to craftMatrix");
            if(!useMatrix || !returnItem(stack2, containerItem, player, craftMatrix, i, simulate) && simulate)
              return false;
          }else{
            LogHelperEI.Debug("Returning to inv");
            if(!returnItem(containerItem, player, invs(invIndex), invs2(invIndex), simulate) && simulate){
              return false;
            }
          }
        }
      }
    }
    return true;
  }
  def pullFromCraftingMatrix(craftMatrix:InventoryCrafting, index:Int):ItemStack={
    return craftMatrix.decrStackSize(index, 1);
  }
  def pullFromInventory(stack1:ItemStack, ores:ArrayList[ItemStack], inv:IInventory, inv2:IInventoryExtended):ItemStack={
    var found:Boolean=false;
    for(sl <- 0 until inv.getSizeInventory() if(inv.getStackInSlot(sl)!=null && inv2.canExtractItem(sl, inv.getStackInSlot(sl)))){
      val stack2:ItemStack = inv.getStackInSlot(sl);
      //Check ores if any
      if(ores != null){
        val itr:Iterator[ItemStack]=ores.iterator();
        while(!found && itr.hasNext()){
          val ore:ItemStack=itr.next();
          if(OreDictionary.itemMatches(ore, stack2, false)){
              found = true;
          }
        }
        if(!found){
            found = OreDictionary.itemMatches(stack1, stack2, false);
        }
        if(found){
            
        }
      //Checks the 2 items if not oredictionary
      }else if(OreDictionary.itemMatches(stack1, stack2, false)){
        found = true;
      }
      if(found){
        return inv.decrStackSize(sl, 1);;
      }
    }
    return null;
  }
  
  def returnItem(stack:ItemStack, containerItem:ItemStack, player:EntityPlayer, matrix:InventoryCrafting, slot:Int, simulate:Boolean):Boolean={
    if(simulate)
      return true;
    if(!stack.getItem().doesContainerItemLeaveCraftingGrid(stack) && matrix.getStackInSlot(slot)==null){
      matrix.setInventorySlotContents(slot, containerItem);
      return true;
    }
    if(player.inventory.addItemStackToInventory(containerItem))
      return true;
    player.dropPlayerItemWithRandomChoice(containerItem, false);
    return true;
  }
  def returnItem(containerItem:ItemStack, player:EntityPlayer, inv:IInventory, inv2:IInventoryExtended, simulate:Boolean):Boolean={
    if(simulate)
      return true;
    LogHelperEI.Debug("Returning item");
    if(InventoryHelper.mergeStack(containerItem, inv2, simulate))
      return true;
    if(player.inventory.addItemStackToInventory(containerItem))
      return true;
    player.dropPlayerItemWithRandomChoice(containerItem, false);
      return true;
  }
  override def onPickupFromSlot(player:EntityPlayer, stack:ItemStack) {
    this.onCrafting(stack);
    var craftMatrix:InventoryCrafting = craftingTable.getCraftingMatrix();
    //grabs the schematic crafting matrix if using a schematic
    if(craftingTable.isUsingSchematic()){
      val craftMatrix1:InventoryCrafting = ItemSchematic.getCraftingMatrix(craftingTable.getSchematic());
      if(craftMatrix1 != null){
        craftMatrix = craftMatrix1;
        craftingTable.findMatchingRecipe(craftMatrix, player.worldObj);
      }
    }
    pullItems(craftMatrix, craftingTable.getCurrentRecipe(), craftingTable.getSubInventories().asInstanceOf[Array[IInventory]], craftingTable.getSubInventories(), player, !craftingTable.isUsingSchematic(), false);
    //int index = -1; //index of the ingredient out of the total number of ingredients
    /*boolean found = false;
    for(int i = 0; i < 9; i++){
      found = false;
      ItemStack stack1 = craftMatrix.getStackInSlot(i);
      if(stack1 == null)
        continue;
      index++;
      ItemStack containerItem = null; //the container item of the item used in crafting
      if(stack1.stackSize == 1){        
        ArrayList<ItemStack> ores = getOresFromRecipe(craftingTable.getCurrentRecipe(), index);
        for(IInventory inv : craftingTable.getSubInventories()){
          for(int sl = 0; sl < inv.getSizeInventory(); sl++){
            ItemStack stack2 = inv.getStackInSlot(sl);
            if(stack2 == null)
              continue;
            //Check if ore dictioaried
            if(ores != null){
              for(ItemStack ore : ores){
                                if(OreDictionary.itemMatches(ore, stack2, false)){
                                    found = true;
                                    break;
                                }
                            }
                            if(!found){
                                found = OreDictionary.itemMatches(stack1, stack2, false);
                            }
                            if(found){
                              if(stack2.getItem().hasContainerItem(stack2))
                                containerItem = stack2.getItem().getContainerItem(stack2);
                                inv.decrStackSize(sl, 1);
                            }
                        //Check matches
            }else if(OreDictionary.itemMatches(stack1, stack2, false)){
              if(stack2.getItem().hasContainerItem(stack2))
                            containerItem = stack2.getItem().getContainerItem(stack2);
              inv.decrStackSize(sl, 1);
              found = true;
            }
            if(found)
              break;
          }
          if(found)
            break;
        }
      }
      //if not found and not using a schematic, pull from the matrix
      if(!found && !this.craftingTable.isUsingSchematic()){
        if(stack1.getItem().hasContainerItem(stack1))
          containerItem = stack1.getItem().getContainerItem(stack1);
        craftMatrix.decrStackSize(i, 1);
      }
      //adds container to player inventory
      if(containerItem != null){
        if(!player.inventory.addItemStackToInventory(containerItem))
          player.dropPlayerItemWithRandomChoice(containerItem, false);
      }
    }*/
    craftingTable.onCraftMatrixChanged(craftMatrix);
  }
  
  def getOresFromRecipe(recipe:IRecipe, index:Int):ArrayList[ItemStack]={
    LogHelperEI.Debug("Checking ores at "+index);
    if(recipe.isInstanceOf[ShapedOreRecipe]){
      var cnt = -1;
      var i = -1;
      //Find the item of index out of acctual items (used so that small recipes don't break
      val inputList:Array[Object] = recipe.asInstanceOf[ShapedOreRecipe].getInput();
      if (inputList != null && inputList.length > index){
        while(cnt < index && i < inputList.length){
          i+=1
          if(inputList(i) != null){
            cnt+=1;
          }
        }
        if(i>=inputList.length || inputList(i)==null)
          return null;
        if(inputList(i).isInstanceOf[ArrayList[_]]){
            return inputList(i).asInstanceOf[ArrayList[ItemStack]];             
        }
      }
    }else if(recipe.isInstanceOf[ShapelessOreRecipe]){
      val inputList:List[Object] = recipe.asInstanceOf[ShapelessOreRecipe].getInput();
      if (inputList != null && inputList.size() > index) {
          if (inputList.get(index) == null)
              return null;
          if(inputList.get(index).isInstanceOf[ArrayList[_]]){
              return inputList.get(index).asInstanceOf[ArrayList[ItemStack]];
          }
      }
    }
    return null;
  }

}