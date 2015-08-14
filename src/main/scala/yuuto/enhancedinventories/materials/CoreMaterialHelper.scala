/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.materials

import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.config.EIConfiguration
import scala.collection.mutable.MutableList
import yuuto.yuutolib.utill.StackIdentifire
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

object CoreMaterialHelper {
  
  val blockList:MutableList[StackIdentifire]=new MutableList[StackIdentifire];
  
  def isValidCoreMaterial(stack:ItemStack):Boolean={
    if(stack == null || stack.getItem() == null || !stack.getItem().isInstanceOf[ItemBlock])
      return false;
    val b:Block = stack.getItem().asInstanceOf[ItemBlock].field_150939_a;
    if(b == null)
      return false;
    if(EIConfiguration.whiteList){
      return isListed(stack) || b==Blocks.planks;
    }else if(isListed(stack)){
      return false;
    }
    if(b.hasTileEntity(stack.getItemDamage()))
      return false;
    return b.isOpaqueCube() || (b.getMaterial().isOpaque() && b.renderAsNormalBlock());
  }
  def getCoreMaterialBlock(stack:ItemStack):Block={
    if(!isValidCoreMaterial(stack))
      return Blocks.planks;
    return stack.getItem().asInstanceOf[ItemBlock].field_150939_a;
  }
  def getCoreMaterialMetadata(stack:ItemStack):Int={
    if(!isValidCoreMaterial(stack))
      return 0;
    return stack.getItemDamage();
  }
  def isListed(stack:ItemStack):Boolean={
    for(f<-blockList){
      if(f.matches(stack)){
        return true;
      }
    }
    return false;
  }
  def loadList(nameList:Array[String]){
    for(n<-nameList){
      val parts:Array[String]=n.split(":");
      if(parts == null || parts.length < 1){}
      else if(parts.length == 1){
        val b:Block=GameRegistry.findBlock("minecraft", parts(0));
        if(b != null && b != Blocks.planks){
          val id:StackIdentifire=new StackIdentifire(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE));
          blockList+=id;
        }
      }else{
        val b:Block=GameRegistry.findBlock(parts(0), parts(1));
        var m:Int=OreDictionary.WILDCARD_VALUE;
        if(parts.size>2){
          m=Integer.parseInt(parts(2));
        }
        if(b != null && b != Blocks.planks){
          val id:StackIdentifire=new StackIdentifire(new ItemStack(b, 1, m));
          blockList+=id;
        }
      }
    }
  }

}