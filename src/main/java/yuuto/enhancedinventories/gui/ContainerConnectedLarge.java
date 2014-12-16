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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;
import yuuto.yuutolib.gui.ContainerAlt;

public class ContainerConnectedLarge extends ContainerAlt implements IConnectedContainer{

	public ContainerConnectedLarge(TileConnectiveInventory inventory,
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

}
