/**
 * @author Yuuto
 */
package yuuto.enhancedinventories

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import yuuto.enhancedinventories.network.MessageRedstoneControl
import yuuto.enhancedinventories.network.MessageRedstoneControlHandler
import yuuto.enhancedinventories.network.MessageSecurityControl
import yuuto.enhancedinventories.network.MessageSecurityControlHandler
import yuuto.enhancedinventories.network.MessageWorktable
import yuuto.enhancedinventories.network.MessageWorktableHandler
import yuuto.enhancedinventories.proxy.ProxyCommon
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.Mod.Instance
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import scala.reflect._;
import yuuto.enhancedinventories.ref.ReferenceEI

@Mod(modid = ReferenceEI.MOD_ID, name = ReferenceEI.MOD_NAME, version = ReferenceEI.VERSION, modLanguage = "scala")
object EnhancedInventories {
  
  
  @SidedProxy(clientSide = ReferenceEI.PROXY_CLIENT, serverSide = ReferenceEI.PROXY_SERVER)
  var proxy:ProxyCommon=null;
  
  var network:SimpleNetworkWrapper=null;
  
  val tab:CreativeTabs = new CreativeTabs("EnhancedInventories"){
    @SideOnly(Side.CLIENT)
    override def getTabIconItem():Item={
      return Item.getItemFromBlock(ProxyCommon.blockImprovedChest);
    }
  };
  
  @EventHandler
  def preInit(event:FMLPreInitializationEvent) {
    proxy.preInit(event);
    //Initialize network messages
    network = NetworkRegistry.INSTANCE.newSimpleChannel(ReferenceEI.CHANNEL);
    network.registerMessage(classOf[MessageWorktableHandler], classOf[MessageWorktable], 0, Side.SERVER);
    network.registerMessage(classOf[MessageRedstoneControlHandler], classOf[MessageRedstoneControl], 1, Side.SERVER);
    network.registerMessage(classOf[MessageSecurityControlHandler], classOf[MessageSecurityControl], 2, Side.SERVER);
  }

  @EventHandler
  def init(event:FMLInitializationEvent) {
    proxy.init(event);
  }

  @EventHandler
  def postInit(event:FMLPostInitializationEvent) {
    proxy.postInit(event);
  }

}