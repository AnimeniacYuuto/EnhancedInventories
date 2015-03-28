package yuuto.enhancedinventories.materials;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.util.Color;

public class FrameMaterial {
	Color color;
	String mod;
	String id;
	int meta = 0;
	ResourceLocation[] textures;
	
	public FrameMaterial(int r, int g, int b, int a, String mat){
		this(mat);
		this.color = new Color(r,g,b,a);
	}
	public FrameMaterial(String mat, String... texture){
		this(mat);
		this.textures = new ResourceLocation[textures.length];
		for(int i = 0; i < textures.length; i++){
			this.textures[i] = new ResourceLocation("enhancedinventories", "textures/uvs/"+textures[i]);
		}
	}
	private FrameMaterial(String mat){
		String[] parts = mat.split(":", 3);
		if(parts.length < 2){
			mod = "minecraft";
			id = "cobblestone";
		}
		mod = parts[0];
		id = parts[1];
		if(parts.length == 3)
			meta = Integer.parseInt(parts[2]);
	}
	public boolean isOre(){
		return mod.equals("ore");
	}
	public String getId(){
		if(isOre())
			return "frame:"+mod+":"+id;
		return "frame:"+mod+":"+id+":"+meta;
	}
	public boolean matches(ItemStack stack){
		if(isOre()){
			
		}
		return true;
	}
	public boolean hasCustomTextures(){
		return textures != null;
	}
	public ResourceLocation getTexture(int i){
		return textures[i];
	}
	public float r(){
		return color.getRed()/255f;
	}
	public float g(){
		return color.getGreen()/255f;
	}
	public float b(){
		return color.getBlue()/255f;
	}
	public float a(){
		return color.getAlpha()/255f;
	}

}
