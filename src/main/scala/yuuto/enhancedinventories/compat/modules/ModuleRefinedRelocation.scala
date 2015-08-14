/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.modules

import yuuto.enhancedinventories.proxy.ProxyCommon
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.oredict.OreDictionary
import yuuto.enhancedinventories.client.renderer.ItemImprovedChestRenderer
import yuuto.enhancedinventories.client.renderer.ItemRendererCabinet
import yuuto.enhancedinventories.client.renderer.ItemRendererLocker
import yuuto.enhancedinventories.materials.ETier
import yuuto.enhancedinventories.compat.refinedrelocation.BlockSortingCabinet
import yuuto.enhancedinventories.compat.refinedrelocation.BlockSortingImprovedChest
import yuuto.enhancedinventories.compat.refinedrelocation.BlockSortingLocker
import yuuto.enhancedinventories.compat.refinedrelocation.RecipeSorting
import yuuto.enhancedinventories.compat.refinedrelocation.TileSortingCabinet
import yuuto.enhancedinventories.compat.refinedrelocation.TileSortingImprovedChest
import yuuto.enhancedinventories.compat.refinedrelocation.TileSortingLocker
import yuuto.enhancedinventories.item.ItemBlockCabinet
import yuuto.enhancedinventories.item.ItemBlockImprovedChest
import yuuto.enhancedinventories.item.ItemBlockLocker
import yuuto.enhancedinventories.materials.PaintHelper
import com.dynious.refinedrelocation.api.ModObjects
import cpw.mods.fml.common.registry.GameRegistry
import yuuto.enhancedinventories.config.json.JsonRecipeFactory
import yuuto.enhancedinventories.config.recipe.RecipeDecorative
import yuuto.enhancedinventories.config.json.RecipeConfigDecorative
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.config.recipe.RecipeAlt
import net.minecraftforge.oredict.RecipeSorter
import net.minecraftforge.oredict.RecipeSorter.Category

object ModuleRefinedRelocation {
  
  var sortingImprovedChest:Block = null;
  var sortingLocker:Block=null;
  var sortingCabinet:Block=null;
  
  def preInit(){   
    sortingImprovedChest = new BlockSortingImprovedChest("sortingImprovedChest");
    sortingLocker = new BlockSortingLocker("sortingLocker");
    sortingCabinet = new BlockSortingCabinet("sortingCabinet");
    
    GameRegistry.registerBlock(sortingImprovedChest, classOf[ItemBlockImprovedChest], "improvedSortingChest");
    GameRegistry.registerBlock(sortingLocker, classOf[ItemBlockLocker], "sortingLocker");
    GameRegistry.registerBlock(sortingCabinet, classOf[ItemBlockCabinet], "sortingCabinet");
    
    GameRegistry.registerTileEntity(classOf[TileSortingImprovedChest], "container.ImprovedChests:ImprovedSortingChest");
    GameRegistry.registerTileEntity(classOf[TileSortingLocker], "container.ImprovedChests:SortingLocker");
    GameRegistry.registerTileEntity(classOf[TileSortingCabinet], "tileSortingCabinet");
  }
  def preInitClient(){
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(sortingImprovedChest), ItemImprovedChestRenderer.instance);
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(sortingLocker), ItemRendererLocker.instance);
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(sortingCabinet), ItemRendererCabinet.instance);
  }
  def initRecipes(factory:JsonRecipeFactory) {
    RecipeSorter.register("Yuuto.EI.sorting", classOf[RecipeSorting], Category.SHAPED, "after:forge:shapedore before:minecraft:shapeless");
    factory.recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(sortingImprovedChest), 1, 7);
    factory.recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(sortingLocker), 1, 7);
    factory.recipeDecorativeConfigs+=new RecipeConfigDecorative(Item.getItemFromBlock(sortingCabinet), 1, 7);
    factory.recipeDecorativeFiles+=("ImprovedChest", "Locker", "Cabinet");
    PaintHelper.addPaintableStack(new ItemStack(sortingImprovedChest, 1, OreDictionary.WILDCARD_VALUE), true);
    PaintHelper.addPaintableStack(new ItemStack(sortingLocker, 1, OreDictionary.WILDCARD_VALUE), false);
    PaintHelper.addPaintableStack(new ItemStack(sortingCabinet, 1, OreDictionary.WILDCARD_VALUE), true);
  }
  def loadOtherRecipes(){
    for(i<-0 until 8){
       if(EIConfiguration.canCraftAlts){
         GameRegistry.addRecipe(new RecipeAlt(new ItemStack(sortingImprovedChest, 1, i), new ItemStack(sortingImprovedChest, 1, i)));
         GameRegistry.addRecipe(new RecipeAlt(new ItemStack(sortingLocker, 1, i), new ItemStack(sortingLocker, 1, i)));
         GameRegistry.addRecipe(new RecipeAlt(new ItemStack(sortingCabinet, 1, i), new ItemStack(sortingCabinet, 1, i)));
       }
       GameRegistry.addRecipe(new RecipeSorting(new ItemStack(sortingImprovedChest, 1, i), new ItemStack(ProxyCommon.blockImprovedChest, 1, i), ModObjects.sortingUpgrade));
       GameRegistry.addRecipe(new RecipeSorting(new ItemStack(sortingLocker, 1, i), new ItemStack(ProxyCommon.blockLocker, 1, i), ModObjects.sortingUpgrade));
       GameRegistry.addRecipe(new RecipeSorting(new ItemStack(sortingCabinet, 1, i), new ItemStack(ProxyCommon.blockCabinet, 1, i), ModObjects.sortingUpgrade));
    }
  }

}