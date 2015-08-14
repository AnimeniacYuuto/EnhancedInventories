package yuuto.enhancedinventories.block.traits

import yuuto.enhancedinventories.block.base.BlockBaseEI
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.ForgeHooks
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.enhancedinventories.tile.traits.TSecurable

/**
 * @author Jacob
 */
trait TBlockSecurable extends BlockBaseEI{
  override def getPlayerRelativeBlockHardness(player:EntityPlayer, world:World, x:Int, y:Int, z:Int):Float={

    val tile:TileEntity = world.getTileEntity(x, y, z);

    if (tile.isInstanceOf[TSecurable] && !tile.asInstanceOf[TSecurable].canPlayerAccess(player)) {
      return -1;
    }
    return ForgeHooks.blockStrength(this, player, world, x, y, z);
  }
}