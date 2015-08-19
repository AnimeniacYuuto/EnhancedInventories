/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.inventorytools

import yuuto.inventorytools.api.dolly.handlers.defaults.tile.InventoryDollyTileHandler
import yuuto.inventorytools.api.dolly.BlockData
import net.minecraft.nbt.NBTTagList
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack

object ConnectiveDollyTileHandler extends InventoryDollyTileHandler{
  
  override def getID():String="tilehandler.enhancedinventories.connective"
  override def canPickUp(tile:TileEntity, blockData:BlockData, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!tile.isInstanceOf[TileConnectiveUpgradeable])
      return false;
    return tile.asInstanceOf[TileConnectiveUpgradeable].canPlayerAccess(player);
  }
  override def onPickedUp(tile:TileEntity, blockData:BlockData, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float){
    if(!tile.isInstanceOf[TileConnectiveUpgradeable])
      return;
    super.onPickedUp(tile, blockData, player, world, x, y, z, side, hitX, hitY, hitZ);
    val stack:ItemStack = tile.asInstanceOf[TileConnectiveUpgradeable].getItemStack();
    if(!stack.hasTagCompound())
      return;
    if(blockData.tileData == null){
      blockData.tileData = new NBTTagCompound();
    }
    blockData.tileData.setTag("stackData", stack.getTagCompound());
  }
  override def  getInventoryTags(data:BlockData):NBTTagList={
    if(data.tileData == null || data.tileData.hasNoTags())
      return null;
    if(!data.tileData.hasKey("Items"))
      return null;
    return data.tileData.getTagList("Items", 10);
  }
  override def setInventory(tagList:NBTTagList, data:BlockData){
    if(data.tileData == null || data.tileData.hasNoTags())
      return;
    if(tagList == null || tagList.tagCount() < 1){
      if(data.tileData.hasKey("Items"))
        data.tileData.removeTag("Items");
      return;
    }
    data.tileData.setTag("Items", tagList);
  }
  
}