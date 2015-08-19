/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.gui

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import yuuto.enhancedinventories.client.gui.GuiContainerAutoAssembler;
import yuuto.enhancedinventories.client.gui.GuiContainerDynamic;
import yuuto.enhancedinventories.client.gui.GuiContainerPainter;
import yuuto.enhancedinventories.client.gui.GuiContainerWorkbench;
import yuuto.enhancedinventories.client.gui.GuiContainerWorktable;
import yuuto.enhancedinventories.tile.TileAutoAssembler;
import yuuto.enhancedinventories.tile.TilePainter;
import yuuto.enhancedinventories.tile.TileWorktable;
import yuuto.enhancedinventories.tile.traits.TInventorySimple;
import yuuto.enhancedinventories.tile.traits.TSecurable;
import cpw.mods.fml.common.network.IGuiHandler;

class GuiHandler extends IGuiHandler{

  override def getServerGuiElement(ID:Int, player:EntityPlayer, world:World,x:Int, y:Int, z:Int):Object={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    ID match{
    //Open chest inventory
    case 0=>{
      if(!tile.isInstanceOf[TInventorySimple])
        return null;
      val inv:TInventorySimple = tile.asInstanceOf[TInventorySimple];
      return new ContainerDynamic(inv.getInventory(), player);
    }
    //Open a workbench inventory
    case 1 =>{
      return new ContainerWorkbench(player, world, x,y,z);
    }
    //Open Worktable inventory
    case 2 =>{
      if(!tile.isInstanceOf[TileWorktable])
        return null;
      return new ContainerWorktable(tile.asInstanceOf[TileWorktable], player);
    }
    //Open Auto Assembler inventory
    case 3 =>{
      if(!tile.isInstanceOf[TileAutoAssembler])
        return null;
      return new ContainerAutoAssembler(tile.asInstanceOf[TileAutoAssembler], player);
    }
    //Open painter inventory
    case 4 =>{
      if(!tile.isInstanceOf[TilePainter])
        return null;
      return new ContainerPainter(tile.asInstanceOf[TilePainter], player);
    }
    case i=> return null;
    }
    return null;
  }

  override def getClientGuiElement(ID:Int, player:EntityPlayer, world:World,x:Int, y:Int, z:Int):Object={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    ID match{
    //Open chest inventory
    case 0 =>{
      if(!tile.isInstanceOf[TInventorySimple])
        return null;
      val inv:TInventorySimple = tile.asInstanceOf[TInventorySimple];
      if(inv.isInstanceOf[TSecurable] && inv.asInstanceOf[TSecurable].isSecured())
        return new GuiContainerDynamic(tile, inv.getInventory(), player);
      return new GuiContainerDynamic(inv.getInventory(), player);
    }
    //Open workbench
    case 1 =>{
      return new GuiContainerWorkbench(player, world, x,y,z);
    }
    //Open Worktable inventory
    case 2 =>{
      if(!tile.isInstanceOf[TileWorktable])
        return null;
      return new GuiContainerWorktable(tile.asInstanceOf[TileWorktable], player);
    }
    //Open Auto Assembler inventory
    case 3 =>{
      if(!tile.isInstanceOf[TileAutoAssembler])
        return null;
      return new GuiContainerAutoAssembler(tile.asInstanceOf[TileAutoAssembler], player);
    }
    //Open painter inventory
    case 4 =>{
      if(!tile.isInstanceOf[TilePainter])
        return null;
      return new GuiContainerPainter(tile.asInstanceOf[TilePainter], player);
    }
    case i=> return null;
    }
    return null;
  }

}