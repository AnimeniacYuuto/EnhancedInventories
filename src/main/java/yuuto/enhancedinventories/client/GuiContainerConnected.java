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
package yuuto.enhancedinventories.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import yuuto.enhancedinventories.gui.ContainerConnected;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;

public class GuiContainerConnected extends GuiContainer{

	static ResourceLocation smallTexture = new ResourceLocation("enhancedinventories","textures/gui/generic_27.png");
	static ResourceLocation largeTexture = new ResourceLocation("textures/gui/container/generic_54.png");
	boolean large;
	public GuiContainerConnected(TileConnectiveInventory tile, EntityPlayer player) {
		super(new ContainerConnected(tile, player));
		if(tile.getSizeInventory() == 54){
			large = true;
			this.ySize = 222;
		}else{
			large = false;
			this.ySize = 168;
		}
		this.xSize = 176;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mX, int mY) {
		//GuiContainerManager.getManager().renderObjects(mX, mY);
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mX,
			int mY) {
		//draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(large)
        	this.mc.renderEngine.bindTexture(largeTexture);
        else
        	this.mc.renderEngine.bindTexture(smallTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
