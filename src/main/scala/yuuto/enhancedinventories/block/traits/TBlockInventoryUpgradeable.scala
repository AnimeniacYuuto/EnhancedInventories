package yuuto.enhancedinventories.block.traits

import yuuto.enhancedinventories.block.base.BlockBaseEI
import net.minecraft.world.IBlockAccess
import yuuto.enhancedinventories.tile.traits.TInventoryConnectiveUpgradeable
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.util.LogHelperEI

/**
 * @author Jacob
 */
trait TBlockInventoryUpgradeable extends BlockBaseEI with TBlockInventory with TBlockUpgradeable{
  
  override def canProvidePower():Boolean=true;
  
  override def initializeTile(tile:TileEntity, world:World, x:Int, y:Int, z:Int, player:EntityLivingBase, stack:ItemStack){
    super.initializeTile(tile, world, x, y, z, player, stack);
    if(stack.hasTagCompound() && tile.isInstanceOf[TInventoryConnectiveUpgradeable]){
      tile.asInstanceOf[TInventoryConnectiveUpgradeable].alternate = stack.getTagCompound().getBoolean(DecorationHelper.KEY_ALTERNATE);
    }
  }
  
  //Gets the redstone power from the tile
  override def isProvidingWeakPower(world:IBlockAccess, x:Int ,y:Int, z:Int, meta:Int):Int={
    val tile:TInventoryConnectiveUpgradeable = world.getTileEntity(x, y, z).asInstanceOf[TInventoryConnectiveUpgradeable];
    return tile.getRedstonePower();
  }
  override def isProvidingStrongPower(world:IBlockAccess, x:Int, y:Int, z:Int, meta:Int):Int={
    val tile:TInventoryConnectiveUpgradeable = world.getTileEntity(x, y, z).asInstanceOf[TInventoryConnectiveUpgradeable];
    return tile.getRedstonePower();
    //return this.isProvidingWeakPower(world, x, y, z, meta);
  }
}