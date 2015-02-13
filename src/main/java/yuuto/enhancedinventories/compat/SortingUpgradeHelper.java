package yuuto.enhancedinventories.compat;

import yuuto.enhancedinventories.tile.TileConnectiveInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SortingUpgradeHelper {
	
	public static Item getUpgradeItem(){
		return Items.gold_ingot;
	}
	public static boolean hasUpgradeMaterials(TileConnectiveInventory inv, EntityPlayer player){
		String s = inv.getType().getMaterial();
		int id = OreDictionary.getOreID(s);
		int cnt = 0;
		int i = 0;
		while(cnt < 2 && i < 36){
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null){
				int[] ids = OreDictionary.getOreIDs(stack);
				for(int j = 0; j < ids.length; j++){
					if(ids[j] == id){
						cnt+= stack.stackSize;
						if(cnt >= 2)
							break;
					}
				}
			}
			i++;
		}
		return cnt >= 2;
	}
	public static void removeUpgradeMaterials(TileConnectiveInventory inv, EntityPlayer player){
		String s = inv.getType().getMaterial();
		int id = OreDictionary.getOreID(s);
		int cnt = 0;
		int i = 0;
		while(cnt < 2 && i < 36){
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(stack != null){
				int[] ids = OreDictionary.getOreIDs(stack);
				for(int j = 0; j < ids.length; j++){
					if(ids[j] == id){
						cnt += player.inventory.decrStackSize(i, 2).stackSize;
						if(cnt >= 2){
							player.inventoryContainer.detectAndSendChanges();
							return;
						}
					}
				}
			}
			i++;
		}
		player.inventoryContainer.detectAndSendChanges();
	}

}
