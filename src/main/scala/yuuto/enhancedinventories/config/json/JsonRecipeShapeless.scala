/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import scala.collection.mutable.MutableList
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import yuuto.yuutolib.recipe.RecipeHelper

class JsonRecipeShapeless(output:ItemStack, private var parts:Array[RecipePart]) extends JsonRecipe{
  def this(outputBlock:Block, parts:Array[RecipePart])=this(new ItemStack(outputBlock), parts);
  def this(outputItem:Item, parts:Array[RecipePart])=this(new ItemStack(outputItem), parts);
  val recipe:MutableList[Object]=new MutableList[Object]();
  var ore:Boolean=false;
  for(part<-parts){
    recipe+=part.o;
    if(part.partType==1){
      ore == true;
    }
  }
  parts=null;
  
  def registerRecipe(){
    RecipeHelper.addShapelessRecipe(ore, output, recipe.toArray);
  }
}