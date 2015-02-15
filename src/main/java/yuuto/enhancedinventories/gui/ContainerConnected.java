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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import yuuto.yuutolib.gui.ContainerAlt;

@ChestContainer()
@InventoryContainer(showOptions = true)
public class ContainerConnected extends ContainerAlt implements IConnectedContainer{

	public ContainerConnected(IInventory inventory,
			EntityPlayer player) {
		super(inventory, player.inventory);
		inventory.openInventory();
	}

	@Override
	protected int[] bindInventorySlots() {
		int maxY = inventory.getSizeInventory() == 27 ? 3 : 6;
		for(int y = 0; y < maxY; y++){
			for(int x = 0; x < 9; x++){
				this.addSlotToContainer(new Slot(inventory, x+y*9, 8+x*18, 18+y*18));
			}
		}
		return new int[]{8,32+maxY*18};
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		if(player != playerInventory.player)
			return;
		inventory.closeInventory();
	}

}
