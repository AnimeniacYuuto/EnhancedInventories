/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import net.minecraft.init.Blocks

object RecipePart{
  def getForStack(c:Char, i:ItemStack):RecipePart={
    return new RecipePart(c,i,0);
  }
  def getForOre(c:Char, s:String):RecipePart={
    return new RecipePart(c, OreDictionary.getOres(s),1);
  }
  def getForOreName(c:Char, s:String):RecipePart={
    return new RecipePart(c, s,1);
  }
  def getForMaterial(c:Char, s:String):RecipePart={
    return new RecipePart(c, s,2);
  }
  def getForMaterialColor(c:Char, s:String):RecipePart={
    s.toLowerCase() match{
      case "wool"=>return new RecipePart(c, new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 3);
      case "clay"=>return new RecipePart(c, new ItemStack(Blocks.stained_hardened_clay, 1, OreDictionary.WILDCARD_VALUE), 3);
      case "dye"=>return new RecipePart(c, OreDictionary.getOres("dye"), 3);
    }
    return null;
  }
  def getForChest(c:Char, i:ItemStack):RecipePart={
    return new RecipePart(c, i, 4);
  }
}
class RecipePart(val c:Char,val o:Object, val partType:Int) {
}