package yuuto.yuutolib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerAlt extends Container{

	public IInventory inventory;
	public int playerInvStart;
	public int playerInvEnd;
	public InventoryPlayer playerInventory;
	
	public ContainerAlt(IInventory inventory, InventoryPlayer playerInventory){
		super();
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		int[] pos = bindInventorySlots();
		bindPlayerInventory(pos[0], pos[1]);
	}
	
	/**
	 * adds the slots from the inventory to the container
	 * @return the x, y pos of the player's inventory
	 */
	protected abstract int[] bindInventorySlots();
	protected void bindPlayerInventory(int x, int y) {
		playerInvStart = this.inventorySlots.size();
		for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                        addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9,
                                        x + j * 18, y + i * 18));
                }
        }

        for (int i = 0; i < 9; i++) {
                addSlotToContainer(new Slot(playerInventory, i, x + i * 18, y+58));
        }
        playerInvEnd = this.inventorySlots.size();
}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inventory.isUseableByPlayer(player);
	}
	
	@Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    //merges the item into player inventory since its in the tileEntity
                    if (slot < playerInvStart) {
                            if (!this.mergeItemStack(stackInSlot, playerInvStart, playerInvEnd, true)) {
                                    return null;
                            }
                    }
                    //places it into the tileEntity is possible since its in the player inventory
                    else if (!this.mergeItemStack(stackInSlot, 0, playerInvStart, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }

                    if (stackInSlot.stackSize == stack.stackSize) {
                            return null;
                    }
                    slotObject.onPickupFromSlot(player, stackInSlot);
            }
            return stack;
    }

}
