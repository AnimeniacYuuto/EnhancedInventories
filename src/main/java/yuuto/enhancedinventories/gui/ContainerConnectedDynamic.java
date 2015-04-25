package yuuto.enhancedinventories.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import yuuto.enhancedinventories.proxy.ConfigHandler;
import yuuto.yuutolib.gui.ContainerAlt;

public class ContainerConnectedDynamic extends ContainerAlt implements IConnectedContainer{

	static int centerXSize = 162;
	int maxDragY;
	
	public ContainerConnectedDynamic(IInventory inventory,
			EntityPlayer player) {
		super(inventory, player.inventory);
		inventory.openInventory();
	}

	@Override
	protected int[] bindInventorySlots() {
		int rows;
		int columns;
		if(inventory.getSizeInventory() < ConfigHandler.MAX_SIZE){
			if(inventory.getSizeInventory() <= 9){
				rows = 1;
				columns = inventory.getSizeInventory();
			}else if(inventory.getSizeInventory() <= 54){
				columns = 9;
				rows = (int)Math.ceil(inventory.getSizeInventory()/9f);
			}else{
				columns = (int)Math.min(Math.ceil(inventory.getSizeInventory()/6f), ConfigHandler.COLOMNS);
				rows = (int)Math.min(Math.ceil(inventory.getSizeInventory()/((float)columns)), ConfigHandler.COLOMNS);
			}
		}else{
			rows = ConfigHandler.ROWS;
			columns = ConfigHandler.COLOMNS;
		}
		int[] ret = new int[2];
		ret[0] = 14+(18*columns);
		ret[1] = 17+15+(18*rows);
		if(inventory.getSizeInventory() > ConfigHandler.MAX_SIZE){
			ret[0]+=18;
		}
		ret[0] = ((ret[0] - centerXSize)/2)+1;
		
		maxDragY = ret[1] - 15;
		
		int maxY = (int) Math.ceil(inventory.getSizeInventory()/(float)columns);
		for(int y = 0; y < maxY; y++){
			for(int x = 0; x < columns; x++){
				int i = x+y*columns;
				if(i >= inventory.getSizeInventory()){
					return ret;
				}
				this.addSlotToContainer(new Slot(inventory, x+y*columns, 8+x*18, 18+y*18));
			}
		}
		return ret;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		if(player != playerInventory.player)
			return;
		inventory.closeInventory();
	}
	
	@Override
	public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_)
    {
        return p_94530_2_.yDisplayPosition > 17;
    }

    /**
     * Returns true if the player can "drag-spilt" items into this slot,. returns true by default. Called to
     * check if the slot can be added to a list of Slots to split the held ItemStack across.
     */
    @Override
	public boolean canDragIntoSlot(Slot p_94531_1_)
    {
        return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 17 && p_94531_1_.yDisplayPosition <= maxDragY;
    }

}
