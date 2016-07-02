/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.refinedrelocation

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.tile.TileCabinet
import yuuto.enhancedinventories.tile.TileImprovedChest
import yuuto.enhancedinventories.tile.TileLocker
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable
import yuuto.enhancedinventories.util.LogHelperEI
import com.dynious.refinedrelocation.api.ModObjects
import com.dynious.refinedrelocation.api.filter.IFilterGUI;
import yuuto.enhancedinventories.ref.ReferenceEI

object RefinedRelocationHelper {

  /*def isFilterEmpty(filter:IFilterGUI):Boolean={
    for(i <-0 until filter.getSize() if(filter.getValue(i))){
        return false;
    }
    return filter.getUserFilter() == null || filter.getUserFilter().trim().isEmpty();
  }*/
  def isSortingUpgrade(stack:ItemStack):Boolean={
    return stack.getItem() == ModObjects.sortingUpgrade.getItem();
  }
  def canUpgradeTile(tile:TileEntity):Boolean={
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileImprovedChest"))
      return upgradeChest(tile.asInstanceOf[TileImprovedChest], true);
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileLocker"))
      return upgradeLocker(tile.asInstanceOf[TileLocker], true);
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileCabinet"))
      return upgradeCabinet(tile.asInstanceOf[TileCabinet], true);
    LogHelperEI.Debug("Not valid for upgrade "+tile.getClass().getName());
    return false;
  }
  def upgradeTile(tile:TileEntity):Boolean={
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileImprovedChest"))
      return upgradeChest(tile.asInstanceOf[TileImprovedChest], false);
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileLocker"))
      return upgradeLocker(tile.asInstanceOf[TileLocker], false);
    if(tile.getClass().getName().matches(ReferenceEI.MOD_PACKAGE+".tile.TileCabinet"))
      return upgradeCabinet(tile.asInstanceOf[TileCabinet], false);
    LogHelperEI.Debug("Could not upgrade tile "+tile.getClass().getName());
    return false;
  }
  private def canPlace(chest:TileConnectiveUpgradeable, newChest:TileConnectiveUpgradeable):Boolean={
    var chests:Int = 0;
    for(dir <- newChest.getValidConnections() if(dir != chest.getPartnerDirection())){
      val tile:TileEntity = chest.getWorldObj().getTileEntity(chest.xCoord+dir.offsetX, chest.yCoord+dir.offsetY, chest.zCoord+dir.offsetZ);
      if(tile == null || !tile.isInstanceOf[TileConnectiveUpgradeable]){}
      else{
        val chest1:TileConnectiveUpgradeable = tile.asInstanceOf[TileConnectiveUpgradeable];
        if(!chest1.isValidForConnection(newChest)){}
        else if(chest1.isConnected()){return false;}
        else{
          chests+=1;
        }
      }
    }
    if(chests > 1)
      return false;
    return true;
  }
  private def replace(chest:TileConnectiveUpgradeable, newChest:TileConnectiveUpgradeable, b:Block, meta:Int):Boolean={
    chest.invalidate();
    chest.getWorldObj().setBlockToAir(chest.xCoord, chest.yCoord, chest.zCoord);
    chest.getWorldObj().setBlock(chest.xCoord, chest.yCoord, chest.zCoord, b, meta, 3);
    chest.getWorldObj().setTileEntity(chest.xCoord, chest.yCoord, chest.zCoord, newChest);
    return true;
  }
  private def upgradeChest(chest:TileImprovedChest, simulate:Boolean):Boolean={
    val newChest:TileSortingImprovedChest = new TileSortingImprovedChest();
    val nbt:NBTTagCompound = new NBTTagCompound();
    chest.writeToNBT(nbt);
    newChest.readFromNBT(nbt);
    
    if(simulate && !canPlace(chest, newChest))
      return false;
    
    return simulate || replace(chest, newChest, ModuleRefinedRelocation.sortingImprovedChest, chest.getBlockMetadata());
  }
  private def upgradeLocker(chest:TileLocker, simulate:Boolean):Boolean={
    val newChest:TileSortingLocker = new TileSortingLocker();
    val nbt:NBTTagCompound = new NBTTagCompound();
    chest.writeToNBT(nbt);
    newChest.readFromNBT(nbt);
    
    if(simulate && !canPlace(chest, newChest))
      return false;
    
    return simulate || replace(chest, newChest, ModuleRefinedRelocation.sortingLocker, chest.getBlockMetadata());
  }
  private def upgradeCabinet(chest:TileCabinet, simulate:Boolean):Boolean={
    val newChest:TileSortingCabinet = new TileSortingCabinet();
    val nbt:NBTTagCompound = new NBTTagCompound();
    chest.writeToNBT(nbt);
    newChest.readFromNBT(nbt);
    
    if(simulate && !canPlace(chest, newChest))
      return false;
    
    return simulate || replace(chest, newChest, ModuleRefinedRelocation.sortingCabinet, chest.getBlockMetadata());
  }
}