package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class NaturaModule {
	
	public static final void init(){
		WoodTypes.addWoodType(new WoodType("EUCALYPTUS","Natura", "planks", 0));
		WoodTypes.addWoodType(new WoodType("SAKURA","Natura", "planks", 1));
		WoodTypes.addWoodType(new WoodType("GHOSTWOOD","Natura", "planks", 2));
		WoodTypes.addWoodType(new WoodType("REDWOOD","Natura", "planks", 3));
		WoodTypes.addWoodType(new WoodType("BLOODWOOD","Natura", "planks", 4));
		WoodTypes.addWoodType(new WoodType("HOPSEED","Natura", "planks", 5));
		WoodTypes.addWoodType(new WoodType("MAPLE","Natura", "planks", 6));
		WoodTypes.addWoodType(new WoodType("SILVERBELL","Natura", "planks", 7));
		WoodTypes.addWoodType(new WoodType("AMARANTH","Natura", "planks", 8));
		WoodTypes.addWoodType(new WoodType("TIGERWOOD","Natura", "planks", 9));
		WoodTypes.addWoodType(new WoodType("WILLOW","Natura", "planks", 10));
		WoodTypes.addWoodType(new WoodType("DARKWOOD","Natura", "planks", 11));
		WoodTypes.addWoodType(new WoodType("FUSEWOOD","Natura", "planks", 12));
	}
	private NaturaModule(){}

}
