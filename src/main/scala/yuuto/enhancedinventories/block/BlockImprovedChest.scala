package yuuto.enhancedinventories.block

import java.util.List
import net.minecraft.block.material.Material
import yuuto.enhancedinventories.block.base.BlockBaseEI
import yuuto.enhancedinventories.block.traits.TBlockDecorative
import yuuto.enhancedinventories.block.traits.TBlockInventoryUpgradeable
import yuuto.enhancedinventories.block.traits.TBlockSecurable
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.materials.ETier
import yuuto.enhancedinventories.tile.TileImprovedChest
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.IBlockAccess
import yuuto.enhancedinventories.materials.DecorationHelper
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraft.init.Blocks
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.item.Item
import yuuto.enhancedinventories.compat.refinedrelocation.RefinedRelocationHelper
import yuuto.enhancedinventories.EnhancedInventories
import cpw.mods.fml.relauncher.Side
import yuuto.yuutolib.block.traits.TBlockContainerRotatable
import cpw.mods.fml.common.FMLCommonHandler
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister
import yuuto.enhancedinventories.config.EIConfiguration

/**
 * @author Jacob
 */
class BlockImprovedChest(name:String) extends BlockBaseEI(Material.wood, name)
  with TBlockDecorative with TBlockInventoryUpgradeable with TBlockSecurable with TBlockContainerRotatable{
  this.setHardness(2.1f);
  this.setResistance(10);
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity=new TileImprovedChest(meta);
  
  @SideOnly(Side.CLIENT)
  override def getSubBlocks(item:Item, tab:CreativeTabs, subItems:List[_]) {
    //Sets a random wood and wool color for the chest
    var nbt:NBTTagCompound = new NBTTagCompound();
    DecorationHelper.setCoreBlock(nbt, Blocks.planks, this.getRandom().nextInt(4));
    DecorationHelper.setWool(nbt, this.getRandom().nextInt(16));
    //For each tier of chest set a random frame material and add chest to the tab
    for (ix <-0 until 8) {
      val stack:ItemStack = new ItemStack(this, 1, ix);
      DecorationHelper.setFrame(nbt, ETier.values()(ix).getRandomFrameMaterial(this.getRandom()));
      stack.setTagCompound(nbt);
      subItems.asInstanceOf[List[ItemStack]].add(stack);
      nbt = nbt.copy().asInstanceOf[NBTTagCompound];
    }
  }

  //Opens the gui
  override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val tileEntity:TileEntity = world.getTileEntity(x, y, z);
    if (tileEntity == null || player.isSneaking()) {
            return false;
    }
    if(!tileEntity.isInstanceOf[TileImprovedChest])
      return false;
    if(tileEntity.asInstanceOf[TileImprovedChest].canPlayerAccess(player)){
      //Attempts to apply sorting upgrade
      if(EIConfiguration.moduleRefinedRelocation && player.getHeldItem() != null && RefinedRelocationHelper.isSortingUpgrade(player.getHeldItem())){
        val l:TileImprovedChest = tileEntity.asInstanceOf[TileImprovedChest];
        if(l.isUpgradeValid(player.getHeldItem(), player)){
          if(l.addUpgrade(player.getHeldItem(), player)){
            if(!player.capabilities.isCreativeMode)
              player.getHeldItem().stackSize-=1;
          }
        }
        return true;
      }
      if(!world.isRemote)
        player.openGui(EnhancedInventories, 0, world, x, y, z);
      return true;
    }
    return false;
  }

  //Makes sure the chest can be placed
  def canPlaceBlockAt(itemBlock:ItemStack, world:World, x:Int, y:Int, z:Int):Boolean={
      
    val dirs:Array[ForgeDirection] = TileImprovedChest.validDirections;
    var chests:Int = 0;
    for(dir <- dirs){
      val tile:TileEntity = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
      if(tile == null || !tile.isInstanceOf[TileImprovedChest]){}
      else{
        val chest:TileImprovedChest = tile.asInstanceOf[TileImprovedChest];
        if(!chest.isValidForConnection(itemBlock)){}
        else if(chest.isConnected()){return false;}
        else{chests+=1;}
      }
    }
    if(chests > 1)
      return false;
    return true;
  }
  
  override def setBlockBoundsBasedOnState(world:IBlockAccess, x:Int, y:Int, z:Int){
    val tile:TileEntity=world.getTileEntity(x, y, z);
    if(tile==null || !tile.isInstanceOf[TileImprovedChest]){
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      return;
    }
    val chest:TileImprovedChest = tile.asInstanceOf[TileImprovedChest];
    if(!chest.isConnected()){
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
      return;
    }
    if(chest.getPartnerDirection() == ForgeDirection.NORTH)
      this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
    else if(chest.getPartnerDirection() == ForgeDirection.SOUTH)
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
    else if(chest.getPartnerDirection() == ForgeDirection.WEST)
      this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    else if(chest.getPartnerDirection() == ForgeDirection.EAST)
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
    else
      this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  }
  override def getCollisionBoundingBoxFromPool(world:World, x:Int, y:Int, z:Int):AxisAlignedBB={
    val tile:TileEntity = world.getTileEntity(x, y, z);
    if(tile == null || !tile.isInstanceOf[TileImprovedChest])
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0625F, x+0.9375F, y+0.875F,z+ 0.9375F);
    val chest:TileImprovedChest=tile.asInstanceOf[TileImprovedChest];
    if(!chest.isConnected())
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0625F, x+0.9375F, y+0.875F,z+ 0.9375F);
    if(chest.getPartnerDirection() == ForgeDirection.NORTH)
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0F, x+0.9375F, y+0.875F,z+ 0.9375F);
    else if(chest.getPartnerDirection() == ForgeDirection.SOUTH)
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0625F, x+0.9375F, y+0.875F, z+1.0F);
    else if(chest.getPartnerDirection() == ForgeDirection.WEST)
      return AxisAlignedBB.getBoundingBox(x+0.0F, y+0.0F, z+0.0625F, x+0.9375F, y+0.875F, z+0.9375F);
    else if(chest.getPartnerDirection() == ForgeDirection.EAST)
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0625F, x+1.0F, y+0.875F, z+0.9375F);
    else
      return AxisAlignedBB.getBoundingBox(x+0.0625F, y+0.0F, z+0.0625F, x+0.9375F, y+0.875F, z+0.9375F);
  } 
    
  override def getSelectedBoundingBoxFromPool(world:World, x:Int, y:Int, z:Int):AxisAlignedBB=this.getCollisionBoundingBoxFromPool(world, x, y, z);
  
  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(reg:IIconRegister)={}
  
  @SideOnly(Side.CLIENT)
  override def getIcon(side:Int, meta:Int):IIcon={
    return Blocks.planks.getIcon(side, meta);
  }
}