package yuuto.enhancedinventories.block.traits

import yuuto.enhancedinventories.block.base.BlockBaseEI
import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import java.util.ArrayList
import yuuto.enhancedinventories.tile.traits.TUpgradeable
import net.minecraft.init.Blocks
import net.minecraft.entity.Entity

/**
 * @author Jacob
 */
trait TBlockUpgradeable extends BlockBaseEI{
  //Adds upgrades to drops
  override def getDrops(world:World, x:Int, y:Int, z:Int, meta:Int, fortune:Int, blockStack:ItemStack, tile:TileBaseEI):ArrayList[ItemStack]={
    val drops:ArrayList[ItemStack] = super.getDrops(world, x, y, z, meta, fortune, blockStack, tile);
    if(tile.isInstanceOf[TUpgradeable]){
      tile.asInstanceOf[TUpgradeable].getUpgradeDrops(drops);
    }
    return drops;
  }
  
  //Sets uses obsidian explosion resistance if tile is reinforced
  override def getExplosionResistance(par1Entity:Entity, world:World, x:Int, y:Int, z:Int, explosionX:Double, explosionY:Double, explosionZ:Double):Float={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile.isInstanceOf[TUpgradeable]){
      if(tile.asInstanceOf[TUpgradeable].isReinforced()){
        return Blocks.obsidian.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
      }
    }
    return getExplosionResistance(par1Entity);
  }
}