/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;

/**
 * A Dummy crafting container for use with static inventories
 * @author Yuuto
 *
 */
class ContainerCraftingDummy(val craftingTable:ICraftingTable) extends Container{
  val craftingMatrix:InventoryCrafting = new InventoryCrafting(this, 3, 3);
  val craftResult:InventoryCraftResult = new InventoryCraftResult();
  
  override def onCraftMatrixChanged(inv:IInventory){
    if(craftingTable != null)
      craftingTable.onCraftMatrixChanged(inv);
  }
  
  override def canInteractWith(player:EntityPlayer):Boolean=false;

}