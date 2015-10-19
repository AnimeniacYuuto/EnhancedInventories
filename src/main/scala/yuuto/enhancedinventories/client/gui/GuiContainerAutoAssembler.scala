/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import yuuto.enhancedinventories.gui.ContainerAutoAssembler
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.ref.ReferenceEI
import yuuto.enhancedinventories.tile.TileAutoAssembler
import cofh.core.gui.GuiBaseAdv
import net.minecraft.client.gui.GuiScreen
import yuuto.enhancedinventories.client.gui.elements.TabRedstone

object GuiContainerAutoAssembler{
  final val texture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/gui/autoAssembler.png");
}
class GuiContainerAutoAssembler(tile:TileAutoAssembler, player:EntityPlayer) extends GuiBaseAdv(new ContainerAutoAssembler(tile, player)){
  
  override def initGui(){
    super.initGui();
    this.addTab(new TabRedstone(this, tile));
  }

  override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mx:Int, my:Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(GuiContainerAutoAssembler.texture);
    val k:Int = (this.width - this.xSize) / 2;
    val l:Int = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        
    mouseX = mx - guiLeft;
    mouseY = my - guiTop;

    //Draw cofh gui elements & tabs
    GL11.glPushMatrix();
    GL11.glTranslatef(guiLeft, guiTop, 0.0F);
    drawElements(partialTick, false);
    drawTabs(partialTick, false);
    GL11.glPopMatrix();
    
    //Draws the schematic
    this.drawSchematic();
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
          drawItemStack(stack, this.guiLeft+8 + x * 18, this.guiTop+17 + y * 18, "");
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

}