package yuuto.enhancedinventories.compat.refinedrelocation

import com.dynious.refinedrelocation.api.filter.IFilterGUI
import com.dynious.refinedrelocation.api.tileentity.IFilterTileGUI
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory.Priority

trait ITileSorting extends ISortingInventory with IFilterTileGUI{
  
  def getTileInvHandler():ISortingInventoryHandler;
  def getTileFilterGui():IFilterGUI;
  def setTileFilterGui(gui:IFilterGUI);
  def getTilePriority():Priority;
  def setTilePriority(priority:Priority);

}