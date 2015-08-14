/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client.renderer

import java.awt.Color
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelChest
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import org.lwjgl.opengl.GL11
import yuuto.enhancedinventories.client.renderer.model.ModelImprovedChestCore
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.materials.FrameMaterials
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.util.UpdateHelper
import net.minecraftforge.client.IItemRenderer.ItemRenderType
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.materials.FrameMaterial
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.ref.ReferenceEI

object ItemImprovedChestRenderer{
  final val instance:ItemImprovedChestRenderer = new ItemImprovedChestRenderer();
}
class ItemImprovedChestRenderer extends IItemRenderer{
  
  //Textures
  val woolTexture:ResourceLocation = new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestWool.png");
  val frameTextures:Array[ResourceLocation] = Array(
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrame.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrameStone.png"),
      new ResourceLocation(ReferenceEI.MOD_ID.toLowerCase(), "textures/uvs/normalChestFrameObsidian.png")
  );
  
  //Models
  val modelSingleCore:ModelImprovedChestCore = new ModelImprovedChestCore();
  val modelChest:ModelChest = new ModelChest();
  
  //Misc
  val mc:Minecraft = Minecraft.getMinecraft();
  val icons:Array[IIcon] = new Array[IIcon](6);

  //stops weird lighting issues
  this.modelSingleCore.useBrightness = false;
  
  //Makes sure that this can hande the render type
  override def handleRenderType(item:ItemStack, t:ItemRenderType):Boolean={
    if(item.getItem() != Item.getItemFromBlock(ProxyCommon.blockImprovedChest) &&
        item.getItem() != Item.getItemFromBlock(ModuleRefinedRelocation.sortingImprovedChest))
      return false;
    if(!item.hasTagCompound() && item.getItemDamage() < 8)
      return t != ItemRenderType.FIRST_PERSON_MAP;
    if(item.hasTagCompound() && !item.getTagCompound().hasKey("woodType"))
      return t != ItemRenderType.FIRST_PERSON_MAP;
    UpdateHelper.updateInventory(item);
    if(item.getItem() != Item.getItemFromBlock(ProxyCommon.blockImprovedChest) &&
        item.getItem() != Item.getItemFromBlock(ModuleRefinedRelocation.sortingImprovedChest))
      return false;
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
  
  //Renders the item
  override def renderItem(t:ItemRenderType, item:ItemStack, data:Object*) {
    t match{
    case ItemRenderType.ENTITY=>renderItem(item, -0.5f,-0.6f,-0.9f);
    case ItemRenderType.EQUIPPED=>renderItem(item, -1,-1,-1);
    case ItemRenderType.EQUIPPED_FIRST_PERSON=>renderItem(item, -1,-1,-1);
    case ItemRenderType.INVENTORY=>renderItem(item, 0,-0.1f,0);
    case default=>return;
    }
  }
  //Render the item with offset
  def renderItem(item:ItemStack, x:Float, y:Float, z:Float){
    //Get item decoration info
    var info:DecorationInfo = null;
    if(item.hasTagCompound()){
      info = new DecorationInfo(item.getTagCompound(), true);
    }else
      info = new DecorationInfo(true);
    //Adjust render attributes
    GL11.glPushMatrix();
    GL11.glEnable(GL11.GL_ALPHA_TEST);
    GL11.glRotatef(-90, 0, 1, 0);
    GL11.glRotatef(180, 0, 0, 1);
    GL11.glTranslatef(x, y, z);
    //Render the chest
    renderCore(info, x,y,z);
    renderFrame(info, x,y,z);
    renderWool(info, x,y,z);
    GL11.glPopMatrix();
  }
  def renderCore(info:DecorationInfo, x:Float, y:Float, z:Float){
    GL11.glPushMatrix();
    GL11.glColor4f(1, 1, 1, 1);
    GL11.glTranslated(0.01, 0.14f, 0.01);
    GL11.glScalef(0.98f, 0.98f, 0.98f);
    
    //renders the core block of the chest
    val t:Tessellator = Tessellator.instance;
    if(info.coreBlock != null){
      val b:Block = info.coreBlock;
      val meta:Int = info.coreMetadata;
      bindTexture(TextureMap.locationBlocksTexture);
      for(i <-0 until icons.length){
        icons(i) = b.getIcon(i, meta);
      }
      modelSingleCore.drawChestCore(icons, t);
    }
    
    GL11.glPopMatrix();
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderFrame(info:DecorationInfo, x:Float, y:Float, z:Float){
    //gets valid fame material and color
    var mat:FrameMaterial = info.frameMaterial;
    if(mat == null)
      mat = FrameMaterials.Stone;
    val c:Color = mat.color();
    
    GL11.glPushMatrix();
    
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
    
    //render chest frame
    this.bindTexture(frameTextures(mat.getTextureIndex()));
    this.modelChest.renderAll();
    
    GL11.glPopMatrix();
    GL11.glColor4f(1, 1, 1, 1);
  }
  def renderWool(info:DecorationInfo, x:Float, y:Float, z:Float){
    //gets wool color
    val c:Color = info.decColor.getColor();
    
    GL11.glPushMatrix();
    
    GL11.glColor4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, c.getAlpha()/255f);
    
    //render chest wool
    this.bindTexture(woolTexture);
    this.modelChest.renderAll();
      
    GL11.glPopMatrix();
    GL11.glColor4f(1, 1, 1, 1);
  }
  protected def bindTexture(texture:ResourceLocation){
    this.mc.renderEngine.bindTexture(texture);
  }

}