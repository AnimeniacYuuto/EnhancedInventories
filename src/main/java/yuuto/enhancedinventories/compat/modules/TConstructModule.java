package yuuto.enhancedinventories.compat.modules;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public final class TConstructModule {
	
	public static final void init(){
		ItemStack ingot = GameRegistry.findItemStack("TConstruct", "materials", 1);
		
		ItemStack 
		Alumite = ingot.copy(),
		Cobalt = ingot.copy(),
		Ardite = ingot.copy(),
		Manyullyn = ingot.copy();
		
		Alumite.setItemDamage(15);
		Cobalt.setItemDamage(3);
		Ardite.setItemDamage(4);
		Manyullyn.setItemDamage(5);
		
		OreDictionary.registerOre("ingotAlumite", Alumite);
		OreDictionary.registerOre("ingotCobalt", Cobalt);
		OreDictionary.registerOre("ingotArdite", Ardite);
		OreDictionary.registerOre("ingotManyullyn", Manyullyn);
	}
	private TConstructModule(){}

}
