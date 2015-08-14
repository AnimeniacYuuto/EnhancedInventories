package yuuto.enhancedinventories.compat.refinedrelocation

import yuuto.enhancedinventories.tile.TileCabinet
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation
import net.minecraft.item.Item

/**
 * @author Jacob
 */
class TileSortingCabinet(tier:Int) extends TileCabinet(tier) with TInventoryConnectiveSorting{
  def this()=this(0);
  
  override def getItem():Item=Item.getItemFromBlock(ModuleRefinedRelocation.sortingCabinet);
}