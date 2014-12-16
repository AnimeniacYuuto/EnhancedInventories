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
import org.lwjgl.opengl.GL12;

import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EWoodType;
import yuuto.enhancedinventories.tile.TileImprovedChest;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class RendererImprovedChest extends TileEntitySpecialRenderer{

	private static ModelChest modelSingleChest = new ModelChest();
    private static ModelChest modelDoubleChest = new ModelLargeChest();
	
    static ResourceLocation singleChestFrame = new ResourceLocation("enhancedinventories", "/textures/uvs/normalChestFrame.png");
    static ResourceLocation singleChestFrameObsidian = new ResourceLocation("enhancedinventories", "/textures/uvs/normalChestFrameObsidian.png");
    static ResourceLocation singleChestWool = new ResourceLocation("enhancedinventories", "/textures/uvs/normalChestWool.png");;
    
    static ResourceLocation doubleChestFrame = new ResourceLocation("enhancedinventories", "/textures/uvs/doubleChestFrame.png");
    static ResourceLocation doubleChestFrameObsidian = new ResourceLocation("enhancedinventories", "/textures/uvs/doubleChestFrameObsidian.png");
    static ResourceLocation doubleChestWool = new ResourceLocation("enhancedinventories", "/textures/uvs/doubleChestWool.png");
    
	@Override
	public void renderTileEntityAt(TileEntity tile, double x,
			double y, double z, float f) {
		if(!(tile instanceof TileImprovedChest))
			return;
		if(((TileImprovedChest)tile).getPartner() == null){
			renderSingle((TileImprovedChest) tile, x, y, z, f, 0);
			renderSingle((TileImprovedChest) tile, x, y, z, f, 1);
			renderSingle((TileImprovedChest) tile, x, y, z, f, 2);
		}else{
			renderDouble((TileImprovedChest) tile, x, y, z, f, 0);
			renderDouble((TileImprovedChest) tile, x, y, z, f, 1);
			renderDouble((TileImprovedChest) tile, x, y, z, f, 2);
		}
		
	}
    
    void renderSingle(TileImprovedChest chest, double x, double y, double z, float f, int pass) {
		int i = chest.getOrientation().ordinal();

        ModelChest modelchest = this.modelSingleChest;

        switch(pass){
        case 0:
        	this.bindTexture(EWoodType.values()[chest.woodType].getSingleChestTexture());
        	break;
        case 1:
        	this.bindTexture(singleChestWool);
        	break;
        case 2:
        	if(chest.getType() == EInventoryMaterial.Obsidian)
        		this.bindTexture(singleChestFrameObsidian);
        	else
        		this.bindTexture(singleChestFrame);
        	break;
        }
        
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        switch(pass){
        case 1:
        	Color c = ColorHelper.WOOL_COLORS[chest.woolType];
        	GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1f);
        	break;
        case 2:
        	GL11.glColor4f(chest.getType().r(), chest.getType().g(), chest.getType().b(), 1f);
        	break;
        default:
        case 0:
        	GL11.glColor4f(1f, 1f, 1f, 1f);
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
        float f1 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * f;
        float f2;
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
        modelchest.renderAll();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
	}
    void renderDouble(TileImprovedChest chest, double x, double y, double z, float f, int pass) {
    	int i = chest.getOrientation().ordinal();

        ModelChest modelchest = this.modelDoubleChest;

        switch(pass){
        case 0:
        	this.bindTexture(EWoodType.values()[chest.woodType].getDoubleChestTexture());
        	break;
        case 1:
        	this.bindTexture(doubleChestWool);
        	break;
        case 2:
        	if(chest.getType() == EInventoryMaterial.Obsidian)
        		this.bindTexture(doubleChestFrameObsidian);
        	else
        		this.bindTexture(doubleChestFrame);
        	break;
        }
        
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        switch(pass){
        case 1:
        	Color c = ColorHelper.WOOL_COLORS[chest.woolType];
        	GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1f);
        	break;
        case 2:
        	GL11.glColor4f(chest.getType().r(), chest.getType().g(), chest.getType().b(), 1f);
        	break;
        default:
        case 0:
        	GL11.glColor4f(1f, 1f, 1f, 1f);
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

        //if (i == 2 && p_147500_1_.adjacentChestXPos != null)
        /*if (i == 2 && chest.getPartnerDir() == ForgeDirection.EAST)
        {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
        }else if(i == 2 && chest.getPartnerDir() == ForgeDirection.WEST){
        	GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
        }

        //if (i == 5 && p_147500_1_.adjacentChestZPos != null)
        if (i == 5 && chest.getPartnerDir() == ForgeDirection.SOUTH)
        {
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        }else if(i == 5 && chest.getPartnerDir() == ForgeDirection.NORTH){
        	GL11.glTranslatef(0.0F, 0.0F, 1.0F);
        }*/
        if(i == 2 && chest.getPartnerDir() == ForgeDirection.EAST){
        	ForgeDirection d = chest.getPartnerDir();
        	GL11.glTranslatef(d.offsetX, d.offsetY, d.offsetZ);
        }
        if(i == 3 && chest.getPartnerDir() == ForgeDirection.WEST){
        	ForgeDirection d = chest.getPartnerDir();
        	GL11.glTranslatef(d.offsetX, d.offsetY, d.offsetZ);
        }
        if(i == 5 && chest.getPartnerDir() == ForgeDirection.SOUTH){
        	ForgeDirection d = chest.getPartnerDir();
        	GL11.glTranslatef(-d.offsetX, -d.offsetY, -d.offsetZ);
        }
        if(i == 4 && chest.getPartnerDir() == ForgeDirection.NORTH){
        	ForgeDirection d = chest.getPartnerDir();
        	GL11.glTranslatef(-d.offsetX, -d.offsetY, -d.offsetZ);
        }
        
        GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float f1 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * f;
        float f2;
        
        /*if (chest.getPartnerDir() == ForgeDirection.NORTH)
        {
            //f2 = p_147500_1_.adjacentChestZNeg.prevLidAngle + (p_147500_1_.adjacentChestZNeg.lidAngle - p_147500_1_.adjacentChestZNeg.prevLidAngle) * p_147500_8_;
        	f2 = chest.getPartner().prevLidAngle + (chest.getPartner().lidAngle - chest.getPartner().prevLidAngle) * f;
        	
            if (f2 > f1)
            {
                f1 = f2;
            }
        }

        if (chest.getPartnerDir() == ForgeDirection.WEST)
        {
            //f2 = p_147500_1_.adjacentChestXNeg.prevLidAngle + (p_147500_1_.adjacentChestXNeg.lidAngle - p_147500_1_.adjacentChestXNeg.prevLidAngle) * p_147500_8_;
            f2 = chest.getPartner().prevLidAngle + (chest.getPartner().lidAngle - chest.getPartner().prevLidAngle) * f;

            if (f2 > f1)
            {
                f1 = f2;
            }
        }*/
        
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
        modelchest.renderAll();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
	}

}
