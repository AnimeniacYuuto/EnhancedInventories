/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei.positionstacks

import codechicken.nei.PositionedStack
import scala.collection.mutable.MutableList
import net.minecraftforge.oredict.OreDictionary
import net.minecraft.item.ItemStack
import net.minecraft.init.Blocks

class ParentPositionStack(obj:Object, x:Int, y:Int, genPerms:Boolean) extends PositionedStack(obj, x, y, genPerms){
  val children:MutableList[ChildPositionStack]=new MutableList[ChildPositionStack]();
  
  def this(obj:Object, x:Int, y:Int)=this(obj, x, y, true);
  
  def addChild(stack:ChildPositionStack){
    children+=stack;
  }
  
  override def setPermutationToRender(index:Int){
      super.setPermutationToRender(index);
      if(children==null || children.size < 1)
        return;
      for(child<-children){
        child.item=this.item;
      }
  }
}