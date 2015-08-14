/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

class ContainerWorkbench(player:EntityPlayer, world:World, x:Int, y:Int, z:Int) extends net.minecraft.inventory.ContainerWorkbench(player.inventory, world, x, y, z){
  //Allow the workbench to work with ANY block
  override def canInteractWith(player:EntityPlayer):Boolean={
      return if(world.isAirBlock(x, y, z)){false}else{ 
        player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D};
  }

}