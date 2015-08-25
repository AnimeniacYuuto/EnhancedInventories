package yuuto.enhancedinventories.tile.traits

import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.materials.DecorationInfo
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.materials.DecorationHelper

/**
 * @author Jacob
 */
trait TDecorativeInventoryConnective extends TInventoryConnectiveUpgradeable with TDecorative{
  
  override def isValidForConnection(inv:TConnective):Boolean={
    if(!super.isValidForConnection(inv))
      return false;
    return this.decor.matches(inv.asInstanceOf[TDecorativeInventoryConnective].decor);
  }
  
  override def isValidForConnection(newChest:ItemStack):Boolean={
    if(!super.isValidForConnection(newChest))
      return false;
    val info:DecorationInfo = getInfo(newChest);
    return this.decor.matches(info);
  }
  
  protected def getInfo(stack:ItemStack):DecorationInfo ={
    if(stack.hasTagCompound())
      return this.ceateDecorationInfo(stack.getTagCompound());
    return new DecorationInfo(true);
  }
  override def addSizeUpgrade(stack:ItemStack, player:EntityPlayer):Boolean={
    if(super.addSizeUpgrade(stack, player)){
      if(stack.hasTagCompound() && !this.isPainted()){
        this.decor.frameMaterial = FrameMaterials.Instance.getMaterial(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME))
      }
      return true;
    }
    return false;
  }
  
}