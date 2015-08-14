package yuuto.enhancedinventories.block

import java.util.List
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.tileentity.TileEntity
import yuuto.enhancedinventories.tile.TileCabinet
import net.minecraft.world.World
import net.minecraft.block.material.Material
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.creativetab.CreativeTabs
import yuuto.enhancedinventories.materials.DecorationHelper
import net.minecraft.init.Blocks
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.materials.ETier
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.compat.refinedrelocation.RefinedRelocationHelper
import yuuto.enhancedinventories.EnhancedInventories
import net.minecraft.world.IBlockAccess
import net.minecraft.util.AxisAlignedBB
import yuuto.enhancedinventories.block.traits.TBlockSecurable
import yuuto.enhancedinventories.block.traits.TBlockDecorative
import yuuto.enhancedinventories.block.traits.TBlockInventoryUpgradeable
import yuuto.enhancedinventories.block.base.BlockBaseEI
import cpw.mods.fml.relauncher.Side
import yuuto.enhancedinventories.block.traits.TBlockReverseable
import net.minecraft.util.IIcon
import net.minecraft.client.renderer.texture.IIconRegister
import yuuto.enhancedinventories.config.EIConfiguration

/**
 * @author Jacob
 */
class BlockCabinet(name:String) extends BlockBaseEI(Material.wood, name)
  with TBlockReverseable with TBlockDecorative with TBlockInventoryUpgradeable with TBlockSecurable{
  this.setHardness(2.1f);
  this.setResistance(10);  
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity=new TileCabinet(meta);
  
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
    if(!tileEntity.isInstanceOf[TileCabinet])
      return false;
    if(tileEntity.asInstanceOf[TileCabinet].canPlayerAccess(player)){
      //Attempts to apply sorting upgrade
      if(EIConfiguration.moduleRefinedRelocation && player.getHeldItem() != null && RefinedRelocationHelper.isSortingUpgrade(player.getHeldItem())){
        val l:TileCabinet = tileEntity.asInstanceOf[TileCabinet];
        if(l.isUpgradeValid(player.getHeldItem(), player)){
          if(l.addUpgrade(player.getHeldItem(), player)){
            if(!player.capabilities.isCreativeMode)
              player.getHeldItem().stackSize-=1;
          }
        }
        return true;
      }
      if(world.isRemote)
        return true;
      //if the top is clicked open a crafting grid
      if(side == 1)
        player.openGui(EnhancedInventories, 1, world, x, y, z);
      //Otherwise open the inventory
      else
        player.openGui(EnhancedInventories, 0, world, x, y, z);
      return true;
    }
    return false;
  }

  //Makes sure the chest can be placed
  def canPlaceBlockAt(itemBlock:ItemStack, world:World, x:Int, y:Int, z:Int):Boolean={
      
    val dirs:Array[ForgeDirection] = TileCabinet.validDirections;
    var chests:Int = 0;
    for(dir <- dirs){
      val tile:TileEntity = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
      if(tile == null || !tile.isInstanceOf[TileCabinet]){}
      else{
        val chest:TileCabinet = tile.asInstanceOf[TileCabinet];
        if(!chest.isValidForConnection(itemBlock)){}
        else if(chest.isConnected()){return false;}
        else{chests+=1;}
      }
    }
    if(chests > 1)
      return false;
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(reg:IIconRegister)={}
  
  @SideOnly(Side.CLIENT)
  override def getIcon(side:Int, meta:Int):IIcon={
    return Blocks.planks.getIcon(side, meta);
  }
  
}