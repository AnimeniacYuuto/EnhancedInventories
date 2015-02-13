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
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import yuuto.enhancedinventories.gui.ContainerConnectedLarge;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;

public class GuiContainerConnectedLarge extends GuiContainer{

	static ResourceLocation texture = new ResourceLocation("enhancedinventories","textures/gui/scrolling_54.png");
	static int minScroll = 18;
	static int maxScroll = 105;
	static int scrollButtonWidth = 18;
	static int scrollButtonHeight = 19;
	int scroll = minScroll;
	int previousY = 0;
	boolean scrolling = false;
	ContainerConnectedLarge container;
	public GuiContainerConnectedLarge(TileConnectiveInventory tile, EntityPlayer player) {
		super(new ContainerConnectedLarge(tile, player));
		container = (ContainerConnectedLarge)this.inventorySlots;
		this.ySize = 222;
		this.xSize = 194;
	}
	
	@Override
	public void initGui(){
		super.initGui();
		updateSlots();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mX, int mY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int mX,
			int mY) {
		//draw your Gui here, only thing you need to change is the path
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        this.drawTexturedModalRect(x+170, y+scroll, xSize, 0, scrollButtonWidth, scrollButtonHeight);
	}
	int getStartingRow(){
		int maxRows = (int)Math.ceil(container.playerInvStart/9d);
		maxRows -= 6;
		double precent = ((double)scroll - minScroll)/((double)maxScroll-minScroll);
		int row = (int)Math.round(maxRows*precent);
		return row;
	}

	@Override
	protected void mouseClicked(int mX, int mY, int button){
		if(scrolling)
			return;
		if(button != 0){
			super.mouseClicked(mX, mY, button);
			return;
		}
		int adjMX = mX-this.guiLeft;
		int adjMY = mY-this.guiTop;
		if(adjMX < 170 || adjMX > 170+scrollButtonWidth){
			super.mouseClicked(mX, mY, button);
			return;
		}
		if(adjMY < scroll || adjMY > scroll+scrollButtonHeight){
			super.mouseClicked(mX, mY, button);
			return;
		}
		previousY = adjMY;
		scrolling = true;
	}
	
	@Override
	protected void mouseClickMove(int mX, int mY, int button, long time){
		if(button != 0 || !scrolling){
			super.mouseClickMove(mX, mY, button, time);
			return;
		}
		int adjMY = mY-this.guiTop;
		if((previousY <= minScroll && adjMY <= minScroll) ||
				(previousY >= maxScroll && adjMY >= maxScroll+scrollButtonHeight)){
			return;
		}
		if(previousY < minScroll)
			scroll += adjMY-minScroll;
		else if(previousY > maxScroll)
			scroll += adjMY-maxScroll;
		else
			scroll += adjMY-previousY;
		
		if(scroll < minScroll)
			scroll = minScroll;
		else if(scroll > maxScroll)
			scroll = maxScroll;
		
		updateSlots();
		
		previousY = adjMY;
    }
	
	public void updateSlots(){
		int startSlot = getStartingRow()*9;
		int endSlot = startSlot + 54;
		int x = 0;
		int y = 18;
		for(int i = 0; i < container.playerInvStart; i++){
			if(i < startSlot || i >= endSlot){
				((Slot)container.inventorySlots.get(i)).yDisplayPosition = -(height*2);
				continue;
			}
			((Slot)container.inventorySlots.get(i)).yDisplayPosition = y;
			x++;
			if(x >= 9){
				x=0;
				y+=18;
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int mX, int mY, int which){
		if(which != 0 || !scrolling){
			super.mouseMovedOrUp(mX, mY, which);
			return;
		}
		scrolling = false;
		previousY = scroll;
    }
}
