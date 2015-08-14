package yuuto.enhancedinventories.compat.refinedrelocation

import yuuto.enhancedinventories.block.BlockCabinet
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import com.dynious.refinedrelocation.api.APIUtils
import yuuto.enhancedinventories.EnhancedInventories

/**
 * @author Jacob
 */
class BlockSortingCabinet(name:String) extends BlockCabinet(name){
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity={
    return new TileSortingCabinet(meta);
  }

  @Override
  //Opens the gui
  override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val tileEntity:TileEntity = world.getTileEntity(x, y, z);
    if (tileEntity == null) {
            return false;
    }
    if(!tileEntity.isInstanceOf[TileSortingCabinet])
      return false;
    //Make sure the player can interact with this block
    if(tileEntity.asInstanceOf[TileSortingCabinet].canPlayerAccess(player)){
      //if sneaking open filter gui
      if(player.isSneaking()){
          APIUtils.openFilteringGUI(player, world, x, y, z);
            return true;
        }
      //if the top is clicked open a crafting grid
      if(side == 1)
        player.openGui(EnhancedInventories, 1, world, x, y, z);
      //Otherwise open the inventory
      else
        player.openGui(EnhancedInventories, 0, world, x, y, z);
      return true;
    }
    return false;
  }
}