/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import yuuto.enhancedinventories.proxy.ProxyCommon;
import yuuto.enhancedinventories.util.MinecraftColors;
import yuuto.yuutolib.utill.StackIdentifire;

object PaintHelper {
  
  val paintableItems:List[StackIdentifire] = new ArrayList[StackIdentifire]();
  val colorableItems:List[StackIdentifire] = new ArrayList[StackIdentifire]();
  
  
  def addPaintableStack(stack:ItemStack){
    addPaintableStack(stack, false);
  }
  def addPaintableStack(stack:ItemStack, colorable:Boolean){
    paintableItems.add(new StackIdentifire(stack));
    if(colorable)
      colorableItems.add(new StackIdentifire(stack));
  }
  def isPaintable(stack:ItemStack):Boolean={
    if(stack.getItem() == ProxyCommon.paintBrush){
      if(!stack.hasTagCompound())
        return true;
      return false;
    }
    for(i <-0 until paintableItems.size()){
      if(paintableItems.get(i).matches(stack))
        return true;
    }
    return false;
  }
  def isColorable(stack:ItemStack):Boolean={
    if(stack.getItem() == ProxyCommon.paintBrush)
      return true;
    for(i <-0 until colorableItems.size()){
      if(colorableItems.get(i).matches(stack))
        return true;
    }
    return false;
  }
  
  def isFrameMaterial(stack:ItemStack):Boolean=FrameMaterials.Instance.isRegistered(stack);
  def isCoreMaterial(stack:ItemStack):Boolean=CoreMaterialHelper.isValidCoreMaterial(stack);
  def isDye(stack:ItemStack):Boolean=stack.getItem() == Items.dye;
  
  def getResult(input:ItemStack, coreStack:ItemStack, frameStack:ItemStack, colorStack:ItemStack):ItemStack={
    if(input == null)
      return null;
    if(!isPaintable(input))
      return null;
    val result:ItemStack = input.copy();
    val color:Boolean = isColorable(result);
    var painting:Boolean = false;
    var info:DecorationInfo = null;
    if(!result.hasTagCompound()){
      info = new DecorationInfo(color);
      result.setTagCompound(new NBTTagCompound());
      info.writeToNBT(result.getTagCompound());
    }else{
      info = new DecorationInfo(result.getTagCompound(), color);
    }
    if(coreStack != null && isCoreMaterial(coreStack)){
      painting = true;
      info.coreBlock = CoreMaterialHelper.getCoreMaterialBlock(coreStack);
      info.coreMetadata = CoreMaterialHelper.getCoreMaterialMetadata(coreStack);
    }
    if(frameStack != null && isFrameMaterial(frameStack)){
      painting = true;
      info.frameMaterial = FrameMaterials.Instance.getMaterial(frameStack);
    }
    if(color && colorStack != null && isDye(colorStack)){
      painting = true;
      info.decColor = MinecraftColors.dye(colorStack.getItemDamage());
    }
    if(painting){
      info.writeToNBT(result.getTagCompound());
      result.getTagCompound().setBoolean(DecorationHelper.KEY_PAINTED, true);
      return result;
    }
    return null;
  }

}