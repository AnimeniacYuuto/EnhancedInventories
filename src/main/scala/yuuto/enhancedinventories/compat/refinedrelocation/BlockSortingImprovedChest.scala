package yuuto.enhancedinventories.compat.refinedrelocation

import com.dynious.refinedrelocation.api.APIUtils

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.block.BlockImprovedChest

/**
 * @author Jacob
 */
class BlockSortingImprovedChest(name:String) extends BlockImprovedChest(name){
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity={
    return new TileSortingImprovedChest(meta);
  }

  //Opens the gui
  override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val tileEntity:TileEntity = world.getTileEntity(x, y, z);
    if (tileEntity == null) {
            return false;
    }
    if(!tileEntity.isInstanceOf[TileSortingImprovedChest])
      return false;
    if(tileEntity.asInstanceOf[TileSortingImprovedChest].canPlayerAccess(player)){
      if(world.isRemote)
        return true;
      //if sneaking open filter gui
      if(player.isSneaking()){
          APIUtils.openFilteringGUI(player, world, x, y, z);
            return true;
        }
      player.openGui(EnhancedInventories, 0, world, x, y, z);
        return true;
    }
    return false;
  }
}