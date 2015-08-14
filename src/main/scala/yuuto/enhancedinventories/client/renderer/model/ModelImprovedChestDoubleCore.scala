/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer.model

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

class ModelImprovedChestDoubleCore extends ModelIconCube{
  
  val minChestX1:Float = -15f/16f;
  val maxChestX1:Float = 0f;
  val minChestX2:Float = 0f;
  val maxChestX2:Float = 15f/16f;
  val minChestY1:Float = 0f;
  val maxChestY1:Float = 10f/16f;
  val minChestY2:Float = 9f/16f;
  val maxChestY2:Float = 14f/16f;
  val minChestZ:Float = 1/16f;
  val maxChestZ:Float = 15/16f;
  
  var rotaionAngle:Float = 0f;
  
  def makeChestCoreList(icons:Array[IIcon], tess:Tessellator){
    this.setSub();
    this.drawCubeFromIcons(icons, tess, minChestX1+0.001f, minChestY1+0.001f, minChestZ+0.001f, maxChestX1, maxChestY1-0.001f, maxChestZ-0.001f, 
        minChestX1+1, minChestY1, minChestZ, maxChestX1+1, maxChestY1, maxChestZ);
    this.setMain();
    this.drawCubeFromIcons(icons, tess, minChestX2, minChestY1+0.001f, minChestZ+0.001f, maxChestX2-0.001f, maxChestY1-0.001f, maxChestZ-0.001f, 
        minChestX2, minChestY1, minChestZ, maxChestX2, maxChestY1, maxChestZ);
    
    GL11.glPushMatrix();
    GL11.glTranslated(0, 9f/16f, maxChestZ);
    GL11.glRotatef(rotaionAngle*(180F / Math.PI.asInstanceOf[Float]), 1, 0, 0);
    this.setSub();
    this.drawCubeFromIcons(icons, tess, -(maxChestX1-minChestX1)+0.001f, 0.001f, -(maxChestZ-minChestZ)+0.001f, 0, 5/16f-0.001f, -0.001f, 
        minChestX1+1, minChestY2, minChestZ, maxChestX1+1, maxChestY2, maxChestZ);
    this.setMain();
    this.drawCubeFromIcons(icons, tess, minChestX2+0.001f, 0.001f, -(maxChestZ-minChestZ)+0.001f, maxChestX2-0.001f, 5/16f-0.001f, -0.001f, 
        minChestX2, minChestY2, minChestZ, maxChestX2, maxChestY2, maxChestZ);
    /*this.drawCubeFromIcons(icons, tess, minChestX1, 0, minChestZ-1f, maxChestX1, 5f/16f, maxChestZ-1f, 
        minChestX1+1, minChestY2, minChestZ, maxChestX1+1, maxChestY2, maxChestZ);
    this.setMain();
    this.drawCubeFromIcons(icons, tess, minChestX2, 0, minChestZ-1, maxChestX2, 5f/16f, maxChestZ-1, 
        minChestX2, minChestY2, minChestZ, maxChestX2, maxChestY2, maxChestZ);*/
    GL11.glPopMatrix();
  }
  protected def setMain(){
    this.hideFace(4) = true;
    this.hideFace(5) = false;
  }
  protected def setSub(){
    this.hideFace(4) = false;
    this.hideFace(5) = true;
  }
}