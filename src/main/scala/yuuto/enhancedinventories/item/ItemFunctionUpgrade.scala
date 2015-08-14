package yuuto.enhancedinventories.item

import java.util.List
import yuuto.enhancedinventories.tile.traits.TUpgradeable
import yuuto.yuutolib.item.traits.TItemMultiBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import yuuto.enhancedinventories.item.base.ItemBaseEI


class ItemFunctionUpgrade(name:String) extends ItemBaseEI(name) with TItemMultiBase{
  this.hasSubtypes = true;
  
  override def onItemUse(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!player.isSneaking() || world.isRemote || stack.getItemDamage() == 0)
      return false;
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile == null || !tile.isInstanceOf[TUpgradeable])
      return false;
    val chest:TUpgradeable = tile.asInstanceOf[TUpgradeable];
    if(!chest.isUpgradeValid(stack, player)){
      if(stack.getItemDamage() == 3)
      return false;
    }
    if(!chest.addUpgrade(stack, player)){
      if(stack.getItemDamage() == 3)
      return false;
    }
    if(player.capabilities.isCreativeMode)
      return true;
    stack.stackSize -=1;
    player.inventoryContainer.detectAndSendChanges();
    return true;
  }

}