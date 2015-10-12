package yuuto.enhancedinventories.compat.craftguide

import java.util;
import net.minecraft.item.ItemStack
import uristqwerty.CraftGuide.api.{Slot, SlotType, ItemSlot}

/**
 * Created by Yuuto on 10/10/2015.
 */
object UtilEI {
  val shapelessCraftingSlots: Array[Slot] = Array[Slot](new ItemSlot(3, 3, 16, 16), new ItemSlot(21, 3, 16, 16), new ItemSlot(39, 3, 16, 16), new ItemSlot(3, 21, 16, 16), new ItemSlot(21, 21, 16, 16), new ItemSlot(39, 21, 16, 16), new ItemSlot(3, 39, 16, 16), new ItemSlot(21, 39, 16, 16), new ItemSlot(39, 39, 16, 16), new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT))
  val craftingSlotsOwnBackground: Array[Slot] = Array[Slot](new ItemSlot(3, 3, 16, 16).drawOwnBackground, new ItemSlot(21, 3, 16, 16).drawOwnBackground, new ItemSlot(39, 3, 16, 16).drawOwnBackground, new ItemSlot(3, 21, 16, 16).drawOwnBackground, new ItemSlot(21, 21, 16, 16).drawOwnBackground, new ItemSlot(39, 21, 16, 16).drawOwnBackground, new ItemSlot(3, 39, 16, 16).drawOwnBackground, new ItemSlot(21, 39, 16, 16).drawOwnBackground, new ItemSlot(39, 39, 16, 16).drawOwnBackground, new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground)
  val smallCraftingSlotsOwnBackground: Array[Slot] = Array[Slot](new ItemSlot(12, 12, 16, 16).drawOwnBackground, new ItemSlot(30, 12, 16, 16).drawOwnBackground, new ItemSlot(12, 30, 16, 16).drawOwnBackground, new ItemSlot(30, 30, 16, 16).drawOwnBackground, new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT).drawOwnBackground)
  val craftingSlots: Array[Slot] = Array[Slot](new ItemSlot(3, 3, 16, 16), new ItemSlot(21, 3, 16, 16), new ItemSlot(39, 3, 16, 16), new ItemSlot(3, 21, 16, 16), new ItemSlot(21, 21, 16, 16), new ItemSlot(39, 21, 16, 16), new ItemSlot(3, 39, 16, 16), new ItemSlot(21, 39, 16, 16), new ItemSlot(39, 39, 16, 16), new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT))
  val smallCraftingSlots: Array[Slot] = Array[Slot](new ItemSlot(12, 12, 16, 16), new ItemSlot(30, 12, 16, 16), new ItemSlot(12, 30, 16, 16), new ItemSlot(30, 30, 16, 16), new ItemSlot(59, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT))
  val furnaceSlots: Array[Slot] = Array[Slot](new ItemSlot(13, 21, 16, 16), new ItemSlot(50, 21, 16, 16, true).setSlotType(SlotType.OUTPUT_SLOT))


  def getSmallShapedRecipe(width: Int, height: Int, items: Array[AnyRef], recipeOutput: ItemStack):Array[Object]={
    val output: Array[Object] = new Array[Object](5);
    var y: Int = 0
    while (y < height) {
      {
        {
          var x: Int = 0
          while (x < width) {
            {
              output(y * 2 + x) = items(y * width + x)
            }
            {
              x += 1; x - 1
            }
          }
        }
      }
      {
        y += 1; y - 1
      }
    }
    output(4) = recipeOutput
    output
  }

  def getCraftingShapelessRecipe(itemsL: util.List[_], recipeOutput: ItemStack): Array[Object] = {
    val output: Array[Object] = new Array[Object](10);
    val items:util.List[Object] = itemsL.asInstanceOf[util.List[Object]];
    var i: Int = 0
    while (i < items.size) {
      {
        output(i) = items.get(i)
      }
      {
        i += 1; i - 1
      }
    }
    output(9) = recipeOutput
    output
  }

  def getCraftingShapedRecipe(width: Int, height: Int, items: Array[Object], recipeOutput: ItemStack): Array[Object] = {
    val output: Array[Object] = new Array[Object](10)
    var y: Int = 0
    while (y < height) {
      {
        {
          var x: Int = 0
          while (x < width) {
            {
              output(y * 3 + x) = items(y * width + x)
            }
            {
              x += 1;
              x - 1
            }
          }
        }
      }
      {
        y += 1; y - 1
      }
    }
    output(9) = recipeOutput
    output
  }
}
