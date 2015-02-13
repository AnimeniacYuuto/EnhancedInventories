package yuuto.enhancedinventories.client.models;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelLockerSingle {
	protected IModelCustom door = AdvancedModelLoader.loadModel(new ResourceLocation("enhancedinventories","models/smallDoor.obj"));
	protected IModelCustom locker = AdvancedModelLoader.loadModel(new ResourceLocation("enhancedinventories","models/smallLocker.obj"));
	
	float rotation = 0;
	
	public void setRotation(float r){
		rotation = r;
	}
	public void renderAll(){
		locker.renderAll();
		
		GL11.glPushMatrix();
		GL11.glTranslated(0.4d, 0d, 0.4d);
		GL11.glRotatef(rotation, 0, 1, 0);
		door.renderAll();
		GL11.glPopMatrix();
	}
	public void renderAllReversed(){
		locker.renderAll();
		
		GL11.glPushMatrix();
		GL11.glTranslated(0.4d, 0d, -0.4d);
		GL11.glRotatef(180-rotation, 0, 1, 0);
		door.renderAll();
		GL11.glPopMatrix();
	}
}
