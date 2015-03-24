package yuuto.yuutolib;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public interface IMod {
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event);
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event);
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event);

}
