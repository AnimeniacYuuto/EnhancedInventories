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

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import yuuto.enhancedinventories.gui.ContainerConnected;
import yuuto.enhancedinventories.gui.ContainerConnectedLarge;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;
import yuuto.yuutolib.gui.GuiContainerAlt;

public class GuiContainerConnectedLarge extends GuiContainerAlt{

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
	protected int drawSlots(int mX, int mY, int k1){
    	int startSlot = getStartingRow()*9;
    	int offset = getStartingRow()*18;
    	for (int s = 0; s < 54; s++)
        {
            int i1 = startSlot+s;
    		if(i1 >= container.playerInvStart)
    			break;
    		Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
            this.drawSlotWithOffset(slot, offset);

            if (this.isMouseOverSlot(slot, mX, mY+offset) && slot.func_111238_b())
            {
                this.theSlot = slot;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot.xDisplayPosition;
                k1 = slot.yDisplayPosition-offset;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
		for (int i1 = container.playerInvStart; i1 < container.playerInvEnd; ++i1)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
            this.drawSlot(slot);

            if (this.isMouseOverSlot(slot, mX, mY) && slot.func_111238_b())
            {
                this.theSlot = slot;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot.xDisplayPosition;
                k1 = slot.yDisplayPosition;
                GL11.glColorMask(true, true, true, false);
                this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    	return k1;
    }
	protected void drawSlotWithOffset(Slot slot, int yOffest)
    {
        int i = slot.xDisplayPosition;
        int j = slot.yDisplayPosition-yOffest;
        ItemStack itemstack = slot.getStack();
        boolean flag = false;
        boolean flag1 = slot == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
        String s = null;

        if (slot == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null)
        {
            itemstack = itemstack.copy();
            itemstack.stackSize /= 2;
        }
        else if (this.field_147007_t && this.field_147008_s.contains(slot) && itemstack1 != null)
        {
            if (this.field_147008_s.size() == 1)
            {
                return;
            }

            if (Container.func_94527_a(slot, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slot))
            {
                itemstack = itemstack1.copy();
                flag = true;
                Container.func_94525_a(this.field_147008_s, this.field_146987_F, itemstack, slot.getStack() == null ? 0 : slot.getStack().stackSize);

                if (itemstack.stackSize > itemstack.getMaxStackSize())
                {
                    s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
                    itemstack.stackSize = itemstack.getMaxStackSize();
                }

                if (itemstack.stackSize > slot.getSlotStackLimit())
                {
                    s = EnumChatFormatting.YELLOW + "" + slot.getSlotStackLimit();
                    itemstack.stackSize = slot.getSlotStackLimit();
                }
            }
            else
            {
                this.field_147008_s.remove(slot);
                this.fixStack();
            }
        }

        this.zLevel = 100.0F;
        itemRender.zLevel = 100.0F;

        if (itemstack == null)
        {
            IIcon iicon = slot.getBackgroundIconIndex();

            if (iicon != null)
            {
                GL11.glDisable(GL11.GL_LIGHTING);
                this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
                this.drawTexturedModelRectFromIcon(i, j, iicon, 16, 16);
                GL11.glEnable(GL11.GL_LIGHTING);
                flag1 = true;
            }
        }

        if (!flag1)
        {
            if (flag)
            {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, i, j);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), itemstack, i, j, s);
        }

        itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
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
		
		previousY = adjMY;
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
	
	@Override
	protected Slot getSlotAtPosition(int x, int y)
    {
		int startSlot = getStartingRow()*9;
		int offset = getStartingRow()*18;
		int plStart = container.playerInvStart;
		for (int k = 0; k < 54; k++){
			int i1 = startSlot+k;
    		if(i1 >= plStart)
    			break;
    		Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
    		if (this.isMouseOverSlot(slot, x, y+offset))
            {
                return slot;
            }
        }
		for (int i1 = plStart; i1 < container.playerInvEnd; i1++){
			Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
    		if (this.isMouseOverSlot(slot, x, y))
            {
                return slot;
            }
		}

        return null;
    }
}
