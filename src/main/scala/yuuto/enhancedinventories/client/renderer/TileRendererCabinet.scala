/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer

import yuuto.enhancedinventories.client.renderer.model.ModelLockerCore
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import yuuto.enhancedinventories.tile.TileCabinet
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraft.tileentity.TileEntity
import yuuto.enhancedinventories.materials.FrameMaterial
import org.lwjgl.opengl.GL11
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.IIcon
import yuuto.enhancedinventories.client.renderer.model.ModelLockerSingle
import org.lwjgl.opengl.GL12
import net.minecraft.init.Blocks
import java.awt.Color
import net.minecraft.block.Block
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.ref.ReferenceEI

object TileRendererCabinet{
  final val instance:TileRendererCabinet = new TileRendererCabinet();
}
class TileRendererCabinet extends TileEntitySpecialRenderer{
  
  //Textures
  val frameTextures:Array[ResourceLocation] = Array(
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalLockerFrame.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalLockerFrameStone.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalLockerFrameObsidian.png")
  );
  
  //Modes
  val coreSingle:ModelLockerCore = new ModelLockerCore();
  val lockerModel:ModelLockerSingle = new ModelLockerSingle();
  
  //Misc
  val icons:Array[IIcon] = new Array[IIcon](6);
  
  override def renderTileEntityAt(tile:TileEntity, x:Double,
      y:Double, z:Double, partialTick:Float) {
    if(tile == null || tile.isInvalid() || !tile.isInstanceOf[TileCabinet])
      return;
    val locker:TileCabinet = tile.asInstanceOf[TileCabinet];
    
    //find rotaion
    var angle:Float = 0;
    var reversed:Boolean = false;
    locker.facing match{
    case ForgeDirection.SOUTH=>angle = 180f;
    case ForgeDirection.WEST=>angle = 90f;
    case ForgeDirection.EAST=>{
      reversed = true;
      angle = -90f;
    }
    case default=>reversed = true;
    }
    
    //find door rotaion
    var f1:Float = locker.prevLidAngle + (locker.lidAngle - locker.prevLidAngle) * partialTick;
    f1 = 1.0F - f1;
    f1 = 1.0F - f1 * f1 * f1;
    f1 = (f1 * Math.PI.asInstanceOf[Float] / 2.0F);
    f1 = f1*(180F / Math.PI.asInstanceOf[Float]);
    
        //Set gl11
    GL11.glPushMatrix();
    GL11.glTranslated(x+0.5, y+0.5, z+0.5);
    GL11.glRotatef(angle, 0, 1, 0);
    GL11.glTranslated(-0.5, -0.5, -0.5);
    //render cabinet
    if(locker.isConnected()){
      if(locker.isMain()){
        val x1:Int = if(locker.facing == ForgeDirection.EAST  || locker.facing == ForgeDirection.NORTH){1}else{-1};
        renderCore(locker, 0, 0, 0, partialTick, f1, reversed);
        renderFrame(locker, 0, 0, 0, partialTick, f1, reversed);
        renderCore(locker, x1, 0, 0, partialTick, f1, !reversed);
        renderFrame(locker, x1, 0, 0, partialTick, f1, !reversed);
      }
    }else{
      renderCore(locker, 0, 0, 0, partialTick, f1, locker.reversed);
      renderFrame(locker, 0, 0, 0, partialTick, f1, locker.reversed);
    }
    GL11.glPopMatrix();
  }
  def renderCore(inv:TileCabinet, x:Double,
      y:Double, z:Double, partialTick:Float, doorAngle:Float, reversed:Boolean){
    GL11.glPushMatrix();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
    GL11.glTranslated(x, y, z);
    
    //render core block
    val t:Tessellator = Tessellator.instance;
    if(inv.decor.coreBlock != null){
      val b:Block = inv.decor.coreBlock;
      val meta:Int = inv.decor.coreMetadata;
      bindTexture(TextureMap.locationBlocksTexture);
      icons(1) = Blocks.stained_hardened_clay.getIcon(0, inv.decor.decColor.getIndex());
      for(i <-0 until icons.length if(i != 1)){
        icons(i) = b.getIcon(i, meta);
      }
      coreSingle.reversed = reversed;
      coreSingle.rotaionAngle = -doorAngle;
      coreSingle.lightValue = b.getMixedBrightnessForBlock(inv.getWorldObj(), inv.xCoord, inv.yCoord, inv.zCoord);
      coreSingle.drawLockerCore(icons, t);
    }
    GL11.glPopMatrix();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderFrame(inv:TileCabinet, x:Double,
      y:Double, z:Double, partialTick:Float, doorAngle:Float, reversed:Boolean){
    //get frame and color
    var mat:FrameMaterial = inv.decor.frameMaterial;
    if(inv.isReinforced() && !inv.isPainted())
      mat = FrameMaterials.Obsidian;
    if(mat == null)
      mat = FrameMaterials.Stone;
    val c:Color = mat.color();
    
    GL11.glPushMatrix();
    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    GL11.glTranslated(x, y, z);
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
    
    //render cabinet frame
    GL11.glTranslatef(0.5f, 0.5f, 0.5f);
    GL11.glRotatef(90, 0, 1, 0);
    this.bindTexture(frameTextures(mat.getTextureIndex()));
    this.lockerModel.setRotation(-doorAngle);
    if(reversed)
      this.lockerModel.renderAllReversed();
    else
      this.lockerModel.renderAll();
    
    GL11.glPopMatrix();
    GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    GL11.glColor4f(1, 1, 1, 1);
  }

}