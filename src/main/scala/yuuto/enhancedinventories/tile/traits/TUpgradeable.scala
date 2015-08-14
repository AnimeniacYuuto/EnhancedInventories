package yuuto.enhancedinventories.tile.traits

import yuuto.enhancedinventories.tile.base.TileBaseEI
import yuuto.enhancedinventories.tile.upgrades.Upgrade
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.util.FakePlayer
import yuuto.enhancedinventories.tile.upgrades.UpgradeRegistry
import scala.collection.mutable.MutableList
import net.minecraft.nbt.NBTTagCompound
import java.util.ArrayList

/**
 * @author Jacob
 */
trait TUpgradeable extends TileBaseEI{
  
  val currentUpgrades:MutableList[Upgrade]=new MutableList();
  
  def getValidUpgrades():Array[Upgrade];  
  def isUpgradeValid(stack:ItemStack, player:EntityPlayer):Boolean={
    val upgrade:Upgrade = UpgradeRegistry.getUpgrade(stack);
    if(upgrade==null)
      return false;
    return isUpgradeValid(upgrade, player);
  }
  def isUpgradeValid(upgrade:Upgrade, player:EntityPlayer):Boolean={
    if(!getValidUpgrades().contains(upgrade))
      return false;
    if(currentUpgrades.contains(upgrade))
      return false;
    return true;
  }
  def addUpgrade(stack:ItemStack, player:EntityPlayer):Boolean={
    val upgrade:Upgrade = UpgradeRegistry.getUpgrade(stack);
    if(upgrade==null)
      return false;
    return addUpgrade(upgrade, player);
  }
  
  def addUpgrade(u:Upgrade, player:EntityPlayer):Boolean=addUpgrade(u);
  def addUpgrade(u:Upgrade):Boolean={
    if(currentUpgrades.contains(u))
      return false;
    currentUpgrades+=u;
    if(currentUpgrades.contains(u)){
      if(this.getWorldObj() != null && !this.getWorldObj().isRemote)
        this.getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 3, u.getIndex());
      return true;
    }
    return false;
  }
  def getUpgradeDrops(drops:ArrayList[ItemStack]){
    for(u <- currentUpgrades){
      drops.add(u.getDrop().copy());
    }
  }
  def isTrapped():Boolean={
    return currentUpgrades.contains(Upgrade.TRAPPED);
  }
  def isReinforced():Boolean={
    return currentUpgrades.contains(Upgrade.REINFORCED);
  }
  def isHopper():Boolean={
    return currentUpgrades.contains(Upgrade.HOPPER);
  }
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    if(currentUpgrades.size > 0){
      val upgrades:Array[Byte] = new Array[Byte](currentUpgrades.size);
      for(i <-0 until upgrades.length){
        upgrades(i) = currentUpgrades.apply(i).getIndex().asInstanceOf[Byte];
      }
      nbt.setByteArray("upgrades", upgrades);
    }
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    if(nbt.hasKey("upgrades")){
       val upgrades:Array[Byte] = nbt.getByteArray("upgrades");
      for(b <- upgrades){
        addUpgrade(Upgrade.values()(b));
      }
    }
  }
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    if(currentUpgrades.size > 0){
      val upgrades:Array[Byte] = new Array[Byte](currentUpgrades.size);
      for(i <-0 until upgrades.length){
        upgrades(i) = currentUpgrades.apply(i).getIndex().asInstanceOf[Byte];
      }
      nbt.setByteArray("upgrades", upgrades);
    }
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    if(nbt.hasKey("upgrades")){
       val upgrades:Array[Byte] = nbt.getByteArray("upgrades");
      for(b <- upgrades){
        addUpgrade(Upgrade.values()(b));
      }
    }
  }
  
  override def receiveClientEvent(event:Int, args:Int):Boolean={
    if(event == 3){
      addUpgrade(Upgrade.values()(args));
      return true;
    }
    return super.receiveClientEvent(event, args);
  }
}