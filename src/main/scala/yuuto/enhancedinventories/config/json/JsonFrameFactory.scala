/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.json

import yuuto.enhancedinventories.config.EIConfiguration
import java.io.File
import java.io.FilenameFilter
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import yuuto.enhancedinventories.util.LogHelperEI
import java.io.FileReader
import yuuto.enhancedinventories.materials.FrameMaterial
import com.google.gson.JsonElement
import scala.collection.mutable.MutableList
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.materials.ETier

class JsonFrameFactory {
  def loadFrames(){
    LogHelperEI.Info("Loading Frames");
    val dir:File=EIConfiguration.getFrameConfigDir();
    val files:Array[File]=getFileList(dir);
    val parser:JsonParser = new JsonParser();
    if(EIConfiguration.canCraftStoneFrame){
      ETier.Tier1.addChestMaterial("cobblestone");
      FrameMaterials.Instance.registerMaterial("cobblestone", FrameMaterials.Stone);
    }else{
      FrameMaterials.Instance.registerMaterial(FrameMaterials.Stone);
    }
    if(EIConfiguration.canPaintObsidianFrame){
      FrameMaterials.Instance.registerMaterial("obsidian", FrameMaterials.Obsidian);
    }else{
      FrameMaterials.Instance.registerMaterial(FrameMaterials.Obsidian);
    }
    for(f<-files){
      loadFramesFromFile(f, parser);
    }
  }
  def getFileList(dir:File):Array[File]={
    val files:Array[File]=dir.listFiles(new FilenameFilter() {
      override def accept(file:File, name:String):Boolean={
        if (name == null) {
          return false;
        }
        return name.toLowerCase().endsWith(".json"); //|| new File(file, name).isDirectory();
      }
    });
    return files;
  }
  def loadFramesFromFile(file:File, parser:JsonParser){
    var frameList:JsonObject=null;
    try {
      frameList = parser.parse(new FileReader(file)).asInstanceOf[JsonObject];
    } catch{
      case t:Throwable=>{
        LogHelperEI.Error("Critical error reading from recipe file "+file.getName()+" Please make sure it is formated correctly");
        LogHelperEI.Error(t.getMessage());
      }
      //log.error("Critical error reading from a world generation file: " + configs(1) + " > Please be sure the file is correct!", t);
      return;
    }
    val itr=frameList.entrySet().iterator();
    val fileName=file.getName();
    while(itr.hasNext()){
      val entry=itr.next()
      if(!entry.getValue().isJsonObject()){
        LogHelperEI.Error("Invalid frame "+entry.getKey()+" in file "+file.getName());
      }else{
        try{
          parseFrame(entry.getValue().getAsJsonObject(), fileName, entry.getKey());
        }catch{
          case e:Exception=>{
            LogHelperEI.Error(e.getMessage());
            var t=e.getCause();
            while(t != null){
              LogHelperEI.Error(t.getMessage())
              t=t.getCause();
            }
          }
        }
      }
    }
  }
  def parseFrame(jsonFrame:JsonObject, fileName:String, key:String){
    val name=getFrameName(jsonFrame);
    val r=getInt(jsonFrame, "ColorRed", 255, fileName, key);
    val g=getInt(jsonFrame, "ColorGreen", 255, fileName, key);
    val b=getInt(jsonFrame, "ColorBlue", 255, fileName, key);
    val frameType=getInt(jsonFrame, "FrameType", 0, fileName, key);
    val frameMaterial=new FrameMaterial(name, frameType, r, g, b);
    val materials:Array[Object]=getMaterials(jsonFrame.get("materials"), fileName, key);
    val tier=getInt(jsonFrame, "tier", -1, fileName, key);
    for(o<-materials){
      if(o.isInstanceOf[ItemStack]){
        FrameMaterials.Instance.registerMaterial(o.asInstanceOf[ItemStack], frameMaterial);
        if(tier>=0 && tier < 8){
          ETier.values()(tier).addChestMaterial(o.asInstanceOf[ItemStack]);
        }
      }else if(o.isInstanceOf[String]){
        FrameMaterials.Instance.registerMaterial(o.asInstanceOf[String], frameMaterial);
        if(tier>=0 && tier < 8){
          ETier.values()(tier).addChestMaterial(o.asInstanceOf[String]);
        }
      }
    }
  }
  def getFrameName(jsonFrame:JsonObject):String={
    if(!jsonFrame.has("name") || !jsonFrame.get("name").isJsonPrimitive()){
      return throw new Exception("Cannot find fame name for frame");
    }
    return jsonFrame.get("name").getAsString();
  }
  def getInt(jsonFrame:JsonObject, key:String, default:Int, fileName:String, framekey:String):Int={
    if(!jsonFrame.has(key)){
      return default;
    }
    if(!jsonFrame.get(key).isJsonPrimitive()){
      LogHelperEI.Error("Invalid "+key+" for frame "+framekey+" in file "+fileName);
      return default;
    }
    if(jsonFrame.getAsJsonPrimitive(key).isNumber()){
      return jsonFrame.get(key).getAsInt();
    }else if(jsonFrame.getAsJsonPrimitive(key).isString()){
      var value=default;
      try{
        value=jsonFrame.get(key).getAsInt();
      }catch{
        case e:NumberFormatException=>{
          LogHelperEI.Error("Invalid "+key+" for frame "+framekey+" in file "+fileName+" could convert string to number");
        }
      }
      return value;
    }
    LogHelperEI.Error("Invalid "+key+" for frame "+framekey+" in file "+fileName+" value must be a number");
    return default;
  }
  def getMaterials(mats:JsonElement, fileName:String, framekey:String):Array[Object]={
    if(mats==null){
      throw new Exception("No materials for frame "+framekey+" in file "+fileName+" frames require atleast one material!");
    }
    if(mats.isJsonArray()){
      val jsArray=mats.getAsJsonArray();
      val matList=new MutableList[Object]();
      for(i<-0 until jsArray.size()){
        if(jsArray.get(i).isJsonObject()){
          try{
            val ret=JsonHelper.getItemStack(jsArray.get(i).getAsJsonObject());
            matList+=ret;
          }catch{
            case e:Exception=>{
              LogHelperEI.Error(e.getMessage()+" for frame "+framekey+" in file "+fileName);
            }
          }
        }else if(jsArray.get(i).isJsonPrimitive()){
          matList+=jsArray.get(i).getAsString();
        }else{
          LogHelperEI.Error("invalid material for frame "+framekey+" in file "+fileName);
        }
      }
      if(matList.size < 1){
        throw new Exception("No materials for frame "+framekey+" in file "+fileName+" frames require atleast one material!");
      }
      return matList.toArray;
    }else if(mats.isJsonObject()){
      try{
        val ret=Array(JsonHelper.getItemStack(mats.getAsJsonObject()))
        return ret.asInstanceOf[Array[Object]];
      }catch{
        case e:Exception=>{
          throw new Exception("No materials for frame "+framekey+" in file "+fileName+" frames require atleast one material!", e)
        }
      }
    }else if(mats.isJsonPrimitive()){
      return Array(mats.getAsString());
    }
    LogHelperEI.Error("invalid material for frame "+framekey+" in file "+fileName);
    throw new Exception("No materials for frame "+framekey+" in file "+fileName+" frames require atleast one material!");
  }
}