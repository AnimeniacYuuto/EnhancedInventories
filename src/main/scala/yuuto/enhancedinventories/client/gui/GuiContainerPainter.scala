/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import yuuto.enhancedinventories.gui.ContainerPainter
import yuuto.enhancedinventories.tile.TilePainter;
import yuuto.enhancedinventories.ref.ReferenceEI

object GuiContainerPainter{
  final val texture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/gui/painterGui.png");
}
class GuiContainerPainter(painter:TilePainter, player:EntityPlayer) extends GuiContainer(new ContainerPainter(painter, player)){

  override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mx:Int, my:Int) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(GuiContainerPainter.texture);
    val k:Int = (this.width - this.xSize) / 2;
    val l:Int = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
  }

}