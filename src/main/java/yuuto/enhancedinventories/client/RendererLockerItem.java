package yuuto.enhancedinventories.client;

import org.lwjgl.opengl.GL11;

import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EWoodType;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.client.models.ModelLockerSingle;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class RendererLockerItem implements IItemRenderer{

	static ModelLockerSingle singleLockerRenderer = new ModelLockerSingle();
	static ResourceLocation singleChestFrame = new ResourceLocation("enhancedinventories", "textures/uvs/normalLockerFrame.png");
	static ResourceLocation singleRefinedLocation;
	static Minecraft mc = Minecraft.getMinecraft();
	
	public RendererLockerItem(){
    	super();
    	if(EnhancedInventories.refinedRelocation){
    		singleRefinedLocation = new ResourceLocation("enhancedinventories", "textures/uvs/refinedRelocation/normalLocker.png");
    	}
    }
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.FIRST_PERSON_MAP)
			return false;
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		switch (type) {
	      case ENTITY: {
	        return (helper == ItemRendererHelper.ENTITY_BOBBING ||
	                helper == ItemRendererHelper.ENTITY_ROTATION ||
	                helper == ItemRendererHelper.BLOCK_3D);
	      }
	      case EQUIPPED: {
	        return (helper == ItemRendererHelper.BLOCK_3D ||
	                helper == ItemRendererHelper.EQUIPPED_BLOCK);
	      }
	      case EQUIPPED_FIRST_PERSON: {
	        return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
	      }
	      case INVENTORY: {
	        return (helper == ItemRendererHelper.INVENTORY_BLOCK);
	      }
	      default: {
	        return false;
	      }
	    }
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		//GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		switch(type){
		case ENTITY:
			renderItem(item, 0f,0f,0f, 0);
			renderItem(item, 0f,0f,0f, 1);
			if(EnhancedInventories.refinedRelocation && item.getItem() == Item.getItemFromBlock(EnhancedInventories.sortingLocker))
				renderItem(item, 0f,0f,0f, 2);
			break;
		case EQUIPPED:
			renderItem(item, 0.1f,0.3f,0.3f, 0);
			renderItem(item, 0.1f,0.3f,0.3f, 1);
			if(EnhancedInventories.refinedRelocation && item.getItem() == Item.getItemFromBlock(EnhancedInventories.sortingLocker))
				renderItem(item, 0.1f,0.3f,0.3f, 2);
			break;
		case EQUIPPED_FIRST_PERSON:
			renderItem(item, 0.3f,0.1f,0.3f, 0);
			renderItem(item, 0.3f,0.1f,0.3f, 1);
			if(EnhancedInventories.refinedRelocation && item.getItem() == Item.getItemFromBlock(EnhancedInventories.sortingLocker))
				renderItem(item, 0.3f,0.1f,0.3f, 2);
			break;
		case INVENTORY:
			renderItem(item, -0.18f,0f,0f, 0);
			renderItem(item, -0.18f,0f,0f, 1);
			if(EnhancedInventories.refinedRelocation && item.getItem() == Item.getItemFromBlock(EnhancedInventories.sortingLocker))
				renderItem(item, -0.18f,0f,0f, 2);
			break;
		default:
			break;
		}
	}
	
	public void renderItem(ItemStack item, float x, float y, float z, int pass){
		String wood = WoodTypes.DEFAULT_WOOD_ID;
		EInventoryMaterial mat = EInventoryMaterial.values()[item.getItemDamage()];
		
		if(item.hasTagCompound() && item.getTagCompound().hasKey("woodType")){
			wood = item.getTagCompound().getString("woodType");
		}
		
		//int i = 5;
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(x+0.5d, y+0.5d, z+0.525d);
		switch (pass){
	    case 0:
	    	mc.renderEngine.bindTexture(WoodTypes.getWoodType(wood).getTexture(0));
	    	break;
	    case 1:
	    	if(mat.hasTexture())
	    		mc.renderEngine.bindTexture(mat.getTexture(3));
		    else{
		    	mc.renderEngine.bindTexture(singleChestFrame);
		    	GL11.glColor4f(mat.r(), mat.g(), mat.b(), 1f);
		    }
	    	break;
	    case 2:
	    	mc.renderEngine.bindTexture(singleRefinedLocation);
	    	break;
    	default:
    		break;
	    }
	    
	    GL11.glTranslatef((float)x-0.7f, (float)y + 0.2F, (float)z + 0.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.55F, 0.65F, 0.55F);
        short short1 = 0;
        GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
        //GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        singleLockerRenderer.renderAll();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
