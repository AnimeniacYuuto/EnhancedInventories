/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

object DecorationHelper {
  
  final val KEY_CORE_BLOCK:String = "coreBlockName";
  final val KEY_CORE_META:String = "coreBlockMeta";
  final val KEY_FRAME_NAME:String = "frameName";
  final val KEY_COLOR_ID:String = "colorID";
  final val KEY_PAINTED:String = "painted";
  final val KEY_ALTERNATE:String = "alternate";
  
  def getEnhancedInventoryResult(output:ItemStack, coreStack:ItemStack, frameStack:ItemStack, woolStack:ItemStack):ItemStack={
    var nbt:NBTTagCompound = output.getTagCompound();
    if(nbt == null){
      nbt = new NBTTagCompound();
      output.setTagCompound(nbt);
    }
    setCoreBlock(nbt, coreStack);
    setFrame(nbt, frameStack);
    setWool(nbt, woolStack);
    return output;
  }
  def setWool(nbt:NBTTagCompound, woolStack:ItemStack) {
    if(woolStack == null)
      return;
    setWool(nbt, woolStack.getItemDamage())
  }
  def setWool(nbt:NBTTagCompound, id:Int){
    nbt.setByte(KEY_COLOR_ID, id.asInstanceOf[Byte]);
  }
  def setFrame(nbt:NBTTagCompound, frameStack:ItemStack) {
    setFrame(nbt, FrameMaterials.Instance.getMaterial(frameStack));
  }
  def setFrame(nbt:NBTTagCompound, mat:FrameMaterial) {
    nbt.setString(KEY_FRAME_NAME, mat.getID());
  }
  def setCoreBlock(nbt:NBTTagCompound, coreStack:ItemStack){
    val coreBlock:Block = CoreMaterialHelper.getCoreMaterialBlock(coreStack);
    val meta:Int = CoreMaterialHelper.getCoreMaterialMetadata(coreStack);
    setCoreBlock(nbt, coreBlock, meta);
  }
  def setCoreBlock(nbt:NBTTagCompound, coreBlock:Block, meta:Int){
    var coreBlockName:String = Block.blockRegistry.getNameForObject(coreBlock);
    var meta2:Int=meta;
    if(coreBlockName == null || coreBlockName.trim().isEmpty()){
      coreBlockName = Block.blockRegistry.getNameForObject(Blocks.planks);
      meta2 = 0;
    }
    nbt.setString(KEY_CORE_BLOCK, coreBlockName);
    nbt.setShort(KEY_CORE_META, meta2.asInstanceOf[Short]);
  }
  
  def setAlternate(stack:ItemStack, alt:Boolean){
    if(!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    setAlternate(stack.getTagCompound(), alt);
  }
  def setAlternate(tag:NBTTagCompound, alt:Boolean){
    tag.setBoolean(KEY_ALTERNATE, alt);
  }
  
  def readCoreBlock(tag:NBTTagCompound):Block=Block.getBlockFromName(tag.getString(KEY_CORE_BLOCK));

}