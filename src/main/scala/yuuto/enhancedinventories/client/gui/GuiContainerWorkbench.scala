/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.lwjgl.opengl.GL11
import yuuto.enhancedinventories.gui.ContainerWorkbench;
import net.minecraft.entity.player.EntityPlayer
object GuiContainerWorkbench{
  final val craftingTableGuiTextures:ResourceLocation = new ResourceLocation("textures/gui/container/crafting_table.png");
}
class GuiContainerWorkbench(player:EntityPlayer, world:World, x:Int, y:Int, z:Int) extends GuiContainer(new ContainerWorkbench(player, world, x, y, z)){

    override protected def drawGuiContainerForegroundLayer(mx:Int, my:Int)
    {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Array[Any](0)), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Array[Any](0)), 8, this.ySize - 96 + 2, 4210752);
    }

    override protected def drawGuiContainerBackgroundLayer(partialTick:Float, mx:Int, my:Int){
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiContainerWorkbench.craftingTableGuiTextures);
        val k:Int = (this.width - this.xSize) / 2;
        val l:Int = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}