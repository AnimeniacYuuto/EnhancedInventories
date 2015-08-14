/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer.model

import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11;
import yuuto.enhancedinventories.ref.ReferenceEI

class ModelLockerSingle {
  protected val door:IModelCustom = AdvancedModelLoader.loadModel(new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(),"models/smallDoor.obj"));
  protected val locker:IModelCustom = AdvancedModelLoader.loadModel(new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(),"models/smallLocker.obj"));
  
  var rotation:Float = 0;
  
  def setRotation(r:Float){
    rotation = r;
  }
  def renderAll(){
    locker.renderAll();
    
    GL11.glPushMatrix();
    GL11.glTranslated(0.4d, 0d, 0.4d);
    GL11.glRotatef(rotation, 0, 1, 0);
    door.renderAll();
    GL11.glPopMatrix();
  }
  def renderAllReversed(){
    locker.renderAll();
    
    GL11.glPushMatrix();
    GL11.glTranslated(0.4d, 0d, -0.4d);
    GL11.glRotatef(180-rotation, 0, 1, 0);
    door.renderAll();
    GL11.glPopMatrix();
  }
}