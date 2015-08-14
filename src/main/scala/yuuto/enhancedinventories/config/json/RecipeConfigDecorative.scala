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
import yuuto.enhancedinventories.config.json.exception.RecipePartException

class RecipeConfigDecorative(item:Item, item2:Item, val minTier:Int, val maxTier:Int, val offest:Int) {
  val recipes:Array[MutableList[JsonRecipeDecorative]]=new Array[MutableList[JsonRecipeDecorative]](8);
  
  def this(item:Item, item2:Item, minTier:Int,maxTier:Int)=this(item, item2, minTier, maxTier, 0);
  def this(item:Item, minTier:Int, maxTier:Int, offest:Int)=this(item, null, minTier, maxTier, offest);
  def this(item:Item, minTier:Int, maxTier:Int)=this(item, null, minTier, maxTier);
  
  def readRecipe(tier:Int, recipe:JsonObject){
    val shape:Array[String]=getShape(recipe.get("template"));
    if(shape==null || shape.size < 1)
      throw new Exception("Invalid recipe shape");
    val partList:Array[RecipePart]=getParts(tier, recipe.get("parts"));
    if(partList.size < 1)
      throw new Exception("Invelid recipe part list no elements in list!");
    if(recipes(tier)==null)
      recipes(tier)=new MutableList[JsonRecipeDecorative]();
    recipes(tier)+=JsonRecipeDecorative.getRecipe(new ItemStack(item, 1, tier), shape, partList);
  }
  def readRecipes(tier:Int, recipes:JsonArray, fileName:String){
    for(i<-0 until recipes.size() if(recipes.get(i).isJsonObject())){
      try{  
        readRecipe(tier, recipes.get(i).asInstanceOf[JsonObject])
      }catch{
        case e:Exception=>{
          LogHelperEI.Error("Error loading recipe "+i+" for tier "+tier+" in file: "+fileName)
          LogHelperEI.Error("Caused by: "+e.getMessage())
          var t=e.getCause();
          while(t!=null){
            LogHelperEI.Error("Caused by: "+t.getMessage())
            t=t.getCause();
          }
        }
      }
    }
  }
  
  def getShape(format:JsonElement):Array[String]={
    if(format == null)
      return null;
    if(format.isJsonArray()){
      val formatList:JsonArray=format.getAsJsonArray();
      val ret:Array[String]=new Array[String](formatList.size());
      for(i<-0 until formatList.size()){
        ret(i)=formatList.get(i).getAsString();
      }
      return ret;
    }else if(format.isJsonPrimitive() && format.getAsJsonPrimitive().isString()){
      return Array(format.getAsString());
    }
    return null;
  }
  def getParts(tier:Int, parts:JsonElement):Array[RecipePart]={
    if(parts==null)
      throw new Exception("Missing parts list field!");
    if(parts.isJsonArray()){
      val partsList:JsonArray=parts.getAsJsonArray();
      val ret:MutableList[RecipePart]=new MutableList[RecipePart]();
      for(i<-0 until partsList.size()){
        if(partsList.get(i) == null){
          LogHelperEI.Error("Error loading part "+i+" no part found");
        }
        else if(partsList.get(i).isJsonObject())
          ret+=getPart(tier, partsList.get(i).getAsJsonObject());
        else
          LogHelperEI.Error("Error loading part "+i+" part is invalid");
      }
      return ret.toArray;
    }else if(parts.isJsonObject()){
      return Array(getPart(tier, parts.getAsJsonObject()));
    }
    throw new Exception("Invalid recipe part list");
  }
  def getPart(tier:Int, part:JsonObject):RecipePart={
    if(!part.has("id"))
      throw new Exception("Invalid recipe part. Part is missing id");
    var c:Char=' ';
    try{
      c=part.get("id").getAsCharacter();
    }catch{
      case e:ClassCastException=>{
        throw new Exception("Invalid recipe part. Part id is not a character");
      }
    }
    if(!part.has("type"))
      throw new Exception("Invalid recipe part. Part is mising type");
    if(!part.get("type").isJsonPrimitive() || !part.getAsJsonPrimitive("type").isString())
      throw new Exception("Invalid recipe part. Part type not a string")
    val tpe:String=part.get("type").getAsString();
    if(!part.has("part") && !tpe.matches("materialChest"))
      throw new Exception("Invelid recipe part. Part is missing part field and Part type is not materialChest")
    try{
      tpe match{
        case "stack"=>return RecipePart.getForStack(c, JsonHelper.getItemStack(part.getAsJsonObject("part")));
        case "ore"=>return RecipePart.getForOre(c, part.get("part").getAsString());
        case "material"=>return RecipePart.getForMaterial(c, part.get("part").getAsString());
        case "materialColor"=>return RecipePart.getForMaterialColor(c, part.get("part").getAsString());
        case "materialChest"=>{
          if(item2 == null)return RecipePart.getForChest(c, new ItemStack(item, 1, tier-1))
          else return RecipePart.getForChest(c, new ItemStack(item2, 1, tier))
        }
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