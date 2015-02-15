/*******************************************************************************
 * Copyright (c) 2014 Yuuto.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * 	   cpw - src reference from Iron Chests
 * 	   doku/Dokucraft staff - base chest texture
 *     Yuuto - initial API and implementation
 ******************************************************************************/
package yuuto.enhancedinventories.client;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EWoodType;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.WoodTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class RendererImprovedChestItem implements IItemRenderer{

	private static ModelChest modelChest = new ModelChest();
	static ResourceLocation singleChestFrame = new ResourceLocation("enhancedinventories", "textures/uvs/normalChestFrame.png");
    static ResourceLocation singleChestWool = new ResourceLocation("enhancedinventories", "textures/uvs/normalChestWool.png");;
    static ResourceLocation singleRefinedLocation;
    static Minecraft mc = Minecraft.getMinecraft();
	
	public RendererImprovedChestItem(){
    	super();
    	if(EnhancedInventories.refinedRelocation){
    		singleRefinedLocation = new ResourceLocation("enhancedinventories", "textures/uvs/refinedRelocation/normalChest.png");
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
			renderItem(item, 0f,0f,0f, 2);
			break;
		case EQUIPPED:
			renderItem(item, 0f,0f,0f, 0);
			renderItem(item, 0f,0f,0f, 1);
			renderItem(item, 0f,0f,0f, 2);
			break;
		case EQUIPPED_FIRST_PERSON:
			renderItem(item, 0f,0f,0f, 0);
			renderItem(item, 0f,0f,0f, 1);
			renderItem(item, 0f,0f,0f, 2);
			break;
		case INVENTORY:
			renderItem(item, 0f,0f,0f, 0);
			renderItem(item, 0f,0f,0f, 1);
			renderItem(item, 0f,0f,0f, 2);
			break;
		default:
			break;
		}
	}
	public void renderItem(ItemStack item, float x, float y, float z, int pass){
		String wood = WoodTypes.DEFAULT_WOOD_ID;
		int wool = 0;
		EInventoryMaterial mat = EInventoryMaterial.values()[item.getItemDamage()];
		
		if(item.hasTagCompound()){
			if(item.getTagCompound().hasKey("woodType"))
				wood = item.getTagCompound().getString("woodType");
			wool = item.getTagCompound().getInteger("wool");
		}
		
		int i = 5;
        GL11.glPushMatrix();
        //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        switch(pass){
        case 0:
        	mc.renderEngine.bindTexture(WoodTypes.getWoodType(wood).getTexture(2));
        	GL11.glColor4f(1f, 1f, 1f, 1f);
        	break;
        case 1:
        	Color c = ColorHelper.WOOL_COLORS[wool];
        	GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1f);
        	mc.renderEngine.bindTexture(singleChestWool);
        	break;
        case 2:
        	if(mat.hasTexture())
        		mc.renderEngine.bindTexture(mat.getTexture(0));
        	else{
        		GL11.glColor4f(mat.r(), mat.g(), mat.b(), 1f);
        		mc.renderEngine.bindTexture(singleChestFrame);
        	}
        	break;
        case 3:
        	mc.renderEngine.bindTexture(singleRefinedLocation);
        	break;
        }
        GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        short short1 = 0;

        if (i == 2)
        {
            short1 = 180;
        }

        if (i == 3)
        {
            short1 = 0;
        }

        if (i == 4)
        {
            short1 = 90;
        }

        if (i == 5)
        {
            short1 = -90;
        }

        GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        //float f1 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * f;
        //float f2;
        //f1 = 1.0F - f1;
        //f1 = 1.0F - f1 * f1 * f1;
        //modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
        modelChest.renderAll();
        //GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
