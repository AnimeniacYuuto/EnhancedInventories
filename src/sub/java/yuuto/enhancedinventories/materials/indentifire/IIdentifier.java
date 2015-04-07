package yuuto.enhancedinventories.materials.indentifire;

import net.minecraft.item.ItemStack;

public interface IIdentifier {
	
	public boolean matches(ItemStack stack);

}
