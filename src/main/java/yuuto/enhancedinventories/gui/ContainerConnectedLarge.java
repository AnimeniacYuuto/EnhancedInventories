/*******************************************************************************
 * Copyright (c) 2014 Yuuto.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * 	   cpw - src reference from Iron Chests
 * 	   doku/Dokucraft staff - base chest texture
 *     Yuuto - initial API and implementation
 ******************************************************************************/
package yuuto.enhancedinventories.gui;


import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import yuuto.yuutolib.gui.ContainerAlt;

@ChestContainer()
@InventoryContainer(showOptions = true)
public class ContainerConnectedLarge extends ContainerAlt implements IConnectedContainer{

	public ContainerConnectedLarge(IInventory inventory,
			EntityPlayer player) {
		super(inventory, player.inventory);
		inventory.openInventory();
	}

	@Override
	protected int[] bindInventorySlots() {
		int maxY = (int) Math.ceil(inventory.getSizeInventory()/9d);
		for(int y = 0; y < maxY; y++){
			for(int x = 0; x < 9; x++){
				int i = x+y*9;
				if(i >= inventory.getSizeInventory())
					return new int[]{17,140};
				this.addSlotToContainer(new Slot(inventory, x+y*9, 8+x*18, 18+y*18));
			}
		}
		return new int[]{17,140};
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
        return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 17 && p_94531_1_.yDisplayPosition <= 124;
    }

}
