/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei.positionstacks

import codechicken.nei.PositionedStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Item

class ChildPositionStack(parent:ParentPositionStack, x:Int, y:Int) extends PositionedStack(parent.items, x, y){
  parent.addChild(this);
  
  override def generatePermutations()=parent.generatePermutations();
  override def setMaxSize(i:Int)=parent.setMaxSize(i);
  override def setPermutationToRender(index:Int)=parent.setPermutationToRender(index);
  override def contains(ingredient:ItemStack):Boolean=parent.contains(ingredient);
  override def contains(ingred:Item):Boolean=parent.contains(ingred);
}