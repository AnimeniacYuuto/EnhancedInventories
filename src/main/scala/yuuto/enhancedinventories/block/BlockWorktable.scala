package yuuto.enhancedinventories.block

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.block.base.BlockBaseEI;
import yuuto.enhancedinventories.tile.TileWorktable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

class BlockWorktable(name:String) extends BlockBaseEI(Material.rock, name){
  this.setHardness(2.1f);
  this.setResistance(10); 
  val icons:Array[IIcon] = new Array[IIcon](4);
  
  override def createNewTileEntity(world:World, meta:Int):TileEntity=new TileWorktable();
  
  override def onBlockActivated(world:World, x:Int, y:Int, z:Int, player:EntityPlayer, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val tileEntity:TileEntity = world.getTileEntity(x, y, z);
    if (tileEntity == null || player.isSneaking()) {
            return false;
    }
    if(!world.isRemote)
      player.openGui(EnhancedInventories, 2, world, x, y, z);
    return true;
  }
  override def breakBlock(world:World, x:Int, y:Int, z:Int, block:Block,  meta:Int){
    val tile:TileEntity = world.getTileEntity(x, y, z);
    
    if (tile != null && tile.isInstanceOf[TileWorktable])
    {
      val table:TileWorktable = tile.asInstanceOf[TileWorktable];
      dropItemsFromInventory(table.getCraftingMatrix(), world, x, y, z, block, meta);
      dropItemsFromInventory(table.getInternalInventory(), world, x, y, z, block, meta);
    }

    super.breakBlock(world, x, y, z, block, meta);
  }
  
  @SideOnly(Side.CLIENT)
  override def registerBlockIcons(reg:IIconRegister){
    icons(0) = reg.registerIcon(this.getTextureName()+"Top");
    icons(1) = reg.registerIcon(this.getTextureName()+"Front");
    icons(2) = reg.registerIcon(this.getTextureName()+"Side");
    icons(3) = reg.registerIcon(this.getTextureName()+"Bottom");
    this.blockIcon = icons(0);
  }
  
  @SideOnly(Side.CLIENT)
  override def getIcon(meta:Int, side:Int):IIcon={
    if(meta == 0)
      return icons(3);
    if(meta == 1)
      return icons(0);
    if(meta == 2 || meta == 3 || meta == 4 || meta == 5)
      return icons(2);
    return this.blockIcon;
  }

}