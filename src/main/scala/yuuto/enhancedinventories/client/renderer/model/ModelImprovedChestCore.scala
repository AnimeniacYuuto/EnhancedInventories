/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer.model

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

class ModelImprovedChestCore extends ModelIconCube{
  
  val minChestX:Float = 1f/16f;
  val maxChestX:Float = 15f/16f;
  val minChestY1:Float = 0f;
  val maxChestY1:Float = 10f/16f;
  val minChestY2:Float = 9f/16f;
  val maxChestY2:Float = 14f/16f;
  val minChestZ:Float = 1/16f;
  val maxChestZ:Float = 15/16f;
  
  var rotaionAngle:Float = 0f;
  
  def drawChestCore(icons:Array[IIcon], tess:Tessellator){
    this.drawCubeFromIcons(icons, tess, minChestX+0.001f, minChestY1+0.001f, minChestZ+0.001f, maxChestX-0.001f, maxChestY1-0.001f, maxChestZ-0.001f, 
        minChestX, minChestY1, minChestZ, maxChestX, maxChestY1, maxChestZ);
    GL11.glPushMatrix();
    GL11.glTranslatef(0, 9f/16f, maxChestZ);
    GL11.glRotatef(rotaionAngle*(180F / Math.PI.asInstanceOf[Float]), 1, 0, 0);
    this.drawCubeFromIcons(icons, tess, minChestX+0.001f, 0.001f, -(maxChestZ-minChestZ)+0.001f, maxChestX-0.001f, 5f/16f-0.001f, -0.001f, 
        minChestX, minChestY2, minChestZ, maxChestX, maxChestY2, maxChestZ);
    GL11.glPopMatrix();
  }

}