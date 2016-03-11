/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config

import net.minecraftforge.common.config.Configuration
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import java.io.File
import yuuto.enhancedinventories.config.json.JsonRecipeFactory
import yuuto.enhancedinventories.materials.CoreMaterialHelper
import yuuto.enhancedinventories.util.LogHelperEI

object EIConfiguration {
  
  private var configMain:Configuration=null;
  private var configRecipeDir:File=null;
  private var configFrameDir:File=null;
  
  var moduleRefinedRelocation:Boolean=false;
  var moduleInventoryTools:Boolean=false;
  var ROWS:Int = 9;
  var COLOMNS:Int = 12;
  var MAX_SIZE:Int=108;
  
  var canCraftStoneFrame:Boolean=true;
  var canPaintObsidianFrame:Boolean=true;
  var canCraftAlts:Boolean=true;
  var whiteList:Boolean=false;
  var autoAssemblerTickRate:Int=5;
  
  private def getModConfigDir(configDir:File):File={
    return new File(configDir, "EnhancedInventories/")
  }
  private def getMainConfig(configDir:File):File={
    return new File(getModConfigDir(configDir), "main.cfg");
  }
  private def getRecipeConfigDir(configDir:File):File={
    return new File(getModConfigDir(configDir), "recipes/");
  }
  private def getFrameConfigDir(configDir:File):File={
    return new File(getModConfigDir(configDir), "frames/");
  }
  def getRecipeConfigDir():File=configRecipeDir;
  def getFrameConfigDir():File=configFrameDir;
  def init(event:FMLPreInitializationEvent){
    LogHelperEI.Info("Initializing main config");
    configMain = new Configuration(getMainConfig(event.getModConfigurationDirectory()));
    configRecipeDir=getRecipeConfigDir(event.getModConfigurationDirectory());
    configFrameDir=getFrameConfigDir(event.getModConfigurationDirectory());
    loadModules();
    LogHelperEI.debug = configMain.getBoolean("DebugLogging", "General", false, "Used for additional debugging info, mainly for use by mod and pack devs");
    canCraftStoneFrame = configMain.getBoolean("CraftStoneFrame", "Crafting", true, "Are chests with stone frames craftable?");
    canPaintObsidianFrame = configMain.getBoolean("PaintObsidianFrame", "Crafting", true, "Can chests be painted with obsidian frames?");
    canCraftAlts = configMain.getBoolean("CraftAlts", "Crafting", true, "Are alternate chests craftable?");
    whiteList = configMain.getBoolean("UseWhiteListForCores", "Crafting", false, "Should the core black list be used as a whitelist? (Vanilla planks are always enabled)");
    val list:Array[String]=configMain.getStringList("CoreBlackList", "Crafting", new Array[String](0), "A list of blocks that should not be used as cores. Formats: 'name' checks all metas, 'mod:name:meta'");
    CoreMaterialHelper.loadList(list);
    val refreshRecipes=configMain.get("Crafting", "RefreshRecipeConfigs", true, "Should the default recipe configs be regenerated? (will be set back to false after refreshing");
    //val refreshRecipes=configMain.getBoolean("RefreshRecipeConfigs", "Recipe", true, "Should the default recipe configs be regenerated? (will be set back to false after refreshing");
    if(refreshRecipes.getBoolean() || !getFrameConfigDir().exists()){
      ConfigHelper.refreshFrameFiles();
    }
    if(refreshRecipes.getBoolean() || !getRecipeConfigDir().exists()){
      ConfigHelper.refreshRecipeFiles();
    }
    refreshRecipes.set(false);
    refreshMainConfig();
  }
  def loadModules(){
    moduleRefinedRelocation = Loader.isModLoaded("RefinedRelocation");
    moduleRefinedRelocation = moduleRefinedRelocation  && configMain.get("modules", "Refined Relocation", true).getBoolean(true);
    moduleInventoryTools = Loader.isModLoaded("InventoryTools");
    moduleInventoryTools = moduleInventoryTools  && configMain.get("modules", "Inventory Tools", true).getBoolean(true);
  }
  def refreshMainConfig(){
    LogHelperEI.Info("Refreshing Main Config")
    ROWS = configMain.getInt("MaxRows", "Gui", ROWS, 6, 24, "The maximum number of rows");
    COLOMNS = configMain.getInt("MaxColomns", "Gui", COLOMNS, 9, 24, "The maximum number of columns");
    autoAssemblerTickRate=configMain.getInt("TickRate", "AutoAssembler", autoAssemblerTickRate, 1, 200, "How often will the auto assembler craft an item in ticks?");
    
    if(configMain.hasChanged())
      configMain.save();
    
    MAX_SIZE = ROWS*COLOMNS;
  }

}