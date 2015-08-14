package yuuto.enhancedinventories.item

import java.util.List
import net.minecraft.client.resources.I18n
import yuuto.enhancedinventories.ref.ReferenceEI
import net.minecraft.entity.player.EntityPlayer
import yuuto.yuutolib.item.ModItemBlockMulti
import yuuto.enhancedinventories.materials.DecorationInfo
import yuuto.enhancedinventories.block.BlockCabinet
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import yuuto.enhancedinventories.materials.DecorationHelper
import cpw.mods.fml.relauncher.Side
import net.minecraft.init.Blocks

/**
 * @author Jacob
 */
class ItemBlockCabinet(block:Block) extends ModItemBlockMulti(block){
  
  override def onItemUse(stack:ItemStack, player:EntityPlayer, world:World, x:Int, y:Int, z:Int, side:Int, hitX:Float, hitY:Float, hitZ:Float):Boolean={
    val block:Block = world.getBlock(x, y, z);
    var side2=side;
    var x1 = x;
    var y1 = y;
    var z1 = z

    if (block == Blocks.snow_layer && (world.getBlockMetadata(x1, y1, z1) & 7) < 1)
    {
        side2 = 1;
    }
    else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x1, y1, z1))
    {
        if (side2 == 0)
        {
            y1-=1;
        }

        if (side2 == 1)
        {
            y1+=1;
        }

        if (side2 == 2)
        {
            z1-=1;
        }

        if (side2 == 3)
        {
            z1+=1;
        }

        if (side2 == 4)
        {
            x1-=1;
        }

        if (side2 == 5)
        {
            x1+=1;
        }
    }

    if (stack.stackSize == 0)
    {
        return false;
    }
    else if (!player.canPlayerEdit(x1, y1, z1, side2, stack))
    {
        return false;
    }
    else if (y1 == 255 && this.field_150939_a.getMaterial().isSolid())
    {
        return false;
    }
    else if(!field_150939_a.asInstanceOf[BlockCabinet].canPlaceBlockAt(stack, world, x1, y1, z1))
    {
      return false;
    }
    else if (world.canPlaceEntityOnSide(this.field_150939_a, x1, y1, z1, false, side2, player, stack))
    {
        val i1:Int = this.getMetadata(stack.getItemDamage());
        val j1:Int = this.field_150939_a.onBlockPlaced(world, x1, y1, z1, side2, hitX, hitY, hitZ, i1);

        if (placeBlockAt(stack, player, world, x1, y1, z1, side2, hitX, hitY, hitZ, j1))
        {
            world.playSoundEffect((x1 + 0.5F), (y1 + 0.5F), (z1 + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
            stack.stackSize-=1;
        }

        return true;
    }
    else
    {
        return false;
    }
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