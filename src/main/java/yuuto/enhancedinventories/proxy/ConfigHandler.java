package yuuto.enhancedinventories.proxy;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	public static boolean chisel = false;
	public static boolean natura = false;
	public static boolean forestry = false;
	public static boolean extraTrees = false;
	
	public static void init(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		chisel = config.get("Modules", "Chisel", true).getBoolean(true);
		natura = config.get("Modules", "Natura", true).getBoolean(true);
		forestry = config.get("Modules", "Forestry", true).getBoolean(true);
		extraTrees = config.get("Modules", "Extra Trees", true).getBoolean(true);
		config.save();
	}

}
