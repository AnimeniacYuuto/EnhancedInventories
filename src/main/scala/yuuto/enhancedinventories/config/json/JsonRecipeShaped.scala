/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import java.lang.Character
import scala.collection.mutable.MutableList
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.ItemStack
import net.minecraft.item.Item
import net.minecraft.block.Block
import cpw.mods.fml.common.registry.GameRegistry
import yuuto.enhancedinventories.util.LogHelperEI
import yuuto.yuutolib.recipe.RecipeHelper

class JsonRecipeShaped(output:ItemStack, format:Array[String], private var parts:Array[RecipePart]) extends JsonRecipe{
  val recipe:MutableList[Object]=new MutableList[Object]();
  recipe+=format;
  var ore:Boolean=false;
  for(part<-parts){
    recipe+=Character.valueOf(part.c);
    recipe+=part.o;
    if(part.partType==1){
      ore = true;
    }
  }
  parts=null;
  
  def this(outputBlock:Block, format:Array[String], parts:Array[RecipePart])=this(new ItemStack(outputBlock), format, parts);
  def this(outputItem:Item, format:Array[String], parts:Array[RecipePart])=this(new ItemStack(outputItem), format, parts);
  
  def registerRecipe(){
    RecipeHelper.addShapedRecipe(ore, output, recipe.toArray);
  }
}