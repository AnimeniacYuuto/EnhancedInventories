package yuuto.enhancedinventories.item

import java.awt.Color
import java.util.List
import java.util.Random
import yuuto.enhancedinventories.util.UpdateHelper
import yuuto.enhancedinventories.tile.traits.TInventoryConnectiveUpgradeable
import net.minecraft.client.resources.I18n
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.util.MinecraftColors
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.materials.ETier
import yuuto.enhancedinventories.materials.FrameMaterial
import net.minecraft.creativetab.CreativeTabs
import yuuto.enhancedinventories.materials.DecorationHelper
import net.minecraft.util.IIcon
import yuuto.enhancedinventories.materials.FrameMaterials
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import cpw.mods.fml.relauncher.SideOnly
import yuuto.enhancedinventories.item.base.ItemBaseEI
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import cpw.mods.fml.relauncher.Side
import yuuto.enhancedinventories.ref.ReferenceEI

class ItemSizeUpgrade(name:String) extends ItemBaseEI(name){
  this.hasSubtypes = true;
  val frames:Array[IIcon]= new Array[IIcon](3);
  val crosses:Array[IIcon] = new Array[IIcon](7);
  
  override def onUpdate(stack:ItemStack, world:World, entity:Entity, meta:Int, bool:Boolean){
    if(stack.getItem() != this || stack.hasTagCompound()){
      super.onUpdate(stack, world, entity, meta, bool);
      return;
    }
    UpdateHelper.updateSizeUpgrade(stack);
    super.onUpdate(stack, world, entity, meta, bool);
  }
  override def onEntityItemUpdate(entityItem:EntityItem):Boolean={
    val stack:ItemStack = entityItem.getEntityItem();
    if(stack.getItem() != this || stack.hasTagCompound()){
      return super.onEntityItemUpdate(entityItem);
    }
    UpdateHelper.updateSizeUpgrade(stack);
    return super.onEntityItemUpdate(entityItem);
  }
  
  @SideOnly(Side.CLIENT)
  override def getSpriteNumber():Int=0;
  
  @Override
  @SideOnly(Side.CLIENT)
  override def getColorFromItemStack(stack:ItemStack, pass:Int):Int={
    pass match{
      case 0 =>{
        if(!stack.hasTagCompound())
          return Color.WHITE.getRGB();
        else{
          val mat:FrameMaterial = FrameMaterials.Instance.getMaterial(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME));
          return mat.color().getRGB();
        }
      }
      case 1 =>{
        return MinecraftColors.YELLOW.getColor().getRGB();
      }
    }
    return Color.WHITE.getRGB();
  }
  
  override def getIcon(stack:ItemStack, pass:Int):IIcon={
   pass match{
    case 0 => {
      if(!stack.hasTagCompound())
        return this.frames(0);
      else{
        val mat:FrameMaterial = FrameMaterials.Instance.getMaterial(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME));
        return this.frames(mat.getTextureIndex());
      }
    }
    case 1 =>{
      return this.crosses(stack.getItemDamage());
    }
    }
    return this.itemIcon;
  }
  
  override def getRenderPasses(metadata:Int):Int=if(requiresMultipleRenderPasses()){2}else{1};
  
  @SideOnly(Side.CLIENT)
  override def requiresMultipleRenderPasses():Boolean=true;
  
  @Override
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister){
    frames(0) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameMetal");
    frames(1) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameStone");
    frames(2) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameObsidian");
    this.itemIcon = frames(0);
    for(i <- 0 until crosses.length){
      crosses(i) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestCross"+i);
    }
    
  }
  override def getIconIndex(stack:ItemStack):IIcon={
    if(stack.getItem() != this || stack.hasTagCompound())
      return super.getIconIndex(stack);
    UpdateHelper.updateSizeUpgrade(stack);
    if(stack.getItem() != this)
      return stack.getIconIndex();
    return super.getIconIndex(stack);
  }
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(metadata:Int):IIcon=if(frames != null && metadata < frames.length){frames(metadata)}else{itemIcon};
  
  @SideOnly(Side.CLIENT)
  override def getSubItems(item:Item, tab:CreativeTabs, subItems:List[_]){
    for(i <- 0 until 7){
      val nbt:NBTTagCompound=new NBTTagCompound();
      val stack:ItemStack = new ItemStack(this, 1, i);
      DecorationHelper.setFrame(nbt, ETier.values()(i+1).getRandomFrameMaterial(this.getRandom()));
      stack.setTagCompound(nbt);
      subItems.asInstanceOf[List[ItemStack]].add(stack);
    }
  }
  
  override def getUnlocalizedName(stack:ItemStack):String=this.getUnlocalizedName()+"."+stack.getItemDamage();
  override def getMetadata (damageValue:Int):Int=damageValue;
  
  override def onItemUse(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    stack.getItem().onUpdate(stack, world, player, side, false);
    
    if(!player.isSneaking())
      return false;
    val tile:TileEntity= world.getTileEntity(x, y, z);
    if(tile == null || !tile.isInstanceOf[TInventoryConnectiveUpgradeable])
      return false;
    val chest:TInventoryConnectiveUpgradeable = tile.asInstanceOf[TInventoryConnectiveUpgradeable];
    if(!chest.isUpgradeValid(stack, player))
      return false;
    if(chest.isConnected())
      chest.disconnect();
    chest.setUninitialized();
    if(!chest.addUpgrade(stack, player))
      return false;
    if(player.capabilities.isCreativeMode)
      return true;
    stack.stackSize -=1;
    player.inventoryContainer.detectAndSendChanges();
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  override def addInformation(stack:ItemStack, player:EntityPlayer, info:List[_], bool:Boolean) {
    super.addInformation(stack, player, info, bool);
    if(stack.hasTagCompound())
      info.asInstanceOf[List[String]].add(I18n.format(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME)));
    else
      info.asInstanceOf[List[String]].add(I18n.format(FrameMaterials.Stone.getID()));
  }
}