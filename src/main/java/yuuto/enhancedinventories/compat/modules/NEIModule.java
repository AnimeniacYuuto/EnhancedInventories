package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.compat.nei.EnhancedShapedRecipeHandler;
import yuuto.enhancedinventories.compat.nei.EnhancedShapelessRecipeHandler;
import codechicken.nei.api.API;

public final class NEIModule {
	
	public static final void init(){
		EnhancedShapedRecipeHandler recipeHandler = new EnhancedShapedRecipeHandler();
		EnhancedShapelessRecipeHandler upgradeHandler = new EnhancedShapelessRecipeHandler();
		API.registerRecipeHandler(recipeHandler);
		API.registerUsageHandler(recipeHandler);
		API.registerRecipeHandler(upgradeHandler);
		API.registerUsageHandler(upgradeHandler);
	}
	private NEIModule(){}

}
