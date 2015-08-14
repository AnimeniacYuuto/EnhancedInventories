/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import java.awt.Color
import java.util.HashMap
import java.util.Iterator
import java.util.Map
import java.util.Map.Entry
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import yuuto.yuutolib.utill.ItemIdentifire
import yuuto.enhancedinventories.util.LogHelperEI
import yuuto.yuutolib.utill.OreIdentifire

object FrameMaterials{
  final val frameStone:Int = 1;
  final val frameMetal:Int = 0;
  final val frameObsidian:Int = 2;
  
  final val Stone:FrameMaterial = new FrameMaterial("frame.mc.stone", frameStone, new Color(1f,1f,1f,1f));
  final val Obsidian:FrameMaterial = new FrameMaterial("frame.mc.obsidian", frameObsidian, new Color(1f, 1f, 1f, 1f));
  final val Instance:FrameMaterials=new FrameMaterials();
}
class FrameMaterials private(){
  val materialStackMap:Map[ItemIdentifire, FrameMaterial] = new HashMap[ItemIdentifire, FrameMaterial]();
  val materialIdMap:Map[String, FrameMaterial] = new HashMap[String, FrameMaterial]();
  
  def registerMaterial(matStack:ItemStack, mat:FrameMaterial){
    if(matStack == null || mat == null)
      return;
    val id:ItemIdentifire = ItemIdentifire.getIdentifire(matStack);
    if(isRegistered(id))
      return;
    materialStackMap.put(id, mat);
    materialIdMap.put(mat.getID(), mat);
  }
  def registerMaterial(matOre:String, mat:FrameMaterial){
    if(matOre == null || matOre.trim().isEmpty() || mat == null)
      return;
    val id:ItemIdentifire = ItemIdentifire.getIdentifire(matOre);
    if(isRegistered(id))
      return;
    materialStackMap.put(id, mat);
    materialIdMap.put(mat.getID(), mat);
  }
  def registerMaterial(mat:FrameMaterial){
    materialIdMap.put(mat.getID(), mat);
  }
  
  def getMaterial(name:String):FrameMaterial={
    if(materialIdMap.containsKey(name))
      return materialIdMap.get(name);
    return FrameMaterials.Stone;
  }
  def getMaterial(stack:ItemStack):FrameMaterial={
    val itr:Iterator[Entry[ItemIdentifire,FrameMaterial]] = materialStackMap.entrySet().iterator();
    while(itr.hasNext()){
      val entry:Entry[ItemIdentifire,FrameMaterial] = itr.next();
      if(entry.getKey().matches(stack))
        return entry.getValue();
    }
    LogHelperEI.Warning("Failded to find frame material for "+stack.getDisplayName()+" returning stone checked "+materialStackMap.size()+" entrys");
    return FrameMaterials.Stone;
  }
  def getMaterialByOre(ore:String):FrameMaterial={
    val stack:ItemIdentifire = ItemIdentifire.getIdentifire(ore);
    val itr:Iterator[Entry[ItemIdentifire,FrameMaterial]] = materialStackMap.entrySet().iterator();
    while(itr.hasNext()){
      val entry:Entry[ItemIdentifire,FrameMaterial] = itr.next();
      if(entry.getKey().matches(stack))
        return entry.getValue();
    }
    LogHelperEI.Warning("Failded to find frame material for "+ore+" returning stone checked "+materialStackMap.size()+" entrys");
    return FrameMaterials.Stone;
  }
  
  def isRegistered(stack:ItemStack):Boolean={
    return isRegistered(ItemIdentifire.getIdentifire(stack));
  }
  def isRegistered(ore:String):Boolean={
    return isRegistered(ItemIdentifire.getIdentifire(ore));
  }
  
  private def isRegistered(id:ItemIdentifire):Boolean={
    val itr:Iterator[ItemIdentifire] = materialStackMap.keySet().iterator();
    while(itr.hasNext()){
      val id1:ItemIdentifire = itr.next();
      if(id.matches(id1))
        return true;
    }
    return false;
  }
  

}