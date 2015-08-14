package yuuto.enhancedinventories.tile.traits

import yuuto.enhancedinventories.tile.upgrades.Upgrade
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import cofh.lib.util.helpers.SecurityHelper
import net.minecraftforge.common.util.FakePlayer

/**
 * @author Jacob
 */
trait TSecurableUpgradeable extends TSecurable with TUpgradeable{
  override def isSecured():Boolean=this.currentUpgrades.contains(Upgrade.SECURITY);
  override def isUpgradeValid(stack:ItemStack, player:EntityPlayer):Boolean={
    return this.canPlayerAccess(player) && super.isUpgradeValid(stack, player);
  }
  override def addUpgrade(stack:ItemStack, player:EntityPlayer):Boolean={
    return this.canPlayerAccess(player) && super.addUpgrade(stack, player);
  }
  override def addUpgrade(u:Upgrade, player:EntityPlayer):Boolean={
    if(u == Upgrade.SECURITY){
      if(player == null || player.isInstanceOf[FakePlayer])
        return false;
      this.setOwner(player.getGameProfile());
    }
    return super.addUpgrade(u, player)
  }
  override def isUpgradeValid(upgrade:Upgrade, player:EntityPlayer):Boolean={
    if(upgrade == Upgrade.SECURITY){
      if(player == null || player.isInstanceOf[FakePlayer])
        return false;
    }
    return super.isUpgradeValid(upgrade, player);
  }
}