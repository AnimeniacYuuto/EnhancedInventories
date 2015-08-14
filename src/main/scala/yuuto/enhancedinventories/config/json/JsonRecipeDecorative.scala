/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import net.minecraft.item.ItemStack
import scala.collection.mutable.HashMap
import scala.collection.mutable.MutableList
import cpw.mods.fml.common.registry.GameRegistry
import yuuto.enhancedinventories.config.recipe.RecipeDecorative
import yuuto.enhancedinventories.util.LogHelperEI
import net.minecraftforge.oredict.OreDictionary

object JsonRecipeDecorative{
  def getRecipe(output:ItemStack, format:Array[String], parts:Array[RecipePart]):JsonRecipeDecorative={
    var width:Int=0;
    var height:Int=0;
    var shape:String="";
    for(s<-format){
      width=s.length();
      shape+=s;
    }
    height=format.length;
    if(width*height != shape.length()){
      
    }
    val itemMap:HashMap[Char, RecipePart]=new HashMap[Char, RecipePart]();
    for(part<-parts){
      itemMap+=part.c->part;
    }
    val input:Array[Object]=new Array[Object](width*height);
    val cores:MutableList[Int]=new MutableList[Int]();
    val frames:MutableList[Int]=new MutableList[Int]();
    val colors:MutableList[Int]=new MutableList[Int]();
    var oldSlot:Int= -1;
    var i=0;
    var needsFrame=false;
    for(c<-shape.toCharArray()){
      if(itemMap.contains(c)){
        val part:RecipePart=itemMap.apply(c);
        part.partType match{
          case 0=>input(i)=part.o;
          case 1=>input(i)=part.o;
          case 2=>{
            part.o.asInstanceOf[String].toLowerCase() match{
              case "core"=>{
               cores+=i;
               input(i)=OreDictionary.getOres("plankWood");
              }
              case "frame"=>{
                frames+=i;
                input(i)=null;
                needsFrame=true;
              }
            }
          }
          case 3=>{
            colors+=i;
            input(i)=part.o;
          }
          case 4=>{
            oldSlot=i;
            input(i)=part.o;
          }
        }
      }else{
        input(i)==null;
      }
      i+=1;
    }
    return new JsonRecipeDecorative(output, input, width, height, cores.toArray, frames.toArray, colors.toArray, oldSlot, needsFrame);
  }
}
class JsonRecipeDecorative(output:ItemStack, inputs:Array[Object], width:Int, height:Int,
    cores:Array[Int], frames:Array[Int], colors:Array[Int], oldSlot:Int, needsFrame:Boolean) {
  var registered:Boolean=false;
  def addRecipe(frame:Object){
    if(needsFrame){
      if(frame.isInstanceOf[ItemStack]){
        for(i<-frames){
          inputs(i)=frame;
        }
      }else if(frame.isInstanceOf[String]){
        for(i<-frames){
          inputs(i)=OreDictionary.getOres(frame.asInstanceOf[String]);
        }
      }
      GameRegistry.addRecipe(new RecipeDecorative(output, inputs.clone(), width, height, false, cores, frames(0), colors, oldSlot));
    }else if(!registered){
      GameRegistry.addRecipe(new RecipeDecorative(output, inputs.clone(), width, height, false, cores, -1, colors, oldSlot));
      registered=true;
    }
  }
}