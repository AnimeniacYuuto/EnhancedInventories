/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer.model

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

class ModelLockerDoubleCore extends ModelIconCube{
  
  protected val hideFront:Array[Boolean]=this.hideFace;
  protected val hideSides:Array[Boolean]=Array(true, true, true, true, false, false);
  protected val hideTop:Array[Boolean]=Array(false, false, true, true, true, true);
  hideFront(0) = true;
  hideFront(1) = true;
  hideFront(4) = true;
  hideFront(5) = true;
  
  var rotaionAngle:Float = 0f;
  var reversed:Boolean = false;
  
  def drawLockerCore(icons:Array[IIcon], tess:Tessellator){
    this.setSide();
    //Top
    this.drawCubeFromIcons(icons, tess, 0.001f, 0f, 0.001f, 1.5f/16f-0.001f, 0.999f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    this.drawCubeFromIcons(icons, tess, 1.001f-(1.5f/16f), 0f, 0.001f, 0.999f, 0.999f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    //Bottom
    this.drawCubeFromIcons(icons, tess, 0.001f, -0.999f, 0.001f, 1.5f/16f-0.001f, 0f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    this.drawCubeFromIcons(icons, tess, 1.001f-(1.5f/16f), -0.999f, 0.001f, 0.999f, 0f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    this.setTop();
    //Top
    this.drawCubeFromIcons(icons, tess, 0.001f, -0.999f, 0.001f, 0.990f, 1.5f/16f-1.001f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    //Bottom
    this.drawCubeFromIcons(icons, tess, 0.001f, 1.001f-(1.5f/16f), 0.001f, 0.990f, 0.999f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    this.setFront();
    //Top
    this.drawCubeFromIcons(icons, tess, 0.001f, 0, 1.001f-(1.5f/16f), 0.990f, 0.999f, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    //Bottom
    this.drawCubeFromIcons(icons, tess, 0.001f, -0.999f, 1.001f-(1.5f/16f), 0.990f, 0, 0.999f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    GL11.glPushMatrix();
    if(reversed)
      drawDoorReversed(icons, tess);
    else
      drawDoor(icons, tess);
    GL11.glPopMatrix();
  }
  def setFront(){
    this.hideFace = hideFront;
  }
  def setSide(){
    this.hideFace = hideSides;
  }
  def setTop(){
    this.hideFace = hideTop;
  }
  def drawDoor(icons:Array[IIcon], tess:Tessellator){
    GL11.glTranslatef(1-(1.5f/16f), 0, 1.5F/16F);
    GL11.glRotatef(rotaionAngle, 0, 1, 0);
    //Top
    this.drawCubeFromIcons(icons, tess, -(1-(3.5f/16f)), 0, 0.001f, 0, 1.001f-(1.5f/16f), 0.001f+0.5f/16f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    //Bottom
    this.drawCubeFromIcons(icons, tess, -(1-(3.5f/16f)), -1.001f+(1.5f/16f), 0.001f, 0, 0, 0.001f+0.5f/16f, 
        0f, 0f, 0f, 1f, 1f, 1f);
  }
  def drawDoorReversed(icons:Array[IIcon], tess:Tessellator){
    GL11.glTranslatef((1.5f/16f), 0, 1.5F/16F);
    GL11.glRotatef(-rotaionAngle, 0, 1, 0);
    //Top
    this.drawCubeFromIcons(icons, tess, 0.001f, 0, 0.001f, (1-(3.5f/16f)), 1.001f-(1.5f/16f), 0.001f+0.5f/16f, 
        0f, 0f, 0f, 1f, 1f, 1f);
    //Bottom
    this.drawCubeFromIcons(icons, tess, 0.001f, -1.001f+(1.5f/16f), 0.001f, (1-(3.5f/16f)), 0, 0.001f+0.5f/16f, 
        0f, 0f, 0f, 1f, 1f, 1f);
  }

}