package yuuto.enhancedinventories.proxy;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {
	public static boolean chisel = false;
	
	public static void init(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		chisel = config.get("Modules", "Chisel", true).getBoolean(true);
		config.save();
	}

}
