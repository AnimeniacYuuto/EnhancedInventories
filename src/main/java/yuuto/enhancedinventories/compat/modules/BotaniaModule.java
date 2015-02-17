package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class BotaniaModule {
	
	public static final void init(){
		WoodTypes.addWoodType(new WoodType("LIVINGWOOD", "Botania", "livingwood", 1));
		WoodTypes.addWoodType(new WoodType("MOSSYLIVINGWOOD", "Botania", "livingwood", 2));
		WoodTypes.addWoodType(new WoodType("DREAMWOOD", "Botania", "dreamwood", 1));
		WoodTypes.addWoodType(new WoodType("MOSSYDREAMWOOD", "Botania", "dreamwood", 2));
	}
	private BotaniaModule(){}

}
