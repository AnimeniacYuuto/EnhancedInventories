package yuuto.enhancedinventories.item

import java.util.List
import java.util.Random
import yuuto.enhancedinventories.item.base.ItemBaseEI
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.tile.traits.TConnective
import yuuto.enhancedinventories.tile.traits.TDecorative
import net.minecraft.util.IIcon
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.client.resources.I18n
import net.minecraft.client.renderer.texture.IIconRegister
import yuuto.enhancedinventories.tile.TileLocker
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.util.MinecraftColors
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.materials.FrameMaterials
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

class ItemPaintBrush(name:String) extends ItemBaseEI(name){
  this.setMaxDamage(50);
  this.setMaxStackSize(1);
  var writtenIcon:IIcon=null;
  
  override def isDamageable():Boolean=true;
  
  override def onItemUse(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    if(!player.isSneaking() || !stack.hasTagCompound())
      return false;
    
    val te:TileEntity = world.getTileEntity(x, y, z);
    if(!te.isInstanceOf[TDecorative])
      return false;
    val tile:TDecorative = te.asInstanceOf[TDecorative];
    val colored:Boolean = tile.decor.doseUseColor();
    val info:DecorationInfo = this.getNewDecor(stack, tile, colored);
    if(info.matches(tile.decor))
      return false;
    val connective:Boolean = tile.isInstanceOf[TConnective];
    if(connective){
      val alteredStack:ItemStack = tile.asInstanceOf[TConnective].getItemStack();
      if(!alteredStack.hasTagCompound())
        stack.setTagCompound(new NBTTagCompound());
      info.writeToNBT(alteredStack.getTagCompound());
      if(tile.isInstanceOf[TileLocker]){
        if(!checkValidity(tile.asInstanceOf[TConnective], alteredStack, world, x, y, z, tile.asInstanceOf[TileLocker].reversed))
          return false;
      }else{
        if(!checkValidity(tile.asInstanceOf[TConnective], alteredStack, world, x, y, z))
          return false;
      }
      tile.asInstanceOf[TConnective].disconnect();
    }
    tile.decor.coreBlock = info.coreBlock;
    tile.decor.coreMetadata = info.coreMetadata;
    tile.decor.frameMaterial = info.frameMaterial;
    if(colored)
      tile.decor.decColor = info.decColor;
    tile.painted_$eq(true);
    if(connective){
      tile.asInstanceOf[TConnective].checkConnections();
    }
    tile.markDirty();
    tile.getWorldObj().markBlockForUpdate(x, y, z);
    if(!player.capabilities.isCreativeMode){
      stack.attemptDamageItem(1, this.getRandom());
      if(stack.getItemDamage() >= stack.getMaxDamage()){
        player.destroyCurrentEquippedItem();
      }
    }
    return false;
    }
  
  def checkValidity(tile:TConnective, newChest:ItemStack, world:World, x:Int, y:Int, z:Int):Boolean={
    var chests:Int = 0;
    for(dir <- tile.getValidConnections()){
      val te:TileEntity = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
        if(te == null || !te.isInstanceOf[TConnective]){}
        else if(dir == tile.getPartnerDirection()){}
        else{
          val chest:TConnective = te.asInstanceOf[TConnective];
          if(!chest.isValidForConnection(newChest)){}
          else if(chest.isConnected()){
            return false;
          }else{
            chests+=1;
          }
        }
    }
    if(chests > 1)
      return false;
    return true;
  }
  def checkValidity(tile:TConnective, newChest:ItemStack, world:World, x:Int, y:Int, z:Int, reversed:Boolean):Boolean={
    var chests:Int = 0;
    for(dir <- tile.getValidConnections()){
      val te:TileEntity = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
      if(te == null || !te.isInstanceOf[TileLocker]){}
      else{
        val chest:TileLocker = te.asInstanceOf[TileLocker];
        if(!chest.isValidForConnection(newChest, reversed)){}
        else if(chest.isConnected() && dir != tile.getPartnerDirection()){return false;}
        else{
         chests+=1; 
        }
      }
    }
    if(chests > 1)
      return false;
    return true;
  }
  
  def getNewDecor(paintBrush:ItemStack, tile:TDecorative, colored:Boolean):DecorationInfo={
    val ret:DecorationInfo = new DecorationInfo(colored);
    val nbt:NBTTagCompound = paintBrush.getTagCompound();
    ret.coreBlock = if(nbt.hasKey(DecorationHelper.KEY_CORE_BLOCK)){DecorationHelper.readCoreBlock(nbt)}else{tile.decor.coreBlock}
    if(ret.coreBlock == null)
      ret.coreBlock = tile.decor.coreBlock;
    ret.coreMetadata = if(nbt.hasKey(DecorationHelper.KEY_CORE_META)){nbt.getInteger(DecorationHelper.KEY_CORE_META)}else{tile.decor.coreMetadata};
    ret.frameMaterial = if(nbt.hasKey(DecorationHelper.KEY_FRAME_NAME)){FrameMaterials.Instance.getMaterial(nbt.getString(DecorationHelper.KEY_FRAME_NAME))}else{tile.decor.frameMaterial};
    if(colored)
      ret.decColor = if(nbt.hasKey(DecorationHelper.KEY_COLOR_ID)){MinecraftColors.wool(nbt.getInteger(DecorationHelper.KEY_COLOR_ID))}else{tile.decor.decColor};
    return ret;
  }
  
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister){
      this.itemIcon = reg.registerIcon(this.getIconString());
      this.writtenIcon = reg.registerIcon(this.getIconString()+"Painted");
  }
  
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(damage:Int):IIcon=if(damage==1){writtenIcon}else{itemIcon};
  @SideOnly(Side.CLIENT)
  override def getIconIndex(stack:ItemStack):IIcon={
      return this.getIconFromDamage(if(stack.hasTagCompound()){1}else{0});        
  }
  
  @SideOnly(Side.CLIENT)
  override def addInformation(stack:ItemStack, player:EntityPlayer, info:List[_], bool:Boolean) {
    super.addInformation(stack, player, info, bool);
    if(!stack.hasTagCompound())
      return;
    val dec:DecorationInfo = new DecorationInfo(stack.getTagCompound(), true);
    val coreStack:ItemStack = new ItemStack(dec.coreBlock, 1, dec.coreMetadata);
    info.asInstanceOf[List[String]].add(coreStack.getDisplayName());
    info.asInstanceOf[List[String]].add(I18n.format(dec.frameMaterial.getID()));
    info.asInstanceOf[List[String]].add(I18n.format(dec.decColor.getUnlocalizedName()));
  }

}