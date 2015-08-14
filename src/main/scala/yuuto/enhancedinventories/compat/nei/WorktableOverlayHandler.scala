/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import yuuto.enhancedinventories.gui.ContainerWorktable;
import codechicken.nei.recipe.DefaultOverlayHandler;

class WorktableOverlayHandler(x:Int, y:Int) extends DefaultOverlayHandler(x,y){
    
    def this()=this(5, 11);

    override def canMoveFrom(slot:Slot, gui:GuiContainer):Boolean={
      val container:ContainerWorktable = gui.inventorySlots.asInstanceOf[ContainerWorktable];
      return slot.inventory.isInstanceOf[InventoryPlayer] || slot.inventory == container.chestInventory;
    }
}