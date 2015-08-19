/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.modules

import yuuto.inventorytools.api.dolly.DollyHandlerRegistry
import yuuto.enhancedinventories.proxy.ProxyCommon
import net.minecraftforge.oredict.OreDictionary
import yuuto.enhancedinventories.compat.inventorytools.ConnectiveDollyBlockHandler
import yuuto.enhancedinventories.compat.inventorytools.ConnectiveDollyTileHandler
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable
import yuuto.enhancedinventories.config.EIConfiguration

object ModuleInventoryTools {
  def init(){
    DollyHandlerRegistry.registerTileHandler(classOf[TileConnectiveUpgradeable], ConnectiveDollyTileHandler);
    
    DollyHandlerRegistry.registerBlockHandler(ProxyCommon.blockCabinet, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
    DollyHandlerRegistry.registerBlockHandler(ProxyCommon.blockImprovedChest, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
    DollyHandlerRegistry.registerBlockHandler(ProxyCommon.blockLocker, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
    
    if(EIConfiguration.moduleRefinedRelocation){
      DollyHandlerRegistry.registerBlockHandler(ModuleRefinedRelocation.sortingCabinet, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
      DollyHandlerRegistry.registerBlockHandler(ModuleRefinedRelocation.sortingImprovedChest, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
      DollyHandlerRegistry.registerBlockHandler(ModuleRefinedRelocation.sortingLocker, OreDictionary.WILDCARD_VALUE, ConnectiveDollyBlockHandler);
    }
  }
  
}