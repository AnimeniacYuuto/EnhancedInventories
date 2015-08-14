package yuuto.enhancedinventories.block.traits

import yuuto.enhancedinventories.block.base.BlockBaseEI
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.block.Block
import net.minecraft.inventory.Container
import yuuto.enhancedinventories.tile.traits.TInventorySimple
import yuuto.enhancedinventories.tile.traits.TInventoryConnective

/**
 * @author Jacob
 */
trait TBlockInventory extends BlockBaseEI{
  override def hasComparatorInputOverride():Boolean=true;
  override def getComparatorInputOverride(world:World, x:Int, y:Int, z:Int, meta:Int):Int={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile == null || !tile.isInstanceOf[TInventorySimple])
      return 0;
    return Container.calcRedstoneFromInventory(tile.asInstanceOf[TInventorySimple].getInventory());
  }
  //drops inventory contents
  override def breakBlock(world:World, x:Int, y:Int, z:Int,block:Block, meta:Int){
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if (tile != null && tile.isInstanceOf[TInventorySimple])
    {
        dropItemsFromTile(tile.asInstanceOf[TInventorySimple], world, x, y, z, block, meta);
    }

    super.breakBlock(world, x, y, z, block, meta);
  }
  //Allows for advanced contents doping without re-finding the tile
  def dropItemsFromTile(chest:TInventorySimple, world:World, x:Int, y:Int, z:Int,block:Block, meta:Int){
    if(chest.isInstanceOf[TInventoryConnective]){
      if(chest.asInstanceOf[TInventoryConnective].isConnected()){
        chest.asInstanceOf[TInventoryConnective].disconnect();
      }
    }
    dropItemsFromInventory(chest.getInventory(), world, x, y, z, block, meta);
  }
}