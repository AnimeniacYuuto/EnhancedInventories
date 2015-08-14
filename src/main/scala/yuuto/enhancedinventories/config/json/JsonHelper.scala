/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import net.minecraft.item.ItemStack
import com.google.gson.JsonObject
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.JsonToNBT
import net.minecraftforge.oredict.OreDictionary
import yuuto.enhancedinventories.util.LogHelperEI

object JsonHelper {
  def getItemStack(stackObj:JsonObject):ItemStack={
    if(stackObj==null){
      throw new Exception("Invalid json item stack, no item stack object found")
    }
    if(!stackObj.has("name") || !stackObj.get("name").isJsonPrimitive() || !stackObj.getAsJsonPrimitive("name").isString()){
      throw new Exception("Invalid json item stack exception! json is missing required field name");
    }
    val name:Array[String]=stackObj.get("name").getAsString().split(":", 2);
    val stack:ItemStack=if(name.length>1){GameRegistry.findItemStack(name(0), name(1), 1)}else{GameRegistry.findItemStack("minecraft", name(0), 1)};
    if(stackObj.has("meta")){
      if(stackObj.getAsJsonPrimitive("meta").isString()){
        stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
      }else if(stackObj.getAsJsonPrimitive("meta").isNumber()){
        stack.setItemDamage(stackObj.get("meta").getAsInt());
      }else{
        LogHelperEI.Error("Error could not load item metadata from json");
      }
    }
    if(stackObj.has("nbt")){
      val nbtbase:NBTBase = JsonToNBT.func_150315_a(stackObj.get("nbt").getAsString());

      if (!(nbtbase.isInstanceOf[NBTTagCompound])) {
        LogHelperEI.Error("Error could not load item nbt data from json");
      }else{
        stack.setTagCompound(nbtbase.asInstanceOf[NBTTagCompound]);
      }
    }
    return stack;
  }
  
}