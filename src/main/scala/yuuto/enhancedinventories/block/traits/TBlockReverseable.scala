package yuuto.enhancedinventories.block.traits

import yuuto.enhancedinventories.block.base.BlockBaseEI
import net.minecraftforge.common.util.ForgeDirection
import yuuto.yuutolib.tile.traits.TTileRotatableMachine
import yuuto.enhancedinventories.tile.TileLocker
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yuuto.yuutolib.block.traits.TBlockContainerRotatable
import net.minecraft.util.MathHelper
import yuuto.enhancedinventories.tile.traits.TReverseable

/**
 * @author Jacob
 */
object TBlockReverseable{
  def getRotation(dir:ForgeDirection):Int={
    if (dir == ForgeDirection.WEST) return 90;
    else if (dir == ForgeDirection.NORTH) return 180;
    else if (dir == ForgeDirection.EAST) return 270;
    else return 0;
  }
  def angleDifference(angle1:Double, angle2:Double):Double={
    return ((angle2 - angle1) % 360 + 540) % 360 - 180;
  }
}
trait TBlockReverseable extends BlockBaseEI with TBlockContainerRotatable{
  override def setRotation(tile:TTileRotatableMachine, world:World, x:Int, y:Int, z:Int, player:EntityLivingBase, stack:ItemStack){
    val l:Int = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    if(l == 0)
      tile.setRotation(ForgeDirection.getOrientation(2));
    else if(l == 1)
      tile.setRotation(ForgeDirection.getOrientation(5));
    else if(l == 2)
      tile.setRotation(ForgeDirection.getOrientation(3));
    else if(l == 3)
      tile.setRotation(ForgeDirection.getOrientation(4));
    if(!tile.isInstanceOf[TReverseable])
      return;
    //Sets the locker revered or not
    val angle:Double = TBlockReverseable.getRotation(tile.facing.getOpposite());
    val yaw:Double = ((player.rotationYaw % 360) + 360) % 360;
    tile.asInstanceOf[TReverseable].reversed = TBlockReverseable.angleDifference(angle, yaw) > 0;
  }
}