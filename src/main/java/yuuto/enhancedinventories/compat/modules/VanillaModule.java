package yuuto.enhancedinventories.compat.modules;

import yuuto.enhancedinventories.WoodType;
import yuuto.enhancedinventories.WoodTypes;

public final class VanillaModule{
	public static final WoodType 
	OAK = new WoodType("OAK","minecraft", "planks", 0),
	SPRUCE = new WoodType("SPRUCE", "minecraft", "planks", 1),
	BIRCH = new WoodType("BIRCH", "minecraft", "planks", 2),
	JUNGLE = new WoodType("JUNGLE", "minecraft", "planks", 3),
	ACACIA = new WoodType("ACACIA", "minecraft", "planks", 4),
	DARK_OAK = new WoodType("DARK_OAK", "minecraft", "planks", 5);
	public static final void init() {
		WoodTypes.addWoodType(OAK);
		WoodTypes.addWoodType(SPRUCE);
		WoodTypes.addWoodType(BIRCH);
		WoodTypes.addWoodType(JUNGLE);
		WoodTypes.addWoodType(ACACIA);
		WoodTypes.addWoodType(DARK_OAK);
	}
	private VanillaModule(){}

}
