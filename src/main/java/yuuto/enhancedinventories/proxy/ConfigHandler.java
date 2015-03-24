package yuuto.enhancedinventories.proxy;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	public static boolean chisel;
	public static boolean natura;
	public static boolean forestry;
	public static boolean extraTrees;
	public static boolean botania;
	public static boolean thaumcraft;
	public static boolean extrabiomes;
	public static boolean biomesOPlenty;
	
	public static boolean showUpgrades;
	public static boolean showTiers;
	
	public static void init(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		chisel = config.get("Modules", "Chisel", true).getBoolean(true);
		natura = config.get("Modules", "Natura", true).getBoolean(true);
		forestry = config.get("Modules", "Forestry", true).getBoolean(true);
		//extraTrees = config.get("Modules", "Extra Trees", true).getBoolean(true);
		extraTrees = config.getBoolean("ExtraTrees", "Modules", true, "Requires Forestry Module");
		botania = config.get("Modules", "Botania", true).getBoolean(true);
		thaumcraft = config.get("Modules", "Thaumcraft", true).getBoolean(true);
		extrabiomes = config.get("Modules", "Extrabiomes", true).getBoolean(true);
		biomesOPlenty = config.get("Modules", "BiomesOPlenty", true).getBoolean(true);
		
		
		showUpgrades = config.getBoolean("ShowUpgrades", "Creative", true, "Show an inventory with each of the upgrades per tier shown");
		showTiers = config.getBoolean("ShowTiers", "Creative", true, "Show each material that effects the size of the inventory");
		config.save();
	}

}
