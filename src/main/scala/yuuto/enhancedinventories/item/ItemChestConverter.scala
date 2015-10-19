package yuuto.enhancedinventories.item

import java.awt.Color
import java.util.List
import java.util.Random
import yuuto.enhancedinventories.util.UpdateHelper
import net.minecraft.client.resources.I18n
import net.minecraft.client.renderer.texture.IIconRegister
import yuuto.enhancedinventories.ref.ReferenceEI
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntityChest
import yuuto.enhancedinventories.util.MinecraftColors
import net.minecraft.tileentity.TileEntity
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.materials.ETier
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.materials.FrameMaterial
import yuuto.enhancedinventories.tile.TileImprovedChest
import net.minecraft.creativetab.CreativeTabs
import yuuto.enhancedinventories.materials.DecorationHelper
import net.minecraft.util.IIcon
import yuuto.enhancedinventories.materials.FrameMaterials
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.init.Blocks
import cpw.mods.fml.relauncher.SideOnly
import yuuto.enhancedinventories.item.base.ItemBaseEI
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.block.Block
import yuuto.enhancedinventories.proxy.ProxyCommon
import net.minecraftforge.common.util.ForgeDirection
import cpw.mods.fml.relauncher.Side

class ItemChestConverter(name:String) extends ItemBaseEI(name){
  this.hasSubtypes = true;
  val frames:Array[IIcon] = new Array[IIcon](3);
  val arrows:Array[IIcon] = new Array[IIcon](8);
  
  @SideOnly(Side.CLIENT)
  override def getSpriteNumber():Int=return 0;
  
  @SideOnly(Side.CLIENT)
  override def getColorFromItemStack(stack:ItemStack, pass:Int):Int={
    if(pass == 1){
      if(!stack.hasTagCompound())
        return 16777215;
      else{
        val mat:FrameMaterial = FrameMaterials.Instance.getMaterial(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME));
        return mat.color().getRGB();
      }
    }else if(pass == 2){
      return MinecraftColors.YELLOW.getColor().getRGB();
    }else{
      return Color.WHITE.getRGB();
    }
  }
  
  override def getIcon(stack:ItemStack, pass:Int):IIcon={
    pass match{
    case 0=>{
      if(!stack.hasTagCompound())
        return Blocks.planks.getIcon(2, 0);
      if(stack.getTagCompound().hasKey("woodType"))
        UpdateHelper.updateInventory(stack);
      val b:Block = {
        val b1:Block = Block.getBlockFromName(stack.getTagCompound().getString(DecorationHelper.KEY_CORE_BLOCK));
        if(b1 != null)b1 else Blocks.planks;
      }
      val meta:Int = stack.getTagCompound().getInteger(DecorationHelper.KEY_CORE_META);
      return b.getIcon(2, meta);
    }
    case 1 => {
      if(!stack.hasTagCompound())
        return this.frames(0);
      else{
        val mat:FrameMaterial = FrameMaterials.Instance.getMaterial(stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME));
        return this.frames(mat.getTextureIndex());
      }
    }
    case 2 =>{
      return this.arrows(stack.getItemDamage());
    }
    }
    return this.itemIcon;
  }
  
  override def getRenderPasses(metadata:Int):Int=if(requiresMultipleRenderPasses()){3}else{1};
  
  @SideOnly(Side.CLIENT)
  override def requiresMultipleRenderPasses():Boolean=true;
  
  override def onUpdate(stack:ItemStack, world:World, enity:Entity, meta:Int, bool:Boolean) {
    if(stack.getItem() != this)
      return;
    if(!stack.hasTagCompound() && stack.getItemDamage() < 8)
      return;
    if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("woodType"))
      return;
    UpdateHelper.updateInventory(stack);
  }
  override def onEntityItemUpdate(entityItem:EntityItem):Boolean={
    val stack:ItemStack = entityItem.getEntityItem();
    if(stack.getItem() != this)
      return false;
    if(!stack.hasTagCompound() && stack.getItemDamage() < 8)
      return false;
    if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("woodType"))
      return false;
    UpdateHelper.updateInventory(stack);
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister){
    frames(0) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameMetal");
    frames(1) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameStone");
    frames(2) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestFrameObsidian");
    this.itemIcon = frames(0);
    for(i <- 0 until arrows.length){
      arrows(i) = reg.registerIcon(ReferenceEI.MOD_ID.toLowerCase()+":chestFrames/chestArrow"+i);
    }
    
  }
  @SideOnly(Side.CLIENT)
  override def getIconIndex(stack:ItemStack):IIcon={
    if(stack.getItem() != this)
      return super.getIconIndex(stack);
    if(!stack.hasTagCompound() && stack.getItemDamage() < 8)
      return super.getIconIndex(stack);
    if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("woodType"))
      return super.getIconIndex(stack);
    UpdateHelper.updateInventory(stack);
    return super.getIconIndex(stack);
  }
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(metadata:Int):IIcon=if(frames != null && metadata < frames.length){frames(metadata)}else{itemIcon};
  
  @SideOnly(Side.CLIENT)
  override def getSubItems(item:Item, tab:CreativeTabs, subItems:List[_]){
    var nbt:NBTTagCompound = new NBTTagCompound();
    DecorationHelper.setCoreBlock(nbt, Blocks.planks, this.getRandom().nextInt(4));
    DecorationHelper.setWool(nbt, this.getRandom().nextInt(16));
    for(i <- 0 until 8){
      val stack:ItemStack = new ItemStack(this, 1, i);
      DecorationHelper.setFrame(nbt, ETier.values()(i).getRandomFrameMaterial(this.getRandom()));
      stack.setTagCompound(nbt);
      subItems.asInstanceOf[List[ItemStack]].add(stack);
      nbt = nbt.copy().asInstanceOf[NBTTagCompound];
    }
  }
  
  override def getUnlocalizedName(stack:ItemStack):String=this.getUnlocalizedName()+"."+stack.getItemDamage();
  
  override def getMetadata (damageValue:Int):Int=damageValue;
  
  override def onItemUse(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    stack.getItem().onUpdate(stack, world, player, side, false);
    if(!player.isSneaking()){
      return false;
    }
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(!tile.isInstanceOf[TileEntityChest])
      return false;
    val newChest:TileImprovedChest = new TileImprovedChest(stack.getItemDamage());
    if(stack.hasTagCompound()){
      newChest.decor.readFromNBT(stack.getTagCompound());
      newChest.painted=stack.getTagCompound().getBoolean(DecorationHelper.KEY_PAINTED);
      newChest.alternate=stack.getTagCompound().getBoolean(DecorationHelper.KEY_ALTERNATE);
    }
    if(!canPlace(newChest, world, x, y, z))
      return false;
    val oldChest:TileEntityChest = tile.asInstanceOf[TileEntityChest];
    val stacks:Array[ItemStack] = newChest.getContents();
    for(i <- 0 until oldChest.getSizeInventory() if(i < stacks.length)){
      stacks(i) = oldChest.getStackInSlotOnClosing(i);
    }
    newChest.setRotation(ForgeDirection.getOrientation( world.getBlockMetadata(x, y, z)));
    val trapped:Boolean = world.getBlock(x, y, z).canProvidePower();
    world.setBlockToAir(x, y, z);
    world.setBlock(x, y, z, ProxyCommon.blockImprovedChest, stack.getItemDamage(), 3);
    world.setTileEntity(x, y, z, newChest);
    world.setBlockMetadataWithNotify(x, y, z, stack.getItemDamage(), 3);
    newChest.setUninitialized();
    world.markBlockForUpdate(x, y, z);
    world.func_147479_m(x, y, z);
    newChest.markDirty();
    if(!player.capabilities.isCreativeMode && stack.stackSize > 0){
      stack.stackSize -=1;
      if(trapped)
        player.inventory.addItemStackToInventory(new ItemStack(Blocks.trapped_chest));
      else
        player.inventory.addItemStackToInventory(new ItemStack(Blocks.chest));
      player.inventoryContainer.detectAndSendChanges();
    }
    return false;
  }
  def canPlace(newChest:TileImprovedChest, world:World, x:Int, y:Int, z:Int):Boolean={
    var chests:Int = 0;
    for(dir <- TileImprovedChest.validDirections){
      val tile:TileEntity = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
      if(tile == null || !tile.isInstanceOf[TileImprovedChest]){}
      else{
        val chest:TileImprovedChest = tile.asInstanceOf[TileImprovedChest];
        if(!chest.isValidForConnection(newChest)){}
        else if(chest.isConnected()){return false;}
        else{
          chests+=1;
          if(chests > 1)
            return false;
        }
      }
    }
    return true;
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
    if(stack.getTagCompound().getBoolean(DecorationHelper.KEY_ALTERNATE))
      info.asInstanceOf[List[String]].add(I18n.format("string."+ReferenceEI.MOD_ID.toLowerCase()+":alt"));
  }

}