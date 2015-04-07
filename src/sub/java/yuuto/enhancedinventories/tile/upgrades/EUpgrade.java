package yuuto.enhancedinventories.tile.upgrades;

import net.minecraft.item.ItemStack;

public enum EUpgrade {
	Unknown, Reinforced, Security, Sorting, Redstone, Hopper, Alternate,
	Color, Appearence, Tier;
	
	ItemStack drop;
	
	public void setDrop(ItemStack stack){
		drop = stack;
	}
	public ItemStack getDrop(){
		return drop;
	};
	
	public static EUpgrade get(int i){
		if(i < 1 || i >= values().length)
			return Unknown;
		return values()[i];
	}

}
