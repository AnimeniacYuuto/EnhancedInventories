package yuuto.enhancedinventories.compat.refinedrelocation

import net.minecraft.item.Item
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import yuuto.enhancedinventories.tile.TileLocker

/**
 * @author Jacob
 */
class TileSortingLocker(tier:Int) extends TileLocker(tier) with TInventoryConnectiveSorting{
  def this()=this(0);
  
  override def getItem():Item=Item.getItemFromBlock(ModuleRefinedRelocation.sortingLocker);
}