package yuuto.enhancedinventories.tile

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable
import yuuto.enhancedinventories.tile.traits.TConnective
import yuuto.enhancedinventories.tile.traits.TDecorative
import yuuto.enhancedinventories.tile.traits.TDecorativeInventoryConnective
import yuuto.enhancedinventories.tile.traits.TInventoryConnectiveUpgradeable
import yuuto.enhancedinventories.tile.upgrades.Upgrade
import yuuto.yuutolib.tile.traits.TTileRotatableMachine
import yuuto.enhancedinventories.tile.traits.TReverseable

/**
 * @author Jacob
 */
object TileCabinet{
  val validDirections:Array[ForgeDirection] = Array(ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST);
  val mainDirections:Array[ForgeDirection] = Array(ForgeDirection.EAST, ForgeDirection.SOUTH);
  val validUpgrades:Array[Upgrade]= Array(Upgrade.HOPPER, Upgrade.REINFORCED, Upgrade.SECURITY, Upgrade.SORTING, Upgrade.TRAPPED);
}
class TileCabinet(chestTier:Int) extends TileConnectiveUpgradeable with TReverseable{
  this.tier=chestTier;
  setDecorationInfo();
  resetInventory();
  
  def this()=this(0);
  
  override def getValidConnections(): Array[ForgeDirection]=TileCabinet.validDirections;
  override def getMainConnections(): Array[ForgeDirection]=TileCabinet.mainDirections;
  
  override def getPullDirection():ForgeDirection=ForgeDirection.UP;
  override def getPushDirection():ForgeDirection=ForgeDirection.DOWN;
  
  override def checkUpgradeValidity(chest:TInventoryConnectiveUpgradeable, newChest:ItemStack):Boolean={
    return chest.isValidForConnection(newChest);
  }
  override def getValidUpgrades(): Array[Upgrade]=TileCabinet.validUpgrades;
  override def getItem():Item=Item.getItemFromBlock(ProxyCommon.blockCabinet);
  
  override def connectTo(tile:TConnective, dir:ForgeDirection){
    super.connectTo(tile, dir);
    if(this.isMain()) {
      val chest=tile.asInstanceOf[TileCabinet];
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
      this.getPartner().asInstanceOf[TileCabinet].setRotation(facing);
    }
    return true;
  }
}