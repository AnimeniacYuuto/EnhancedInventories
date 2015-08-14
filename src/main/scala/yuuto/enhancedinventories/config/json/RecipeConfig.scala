/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import scala.collection.mutable.MutableList
import yuuto.enhancedinventories.util.LogHelperEI

class RecipeConfig() {
  val recipes:MutableList[JsonRecipe]=new MutableList[JsonRecipe]();
  
  def readRecipe(recipe:JsonObject){
    val output:ItemStack=getOutput(recipe.get("output"));
    val shape:Array[String]=getShape(recipe.get("template"));
    val partList:Array[RecipePart]=getParts(recipe.get("parts"), shape!=null);
    if(partList.size < 1)
      throw new Exception("Invelid recipe part list no elements in list!");
    if(shape!=null)
      recipes+=new JsonRecipeShaped(output, shape, partList);
    else
      recipes+=new JsonRecipeShapeless(output, partList);
  }
  def readRecipes(recipes:JsonArray, fileName:String){
    
  }
  
  def getOutput(element:JsonElement):ItemStack={
    if(element==null){
      throw new Exception("Invalilid output");
    }
    if(element.isJsonObject()){
      try{
        return JsonHelper.getItemStack(element.getAsJsonObject());
      }catch{
        case e:Exception=>{
          throw new Exception("Invelid output", e);
        }
      }
    }
    throw new Exception("Invalilid output");
  }
  def getShape(format:JsonElement):Array[String]={
    if(format == null)
      return null;
    if(format.isJsonArray()){
      val formatList:JsonArray=format.getAsJsonArray();
      val ret:Array[String]=new Array[String](formatList.size());
      var parts=0;
      for(i<-0 until formatList.size()){
        ret(i)=formatList.get(i).getAsString();
        parts+=1;
      }
      if(parts < 1)
        throw new Exception("Invelid recipe part list no valid elements in list!");
      return ret;
    }else if(format.isJsonPrimitive() && format.getAsJsonPrimitive().isString()){
      return Array(format.getAsString());
    }
    throw new Exception("Invelid recipe part list no valid elements in list!");
  }
  def getParts(parts:JsonElement, shaped:Boolean):Array[RecipePart]={
    if(parts.isJsonArray()){
      val partsList:JsonArray=parts.getAsJsonArray();
      val ret:MutableList[RecipePart]=new MutableList[RecipePart]();
      for(i<-0 until partsList.size() if(partsList.get(i).isJsonObject())){
        ret+=getPart(partsList.get(i).getAsJsonObject(), shaped);
      }
      return ret.toArray;
    }else if(parts.isJsonObject()){
      return Array(getPart(parts.getAsJsonObject(), shaped));
    }
    throw new Exception("Invalid recipe part list");
  }
  def getPart(part:JsonObject, shaped:Boolean):RecipePart={
    if(!part.has("id"))
      throw new Exception("Invalid recipe part. Part is missing id");
    var c:Char=' ';
    if(shaped){
      try{
        c=part.get("id").getAsCharacter();
      }catch{
        case e:ClassCastException=>{
          throw new Exception("Invalid recipe part. Part id is not a character");
        }
      }
    }
    if(!part.has("type"))
      throw new Exception("Invalid recipe part. Part is mising type");
    if(!part.get("type").isJsonPrimitive() || !part.getAsJsonPrimitive("type").isString())
      throw new Exception("Invalid recipe part. Part type not a string")
    val tpe:String=part.get("type").getAsString();
    if(!part.has("part"))
      throw new Exception("Invelid recipe part. Part is missing part field.")
    try{
      tpe match{
        case "stack"=>return RecipePart.getForStack(c, JsonHelper.getItemStack(part.getAsJsonObject("part")));
        case "ore"=>return RecipePart.getForOreName(c, part.get("part").getAsString());
      }
    }catch{
      case e:Exception=>{
        if(e.isInstanceOf[ClassCastException])
          throw new Exception("Invalid recipe part. Part part is not valid for Part Type:"+tpe)
        else
          throw new Exception("Invalid recipe part", e);
      }
    }
    throw new Exception("Invalid recipe part. part type:"+tpe+" not a vaid type for recipe")
  }
}