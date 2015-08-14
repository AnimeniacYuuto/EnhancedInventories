package yuuto.enhancedinventories.tile.traits

import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraft.item.ItemStack

/**
 * @author Jacob
 */
trait TDecorative extends TileBaseEI{
  
  var decor:DecorationInfo=null;
  var painted:Boolean=false;
  
  protected def setDecorationInfo(){
    decor = new DecorationInfo(true);
  }
  
  def isPainted():Boolean={
    return painted;
  }
  
  def ceateDecorationInfo(tag:NBTTagCompound):DecorationInfo={
    return new DecorationInfo(tag, true);
  }
  
  override def invalidate(){
    super.invalidate();
    this.decor = null;
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    nbt.setTag("decor", decor.writeToNBT(new NBTTagCompound()));
    if(isPainted()){
      nbt.setBoolean(DecorationHelper.KEY_PAINTED, true);
    }
  }
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    nbt.setTag("decor", decor.writeToNBT(new NBTTagCompound()));
    if(isPainted()){
      nbt.setBoolean(DecorationHelper.KEY_PAINTED, true);
    }
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    decor.readFromNBT(nbt.getCompoundTag("decor"));
    this.painted = nbt.getBoolean(DecorationHelper.KEY_PAINTED);
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    decor.readFromNBT(nbt.getCompoundTag("decor"));
    this.painted = nbt.getBoolean(DecorationHelper.KEY_PAINTED);
  }
  
  override def getItemStack(stack:ItemStack):ItemStack={
    if(!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    decor.writeToNBT(stack.getTagCompound());
    if(painted)
      stack.getTagCompound().setBoolean(DecorationHelper.KEY_PAINTED, true);
    return stack;
  }
}