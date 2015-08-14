package yuuto.enhancedinventories.tile.base

import yuuto.yuutolib.tile.ModTile
import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Jacob
 */
abstract class TileBaseEI extends ModTile{
  protected var initialized:Boolean=false;
  protected var synced:Boolean=false;
  
  def isInitialized():Boolean=initialized;
  def initialize(){
    this.initialized = true;
  }
  def isInstanceOfThis(tile:TileEntity):Boolean={
    this.getClass() == tile.getClass();
  }
  
  def getItemStack(stack:ItemStack):ItemStack=stack;
  def getItemStack():ItemStack;
  
  override def updateEntity(){
    if(!initialized && !this.isInvalid() && isSynced()){
      initialize();
    }
  }
  
  protected def isSynced():Boolean={
    if(this.getWorldObj() == null)
      return false;
    if(this.getWorldObj().isRemote)
      return synced;
    return true;
  }
  
  override def readFromNBTPacket(nbt:NBTTagCompound)={
    super.readFromNBTPacket(nbt);
    synced=true;
  }
}