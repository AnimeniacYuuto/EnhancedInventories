/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui

import java.util.List
import java.util.Iterator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.gui.ContainerDynamic
import cofh.api.tileentity.ISecurable
import cofh.core.gui.GuiBaseAdv
import cofh.core.gui.element.TabSecurity
import cofh.lib.util.helpers.SecurityHelper;
import yuuto.enhancedinventories.ref.ReferenceEI

object GuiContainerDynamic{
  final val topTexture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(),"textures/gui/dynamicTop.png");
  final val bottomTexture1:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(),"textures/gui/dynamicBottom1.png");
  final val bottomTexture2:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(),"textures/gui/dynamicBottom2.png");
  
  //Scroll Data
  final val centerXSize:Int = 162;
  final val bottomYSize:Int = 97;
  
  final val minScroll:Int = 18;
  final val scrollButtonWidth:Int = 16;
  final val scrollButtonHeight:Int = 19;
}
class GuiContainerDynamic(tile:TileEntity, inv:IInventory, player:EntityPlayer) extends GuiBaseAdv(new ContainerDynamic(inv, player)){
   //TileData
  val container:ContainerDynamic=this.inventorySlots.asInstanceOf[ContainerDynamic];
  var rows, columns:Int=0;
  //Adjusts scroll data if the size of the inventory is smaller then can be displayed at once
  if(inv.getSizeInventory() < EIConfiguration.MAX_SIZE){
    //Adjusts the number of columns if there are less then 9 slots!
    if(inv.getSizeInventory() <= 9){
      rows = 1;
      columns = inv.getSizeInventory();
    //Adjusts the number of rows until there is a 9x6 chest
    }else if(inv.getSizeInventory() <= 54){
      columns = 9;
      rows = Math.ceil(inv.getSizeInventory()/9f).asInstanceOf[Int];
    //Expands columns then rows to max size
    }else{
      columns = Math.min(Math.ceil(inv.getSizeInventory()/6f), EIConfiguration.COLOMNS).asInstanceOf[Int];
      rows = Math.min(Math.ceil(inv.getSizeInventory()/columns.asInstanceOf[Float]), EIConfiguration.COLOMNS).asInstanceOf[Int];
    }
  //Uses the max display size
  }else{
    rows = EIConfiguration.ROWS;
    columns = EIConfiguration.COLOMNS;
  }
  var canScroll:Boolean = false;
  var scroll:Int = 18;
  var previousY:Int = 0;
  var scrolling:Boolean = false;
  var maxScroll:Int = 105;
  //Adjust screen size
  this.xSize = 14+(18*columns);
  this.ySize = 17+97+(18*rows);
  //Sets scrollable
  if(inv.getSizeInventory() > EIConfiguration.MAX_SIZE){
    this.xSize+=18;
    canScroll = true;
    maxScroll = ySize - (GuiContainerDynamic.bottomYSize+GuiContainerDynamic.scrollButtonHeight+1);
  }
  
  def this(inv:IInventory, player:EntityPlayer)=this(null, inv, player);
  
  override def initGui(){
    super.initGui();
    //Updates the slot positions so non visible slots can not be drawn or clicked
    if(canScroll)
      updateSlots();
    //If the tile is securable adds the security tab 
    if(tile != null && tile.isInstanceOf[ISecurable]){
      this.addTab(new TabSecurity(this, tile.asInstanceOf[ISecurable], SecurityHelper.getID(container.playerInventory.player)));
    }
  }

  override def drawGuiContainerForegroundLayer(mx:Int, my:Int) {
    //GuiContainerManager.getManager().renderObjects(mX, mY);
    
  }

  override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mX:Int, mY:Int){
    //draw your Gui here, only thing you need to change is the path
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(GuiContainerDynamic.topTexture);
    val x:Int = (width - xSize) / 2;
    val y:Int = (height - ySize) / 2;
    
    //x = 0;
    //y = 0;
    
    val x1:Int = 7+(18*Math.min(columns,12));
    val y1:Int = 17+(18*Math.min(rows,12));
    val x2:Int = 7+(18*Math.max(columns-12,0));
    val y2:Int = (18*Math.max(rows-12,0));
    var bSX1:Int = (xSize - GuiContainerDynamic.centerXSize)/2;
    var bSX2:Int = 0;
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
      this.drawTexturedModalRect(x+x1+x2-6, y+scroll, 0, 234, GuiContainerDynamic.scrollButtonWidth, GuiContainerDynamic.scrollButtonHeight);
    }
    this.mc.renderEngine.bindTexture(GuiContainerDynamic.bottomTexture1);
    //Draw bottom left
    this.drawTexturedModalRect(x, y+y1+y2, 0, 118, bSX1, 97);
    this.drawTexturedModalRect(x+bSX1, y+y1+y2, 7, 118, bSX2, 97);
    //Draw bottom center
    this.drawTexturedModalRect(x+bSX1+bSX2, y+y1+y2, 0, 0, 162, 97);
    this.mc.renderEngine.bindTexture(GuiContainerDynamic.bottomTexture2);
    //Draw bottom right
    this.drawTexturedModalRect(x+bSX1+bSX2+GuiContainerDynamic.centerXSize, y+y1+y2, 0, 0,bSX2, 97);
    this.drawTexturedModalRect(x+bSX1+(2*bSX2)+GuiContainerDynamic.centerXSize, y+y1+y2, 250-bSX1, 0,bSX1, 97);
    
    
    //Draws cofh gui elements and tabs
    mouseX = mX - guiLeft;
    mouseY = mY - guiTop;

    GL11.glPushMatrix();
    GL11.glTranslatef(guiLeft, guiTop, 0.0F);
    drawElements(partialTick, false);
    drawTabs(partialTick, false);
    GL11.glPopMatrix();
  }
  
  //finds the starting row for the inventory
  private def getStartingRow():Int={
    var maxRows:Int = Math.ceil(container.playerInvStart/columns.asInstanceOf[Float]).asInstanceOf[Int];
    maxRows -= rows;
    val precent:Double = (scroll - GuiContainerDynamic.minScroll).asInstanceOf[Double]/(maxScroll-GuiContainerDynamic.minScroll).asInstanceOf[Double];
    val row:Int = Math.round(maxRows*precent).asInstanceOf[Int];
    return row;
  }

  //Scrolls inventory
  override protected def mouseClicked(mX:Int, mY:Int, button:Int){
    if(scrolling)
      return;
    if(button != 0 || !canScroll){
      super.mouseClicked(mX, mY, button);
      return;
    }
    val adjMX:Int = mX-this.guiLeft;
    val adjMY:Int = mY-this.guiTop;
    if(adjMX < xSize-25 || adjMX > xSize-7){
      super.mouseClicked(mX, mY, button);
      return;
    }
    if(adjMY < scroll || adjMY > scroll+GuiContainerDynamic.scrollButtonHeight){
      super.mouseClicked(mX, mY, button);
      return;
    }
    previousY = adjMY;
    scrolling = true;
  }
  
  //Scrolls the inventory
  override protected def mouseClickMove(mX:Int, mY:Int, button:Int, time:Long){
    if(button != 0 || !scrolling || !canScroll){
      super.mouseClickMove(mX, mY, button, time);
      return;
    }
    val adjMY:Int = mY-this.guiTop;
    if((previousY <= GuiContainerDynamic.minScroll && adjMY <= GuiContainerDynamic.minScroll) ||
        (previousY >= maxScroll && adjMY >= maxScroll+GuiContainerDynamic.scrollButtonHeight)){
      return;
    }
    if(previousY < GuiContainerDynamic.minScroll)
      scroll += adjMY-GuiContainerDynamic.minScroll;
    else if(previousY > maxScroll)
      scroll += adjMY-maxScroll;
    else
      scroll += adjMY-previousY;
    
    if(scroll < GuiContainerDynamic.minScroll)
      scroll = GuiContainerDynamic.minScroll;
    else if(scroll > maxScroll)
      scroll = maxScroll;
    
    updateSlots();
    
    previousY = adjMY;
    }
  
  //Scrolls the inventory
  override protected def mouseMovedOrUp(mX:Int, mY:Int, which:Int){
    if(which != 0 || !scrolling || !canScroll){
      super.mouseMovedOrUp(mX, mY, which);
      return;
    }
    scrolling = false;
    previousY = scroll;
  }
  //Scrolls inventory with scroll wheel
  override def handleMouseInput(){
    super.handleMouseInput();
    val x:Int = Mouse.getEventX() * this.width / this.mc.displayWidth;
    val y:Int = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    if(!canScroll)
      return;
    val s:Slot = this.getSlot(x, y);
    if(s != null && s.getHasStack())
      return;
    val i:Int = Mouse.getEventDWheel();
    //int delta = Math.max( Math.min( -i, 1 ), -1 );
    //this.scroll += (delta*0.5f) * (this.maxScroll-this.minScroll);
    val maxRows:Int = Math.ceil(container.playerInvStart/columns.asInstanceOf[Float]).asInstanceOf[Int];
    val scrollSize:Int = Math.ceil((this.maxScroll-GuiContainerDynamic.minScroll)/maxRows.asInstanceOf[Float]).asInstanceOf[Int];
    
    
    if(i > 0){
      this.scroll-=scrollSize;
    }else if (i < 0){
      this.scroll+=scrollSize;
    }
    
    if(scroll < GuiContainerDynamic.minScroll)
      scroll = GuiContainerDynamic.minScroll;
    else if(scroll > maxScroll)
      scroll = maxScroll;
    
    updateSlots();
  }
  
  /**
   * Updates slot positions based on the scroll position
   */
  def updateSlots(){
    val startSlot:Int = getStartingRow()*columns;
    val endSlot:Int = startSlot + EIConfiguration.MAX_SIZE;
    var x:Int = 0;
    var y:Int = 18;
    for(i <-0 until container.playerInvStart){
      if(i < startSlot || i >= endSlot){
        container.inventorySlots.get(i).asInstanceOf[Slot].yDisplayPosition = -(height*2);
      }else{
        container.inventorySlots.get(i).asInstanceOf[Slot].yDisplayPosition = y;
        x+=1;
        if(x >= columns){
          x=0;
          y+=18;
        }
      }
    }
  }
  
  /**
   * Checks if there is a slot at the mouse position
   * @param mouseX
   * @param mouseY
   * @return
   */
  protected def getSlot(mouseX:Int, mouseY:Int):Slot={
    val slots:Iterator[Slot] = this.inventorySlots.inventorySlots.iterator().asInstanceOf[Iterator[Slot]];
    while (slots.hasNext()){
      val slot=slots.next();
      // isPointInRegion
      if ( this.func_146978_c( slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY ) )
      {
        return slot;
      }
    }

    return null;
  }
  
  //@Override
  //draws the button icons, icons made courtesy of TeamCofh
  /*public void drawButton(IIcon icon, int x, int y, int spriteSheet, int mode) {

    switch (mode) {
    case 0:
      drawIcon(IconRegistry.getIcon("IconButton"), x, y, 1);
      break;
    case 1:
      drawIcon(IconRegistry.getIcon("IconButtonHighlight"), x, y, 1);
      break;
    default:
      drawIcon(IconRegistry.getIcon("IconButtonInactive"), x, y, 1);
      break;
    }
    drawIcon(icon, x, y, spriteSheet);
  }*/

  //@Override
  //Retrieves an icon from MY icon registry
  /*public IIcon getIcon(String name) {

    return IconRegistry.getIcon(name);
  }*/
}