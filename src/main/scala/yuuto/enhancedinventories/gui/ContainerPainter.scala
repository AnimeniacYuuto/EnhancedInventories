/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import yuuto.enhancedinventories.gui.slot.SlotPaintResult
import yuuto.enhancedinventories.tile.TilePainter
import yuuto.enhancedinventories.tile.TilePainter
import yuuto.yuutolib.block.ContainerAlt

class ContainerPainter(painter:TilePainter, player:EntityPlayer) extends ContainerAlt(painter.getInventory(), player.inventory){
  private var result:IInventory=null;
  init();
  
  override def bindInventorySlots():Array[Int]={
    return Array(8, 84);
  }
  override def bindOtherSlots()={
    this.result = painter.getResultInventory();
    
    //Add slots to avoid shift click
    this.addSlotToContainer(new SlotPaintResult(this.result, this.inventory, 0, 148,34));
    for(i <-0 until 4){
      this.addSlotToContainer(new Slot(this.inventory, i, 8+27*i, 34));
    }
  }
  
  override def canInteractWith(player:EntityPlayer):Boolean={
    return true;
  }

}