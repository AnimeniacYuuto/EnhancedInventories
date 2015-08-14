/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.config.recipe

import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.ItemStack
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import java.util.ArrayList
import java.util.Iterator
import net.minecraft.item.Item
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.materials.DecorationHelper
import yuuto.enhancedinventories.materials.PaintHelper

object RecipeDecorative{
  private final val MAX_CRAFT_GRID_WIDTH:Int = 3;
  private final val MAX_CRAFT_GRID_HEIGHT:Int = 3;
  private final val DYE_NAMES:Array[String]=Array("dyeBlack","dyeRed","dyeGreen","dyeBrown","dyeBlue",
                      "dyePurple","dyeCyan","dyeLightGray","dyeGray","dyePink","dyeLime","dyeYellow","dyeLightBlue",
                      "dyeMagenta","dyeOrange","dyeWhite");
}
class RecipeDecorative(val output:ItemStack, val input:Array[Object], val width:Int, val height:Int, val mirrored:Boolean,
    val cores:Array[Int], private val frame:Int, val colors:Array[Int], private val oldSlot:Int) extends IRecipe{
    private class DecoStacks(){
      var oldStack:ItemStack=null;
      var coreStack:ItemStack=null
      var frameStack:ItemStack=null;
      var colorStack:ItemStack=null;
    }
    /**
     * Returns an Item that is the result of this recipe
     */
    override def getCraftingResult(inv:InventoryCrafting):ItemStack={
      val ret:ItemStack = getRecipeOutput().copy();
      val decoStacks:DecoStacks=findDecoStacks(inv);
      if(decoStacks==null){
        return ret;
      }
      if(decoStacks.oldStack != null && decoStacks.oldStack.hasTagCompound()){
        ret.setTagCompound(decoStacks.oldStack.getTagCompound().copy().asInstanceOf[NBTTagCompound]);
      }else{
        ret.setTagCompound(new NBTTagCompound());        
      }
      if(!ret.getTagCompound().getBoolean(DecorationHelper.KEY_PAINTED)){
        if(decoStacks.coreStack != null){
          DecorationHelper.setCoreBlock(ret.getTagCompound(), decoStacks.coreStack);
        }
        if(decoStacks.frameStack != null){
          DecorationHelper.setFrame(ret.getTagCompound(), decoStacks.frameStack);
        }
        if(decoStacks.colorStack != null && PaintHelper.isColorable(ret)){
          DecorationHelper.setWool(ret.getTagCompound(), getColor(decoStacks.colorStack))
        }
      }
      return ret;
    }

    /**
     * Returns the size of the recipe area
     */
    override def getRecipeSize():Int=input.length;

    override def getRecipeOutput():ItemStack=output;

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    override def matches(inv:InventoryCrafting, world:World):Boolean={
      for (x <-0 to RecipeDecorative.MAX_CRAFT_GRID_WIDTH - width)
      {
          for (y <-0 to RecipeDecorative.MAX_CRAFT_GRID_HEIGHT - height)
          {
              if (checkMatch(inv, x, y, false))
              {
                  return true;
              }

              if (mirrored && checkMatch(inv, x, y, true))
              {
                  return true;
              }
          }
      }

      return false;
    }

    //@SuppressWarnings("unchecked")
    private def checkMatch(inv:InventoryCrafting, startX:Int, startY:Int, mirror:Boolean):Boolean={
      var core:ItemStack=null;
      var color:Int= -1;
      for (x <-0 to RecipeDecorative.MAX_CRAFT_GRID_WIDTH)
        {
            for (y <-0 to RecipeDecorative.MAX_CRAFT_GRID_HEIGHT)
            {
                val subX:Int = x - startX;
                val subY:Int = y - startY;
                var target:Object = null;
                var index:Int=0;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror){
                        index=width - subX - 1 + subY * width;
                    }
                    else{
                        index=subX + subY * width
                    }
                    target=input(index);
                }

                val slot:ItemStack = inv.getStackInRowAndColumn(x, y);

                if (target == null && slot != null)
                {
                    return false;
                }
                else if (target.isInstanceOf[ItemStack])
                {
                    if (!OreDictionary.itemMatches(target.asInstanceOf[ItemStack], slot, false))
                    {
                        return false;
                    }
                }
                else if (target.isInstanceOf[ArrayList[_]])
                {
                    var matched:Boolean = false;

                    val itr:Iterator[ItemStack] = target.asInstanceOf[ArrayList[ItemStack]].iterator();
                    while (itr.hasNext() && !matched)
                    {
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                if(target != null){
                  if(cores.contains(index)){
                    if(slot == null)
                      return false;
                    if(core==null){
                      core=slot;
                    }else if(!OreDictionary.itemMatches(core, slot, true)){
                      return false;
                    }
                  }else if(colors.contains(index)){
                    if(slot == null)
                      return false;
                    if(color== -1){
                      color=getColor(slot);
                      if(color== -1)
                        return false;
                    }else{
                      if(color!=getColor(slot)){
                        return false;
                      }
                    }
                  }
                }
            }
        }

        return true;
    }

    /**
     * Returns the input for this recipe, any mod accessing this value should never
     * manipulate the values in this array as it will effect the recipe itself.
     * @return The recipes input vales.
     */
    def getInput():Array[Object]=this.input;
    
    def getColor(stack:ItemStack):Int={
      if(stack.getItem() == Item.getItemFromBlock(Blocks.wool) || 
          stack.getItem()== Item.getItemFromBlock(Blocks.stained_hardened_clay)){
        return stack.getItemDamage();
      }
      if(stack.getItem() == Items.dye){
        return 15-stack.getItemDamage();
      }
      for(i<-0 until RecipeDecorative.DYE_NAMES.length){
        val itr:Iterator[ItemStack]=OreDictionary.getOres(RecipeDecorative.DYE_NAMES(i)).iterator();
        while(itr.hasNext()){
          if(OreDictionary.itemMatches(itr.next(), stack, false)){
            return 15-i;
          }
        }
      }
      return 4;
    }
    private def findDecoStacks(inv:InventoryCrafting):DecoStacks={
      val ret:DecoStacks=new DecoStacks();
      for (x <-0 to RecipeDecorative.MAX_CRAFT_GRID_WIDTH - width){
          for (y <-0 to RecipeDecorative.MAX_CRAFT_GRID_HEIGHT - height){
            if(findDecoStacks(ret, inv, x, y, false)){
              return ret;
            }
            if(mirrored && findDecoStacks(ret, inv, x, y, true)){
              return ret;
            }
          }
      }
      return null;
    }
    private def findDecoStacks(ret:DecoStacks, inv:InventoryCrafting, startX:Int, startY:Int, mirror:Boolean):Boolean={
      for (x <-0 to RecipeDecorative.MAX_CRAFT_GRID_WIDTH)
        {
            for (y <-0 to RecipeDecorative.MAX_CRAFT_GRID_HEIGHT)
            {
                val subX:Int = x - startX;
                val subY:Int = y - startY;
                var target:Object = null;
                var index:Int=0;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror){
                        index=width - subX - 1 + subY * width;
                    }
                    else{
                        index=subX + subY * width
                    }
                    target=input(index);
                }

                val slot:ItemStack = inv.getStackInRowAndColumn(x, y);

                if (target == null && slot != null)
                {
                    return false;
                }
                else if (target.isInstanceOf[ItemStack])
                {
                    if (!OreDictionary.itemMatches(target.asInstanceOf[ItemStack], slot, false))
                    {
                        return false;
                    }
                }
                else if (target.isInstanceOf[ArrayList[_]])
                {
                    var matched:Boolean = false;

                    val itr:Iterator[ItemStack] = target.asInstanceOf[ArrayList[ItemStack]].iterator();
                    while (itr.hasNext() && !matched)
                    {
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                if(target != null){
                  if(oldSlot==index){
                    ret.oldStack=slot;
                  }else if(frame==index){
                    ret.frameStack=slot;
                  }else if(cores.contains(index)){
                    ret.coreStack=slot;
                  }else if(colors.contains(index)){
                    ret.colorStack=slot;
                  }
                }
            }
        }

        return true;
    }
}