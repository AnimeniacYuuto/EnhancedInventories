package yuuto.enhancedinventories.compat

import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.config.recipe.RecipeFactory
import yuuto.enhancedinventories.config.json.JsonRecipeFactory
import yuuto.enhancedinventories.config.json.JsonFrameFactory

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
    recipefactory.readRecipes();
    recipefactory.loadRecipes();
    if(EIConfiguration.moduleRefinedRelocation){
      ModuleRefinedRelocation.loadOtherRecipes();
    }
  }

}