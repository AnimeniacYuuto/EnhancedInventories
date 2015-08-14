package yuuto.enhancedinventories.compat.refinedrelocation

import yuuto.enhancedinventories.tile.TileImprovedChest
import net.minecraft.item.Item
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation

/**
 * @author Jacob
 */
class TileSortingImprovedChest(tier:Int) extends TileImprovedChest(tier) with TInventoryConnectiveSorting{
  def this()=this(0);
  
  override def getItem():Item=Item.getItemFromBlock(ModuleRefinedRelocation.sortingImprovedChest);
}