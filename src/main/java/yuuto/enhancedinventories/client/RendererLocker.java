package yuuto.enhancedinventories.client;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.client.models.ModelLockerDouble;
import yuuto.enhancedinventories.client.models.ModelLockerSingle;
import yuuto.enhancedinventories.tile.TileLocker;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class RendererLocker extends TileEntitySpecialRenderer{

	static ModelLockerDouble doubleLockerRenderer = new ModelLockerDouble();
	static ModelLockerSingle singleLockerRenderer = new ModelLockerSingle();
	static ResourceLocation singleChestFrame = new ResourceLocation("enhancedinventories", "textures/uvs/normalLockerFrame.png");
	static ResourceLocation doubleChestFrame = new ResourceLocation("enhancedinventories", "textures/uvs/doubleLockerFrame.png");
	static ResourceLocation doubleRefinedLocation;
	 static ResourceLocation singleRefinedLocation;
	public RendererLocker(){
    	super();
    	if(EnhancedInventories.refinedRelocation){
    		singleRefinedLocation = new ResourceLocation("enhancedinventories", "textures/uvs/refinedRelocation/normalLocker.png");
    		doubleRefinedLocation = new ResourceLocation("enhancedinventories", "textures/uvs/refinedRelocation/doubleLocker.png");
    	}
    }
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x,
			double y, double z, float f) {
		if(!(tile instanceof TileLocker))
			return;
		TileLocker locker = (TileLocker)tile;
		if(locker.getPartner() != null){
			renderDouble((TileLocker)tile, x, y, z, f, 0);
			renderDouble((TileLocker)tile, x, y, z, f, 1);
			if(((TileLocker)tile).sortingChest)
				renderDouble((TileLocker)tile, x, y, z, f, 2);
		}else if(!locker.getTopSides().contains(locker.getPartnerDir())){
			renderSingle((TileLocker) tile, x, y, z, f, 0);
			renderSingle((TileLocker) tile, x, y, z, f, 1);
			if(((TileLocker)tile).sortingChest)
				renderSingle((TileLocker)tile, x, y, z, f, 2);
		}
			
		
	}
	void renderSingle(TileLocker chest, double x, double y, double z, float f, int pass) {
		int i = chest.getOrientation().ordinal();
		short short1 = 0;
		GL11.glPushMatrix();
		
		GL11.glTranslated(x+0.5d, y+0.5d, z+0.502d);
		switch (pass){
	    case 0:
	    	bindTexture(WoodTypes.getWoodType(chest.woodType).getTexture(0));
	    	break;
	    case 1:
	    	if(chest.getType().hasTexture())
	    		bindTexture(chest.getType().getTexture(2));
		    else{
		    	bindTexture(singleChestFrame);
		    	GL11.glColor4f(chest.getType().r(), chest.getType().g(), chest.getType().b(), 1f);
		    }
	    	break;
	    case 2:
	    	bindTexture(singleRefinedLocation);
	    	break;
    	default:
    		break;
	    }
	    if (i == 2)
        {
            short1 = 90;
        }

        if (i == 3)
        {
            short1 = -90;
        }

        if (i == 4)
        {
            short1 = 180;
        }

        if (i == 5)
        {
            short1 = 0;
        }
	     
	     GL11.glRotatef(short1, 0, 1, 0);
	     
	     float f1 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * f;
	     f1 = 1.0F - f1;
	     f1 = 1.0F - f1 * f1 * f1;
	     singleLockerRenderer.setRotation(-(f1 * (float)Math.PI / 2.0F) * (180F / (float)Math.PI));
	     if(chest.reversed)
	    	 singleLockerRenderer.renderAllReversed();
	     else
	    	 singleLockerRenderer.renderAll();
	     // OpenGL stuff to put everything back
	     GL11.glPopMatrix();
		
		
	}
	
	void renderDouble(TileLocker chest, double x, double y, double z, float f, int pass) {
		int i = chest.getOrientation().ordinal();
		short short1 = 0;
		GL11.glPushMatrix();
	    
	    switch (pass){
	    case 0:
	    	bindTexture(WoodTypes.getWoodType(chest.woodType).getTexture(0));
	    	break;
	    case 1:
	    	if(chest.getType().hasTexture())
	    		bindTexture(chest.getType().getTexture(3));
		    else{
		    	bindTexture(doubleChestFrame);
		    	GL11.glColor4f(chest.getType().r(), chest.getType().g(), chest.getType().b(), 1f);
		    }
	    	break;
	    case 2:
	    	bindTexture(doubleRefinedLocation);
	    	break;
    	default:
    		break;
	    }
	    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	    if(chest.getPartnerDir() == ForgeDirection.UP)
			GL11.glTranslated(x+0.49375d, y+1.0d, z+0.50825d);
	    if(chest.getPartnerDir() == ForgeDirection.DOWN)
	     	GL11.glTranslated(x+0.49375d, y-0d, z+0.50825d);
	    if (i == 2)
        {
            short1 = 90;
        }

        if (i == 3)
        {
            short1 = -90;
        }

        if (i == 4)
        {
            short1 = 180;
        }

        if (i == 5)
        {
            short1 = 0;
        }
	     
	     GL11.glRotatef(short1, 0, 1, 0);
	     
	     float f1 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * f;
	     f1 = 1.0F - f1;
	     f1 = 1.0F - f1 * f1 * f1;
	     doubleLockerRenderer.setRotation(-(f1 * (float)Math.PI / 2.0F) * (180F / (float)Math.PI));
	     if(chest.reversed)
	    	 doubleLockerRenderer.renderAllReversed();
	     else
	    	 doubleLockerRenderer.renderAll();
	     // OpenGL stuff to put everything back
	     GL11.glColor4f(1f, 1f, 1f, 1f);
	     GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	     GL11.glPopMatrix();
		
		
	}

}
