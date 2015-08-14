package yuuto.enhancedinventories.compat.nei;

import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class EIRecipeHandlerTemplate extends TemplateRecipeHandler{

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
        if(!loadCraftingRecipesFromArray(outputId, results))
        	super.loadCraftingRecipes(outputId, results);
    }
	public abstract boolean loadCraftingRecipesFromArray(String outputId, Object[] results);
}
