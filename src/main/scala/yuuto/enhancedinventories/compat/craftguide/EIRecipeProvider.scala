package yuuto.enhancedinventories.compat.craftguide

import java.util;
import net.minecraft.item.crafting.{CraftingManager, IRecipe}
import uristqwerty.CraftGuide.{RecipeGeneratorImplementation, DefaultRecipeTemplate}
import uristqwerty.CraftGuide.api.{RecipeGenerator, RecipeProvider, CraftGuideAPIObject}
import uristqwerty.gui_craftguide.texture.{DynamicTexture, TextureClip}
import yuuto.enhancedinventories.config.recipe.RecipeDecorative

/**
 * Created by Yuuto on 10/11/2015.
 */
object EIRecipeProvider extends CraftGuideAPIObject with RecipeProvider{

  def init(){};

  override def generateRecipes(generator: RecipeGenerator){
    val list:util.List[IRecipe] = CraftingManager.getInstance.getRecipeList.asInstanceOf[util.List[IRecipe]];
    val slotCrafting=new DefaultRecipeTemplate(UtilEI.craftingSlots, RecipeGeneratorImplementation.workbench, new TextureClip(DynamicTexture.instance("recipe_backgrounds"), 1, 1, 79, 58), new TextureClip(DynamicTexture.instance("recipe_backgrounds"), 82, 1, 79, 58));
    val slotCraftingSmall=new DefaultRecipeTemplate(UtilEI.smallCraftingSlots, RecipeGeneratorImplementation.workbench, new TextureClip(DynamicTexture.instance("recipe_backgrounds"), 1, 61, 79, 58), new TextureClip(DynamicTexture.instance("recipe_backgrounds"), 82, 61, 79, 58));

    val itr:util.Iterator[IRecipe]=list.iterator();
    while (itr.hasNext){
      val rTest:IRecipe=itr.next();
      if(rTest.isInstanceOf[RecipeDecorative]) {
        val r:RecipeDecorative=rTest.asInstanceOf[RecipeDecorative];
        if (r.width < 3 && r.height < 3) {
          generator.addRecipe(slotCraftingSmall, UtilEI.getSmallShapedRecipe(r.width, r.height, r.getInput(), r.getRecipeOutput()));
        } else {
          generator.addRecipe(slotCrafting, UtilEI.getCraftingShapedRecipe(r.width, r.height, r.getInput(), r.getRecipeOutput()));
        }
      }
    }
  }
}
