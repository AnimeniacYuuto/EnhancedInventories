package yuuto.enhancedinventories.materials

import java.util.Random
import scala.collection.mutable.MutableList
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.util.LogHelperEI


object ETier{
  private val list:MutableList[ETier]= new MutableList[ETier]();
  
  val Tier1:ETier=new ETier(); 
  val Tier2:ETier=new ETier();
  val Tier3:ETier=new ETier(); 
  val Tier4:ETier=new ETier(); 
  val Tier5:ETier=new ETier(); 
  val Tier6:ETier=new ETier();
  val Tier7:ETier=new ETier(); 
  val Tier8:ETier=new ETier();
  
  def values():Array[ETier]=list.toArray;
}
class ETier private(){
  val chestMaterials:MutableList[Object] = new MutableList[Object]();
  val tier:Int=ETier.list.size;
  ETier.list+=this;
  
  def addChestMaterial(o:Object)=chestMaterials+=o;
  def getChestMaterials():Array[Object]={
    return chestMaterials.toArray;
  }
  def getRandomFrameMaterial(rand:Random):FrameMaterial={
    if(chestMaterials.size < 1)
      return FrameMaterials.Stone;
    val o:Object = chestMaterials.apply(rand.nextInt(chestMaterials.size));
    //LogHelperEI.Debug("Got random for "+chestMaterials.size+" materails at "+this.toString());
    if(o == null){
      LogHelperEI.Warning("material object is null");
      return FrameMaterials.Stone;
    }
    if(o.isInstanceOf[ItemStack]){
      return FrameMaterials.Instance.getMaterial(o.asInstanceOf[ItemStack]);
    }
    if(o.isInstanceOf[String]){
      return FrameMaterials.Instance.getMaterialByOre(o.asInstanceOf[String]);
    }
    if(o.isInstanceOf[Item]){
      return FrameMaterials.Instance.getMaterial(new ItemStack(o.asInstanceOf[Item]));
    }
    if(o.isInstanceOf[Block])
      return FrameMaterials.Instance.getMaterial(new ItemStack(o.asInstanceOf[Block]));
    LogHelperEI.Warning("material object is not valid at tier "+tier+", object is "+o.getClass().getName());
    return FrameMaterials.Stone;
  }
}