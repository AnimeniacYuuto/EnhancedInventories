package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class ForestryModule {
	
	public static final void init(){
		registerWood("MAHOE", 1, 0);
		registerWood("POPLAR", 1, 1);
		registerWood("PALM", 1, 2);
		registerWood("PAPAYA", 1, 3);
		registerWood("PINE", 1, 4);
		registerWood("PLUM", 1, 5);
		registerWood("MAPLE", 1, 6);
		registerWood("CITRUS", 1, 7);
		registerWood("GIGANTEUM", 1, 8);
		registerWood("IPE", 1, 9);
		registerWood("PADAUK", 1, 10);
		registerWood("COCOBOLO", 1, 11);
		registerWood("ZEBRAWOOD", 1, 12);
		
		registerWood("LARCH", 0, 0);
		registerWood("TEAK", 0, 1);
		registerWood("ACACIA", 1, 2);
		registerWood("LIME", 0, 2);
		registerWood("CHESTNUT", 0, 4);
		registerWood("WENGE", 0, 5);
		registerWood("BAOBAB", 0, 6);
		registerWood("SEQUOIA", 0, 7);
		registerWood("KAPOK", 0, 8);
		registerWood("EBONY", 0, 9);
		registerWood("MAHOGANY", 0, 10);
		registerWood("BALSA", 0, 11);
		registerWood("WILLOW", 0, 12);
		registerWood("WALNUT", 0, 13);
		registerWood("GREENHEART", 0, 14);
		registerWood("CHERRY", 0, 15);
	}
	private static final void registerWood(String name, int subWood, int metaData){
		if(subWood == 0){
			WoodTypes.addWoodType(new WoodType(name, "Forestry", "planks", metaData));
			WoodTypes.addWoodType(new WoodType(name, "Forestry", "fireproofPlanks1", metaData));
		}else{
			WoodTypes.addWoodType(new WoodType(name, "Forestry", "planks2", metaData));
			WoodTypes.addWoodType(new WoodType(name, "Forestry", "fireproofPlanks2", metaData));
		}
	}
	private ForestryModule(){}

}
