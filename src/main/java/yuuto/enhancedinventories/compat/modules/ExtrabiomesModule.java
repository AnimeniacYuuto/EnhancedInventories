package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class ExtrabiomesModule {
	
	public final static void init(){
		WoodTypes.addWoodType(new WoodType("REDWOOD", "ExtrabiomesXL", "planks", 0));
		WoodTypes.addWoodType(new WoodType("FIR", "ExtrabiomesXL", "planks", 1));
		WoodTypes.addWoodType(new WoodType("ACACIA", "ExtrabiomesXL", "planks", 2));
		WoodTypes.addWoodType(new WoodType("CYPRESS", "ExtrabiomesXL", "planks", 3));
		WoodTypes.addWoodType(new WoodType("JAPANESEMAPLE", "ExtrabiomesXL", "planks", 4));
		WoodTypes.addWoodType(new WoodType("RAINBOWEUCALYPTUS", "ExtrabiomesXL", "planks", 5));
		WoodTypes.addWoodType(new WoodType("AUTUMN", "ExtrabiomesXL", "planks", 6));
		WoodTypes.addWoodType(new WoodType("BALDCYPRESS", "ExtrabiomesXL", "planks", 7));
		WoodTypes.addWoodType(new WoodType("SAKURA", "ExtrabiomesXL", "planks", 8));
	}
	private ExtrabiomesModule(){}

}
