package yuuto.enhancedinventories.materials.indentifire;

import net.minecraft.item.ItemStack;

public class ItemIdentifier implements IIdentifier{

	ItemStack stack;
	
	public ItemIdentifier(ItemStack stack){
		this.stack = stack;
	}
	
	@Override
	public boolean matches(ItemStack stack) {
		if(this.stack.getItem() != stack.getItem())
			return false;
		if(this.stack.getItemDamage() != stack.getItemDamage())
			return false;
		if(this.stack.hasTagCompound()){
			if(!stack.hasTagCompound())
				return false;
			if(!ItemStack.areItemStackTagsEqual(this.stack, stack))
				return false;
		}
		else if(stack.hasTagCompound())
			return false;
		return true;
	}

}
