/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui

import java.util.List
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.gui.ContainerWorktable
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.network.MessageWorktable
import yuuto.enhancedinventories.ref.ReferenceEI
import yuuto.enhancedinventories.tile.TileWorktable
import net.minecraft.client.gui.GuiScreen
import cofh.core.gui.GuiBaseAdv
import yuuto.enhancedinventories.client.gui.elements.ButtonSchematic

object GuiContainerWorktable{
  final val texture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/gui/work_table.png");
}
class GuiContainerWorktable(tile:TileWorktable, player:EntityPlayer) extends GuiBaseAdv(new ContainerWorktable(tile, player)){
  this.ySize = 224;
  var saveButton:ButtonSchematic=null;
  
  override def initGui(){
    super.initGui();
    //Adds clear and save buttons
    this.addElement(new ButtonSchematic(this, 8, 17, 16, 16, 0));
    saveButton=new ButtonSchematic(this, 8, 53, 16, 16, 1);
    if(tile.getCraftResult().getStackInSlot(0) == null || tile.isUsingSchematic())
      saveButton.setEnabled(false);
    this.addElement(saveButton);
    //this.buttonList.asInstanceOf[List[GuiButton]].add(new GuiButton(0, this.guiLeft+10, this.guiTop+19, 12, 12, ""));
    //this.buttonList.asInstanceOf[List[GuiButton]].add(new GuiButton(1, this.guiLeft+10, this.guiTop+55, 12, 12, ""));
  }
  
  override protected def drawGuiContainerForegroundLayer(mx:Int, my:Int) {}

  override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mx:Int, my:Int) {
    
    //Draws the background
    GL11.glPushMatrix();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
    this.mc.getTextureManager().bindTexture(GuiContainerWorktable.texture);
    val k:Int = (this.width - this.xSize) / 2;
    val l:Int = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    GL11.glPopMatrix();
    
    //Draw cofh gui elements & tabs
    GL11.glPushMatrix();
    GL11.glTranslatef(guiLeft, guiTop, 0.0F);
    drawElements(partialTick, false);
    drawTabs(partialTick, false);
    GL11.glPopMatrix();
    
    drawSchematic();
  }
  def drawSchematic(){
    val schematic:ItemStack = tile.getSchematic();
    if(schematic == null)
      return;
    val craftMatrix:InventoryCrafting = ItemSchematic.getCraftingMatrix(schematic);
    if(craftMatrix == null)
      return;
    
    GL11.glPushMatrix();
    //Set GL options
    RenderHelper.enableGUIStandardItemLighting();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(770, 1);
    GL11.glColor4d(0.6, 0.6, 0.6, 0.7);
    //Draw stacks
    GuiScreen.itemRender.renderWithColor = false;
    for(y <-0 until 3){
      for(x <-0 until 3){
          val stack:ItemStack = craftMatrix.getStackInRowAndColumn(x, y);
          drawItemStack(stack, this.guiLeft+30 + x * 18, this.guiTop+17 + y * 18, "");
        }
    }
    GuiScreen.itemRender.renderWithColor = true;
    GL11.glColor4f(1, 1, 1, 1);
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glPopMatrix();
    RenderHelper.enableStandardItemLighting();
  }
  protected def drawItemStack(stack:ItemStack, x:Int, y:Int, s:String){
    GL11.glPushMatrix();
    GL11.glEnable(GL11.GL_LIGHTING);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glTranslatef(0.0F, 0.0F, 32.0F);
    this.zLevel = -55.0F;
    GuiScreen.itemRender.zLevel = -55.0F;
    var font:FontRenderer = null;
    if (stack != null) font = stack.getItem().getFontRenderer(stack);
    if (font == null) font = fontRendererObj;
    GuiScreen.itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
    GuiScreen.itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
    //itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_ - (this.draggedStack == null ? 0 : 8), p_146982_4_);
    this.zLevel = 0.0F;
    GuiScreen.itemRender.zLevel = 0.0F;
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glPopMatrix();
  }
  
  override def updateScreen(){
    super.updateScreen();
    saveButton.setEnabled(tile.getCraftResult().getStackInSlot(0) != null && !tile.isUsingSchematic());
  }
  
  //Sends packets for clicks
  override protected def actionPerformed(button:GuiButton) {
    button.id match{
    case 0 => EnhancedInventories.network.sendToServer(new MessageWorktable(0, this.tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord));
    case 1 => EnhancedInventories.network.sendToServer(new MessageWorktable(1, this.tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord));
    case i => super.actionPerformed(button);
    }
  }
  
  override def handleElementButtonClick(name:String, button:Int){
    if(name == null)
      return;
    name match{
      case "buttonCancel"=> EnhancedInventories.network.sendToServer(new MessageWorktable(0, this.tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord));
      case "buttonSave"=> EnhancedInventories.network.sendToServer(new MessageWorktable(1, this.tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord));
      case s=>super.handleElementButtonClick(name, button);
    }
  }

}