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
	
	public static int ROWS = 9;
	public static int COLOMNS = 12;
	public static int MAX_SIZE;
	
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
		ROWS = config.getInt("MaxRows", "Misc", ROWS, 6, 24, "The maximum number of rows");
		COLOMNS = config.getInt("MaxColomns", "Misc", COLOMNS, 9, 24, "The maximum number of columns");
		config.save();
		
		MAX_SIZE = ROWS*COLOMNS;
	}

}
