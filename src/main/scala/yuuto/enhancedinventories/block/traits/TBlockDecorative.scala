package yuuto.enhancedinventories.block.traits

import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.enhancedinventories.block.base.BlockBaseEI
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.tile.traits.TDecorative
import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraft.world.IBlockAccess

/**
 * @author Jacob
 */
trait TBlockDecorative extends BlockBaseEI{
  
  override def initializeTile(tile:TileEntity, world:World, x:Int, y:Int, z:Int, player:EntityLivingBase, stack:ItemStack){
    super.initializeTile(tile, world, x, y, z, player, stack);
    if(stack.hasTagCompound() && tile.isInstanceOf[TDecorative]){
      tile.asInstanceOf[TDecorative].decor.readFromNBT(stack.getTagCompound());
      tile.asInstanceOf[TDecorative].painted = stack.getTagCompound().getBoolean(DecorationHelper.KEY_PAINTED);
    }
  }
  //Use the core block light value if possible
  override def getLightValue(world:IBlockAccess, x:Int, y:Int, z:Int):Int={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile != null && tile.isInstanceOf[TDecorative])
      return tile.asInstanceOf[TDecorative].decor.coreBlock.getLightValue();
    return super.getLightValue(world, x, y, z);
  }
  
  override def getRenderType():Int=22;
  override def isOpaqueCube():Boolean=false;
  override def renderAsNormalBlock():Boolean=false;
}