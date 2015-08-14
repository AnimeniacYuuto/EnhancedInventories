/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import yuuto.enhancedinventories.util.MinecraftColors;

object DecorationInfo{
  def loadFromNbt(tag:NBTTagCompound, color:Boolean):DecorationInfo={
    return new DecorationInfo(tag, color);
  }
}
class DecorationInfo(private val usesColor:Boolean) {
  var coreBlock:Block=Blocks.planks;
  var coreMetadata:Int=0;
  var frameMaterial:FrameMaterial = FrameMaterials.Stone;
  var decColor:MinecraftColors = MinecraftColors.RED;
  
  def this(nbt:NBTTagCompound, usesColor:Boolean)={
    this(usesColor);
    readFromNBT(nbt);
  }
  
  
  def readFromNBT(tag:NBTTagCompound):DecorationInfo={
    this.coreBlock = Block.getBlockFromName(tag.getString(DecorationHelper.KEY_CORE_BLOCK));
    if(coreBlock == null)
      coreBlock = Blocks.planks;
    this.coreMetadata = tag.getInteger(DecorationHelper.KEY_CORE_META);
    this.frameMaterial = FrameMaterials.Instance.getMaterial(tag.getString(DecorationHelper.KEY_FRAME_NAME));
    if(usesColor)
      this.decColor = MinecraftColors.wool(tag.getInteger(DecorationHelper.KEY_COLOR_ID));
    return this;
  }
  def writeToNBT(nbt:NBTTagCompound):NBTTagCompound={
    nbt.setString(DecorationHelper.KEY_CORE_BLOCK, Block.blockRegistry.getNameForObject(coreBlock));
    nbt.setShort(DecorationHelper.KEY_CORE_META, coreMetadata.asInstanceOf[Short]);
    nbt.setString(DecorationHelper.KEY_FRAME_NAME, frameMaterial.getID());
    if(usesColor)
      nbt.setByte(DecorationHelper.KEY_COLOR_ID, decColor.getIndex().asInstanceOf[Byte]);
    return nbt;
  }
  
  def matches(info:DecorationInfo):Boolean={
    if(coreBlock != info.coreBlock)
      return false;
    if(coreMetadata != info.coreMetadata)
      return false;
    if(frameMaterial != info.frameMaterial)
      return false;
    if(usesColor != info.usesColor)
      return false;
    if(usesColor)
      return decColor == info.decColor;
    return true;
  }
  
  def doseUseColor():Boolean=usesColor;

}