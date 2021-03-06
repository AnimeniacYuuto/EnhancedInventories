package yuuto.enhancedinventories.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IIcon
import net.minecraft.world.World
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.block.base.BlockBaseEI
import yuuto.enhancedinventories.tile.TilePainter
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly;
import yuuto.enhancedinventories.tile.TilePainter

class BlockPainter(name:String) extends BlockBaseEI(Material.rock, name){
  this.setHardness(2.1f);
  this.setResistance(10); 
  val icons:Array[IIcon]=new Array[IIcon](2);

  override def createNewTileEntity(world:World, meta:Int):TileEntity={
    return new TilePainter();
  }
  
  override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val tileEntity:TileEntity = world.getTileEntity(x, y, z);
    if (tileEntity == null || player.isSneaking()) {
            return false;
    }
    if(!world.isRemote)
      player.openGui(EnhancedInventories, 4, world, x, y, z);
    return true;
  }
  
  @Override
  override def breakBlock(world:World, x:Int, y:Int, z:Int, block:Block,  meta:Int){
    val tile:TileEntity = world.getTileEntity(x, y, z);
    
    if (tile != null && tile.isInstanceOf[TilePainter])
    {
      val table:TilePainter = tile.asInstanceOf[TilePainter];
      dropItemsFromInventory(table.getInventory(), world, x, y, z, block, meta);
    }

    super.breakBlock(world, x, y, z, block, meta);
  }
  
  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(reg:IIconRegister){
    icons(0) = reg.registerIcon(this.getTextureName()+"Top");
    icons(1) = reg.registerIcon(this.getTextureName()+"Side");
    this.blockIcon = icons(0);
  }
  
  @SideOnly(Side.CLIENT)
  override def getIcon(meta:Int, side:Int):IIcon={
    if(meta == 1)
      return icons(0);
    if(meta < 6)
      return icons(1);
    return this.blockIcon;
  }

}