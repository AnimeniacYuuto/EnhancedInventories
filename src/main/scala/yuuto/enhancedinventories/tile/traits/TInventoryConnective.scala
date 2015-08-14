package yuuto.enhancedinventories.tile.traits

import net.minecraft.inventory.IInventory
import net.minecraftforge.common.util.ForgeDirection
import yuuto.enhancedinventories.inventory.TileInventoryBasic

/**
 * @author Jacob
 */
trait TInventoryConnective extends TInventorySimple with TConnective{
  var connectedInventory:IInventory=null;
  
  override def getInventory():IInventory={
    if(this.connectedInventory == null)
      return this.invHandler;
    return this.connectedInventory;
  }
  override def connectTo(tile:TConnective, dir:ForgeDirection){
    super.connectTo(tile, dir);
    if(this.isMain()){
      setInventoryConnective();
      this.partner.asInstanceOf[TInventoryConnective].setConnectedInventory(connectedInventory);
    }
  }
  def setInventoryConnective(){
    connectedInventory = new TileInventoryBasic(this.asInstanceOf[TInventorySimple], Array(this.asInstanceOf[TInventorySimple], this.getPartner().asInstanceOf[TInventorySimple]))
  }
  def setConnectedInventory(inv:IInventory){
    this.connectedInventory = inv;
  }
  override def disconnect(){
    super.disconnect();
    this.connectedInventory = null;
  }
}