/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import java.io.File
import java.io.FileReader
import java.util.Iterator
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.item.Item
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.util.LogHelperEI
import scala.collection.mutable.MutableList

class JsonRecipeFactory {
  //private val configs:Array[File]=new Array[File](4);
  var recipeDecorativeConfigs:MutableList[RecipeConfigDecorative]=new MutableList[RecipeConfigDecorative]();
  var recipeDecorativeFiles:MutableList[String]=new MutableList[String]();
  var genericRecipes:RecipeConfig=null;
  def init(){
    recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(ProxyCommon.blockImprovedChest), 0, 7);
    recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(ProxyCommon.blockLocker), 0, 7);
    recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(ProxyCommon.blockCabinet),0,7);
    recipeDecorativeConfigs+=new RecipeConfigDecorative(ProxyCommon.chestConverter, Item.getItemFromBlock(ProxyCommon.blockImprovedChest), 0, 7);
    recipeDecorativeConfigs+=new RecipeConfigDecorative(ProxyCommon.sizeUpgrades, 0, 6, 1);
    recipeDecorativeFiles+=("ImprovedChest", "Locker", "Cabinet", "ChestConverter", "SizeUpgrade");
    genericRecipes=new RecipeConfig();
  }
  def loadRecipes(){
    LogHelperEI.Info("Loading Recipes from configs");
    val dir:File=EIConfiguration.getRecipeConfigDir();
    val parser:JsonParser = new JsonParser();
    loadGenericRecipes(genericRecipes, new File(dir, "Generic.json"), parser);
    for(i<-0 until recipeDecorativeFiles.size){
      loadDecorativeRecipes(recipeDecorativeConfigs(i), new File(dir, recipeDecorativeFiles(i)+".json"), parser);
    }
  }
  private def loadGenericRecipes(cfg:RecipeConfig, file:File, parser:JsonParser){
    var json:JsonObject=null;
    try {
      json = parser.parse(new FileReader(file)).asInstanceOf[JsonObject];
    } catch{
      case t:Throwable=>{
        LogHelperEI.Error("Critical error reading from recipe file "+file.getName()+" Please make sure it is formated correctly");
        LogHelperEI.Error(t.getMessage());
      }
      //log.error("Critical error reading from a world generation file: " + configs(1) + " > Please be sure the file is correct!", t);
      return;
    }
    val itr=json.entrySet().iterator();
    val fileName=file.getName();
    while(itr.hasNext()){
      val entry=itr.next();
      try{
        cfg.readRecipe(entry.getValue().getAsJsonObject());
      }catch{
        case e:Exception=>{
          LogHelperEI.Error("Error loading recipe "+entry.getKey()+" in file: "+fileName)
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
  private def loadDecorativeRecipes(cfg:RecipeConfigDecorative, file:File, parser:JsonParser){
    var json:JsonObject=null;
    try {
      json = parser.parse(new FileReader(file)).asInstanceOf[JsonObject];
    } catch{
      case t:Throwable=>{
        LogHelperEI.Error("Critical error reading from recipe file "+file.getName()+" Please make sure it is formated correctly");
        LogHelperEI.Error(t.getMessage());
      }
      //log.error("Critical error reading from a world generation file: " + configs(1) + " > Please be sure the file is correct!", t);
      return;
    }
    val fileName=file.getName();
    for(i<-cfg.minTier to cfg.maxTier if(json.has("Tier"+i))){
      val tier:JsonObject=json.getAsJsonObject("Tier"+i);
      if(tier.has("recipes")){
        if(tier.get("recipes").isJsonArray()){
          cfg.readRecipes(i, tier.getAsJsonArray("recipes"), fileName);
        }else if(tier.get("recipes").isJsonObject()){
          try{ 
            cfg.readRecipe(i, tier.getAsJsonObject("recipes"));
          }catch{
            case e:Exception=>{
              LogHelperEI.Error("Error loading recipe for tier"+i+" in file: "+fileName)
              LogHelperEI.Error("Caused by: "+e.getMessage())
              var t=e.getCause();
              while(t!=null){
                LogHelperEI.Error("Caused by: "+t.getMessage())
                t=t.getCause();
              }
            }
          }
        }else{
          LogHelperEI.Error("Error loading recipe for tier"+i+" in file: "+fileName+" recipes field is invalid!")
        }
      }else{
        LogHelperEI.Error("Error loading recipe for tier"+i+" in file: "+fileName+" recipes field is missing!")
      }
    }
  }
  
}