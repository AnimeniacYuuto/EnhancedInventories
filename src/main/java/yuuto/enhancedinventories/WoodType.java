package yuuto.enhancedinventories;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.registry.GameRegistry;

public class WoodType {
	
	String textureName;
	String modId;
	String itemId;
	int meta;
	ResourceLocation[] textures;
	public WoodType(String name, String modId, String itemId, int meta){
		this.textureName = name;
		this.modId = modId;
		this.itemId = itemId;
		this.meta = meta;
		textures = new ResourceLocation[4];
		textures[0] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/64x64/"+modId+"/"+this.textureName.toLowerCase()+".png");
		textures[1] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/128x128/"+modId+"/"+this.textureName.toLowerCase()+".png");
		
		textures[2] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/chestSmall/"+modId+"/"+this.textureName.toLowerCase()+".png");
		textures[3] = new ResourceLocation("enhancedinventories", "textures/uvs/wood/chestLarge/"+modId+"/"+this.textureName.toLowerCase()+".png");
	}
	public String getModID(){
		return modId;
	}
	public String getItemID(){
		return itemId;
	}
	public int getMetaData(){
		return meta;
	}
	public String name(){
		return getPlanksStack().getDisplayName();
	}
	public String id(){
		return "wood:"+modId+":"+itemId+":"+meta;
	}
	public boolean matches(ItemStack target){
		ItemStack woodStack = getPlanksStack();
		if(woodStack == null)
			return false;
		if(woodStack.getItem() == target.getItem() && woodStack.getItemDamage() == target.getItemDamage()){
			return true;
		}
		return false;
	}
	public ItemStack getPlanksStack(){
		ItemStack ret = GameRegistry.findItemStack(modId, itemId, 1);
		if(ret == null){
			return null;
		}
		ret = new ItemStack(ret.getItem(), 1, meta);
		return ret;
	}
	public ResourceLocation getTexture(int index){
		return textures[index];
	}
	
	public void addInformation(ItemStack stack, EntityPlayer player, List results, boolean bool){
		results.add(getPlanksStack().getDisplayName());
	}

}
