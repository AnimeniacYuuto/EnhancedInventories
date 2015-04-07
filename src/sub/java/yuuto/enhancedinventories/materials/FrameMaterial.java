package yuuto.enhancedinventories.materials;

import org.lwjgl.util.Color;

import yuuto.enhancedinventories.materials.indentifire.IIdentifier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FrameMaterial {
	
	IIdentifier material;
	float r=1f,g=1f,b=1f,a=1f;
	ResourceLocation texture;
	
	public FrameMaterial(ItemStack mat, int r, int g, int b, int a){
		
	}
	public FrameMaterial(ItemStack mat, String texture){
		
	}
	public FrameMaterial(ItemStack mat, String texture, int r, int g, int b){
		
	}
	public FrameMaterial(String mat, int r, int g, int b, int a){
		
	}
	public FrameMaterial(String mat, String texture){
		
	}
	public FrameMaterial(String mat, String texture, int r, int g, int b){
		
	}
	
	public String getId(){
		return null;
	}
	
	public boolean matches(ItemStack stack){
		return false;
	}
	
	public boolean hasTexture(){
		return texture != null;
	}
	public ResourceLocation getTexture(){
		return texture;
	}
	
	public float r(){
		return r;
	}
	public float g(){
		return g;
	}
	public float b(){
		return b;
	}
	public float a(){
		return a;
	}

}
