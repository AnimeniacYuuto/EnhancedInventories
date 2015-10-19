/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import invtweaks.api.container.ChestContainer
import invtweaks.api.container.ContainerSection
import invtweaks.api.container.ContainerSectionCallback
import java.util.Map
import java.util.HashMap
import java.util.List
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.gui.slot.SlotCraftingExtended
import yuuto.enhancedinventories.gui.slot.SlotSchematic
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.tile.TileAutoAssembler
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

@ChestContainer(rowSize=3)
class ContainerAutoAssembler(val tile:TileAutoAssembler, player:EntityPlayer) extends Container{
  val craftingMatrix:InventoryCrafting=tile.getCraftingMatrix();
  val craftResult:IInventory=tile.getCraftResult();
  val chestInventory:IInventory=tile.getInventory();
  val playerInv:InventoryPlayer=player.inventory;
  var craftMatrixStart:Int = 2;
  var chestStart:Int=0;
  var playerStart:Int=0;
  var slotMap:Map[ContainerSection, List[Slot]]=null;
  init();
  
  def init(){
    this.addSlotToContainer(new SlotSchematic(chestInventory, 9, 80, 53));
    this.addSlotToContainer(new SlotCraftingExtended(tile, this.playerInv.player, this.craftingMatrix, this.craftResult, 0, 80, 21));
    
    //var x,y:Int=0;
    
    //Crafting Matrix
    craftMatrixStart = this.inventorySlots.size();
    for (y <-0 until 3){
        for (x <-0 until 3)
        {
            this.addSlotToContainer(new Slot(this.craftingMatrix, x + y * 3, 8 + x * 18, 17 + y * 18));
        }
    }
    
    //Chest slots
    chestStart = this.inventorySlots.size();
    for (y <-0 until 3)
        {
            for (x <-0 until 3)
            {
                this.addSlotToContainer(new Slot(this.chestInventory, x + y * 3, 116 + x * 18, 17 + y * 18));
            }
        }
    
    //Player Inventory
    playerStart = this.inventorySlots.size();
    for (y <-0 until 3)
        {
            for (x <- 0 until 9)
            {
                this.addSlotToContainer(new Slot(this.playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
    for (x <- 0 until 9){
        this.addSlotToContainer(new Slot(this.playerInv, x, 8 + x * 18, 142));
    }
    
    //Update the craft matrix!
    tile.onCraftMatrixChanged(craftingMatrix);
  }
  
  override def canInteractWith(player:EntityPlayer):Boolean={
    return player.getDistanceSq(this.tile.xCoord + 0.5D, this.tile.yCoord + 0.5D, this.tile.zCoord + 0.5D) <= 64.0D;
  }
  @SideOnly(Side.CLIENT)
  override def putStacksInSlots(stacks:Array[ItemStack]){
    tile.syncronizing=true;
    super.putStacksInSlots(stacks);
    tile.syncronizing=false;
    tile.onCraftMatrixChanged(this.craftingMatrix);
  }
  
  override def transferStackInSlot(player:EntityPlayer, slotIndex:Int):ItemStack={
        var itemstack:ItemStack = null;
        val slot:Slot = this.inventorySlots.get(slotIndex).asInstanceOf[Slot];

        if (slot != null && slot.getHasStack())
        {
            val itemstack1:ItemStack = slot.getStack();
            itemstack = itemstack1.copy();

            //Move stack from schematic slot
            if(slotIndex == 0){
              if(!this.mergeItemStack(itemstack1, chestStart, this.inventorySlots.size()-1, false)){
                return null;
              }
            //Attempt to move schematic to schematic slot --Broken
            }else if(itemstack1.getItem() == ProxyCommon.schematic && this.mergeItemStack(itemstack1, 0, 0, false)){
              this.tile.onCraftMatrixChanged(this.chestInventory);
            //Attempt to move result stack
            }else if (slotIndex == 1){
                if (!this.mergeItemStack(itemstack1, chestStart, this.inventorySlots.size()-1, true)){
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            //Moving crafting matrix stacks to chest or player inventory
            }else if (slotIndex >= craftMatrixStart && slotIndex < chestStart){
                if (!this.mergeItemStack(itemstack1, chestStart, this.inventorySlots.size()-1, false))
                {
                    return null;
                }
            //move chest stacks to player inventory
            }else if (slotIndex >= chestStart && slotIndex < playerStart){
                if (!this.mergeItemStack(itemstack1, playerStart, this.inventorySlots.size()-1, false))
                {
                    return null;
                }
            //Move player stacks to chest inventory
            }else if(slotIndex >= playerStart && slotIndex < this.inventorySlots.size()){
              if(!this.mergeItemStack(itemstack1, chestStart, playerStart-1, false)){
                return null;
              }
            //Final catch move to player inventory
            }else if (!this.mergeItemStack(itemstack1, this.playerStart, this.inventorySlots.size()-1, false)){
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

  override def func_94530_a(stack:ItemStack, slot:Slot):Boolean={
      return slot.inventory != this.craftResult && super.func_94530_a(stack, slot);
  }
  
  @ContainerSectionCallback
  def getTweakSections():Map[ContainerSection, List[Slot]]={
    if(slotMap==null){
      slotMap=new HashMap[ContainerSection, List[Slot]]();
      slotMap.put(ContainerSection.CRAFTING_OUT, inventorySlots.asInstanceOf[List[Slot]].subList(1, 2))
      slotMap.put(ContainerSection.CRAFTING_IN, inventorySlots.asInstanceOf[List[Slot]].subList(craftMatrixStart, chestStart))
      slotMap.put(ContainerSection.CHEST, inventorySlots.asInstanceOf[List[Slot]].subList(chestStart, playerStart))
      slotMap.put(ContainerSection.INVENTORY, inventorySlots.asInstanceOf[List[Slot]].subList(playerStart, inventorySlots.size()))
      slotMap.put(ContainerSection.INVENTORY_NOT_HOTBAR, inventorySlots.asInstanceOf[List[Slot]].subList(playerStart, inventorySlots.size()-9))
      slotMap.put(ContainerSection.INVENTORY_HOTBAR, inventorySlots.asInstanceOf[List[Slot]].subList(inventorySlots.size()-9, inventorySlots.size()))
    }
    return slotMap;
  }

}