/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config

import java.io.File
import java.io.FileOutputStream
import org.apache.commons.io.IOUtils
import yuuto.enhancedinventories.util.LogHelperEI

object ConfigHelper {
  def refreshRecipeFiles(){
    LogHelperEI.Info("Reloading Recipe Defaults");
    if(!EIConfiguration.getRecipeConfigDir().exists()){
      EIConfiguration.getRecipeConfigDir().mkdir();
    }
    refreshRecipeFile("Cabinet.json");
    refreshRecipeFile("ChestConverter.json");
    refreshRecipeFile("Generic.json");
    refreshRecipeFile("ImprovedChest.json");
    refreshRecipeFile("Locker.json");
    refreshRecipeFile("SizeUpgrade.json");
  }
  protected def refreshRecipeFile(name:String){
    val f:File=getRecipeConfigFile(name);
    //if(!f.exists()){
    //  f.createNewFile();
    //}
    IOUtils.copy(ConfigHelper.getClass().getResourceAsStream(getRecipeConfigAsset(name)), new FileOutputStream(f));
  }
  def refreshFrameFiles(){
    LogHelperEI.Info("Reloading Default Frames");
    if(!EIConfiguration.getFrameConfigDir().exists()){
      EIConfiguration.getFrameConfigDir().mkdir();
    }
    refreshFrameFile("VanillaFrames.json");
    refreshFrameFile("CompatFrames.json");
  }
  protected def refreshFrameFile(name:String){
    val f:File=getFrameConfigFile(name);
    //if(!f.exists()){
    //  f.createNewFile();
    //}
    IOUtils.copy(ConfigHelper.getClass().getResourceAsStream(getFrameConfigAsset(name)), new FileOutputStream(f));
  }
  def getRecipeConfigAsset(name:String):String="/assets/enhancedinventories/configs/recipes/"+name;
  def getFrameConfigAsset(name:String):String="/assets/enhancedinventories/configs/frames/"+name;
  def getRecipeConfigFile(name:String):File=new File(EIConfiguration.getRecipeConfigDir(), name);
  def getFrameConfigFile(name:String):File=new File(EIConfiguration.getFrameConfigDir(), name);;
}