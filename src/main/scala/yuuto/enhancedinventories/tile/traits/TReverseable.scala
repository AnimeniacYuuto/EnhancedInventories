package yuuto.enhancedinventories.tile.traits

import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Jacob
 */
trait TReverseable extends TileBaseEI{
  var reversed:Boolean=false
  
  override def writeToNBT(nbt:NBTTagCompound){
    nbt.setBoolean("reversed", reversed);
    super.writeToNBT(nbt);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    this.reversed = nbt.getBoolean("reversed");
    super.readFromNBT(nbt);
  }
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    nbt.setBoolean("reversed", reversed);
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    this.reversed = nbt.getBoolean("reversed");
  }
  
}