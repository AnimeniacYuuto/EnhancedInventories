/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer

import java.awt.Color
import net.minecraft.block.Block
import net.minecraft.client.model.ModelChest
import net.minecraft.client.model.ModelLargeChest
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import yuuto.enhancedinventories.client.renderer.model.ModelImprovedChestCore
import yuuto.enhancedinventories.client.renderer.model.ModelImprovedChestDoubleCore
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.tile.TileImprovedChest;
import yuuto.enhancedinventories.ref.ReferenceEI

object TileImprovedChestRenderer{
   final val instance:TileImprovedChestRenderer = new TileImprovedChestRenderer();
}
class TileImprovedChestRenderer extends TileEntitySpecialRenderer{
  
  //Textures
  val woolTexture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestWool.png");
  val woolTexture2:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/doubleChestWool.png");
  val frameTextures:Array[ResourceLocation] = Array(
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrame.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrameStone.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrameObsidian.png")
  );
  val frameTexturesDouble:Array[ResourceLocation] = Array(
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/doubleChestFrame.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/doubleChestFrameStone.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/doubleChestFrameObsidian.png")
  );
  
  //Models
  val modelSingleCore:ModelImprovedChestCore = new ModelImprovedChestCore();
  val modelDoubleCore:ModelImprovedChestDoubleCore = new ModelImprovedChestDoubleCore();
  val modelChest:ModelChest = new ModelChest();
  val modelDoubleChest:ModelLargeChest = new ModelLargeChest();
  
  //Icons
  val icons:Array[IIcon] = new Array[IIcon](6);
  
  override def renderTileEntityAt(tile:TileEntity, x:Double,
      y:Double, z:Double, partialTick:Float) {
    if(tile == null || tile.isInvalid() || !tile.isInstanceOf[TileImprovedChest])
      return;
    val chest:TileImprovedChest = tile.asInstanceOf[TileImprovedChest];
    if(chest.decor == null)
      return;
    GL11.glPushMatrix();
    
    //finds rotation
    var angle:Float = 0;
    chest.facing match{
    case ForgeDirection.SOUTH=>angle = 180f;
    case ForgeDirection.WEST=>angle = 90f;
    case ForgeDirection.EAST=>angle = -90f;
    case default=>{}
    }
    
    //finds door rotaion
    var f1:Float = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTick;
    f1 = 1.0F - f1;
    f1 = 1.0F - f1 * f1 * f1;
    f1 = (f1 * Math.PI.asInstanceOf[Float]/ 2.0F);
    
    //Set Gl11 state
    GL11.glTranslated(x+0.5, y+0.5, z+0.5);
    GL11.glRotatef(angle, 0, 1, 0);
    GL11.glTranslated(-0.5, -0.5, -0.5);
    //render connected
    if(chest.isConnected()){
      if(chest.isMain()){
        val x1:Int = if(chest.facing == ForgeDirection.EAST  || chest.facing == ForgeDirection.NORTH){1}else{0};
        renderCore(chest, x1, 0, 0, partialTick, f1, true);
        renderFrame(chest, x1, 0, 0, partialTick, f1, true);
        renderWool(chest, x1, 0, 0, partialTick, f1, true);
      }
    //render single
    }else{
      renderCore(chest, 0, 0, 0, partialTick, f1, false);
      renderFrame(chest, 0, 0, 0, partialTick, f1, false);
      renderWool(chest, 0, 0, 0, partialTick, f1, false);
    }
    GL11.glPopMatrix();
    
  }
  def renderCore(inv:TileImprovedChest, x:Double,
      y:Double, z:Double, partialTick:Float, doorAngle:Float, doubleChest:Boolean){
    GL11.glPushMatrix();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
    GL11.glTranslated(x, y, z);
    
    //Render core block
    val t:Tessellator = Tessellator.instance;
    if(inv.decor.coreBlock != null){
      val b:Block = inv.decor.coreBlock;
      val meta:Int = inv.decor.coreMetadata;
      bindTexture(TextureMap.locationBlocksTexture);
      for(i <-0 until icons.length){
        icons(i) = b.getIcon(i, meta);
      }
      if(doubleChest){
        modelDoubleCore.rotaionAngle = doorAngle;
        modelDoubleCore.lightValue = b.getMixedBrightnessForBlock(inv.getWorldObj(), inv.xCoord, inv.yCoord, inv.zCoord);
        modelDoubleCore.makeChestCoreList(icons, t);
      }else{
        modelSingleCore.rotaionAngle = doorAngle;
        modelSingleCore.lightValue = b.getMixedBrightnessForBlock(inv.getWorldObj(), inv.xCoord, inv.yCoord, inv.zCoord);
        modelSingleCore.drawChestCore(icons, t);
      }
    }
    GL11.glPopMatrix();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderFrame(inv:TileImprovedChest, x:Double,
      y:Double, z:Double, partialTick:Float, doorAngle:Float, doubleChest:Boolean){
    //Get frame material and color
    var mat:FrameMaterial = inv.decor.frameMaterial;
    if(inv.isReinforced() && !inv.isPainted())
      mat = FrameMaterials.Obsidian;
    if(mat == null)
      mat = FrameMaterials.Stone;
    
    val c:Color = mat.color();
    
    GL11.glPushMatrix();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    GL11.glTranslated(x+1, y+1, z);
    
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);

    //render chest frame
    if(doubleChest){
      GL11.glScalef(-1, -1, 1);
      this.modelDoubleChest.chestLid.rotateAngleX = -doorAngle;
      this.bindTexture(frameTexturesDouble(mat.getTextureIndex()));
      this.modelDoubleChest.renderAll();
    }else{
      GL11.glRotatef(180, 0, 0, 1);
      this.modelChest.chestLid.rotateAngleX = -doorAngle;//-(doorAngle * (float)Math.PI / 2.0F);
      this.bindTexture(frameTextures(mat.getTextureIndex()));
      this.modelChest.renderAll();
    }
    GL11.glPopMatrix();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderWool(inv:TileImprovedChest, x:Double,
      y:Double, z:Double, partialTick:Float, doorAngle:Float, doubleChest:Boolean){
    val c:Color = inv.decor.decColor.getColor();
    
    GL11.glPushMatrix();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
    
    //Render chest wool
    if(doubleChest){
      GL11.glTranslated(x+1, y+1, z);
      GL11.glScalef(-1, -1, 1);
      this.bindTexture(woolTexture2);
      this.modelDoubleChest.chestLid.rotateAngleX = -doorAngle;//-(f1 * (float)Math.PI / 2.0F);
      this.modelDoubleChest.renderAll();
    }else{
      GL11.glTranslated(x+1, y+1, z);
      GL11.glRotatef(180, 0, 0, 1);
      this.bindTexture(woolTexture);
          this.modelChest.chestLid.rotateAngleX = -doorAngle;//-(f1 * (float)Math.PI / 2.0F);
      this.modelChest.renderAll();
    }
    GL11.glPopMatrix();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
  }

}