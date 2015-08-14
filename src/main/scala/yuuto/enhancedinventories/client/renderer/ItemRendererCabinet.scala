/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer

import java.awt.Color
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import org.lwjgl.opengl.GL11
import yuuto.enhancedinventories.client.renderer.model.ModelLockerCore
import yuuto.enhancedinventories.client.renderer.model.ModelLockerSingle
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.materials.FrameMaterials
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import yuuto.enhancedinventories.materials.FrameMaterial
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.ref.ReferenceEI

object ItemRendererCabinet{
  final val instance:ItemRendererCabinet = new ItemRendererCabinet();
}
class ItemRendererCabinet extends IItemRenderer{ 
  
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
  val mc:Minecraft = Minecraft.getMinecraft();
  val icons:Array[IIcon] = new Array[IIcon](6);
  
  //stop render weirdness
  this.coreSingle.useBrightness = false;
  
  override def handleRenderType(item:ItemStack, t:ItemRenderType):Boolean={
    return t != ItemRenderType.FIRST_PERSON_MAP;
  }
  
  //Checks if a render helper should be used
  override def shouldUseRenderHelper(t:ItemRenderType, item:ItemStack, helper:ItemRendererHelper):Boolean={
    t match {
      case ItemRenderType.ENTITY=>{
        return (helper == ItemRendererHelper.ENTITY_BOBBING ||
                  helper == ItemRendererHelper.ENTITY_ROTATION ||
                  helper == ItemRendererHelper.BLOCK_3D);
      }
      case ItemRenderType.EQUIPPED=>{
        return (helper == ItemRendererHelper.BLOCK_3D ||
                helper == ItemRendererHelper.EQUIPPED_BLOCK);
      }
      case ItemRenderType.EQUIPPED_FIRST_PERSON=>{
        return (helper == ItemRendererHelper.EQUIPPED_BLOCK);
      }
      case ItemRenderType.INVENTORY=>{
        return (helper == ItemRendererHelper.INVENTORY_BLOCK);
      }
      case default=>return false;
    }
  }

  override def renderItem(t:ItemRenderType, item:ItemStack, data:Object*) {
    t match{
    case ItemRenderType.ENTITY=>renderItem(item, -0.5f,-0.4f,-0.9f);
    case ItemRenderType.EQUIPPED=>renderItem(item, -1,-1,-1);
    case ItemRenderType.EQUIPPED_FIRST_PERSON=>renderItem(item, -1,-1,-1);
    case ItemRenderType.INVENTORY=>renderItem(item, -0.5f,-0.5f,-0.5f);
    case default=>return;
    }
  }
  //renders item with offset
  def renderItem(item:ItemStack, x:Float, y:Float, z:Float){
    var info:DecorationInfo = null;
    if(item.hasTagCompound()){
      info = new DecorationInfo(item.getTagCompound(), true);
    }else
      info = new DecorationInfo(true);
    //set gl11
    GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    GL11.glRotatef(-90, 0, 1, 0);
    GL11.glTranslatef(x, y, z);
    //render cabinet
    renderCore(info, x,y,z);
    renderFrame(info, x,y,z);
    GL11.glPopMatrix();
  }
  
  def renderCore(info:DecorationInfo, x:Float, y:Float, z:Float){
    GL11.glPushMatrix();
    GL11.glColor4f(1, 1, 1, 1);
    
    //render core block of cabinet & top clay
    val t:Tessellator = Tessellator.instance;
    if(info.coreBlock != null){
      val b:Block = info.coreBlock;
      val meta:Int = info.coreMetadata;
      bindTexture(TextureMap.locationBlocksTexture);
      icons(1) = Blocks.stained_hardened_clay.getIcon(0, info.decColor.getIndex());
      for(i <-0 until icons.length if(i!=1)){
        icons(i) = b.getIcon(i, meta);
      }
      coreSingle.drawLockerCore(icons, t);
    }
    GL11.glPopMatrix();
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderFrame(info:DecorationInfo, x:Float, y:Float, z:Float){
    //get the frame and frame color
    var mat:FrameMaterial = info.frameMaterial;
    if(mat == null)
      mat = FrameMaterials.Stone;
    val c:Color = mat.color();
    
    GL11.glPushMatrix();;
    
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
    
    //render the cabinet frame
    GL11.glTranslated(0.5, 0.5, 0.5);
    GL11.glRotatef(90, 0, 1, 0);
    this.bindTexture(frameTextures(mat.getTextureIndex()));
    this.lockerModel.renderAll();
    
    GL11.glPopMatrix();
    GL11.glColor4f(1, 1, 1, 1);
  }
  
  protected def bindTexture(texture:ResourceLocation){
    this.mc.renderEngine.bindTexture(texture);
  }

}