/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei

import java.util.ArrayList
import java.util.List
import codechicken.nei.PositionedStack
import codechicken.nei.NEIClientConfig
import yuuto.enhancedinventories.config.recipe.RecipeDecorative
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraftforge.oredict.ShapedOreRecipe
import codechicken.nei.recipe.TemplateRecipeHandler
import net.minecraft.inventory.Container
import yuuto.enhancedinventories.compat.nei.positionstacks.ParentPositionStack
import yuuto.enhancedinventories.compat.nei.positionstacks.ChildPositionStack
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect
import java.awt.Rectangle
import net.minecraft.client.gui.inventory.GuiCrafting
import codechicken.nei.NEIClientUtils
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.CraftingManager
import codechicken.nei.NEIServerUtils
import net.minecraft.item.ItemStack
import codechicken.nei.api.DefaultOverlayRenderer
import codechicken.nei.recipe.RecipeInfo
import codechicken.nei.api.IRecipeOverlayRenderer
import codechicken.nei.api.IOverlayHandler
import codechicken.nei.api.IStackPositioner

class EIShapedHandlerBasic extends EIRecipeHandlerTemplate{
    class CachedEIShapedRecipe(recipe:RecipeDecorative) extends CachedRecipe{
        val ingredients:ArrayList[PositionedStack]=new ArrayList[PositionedStack]();
        val result:PositionedStack=new PositionedStack(recipe.getRecipeOutput(), 119, 24);
        var core:ParentPositionStack=null;
        var color:ParentPositionStack=null;
        setIngredients(recipe);

        /**
         * @param width
         * @param height
         * @param items  an ItemStack[] or ItemStack[][]
         */
        def setIngredients(recipe:RecipeDecorative) {
            for (x <-0 until recipe.width) {
                for (y <-0 until recipe.height) {
                  val i:Int=y * recipe.width + x
                  if (recipe.getInput()(i) == null){}
                  else{
                    if(recipe.cores.contains(i)){
                      var stack:PositionedStack=null;
                      if(core==null){
                        core=new ParentPositionStack(recipe.getInput()(i), 25 + x * 18, 6 + y * 18, false);
                        stack=core;
                      }else{
                        stack=new ChildPositionStack(core, 25 + x * 18, 6 + y * 18);
                      }
                      stack.setMaxSize(1);
                      ingredients.add(stack);
                    }else if(recipe.colors.contains(i)){
                      var stack:PositionedStack=null;
                      if(color==null){
                        color=new ParentPositionStack(recipe.getInput()(i), 25 + x * 18, 6 + y * 18, false);
                        stack=color;
                      }else{
                        stack=new ChildPositionStack(color, 25 + x * 18, 6 + y * 18);
                      }
                      stack.setMaxSize(1);
                      ingredients.add(stack);
                    }else{
                      val stack:PositionedStack = new PositionedStack(recipe.getInput()(i), 25 + x * 18, 6 + y * 18, false);
                      stack.setMaxSize(1);
                      ingredients.add(stack);
                    }
                  }
                }
            }
        }

        override def getIngredients():List[PositionedStack]={
            return getCycledIngredients(cycleticks / 20, ingredients);
        }
        override def getCycledIngredients(cycle:Int, ingredients:List[PositionedStack]):List[PositionedStack]={
            for (itemIndex <-0 until ingredients.size() if(!ingredients.get(itemIndex).isInstanceOf[ChildPositionStack]))
                randomRenderPermutation(ingredients.get(itemIndex), cycle + itemIndex);
            return ingredients;
        }

        def getResult():PositionedStack=result;

        def computeVisuals() {
          val itr=ingredients.iterator();
          while(itr.hasNext()){
            itr.next().generatePermutations();
          }
        }
    }

    override def loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
    }

    override def getGuiClass():Class[_ <: GuiContainer]=classOf[GuiCrafting];

    override def getRecipeName():String=NEIClientUtils.translate("recipe.shaped");

    override def loadCraftingRecipesFromArray(outputId:String, results:Array[Object]):Boolean={
        if (outputId.equals("crafting") && getClass() == classOf[EIShapedHandlerBasic]) {
            val itr=CraftingManager.getInstance().getRecipeList().asInstanceOf[List[IRecipe]].iterator();
            while (itr.hasNext()) {
              val irecipe=itr.next();
              var recipe:CachedEIShapedRecipe = null;
              if (irecipe.isInstanceOf[RecipeDecorative])
                  recipe = getCachedRecipe(irecipe.asInstanceOf[RecipeDecorative]);

              if (recipe == null){}
              else{
                recipe.computeVisuals();
                arecipes.add(recipe);
              }
            }
            return true;
        } else {
            return false;
        }
    }

    override def loadCraftingRecipes(result:ItemStack) {
      val itr=CraftingManager.getInstance().getRecipeList().asInstanceOf[List[IRecipe]].iterator();
      while (itr.hasNext()) {
        val irecipe=itr.next();
        if(irecipe.isInstanceOf[RecipeDecorative]){
          if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
              val recipe = getCachedRecipe(irecipe.asInstanceOf[RecipeDecorative]);
              if(recipe!=null){
                recipe.computeVisuals();
                arecipes.add(recipe);
              }
          }
        }
      }
    }

    override def loadUsageRecipes(ingredient:ItemStack) {
      if(ingredient==null)
        return;
      val itr=CraftingManager.getInstance().getRecipeList().asInstanceOf[List[IRecipe]].iterator();
      while (itr.hasNext()) {
        val irecipe=itr.next();
        if(irecipe != null && irecipe.isInstanceOf[RecipeDecorative]){
          val recipe = getCachedRecipe(irecipe.asInstanceOf[RecipeDecorative]);
          if(recipe==null){}
          else if (!recipe.contains(recipe.ingredients, ingredient.getItem())){}
          else{
            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
          }
        }
      }
    }
    
    def getCachedRecipe(recipe:RecipeDecorative):CachedEIShapedRecipe={
      val inputs:Array[Object]=recipe.getInput();
      for(inpt<-inputs){
        if(inpt.isInstanceOf[ArrayList[_]] && inpt.asInstanceOf[ArrayList[_]].isEmpty()){
          return null;
        }
      }
      return new CachedEIShapedRecipe(recipe);
    }

    override def getGuiTexture():String="textures/gui/container/crafting_table.png";

    override def getOverlayIdentifier():String="crafting";

    override def hasOverlay(gui:GuiContainer, container:Container, recipe:Int):Boolean={
        return super.hasOverlay(gui, container, recipe) ||
                isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
    }

    override def getOverlayRenderer(gui:GuiContainer, recipe:Int):IRecipeOverlayRenderer={
        val renderer:IRecipeOverlayRenderer = super.getOverlayRenderer(gui, recipe);
        if (renderer != null)
            return renderer;

        val positioner:IStackPositioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
        if (positioner == null)
            return null;
        return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
    }

    override def getOverlayHandler(gui:GuiContainer, recipe:Int):IOverlayHandler={
        val handler:IOverlayHandler = super.getOverlayHandler(gui, recipe);
        if (handler != null)
            return handler;

        return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
    }

    def isRecipe2x2(recipe:Int):Boolean={
      val itr=getIngredientStacks(recipe).iterator();
      while(itr.hasNext()){//  for (PositionedStack stack : getIngredientStacks(recipe))
        val stack:PositionedStack=itr.next();
        if (stack.relx > 43 || stack.rely > 24)
            return false;
      }
      return true;
    }
}