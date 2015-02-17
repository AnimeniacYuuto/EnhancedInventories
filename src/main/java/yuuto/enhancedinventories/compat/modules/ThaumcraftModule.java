package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class ThaumcraftModule {
	
	public static final void init(){
		WoodTypes.addWoodType(new WoodType("GREATWOOD", "Thaumcraft", "blockWoodenDevice", 6));
		WoodTypes.addWoodType(new WoodType("SILVERWOOD", "Thaumcraft", "blockWoodenDevice", 7));
	}
	private ThaumcraftModule(){}

}
