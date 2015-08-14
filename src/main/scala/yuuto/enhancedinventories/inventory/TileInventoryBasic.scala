package yuuto.enhancedinventories.inventory

import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.tile.traits.TInventorySimple
import net.minecraft.inventory.IInventory
import net.minecraft.entity.player.EntityPlayer

/**
 * @author Jacob
 */
object TileInventoryBasic{
  def getContents(tiles:Array[TInventorySimple]):Array[Array[ItemStack]]={
    val mergedInvs:Array[Array[ItemStack]] = new Array[Array[ItemStack]](tiles.length);
    for(i <- 0 until tiles.length){
      mergedInvs(i) = tiles(i).getContents();
    }
    return mergedInvs;
  }
}
class TileInventoryBasic(protected val mainTile:TInventorySimple, 
    protected val tiles:Array[TInventorySimple], 
    protected val inv:IInventory) extends InventoryBase{
  
  /*public TileInventory(TInventorySimple mainTile, TileInventorySimple[] tiles, 
      IInventory inv){
    this.mainTile = mainTile;
    this.tiles = tiles;
    this.inv = inv;
  }*/
  def this(mainTile:TInventorySimple, tiles:Array[TInventorySimple])=
    this(mainTile, tiles, new InventoryMerged(TileInventoryBasic.getContents(tiles)));
  def this(tile:TInventorySimple)=
    this(tile, Array(tile), new InventoryMerged(Seq(tile.getContents())));
  
  protected def getMainTile():TInventorySimple=mainTile;
  override def getSizeInventory():Int=inv.getSizeInventory();
  override def getStackInSlot(slot:Int):ItemStack=inv.getStackInSlot(slot);
  override def setInventorySlotContents(slot:Int, stack:ItemStack)=inv.setInventorySlotContents(slot, stack);
  override def getInventoryName():String=null;
  override def hasCustomInventoryName():Boolean=false;
  override def markDirty() {
    for (te <- tiles)
      te.markDirty();
  }
  override def isUseableByPlayer(player:EntityPlayer):Boolean={
    for (te <- tiles){
      if(!te.isUseableByPlayer(player))
        return false;
    }
    return true;
  }
  override def openInventory()=this.mainTile.onOpenInventory();
  override def closeInventory()=this.mainTile.onCloseInventory();
}