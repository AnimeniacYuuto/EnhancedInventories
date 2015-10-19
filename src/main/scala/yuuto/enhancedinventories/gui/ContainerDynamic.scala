/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import invtweaks.api.container.ChestContainer
import invtweaks.api.container.ChestContainer.RowSizeCallback
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.yuutolib.block.ContainerAlt;


object ContainerDynamic{
  val centerXSize:Int = 162;
}
@ChestContainer
class ContainerDynamic(inventory:IInventory, player:EntityPlayer) extends ContainerAlt(inventory, player.inventory){
  inventory.openInventory();
  var maxDragY:Int=0;
  var columns:Int=0;
  init();

  override def bindInventorySlots():Array[Int]={
    //Compute Rows&Columns
    var rows=0;
    if(inventory.getSizeInventory() < EIConfiguration.MAX_SIZE){
      if(inventory.getSizeInventory() <= 9){
        rows = 1;
        columns = inventory.getSizeInventory();
      }else if(inventory.getSizeInventory() <= 54){
        columns = 9;
        rows = Math.ceil(inventory.getSizeInventory()/9f).asInstanceOf[Int];
      }else{
        columns = Math.min(Math.ceil(inventory.getSizeInventory()/6f), EIConfiguration.COLOMNS).asInstanceOf[Int];
        rows = Math.min(Math.ceil(inventory.getSizeInventory()/columns.asInstanceOf[Float]), EIConfiguration.COLOMNS).asInstanceOf[Int];
      }
    }else{
      rows = EIConfiguration.ROWS;
      columns = EIConfiguration.COLOMNS;
    }
    
    //Compute Player inventory positions
    val ret:Array[Int] = new Array[Int](2);
    ret(0) = 14+(18*columns);
    ret(1) = 17+15+(18*rows);
    if(inventory.getSizeInventory() > EIConfiguration.MAX_SIZE){
      ret(0)+=18;
    }
    ret(0) = ((ret(0) - ContainerDynamic.centerXSize)/2)+1;
    
    maxDragY = ret(1) - 15;
    
    //Add slots
    val maxY:Int = Math.ceil(inventory.getSizeInventory()/columns.asInstanceOf[Float]).asInstanceOf[Int];
    for(y <-0 until maxY){
      for(x <-0 until columns){
        val i:Int = x+y*columns;
        if(i >= inventory.getSizeInventory()){
          return ret;
        }
        this.addSlotToContainer(new Slot(inventory, i, 8+x*18, 18+y*18));
      }
    }
    return ret;
  }
  
  override def onContainerClosed(player:EntityPlayer){
    super.onContainerClosed(player);
    if(player != this.player)
      return;
    inventory.closeInventory();
  }
  
  override def func_94530_a(stack:ItemStack, slot:Slot):Boolean=slot.yDisplayPosition > 17;

  /**
   * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to
   * check if the slot can be added to a list of Slots to split the held ItemStack across.
   */
  override def canDragIntoSlot(slot:Slot):Boolean={
     return slot.inventory.isInstanceOf[InventoryPlayer] || (slot.yDisplayPosition > 17 && slot.yDisplayPosition <= maxDragY);
  }
  
  override def canInteractWith(player:EntityPlayer):Boolean={
    return true;
  }
  
  @RowSizeCallback
  def getRowSize():Int=columns;

}