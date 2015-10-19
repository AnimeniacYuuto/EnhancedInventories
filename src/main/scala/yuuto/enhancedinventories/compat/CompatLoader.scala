package yuuto.enhancedinventories.compat

import cpw.mods.fml.common.Loader
import yuuto.enhancedinventories.compat.craftguide.{EIRecipeProvider}
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.config.recipe.RecipeFactory
import yuuto.enhancedinventories.config.json.JsonRecipeFactory
import yuuto.enhancedinventories.config.json.JsonFrameFactory
import yuuto.enhancedinventories.compat.modules.ModuleInventoryTools

object CompatLoader {
  
  def preInit(){
    if(EIConfiguration.moduleRefinedRelocation)
      ModuleRefinedRelocation.preInit();
  }
  def preInitClient(){
    if(EIConfiguration.moduleRefinedRelocation)
      ModuleRefinedRelocation.preInitClient();
  }
  def postInit(){
    val frameFactory:JsonFrameFactory=new JsonFrameFactory();
    val recipefactory:RecipeFactory=new RecipeFactory();
    frameFactory.loadFrames();
    recipefactory.init();
    if(EIConfiguration.moduleRefinedRelocation){
      ModuleRefinedRelocation.initRecipes(recipefactory.recipeLoader);
    }
    if(Loader.isModLoaded("craftguide")){
      EIRecipeProvider.init();
    }
    recipefactory.readRecipes();
    recipefactory.loadRecipes();
    if(EIConfiguration.moduleRefinedRelocation){
      ModuleRefinedRelocation.loadOtherRecipes();
    }
    if(EIConfiguration.moduleInventoryTools){
      ModuleInventoryTools.init();
    }
  }

}