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

/**
 * @author Jacob
 */
object TileImprovedChest{
  val validDirections:Array[ForgeDirection] = Array(ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST);
  val mainDirections:Array[ForgeDirection] = Array(ForgeDirection.EAST, ForgeDirection.SOUTH);
  val validUpgrades:Array[Upgrade]= Array(Upgrade.HOPPER, Upgrade.REINFORCED, Upgrade.SECURITY, Upgrade.SORTING, Upgrade.TRAPPED);
}
class TileImprovedChest(chestTier:Int) extends TileConnectiveUpgradeable{
  this.tier=chestTier;
  setDecorationInfo();
  resetInventory();
  var oldTag:NBTTagCompound = null;
  
  def this()=this(0);
  override def initialize(){
    if(oldTag != null){
      UpdateHelper.updateTile(this, oldTag);
    }
    super.initialize();
  }
  
  override def getValidConnections(): Array[ForgeDirection]=TileImprovedChest.validDirections;
  override def getMainConnections(): Array[ForgeDirection]=TileImprovedChest.mainDirections;
  
  override def getPullDirection():ForgeDirection=ForgeDirection.UP;
  override def getPushDirection():ForgeDirection=ForgeDirection.DOWN;
  
  override def checkUpgradeValidity(chest:TInventoryConnectiveUpgradeable, newChest:ItemStack):Boolean={
    return chest.isValidForConnection(newChest);
  }
  override def getValidUpgrades(): Array[Upgrade]=TileImprovedChest.validUpgrades;
  override def getItem():Item=Item.getItemFromBlock(ProxyCommon.blockImprovedChest);
  
  override def connectTo(tile:TConnective, dir:ForgeDirection){
    super.connectTo(tile, dir);
    if(this.isMain()) {
      val chest=tile.asInstanceOf[TileImprovedChest];
      if(this.facing == dir ||this.facing == dir.getOpposite()){
        if(chest.facing != dir && chest.facing != dir.getOpposite() && chest.facing != ForgeDirection.UNKNOWN){
          this.setRotation(chest.facing);
        }else{
          this.setRotation(facing.getRotation(ForgeDirection.UP));
          chest.setRotation(this.facing);
          this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
      }else{
        chest.setRotation(facing);
      }
    }
  }
  
  override def rotateAround(axis:ForgeDirection): Boolean={
    setRotation(facing.getRotation(axis));
    if(this.isConnected()) {
      setRotation(facing.getRotation(axis));
      this.getPartner().asInstanceOf[TileImprovedChest].setRotation(facing);
    }
    return true;
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