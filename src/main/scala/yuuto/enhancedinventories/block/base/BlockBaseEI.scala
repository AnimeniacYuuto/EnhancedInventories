package yuuto.enhancedinventories.block.base

import java.util.ArrayList
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.ref.ReferenceEI
import yuuto.enhancedinventories.tile.base.TileBaseEI
import yuuto.enhancedinventories.tile.traits.TDecorative
import yuuto.yuutolib.block.ModBlockContainer
import yuuto.yuutolib.block.traits.TBlockContainerInventory
import net.minecraft.util.MovingObjectPosition

abstract class BlockBaseEI(mat:Material, name:String) extends ModBlockContainer(mat, EnhancedInventories.tab, ReferenceEI.MOD_ID, name) with TBlockContainerInventory{
  
  override def harvestBlock(world:World, player:EntityPlayer, x:Int, y:Int, z:Int, meta:Int)
  {
  }
  override def onBlockHarvested(world:World, x:Int, y:Int, z:Int, meta:Int, player:EntityPlayer) {
    if(player.capabilities.isCreativeMode)
      return;
    player.addStat(StatList.mineBlockStatArray(Block.getIdFromBlock(this)), 1);
      player.addExhaustion(0.025F);

      harvesters.set(player);
      val i1:Int = EnchantmentHelper.getFortuneModifier(player);
      this.dropBlockAsItem(world, x, y, z, meta, i1);
      harvesters.set(null);
  }
  override def getDrops(world:World, x:Int, y:Int, z:Int, meta:Int, fortune:Int):ArrayList[ItemStack]={  
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile != null && tile.isInstanceOf[TileBaseEI]){
      val item:Item = getItemDropped(meta, world.rand, fortune);
      if(item != null){
        return getDrops(world, x, y, z, meta, fortune, new ItemStack(item), tile.asInstanceOf[TileBaseEI]);
      }
    }
    return super.getDrops(world, x, y, z, meta, fortune);
  }
  /**
   * gets a list of drops from a tile entity, allows for easy addition of upgrade drops and nbtdata
   * @param world
   * @param x
   * @param y
   * @param z
   * @param metadata
   * @param fortune
   * @param blockStack
   * @param tile
   * @return
   */
  def getDrops(world:World, x:Int, y:Int, z:Int, meta:Int, fortune:Int, blockStack:ItemStack, tile:TileBaseEI):ArrayList[ItemStack]={
    val blockStack2 = tile.getItemStack(blockStack);
    blockStack2.stackSize = quantityDropped(meta, fortune, world.rand);
    val ret:ArrayList[ItemStack] = new ArrayList[ItemStack]();
    ret.add(blockStack2);
    return ret;
  }
  
  //Adds nbtdata to the picked itemstack
  override def getPickBlock(target:MovingObjectPosition, world:World, x:Int, y:Int, z:Int):ItemStack={
    val item:Item = getItem(world, x, y, z);

    if (item == null)
    {
        return null;
    }

    //Block block = item instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(item) : this;
    var ret:ItemStack = new ItemStack(item, 1, world.getBlockMetadata(x, y, z));
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile != null && tile.isInstanceOf[TileBaseEI]){
       ret = tile.asInstanceOf[TileBaseEI].getItemStack(ret);
         return ret;
    }
    return ret;
  }
}