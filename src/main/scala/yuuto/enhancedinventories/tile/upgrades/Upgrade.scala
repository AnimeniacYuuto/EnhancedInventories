package yuuto.enhancedinventories.tile.upgrades

import yuuto.enhancedinventories.proxy.ProxyCommon
import net.minecraft.item.ItemStack

/**
 * @author Jacob
 */
object Upgrade{
  private val vals:Array[Upgrade] = new Array[Upgrade](5);
  
  val HOPPER=new Upgrade(0, "upgrade.hopper", new ItemStack(ProxyCommon.functionUpgrades, 1, 1));
  val TRAPPED=new Upgrade(1, "upgrade.trapped", new ItemStack(ProxyCommon.functionUpgrades, 1, 2)); 
  val SECURITY=new Upgrade(2, "upgrade.security", new ItemStack(ProxyCommon.functionUpgrades, 1, 3));
  val REINFORCED=new Upgrade(3, "upgrade.reinforced", new ItemStack(ProxyCommon.functionUpgrades, 1, 4)); 
  val SORTING=new Upgrade(4, "upgrade.sorting", null);
  
  def values():Array[Upgrade]=vals.clone();
  def apply(index:Int):Upgrade=vals(index);
}
class Upgrade private (private val index:Int, private val id:String, private var drop:ItemStack) {
  Upgrade.vals(index) = this;
  UpgradeRegistry.addUpgrade(this);
  
  def getIndex()=index;
  def getID():String=id;
  def getDrop():ItemStack=drop;
  def setDrop(stack:ItemStack){
    this.drop = stack;
  }

}