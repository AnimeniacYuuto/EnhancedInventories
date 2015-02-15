package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.compat.nei.RecipeFunctionUpgradesHandler;
import yuuto.enhancedinventories.compat.nei.RecipeStoneCraftingHandler;
import codechicken.nei.api.API;

public final class NEIModule {
	
	public static final void init(){
		RecipeStoneCraftingHandler recipeHandler = new RecipeStoneCraftingHandler();
		RecipeFunctionUpgradesHandler upgradeHandler = new RecipeFunctionUpgradesHandler();
		API.registerRecipeHandler(recipeHandler);
		API.registerUsageHandler(recipeHandler);
		API.registerRecipeHandler(upgradeHandler);
		API.registerUsageHandler(upgradeHandler);
	}
	private NEIModule(){}

}
