package yuuto.enhancedinventories.tile.base

import yuuto.yuutolib.tile.traits.TTileRotatableMachine
import yuuto.enhancedinventories.tile.traits.TDecorativeInventoryConnective
import yuuto.enhancedinventories.tile.traits.TInventoryConnectiveUpgradeable
import yuuto.enhancedinventories.tile.traits.TInventorySecurable
import yuuto.enhancedinventories.tile.traits.TSecurableUpgradeable
import yuuto.enhancedinventories.tile.traits.TConnective
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Jacob
 */
abstract class TileConnectiveUpgradeable extends TileBaseEI 
  with TTileRotatableMachine
  with TDecorativeInventoryConnective
  with TInventorySecurable
  with TSecurableUpgradeable{
  
  
}