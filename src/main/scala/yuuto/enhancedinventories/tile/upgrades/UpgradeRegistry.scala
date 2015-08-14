package yuuto.enhancedinventories.tile.upgrades

import yuuto.yuutolib.utill.StackIdentifire
import net.minecraft.item.ItemStack
import scala.collection.mutable.Map;
object UpgradeRegistry {
  final private val stackMap:Map[StackIdentifire, Upgrade] = Map();
  
  def addUpgrade(upgrade:Upgrade){
    val id:StackIdentifire = new StackIdentifire(upgrade.getDrop());
    stackMap.put(id, upgrade);
  }
  def getUpgrade(stack:ItemStack):Upgrade={
    for(t<-stackMap){
      if(t._1.matches(stack)){
        return t._2;
      }
    }
    return null;
  }
}