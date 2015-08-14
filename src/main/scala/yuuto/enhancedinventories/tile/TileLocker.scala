package yuuto.enhancedinventories.tile

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.tile.traits.TConnective
import yuuto.enhancedinventories.tile.traits.TDecorative
import yuuto.enhancedinventories.tile.traits.TDecorativeInventoryConnective
import yuuto.enhancedinventories.tile.traits.TInventoryConnectiveUpgradeable
import yuuto.enhancedinventories.tile.upgrades.Upgrade
import yuuto.yuutolib.tile.traits.TTileRotatableMachine
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.util.UpdateHelper
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable
import yuuto.enhancedinventories.tile.traits.TReverseable

/**
 * @author Jacob
 */
object TileLocker{
  val validDirections:Array[ForgeDirection] = Array(ForgeDirection.UP, ForgeDirection.DOWN);
  val mainDirections:Array[ForgeDirection] = Array(ForgeDirection.DOWN);
  val validUpgrades:Array[Upgrade]= Array(Upgrade.HOPPER, Upgrade.REINFORCED, Upgrade.SECURITY, Upgrade.SORTING, Upgrade.TRAPPED);
}
class TileLocker(chestTier:Int) extends TileConnectiveUpgradeable with TReverseable{
  this.tier=chestTier;
  setDecorationInfo();
  resetInventory();
  var oldTag:NBTTagCompound=null;
  
  def this()=this(0);
  
  override def initialize(){
    if(oldTag != null){
      UpdateHelper.updateTile(this, oldTag);
    }
    super.initialize();
  }
  
  override def getValidConnections(): Array[ForgeDirection]=TileLocker.validDirections;
  override def getMainConnections(): Array[ForgeDirection]=TileLocker.mainDirections;
  
  override def getPullDirection():ForgeDirection=if(reversed){this.facing.getRotation(ForgeDirection.UP).getOpposite()}else{this.facing.getRotation(ForgeDirection.UP)};
  override def getPushDirection():ForgeDirection=if(reversed){this.facing.getRotation(ForgeDirection.UP)}else{this.facing.getRotation(ForgeDirection.UP).getOpposite()};
  
  override def checkUpgradeValidity(chest:TInventoryConnectiveUpgradeable, newChest:ItemStack):Boolean={
    return chest.asInstanceOf[TileLocker].isValidForConnection(newChest, reversed);
  }
  override def getValidUpgrades(): Array[Upgrade]=TileLocker.validUpgrades;
  override def getItem():Item=Item.getItemFromBlock(ProxyCommon.blockLocker);
  
  override def isValidForConnection(tile:TConnective):Boolean={
    return super.isValidForConnection(tile).&&(this.reversed == tile.asInstanceOf[TileLocker].reversed);
  }
  def isValidForConnection(newChest:ItemStack,rev:Boolean):Boolean={
    return isValidForConnection(newChest)&&(this.reversed == rev);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    if(nbt.hasKey("woodType")){
      this.oldTag = nbt;
      this.xCoord = nbt.getInteger("x");
          this.yCoord = nbt.getInteger("y");
          this.zCoord = nbt.getInteger("z");
      return;
    }
    super.readFromNBT(nbt);
  }
}