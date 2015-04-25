package yuuto.enhancedinventories.client;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import yuuto.enhancedinventories.gui.ContainerDynamic;
import yuuto.enhancedinventories.proxy.ConfigHandler;

public class GuiContainerDynamic extends GuiContainer{

	static ResourceLocation topTexture = new ResourceLocation("enhancedinventories","textures/gui/dynamicTop.png");
	static ResourceLocation bottomTexture1 = new ResourceLocation("enhancedinventories","textures/gui/dynamicBottom1.png");
	static ResourceLocation bottomTexture2 = new ResourceLocation("enhancedinventories","textures/gui/dynamicBottom2.png");
	static int centerXSize = 162;
	static int bottomYSize = 97;
	
	static int minScroll = 18;
	static int scrollButtonWidth = 16;
	static int scrollButtonHeight = 19;
	
	int rows;
	int columns;
	
	boolean canScroll = false;
	int scroll = 18;
	int previousY = 0;
	boolean scrolling = false;
	int maxScroll = 105;
	
	ContainerDynamic container;
	
	public GuiContainerDynamic(IInventory tile, EntityPlayer player) {
		super(new ContainerDynamic(tile, player));
		container = (ContainerDynamic)this.inventorySlots;
		if(tile.getSizeInventory() < ConfigHandler.MAX_SIZE){
			if(tile.getSizeInventory() <= 9){
				rows = 1;
				columns = tile.getSizeInventory();
			}else if(tile.getSizeInventory() <= 54){
				columns = 9;
				rows = (int)Math.ceil(tile.getSizeInventory()/9f);
			}else{
				columns = (int)Math.min(Math.ceil(tile.getSizeInventory()/6f), ConfigHandler.COLOMNS);
				rows = (int)Math.min(Math.ceil(tile.getSizeInventory()/((float)columns)), ConfigHandler.COLOMNS);
			}
		}else{
			rows = ConfigHandler.ROWS;
			columns = ConfigHandler.COLOMNS;
		}
		
		this.xSize = 14+(18*columns);
		this.ySize = 17+97+(18*rows);
		if(tile.getSizeInventory() > ConfigHandler.MAX_SIZE){
			this.xSize+=18;
			canScroll = true;
			maxScroll = ySize - (bottomYSize+scrollButtonHeight+1);
		}
	}
	
	@Override
	public void initGui(){
		super.initGui();
		if(canScroll)
			updateSlots();
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
        this.mc.renderEngine.bindTexture(topTexture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        int x1 = 7+(18*Math.min(columns,12));
        int y1 = 17+(18*Math.min(rows,12));
        int x2 = 7+(18*Math.max(columns-12,0));
        int y2 = (18*Math.max(rows-12,0));
        int bSX1 = (xSize - centerXSize)/2;
        int bSX2 = 0;
        if(bSX1 > 250){
        	bSX2 = bSX1 -250;
        	bSX1 = 250;
        }
        //Draw top left
        this.drawTexturedModalRect(x, y, 0, 0, x1, y1);
        //Draw top right
        this.drawTexturedModalRect(x+x1, y, 230-x2, 0, x2, y1);
        if(canScroll)
        	this.drawTexturedModalRect(x+x1+x2-7, y, 230, 0, 25, y1);
        if(y2 > 0){
        	//Draw mid left
            this.drawTexturedModalRect(x, y+y1, 0, 17, x1, y2);
            //Draw mid right
            this.drawTexturedModalRect(x+x1, y+y1, 230-x2, 17, x2, y2);
            if(canScroll)
            	this.drawTexturedModalRect(x+x1+x2-7, y+y1, 230, 17, 25, y2);
        }
        if(canScroll){
        	this.drawTexturedModalRect(x+x1+x2-6, y+scroll, 0, 234, scrollButtonWidth, scrollButtonHeight);
        }
        this.mc.renderEngine.bindTexture(bottomTexture1);
        //Draw bottom left
        this.drawTexturedModalRect(x, y+y1+y2, 0, 118, bSX1, 97);
        this.drawTexturedModalRect(x+bSX1, y+y1+y2, 7, 118, bSX2, 97);
        //Draw bottom center
        this.drawTexturedModalRect(x+bSX1+bSX2, y+y1+y2, 0, 0, 162, 97);
        this.mc.renderEngine.bindTexture(bottomTexture2);
        //Draw bottom right
        this.drawTexturedModalRect(x+bSX1+bSX2+centerXSize, y+y1+y2, 0, 0,bSX2, 97);
        this.drawTexturedModalRect(x+bSX1+(2*bSX2)+centerXSize, y+y1+y2, 250-bSX1, 0,bSX1, 97);
	}
	
	int getStartingRow(){
		int maxRows = (int)Math.ceil(container.playerInvStart/(float)columns);
		maxRows -= rows;
		double precent = ((double)scroll - minScroll)/((double)maxScroll-minScroll);
		int row = (int)Math.round(maxRows*precent);
		return row;
	}

	@Override
	protected void mouseClicked(int mX, int mY, int button){
		if(scrolling)
			return;
		if(button != 0 || !canScroll){
			super.mouseClicked(mX, mY, button);
			return;
		}
		int adjMX = mX-this.guiLeft;
		int adjMY = mY-this.guiTop;
		if(adjMX < xSize-25 || adjMX > xSize-7){
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
		if(button != 0 || !scrolling || !canScroll){
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
		int startSlot = getStartingRow()*columns;
		int endSlot = startSlot + ConfigHandler.MAX_SIZE;
		int x = 0;
		int y = 18;
		for(int i = 0; i < container.playerInvStart; i++){
			if(i < startSlot || i >= endSlot){
				((Slot)container.inventorySlots.get(i)).yDisplayPosition = -(height*2);
				continue;
			}
			((Slot)container.inventorySlots.get(i)).yDisplayPosition = y;
			x++;
			if(x >= columns){
				x=0;
				y+=18;
			}
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int mX, int mY, int which){
		if(which != 0 || !scrolling || !canScroll){
			super.mouseMovedOrUp(mX, mY, which);
			return;
		}
		scrolling = false;
		previousY = scroll;
    }
	
	@Override
	public void handleMouseInput()
    {
		super.handleMouseInput();
		int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		if(!canScroll)
			return;
		Slot s = this.getSlot(x, y);
		if(s != null && s.getHasStack())
			return;
		int i = Mouse.getEventDWheel();
		//int delta = Math.max( Math.min( -i, 1 ), -1 );
		//this.scroll += (delta*0.5f) * (this.maxScroll-this.minScroll);
		int maxRows = (int)Math.ceil(container.playerInvStart/(float)columns);
		int scrollSize = (int)Math.ceil((this.maxScroll-minScroll)/maxRows);
		
		
		if(i > 0){
			this.scroll-=scrollSize;
		}else if (i < 0){
			this.scroll+=scrollSize;
		}
		
		if(scroll < minScroll)
			scroll = minScroll;
		else if(scroll > maxScroll)
			scroll = maxScroll;
		
		updateSlots();
    }
	
	@SuppressWarnings("unchecked")
	protected Slot getSlot(int mouseX, int mouseY)
	{
		final List<Slot> slots = this.inventorySlots.inventorySlots;
		for (Slot slot : slots)
		{
			// isPointInRegion
			if ( this.func_146978_c( slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY ) )
			{
				return slot;
			}
		}

		return null;
	}
}
