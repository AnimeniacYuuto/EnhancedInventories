/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.recipe

import yuuto.enhancedinventories.config.json.JsonRecipeFactory
import yuuto.enhancedinventories.materials.ETier
import yuuto.enhancedinventories.util.LogHelperEI
import yuuto.enhancedinventories.config.EIConfiguration
import cpw.mods.fml.common.registry.GameRegistry
import yuuto.enhancedinventories.proxy.ProxyCommon
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.RecipeSorter.Category
import yuuto.enhancedinventories.materials.PaintHelper
import net.minecraftforge.oredict.OreDictionary

class RecipeFactory {
  var recipeLoader:JsonRecipeFactory=null;
  def init(){
    recipeLoader=new JsonRecipeFactory();
    recipeLoader.init();
    RecipeSorter.register("Yuuto.EI.decorative", classOf[RecipeDecorative], Category.SHAPED, "after:forge:shapedore before:minecraft:shapeless");
    RecipeSorter.register("Yuuto.EI.alternate", classOf[RecipeAlt], Category.SHAPED, "after:forge:shapelessore");
    PaintHelper.addPaintableStack(new ItemStack(ProxyCommon.blockImprovedChest, 1, OreDictionary.WILDCARD_VALUE), true);
    PaintHelper.addPaintableStack(new ItemStack(ProxyCommon.blockLocker, 1, OreDictionary.WILDCARD_VALUE), false);
    PaintHelper.addPaintableStack(new ItemStack(ProxyCommon.blockCabinet, 1, OreDictionary.WILDCARD_VALUE), true);
    PaintHelper.addPaintableStack(new ItemStack(ProxyCommon.chestConverter, 1, OreDictionary.WILDCARD_VALUE), true);
  }
  def readRecipes(){
    recipeLoader.loadRecipes();
  }
  def loadRecipes(){
    val recipeConfigs=recipeLoader.recipeDecorativeConfigs;
    for(recipe<-recipeLoader.genericRecipes.recipes){
      recipe.registerRecipe();
    }
    for(i<-0 until 8){
      for(o<-ETier.values()(i).getChestMaterials()){
        for(config<-recipeConfigs){
          if(i < config.minTier+config.offest || i > config.maxTier+config.offest || config.recipes(i-config.offest)==null){}
          else{
            for(recipe<-config.recipes(i-config.offest)){
              recipe.addRecipe(o);
            }
          }
        }
      }
    }
    if(EIConfiguration.canCraftAlts){
      for(i<-0 until 8){
        GameRegistry.addRecipe(new RecipeAlt(new ItemStack(ProxyCommon.blockImprovedChest, 1, i), new ItemStack(ProxyCommon.blockImprovedChest, 1, i)));
        GameRegistry.addRecipe(new RecipeAlt(new ItemStack(ProxyCommon.blockLocker, 1, i), new ItemStack(ProxyCommon.blockLocker, 1, i)));
        GameRegistry.addRecipe(new RecipeAlt(new ItemStack(ProxyCommon.blockCabinet, 1, i), new ItemStack(ProxyCommon.blockCabinet, 1, i)));
        GameRegistry.addRecipe(new RecipeAlt(new ItemStack(ProxyCommon.chestConverter, 1, i), new ItemStack(ProxyCommon.chestConverter, 1, i)));
      }
    }
  }
  def clean(){
    
  }
}