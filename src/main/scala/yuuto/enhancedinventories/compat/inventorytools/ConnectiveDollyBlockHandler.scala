/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.inventorytools

import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import yuuto.inventorytools.api.dolly.BlockData
import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.inventorytools.api.dolly.handlers.defaults.block.DefaultDollyBlockHandler
import yuuto.inventorytools.api.dolly.handlers.defaults.tile.IDollyTileHandler
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.block.BlockImprovedChest
import yuuto.enhancedinventories.block.BlockLocker
import yuuto.enhancedinventories.block.BlockCabinet
import yuuto.enhancedinventories.util.LogHelperEI
import yuuto.enhancedinventories.block.traits.TBlockReverseable
import net.minecraft.util.MathHelper

object ConnectiveDollyBlockHandler extends DefaultDollyBlockHandler{
  override def onPlaced(blockData:BlockData, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    var targetSide:ForgeDirection = ForgeDirection.getOrientation(side);
    var targX:Int=x;
    var targY:Int=y;
    var targZ:Int=z;
    var targetBlock:Block = world.getBlock(targX, targY, targZ);
    val blockStack:ItemStack=new ItemStack(blockData.block, 1, blockData.meta);
    if(blockData.tileData != null && blockData.tileData.hasKey("stackData")){
      blockStack.setTagCompound(blockData.tileData.getCompoundTag("stackData"))
    }
    if(targetBlock != null && !targetBlock.isReplaceable(world, targX, targY, targZ)){
      if(targetBlock == Blocks.snow)
        targetSide=ForgeDirection.UP;
      if(targetBlock != Blocks.vine && targetBlock != Blocks.tallgrass && targetBlock != Blocks.deadbush){
        targX+=targetSide.offsetX;
        targY+=targetSide.offsetY;
        targZ+=targetSide.offsetZ;
      }
    }
    if(!player.canPlayerEdit(targX, targY, targZ, side, blockStack))
      return false;
    if(targY==255 && blockData.block.getMaterial().isSolid())
      return false;
    if(!(world.canPlaceEntityOnSide(blockData.block, targX, targY, targZ, false, side, player, blockStack)))
      return false;
    if(!canPlaceBlock(blockStack, blockData, player, world, targX, targY, targZ, side,  hitX, hitY, hitZ))
      return false;
    var handler=DollyHandlerRegistry.getTileHandler(blockData.handlerName);
    blockData.meta=blockData.block.onBlockPlaced(world, targX, targY, targZ, side, hitX, hitY, hitZ, blockData.meta);
    if(!world.setBlock(targX, targY, targZ, blockData.block, blockData.meta, 3))
      return false;
    if(world.getBlock(targX, targY, targZ) != blockData.block)
      return true;
    if(handler!=null){
      val tile:TileEntity=world.getTileEntity(targX, targY, targZ);
      handler.onPlaced(tile, blockData, player, world, targX, targY, targZ, side, hitX, hitY, hitZ);
    }
    blockData.block.onBlockPlacedBy(world, targX, targY, targZ, player, blockStack);
    blockData.block.onPostBlockPlaced(world, targX, targY, targZ, blockData.meta);
    return true;
  }
  private def canPlaceBlock(blockStack:ItemStack, blockData:BlockData, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(blockData.block.isInstanceOf[BlockImprovedChest]){
      return blockData.block.asInstanceOf[BlockImprovedChest].canPlaceBlockAt(blockStack, world, x, y, z);
    }
    if(blockData.block.isInstanceOf[BlockLocker]){
      val l:Int = MathHelper.floor_double((player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      var dir:ForgeDirection = ForgeDirection.UNKNOWN;
      if(l == 0)
        dir = ForgeDirection.getOrientation(2);
      else if(l == 1)
        dir = ForgeDirection.getOrientation(5);
      else if(l == 2)
        dir = ForgeDirection.getOrientation(3);
      else if(l == 3)
        dir = ForgeDirection.getOrientation(4);
      
      if(dir == ForgeDirection.UNKNOWN)
        return false;
      
      val angle:Double = TBlockReverseable.getRotation(dir.getOpposite());
      val yaw:Double = ((player.rotationYaw % 360) + 360) % 360;
      return blockData.block.asInstanceOf[BlockLocker].canPlaceBlockAt(blockStack, world, x, y, z, TBlockReverseable.angleDifference(angle, yaw) > 0);
    }
    if(blockData.block.isInstanceOf[BlockCabinet]){
      return blockData.block.asInstanceOf[BlockCabinet].canPlaceBlockAt(blockStack, world, x, y, z);
    }
    return false;
  }
}