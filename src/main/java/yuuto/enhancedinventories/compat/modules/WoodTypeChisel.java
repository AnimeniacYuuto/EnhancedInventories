package yuuto.enhancedinventories.compat.modules;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import yuuto.enhancedinventories.WoodType;

public class WoodTypeChisel extends WoodType{

	public WoodTypeChisel(String name, String modId, String itemId, int meta) {
		super(name, modId, itemId, meta);
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List results, boolean bool){
		ItemStack planks = getPlanksStack();
		results.add(planks.getDisplayName());
		planks.getItem().addInformation(planks, player, results, bool);
	}

}
