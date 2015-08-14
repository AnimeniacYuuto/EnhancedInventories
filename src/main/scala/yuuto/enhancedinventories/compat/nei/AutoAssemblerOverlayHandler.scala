/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import yuuto.enhancedinventories.gui.ContainerAutoAssembler;
import codechicken.nei.recipe.DefaultOverlayHandler;

class AutoAssemblerOverlayHandler(x:Int, y:Int) extends DefaultOverlayHandler(x, y){
    
    def this()=this(-17, 11);

    override def canMoveFrom(slot:Slot, gui:GuiContainer):Boolean={
      val container:ContainerAutoAssembler = gui.inventorySlots.asInstanceOf[ContainerAutoAssembler];
      return slot.inventory.isInstanceOf[InventoryPlayer] || slot.inventory == container.chestInventory;
    }
}