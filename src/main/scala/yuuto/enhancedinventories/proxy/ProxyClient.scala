/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.proxy

import net.minecraft.item.Item
import net.minecraftforge.client.MinecraftForgeClient
import yuuto.enhancedinventories.client.renderer.ItemImprovedChestRenderer
import yuuto.enhancedinventories.client.renderer.ItemRendererCabinet
import yuuto.enhancedinventories.client.renderer.ItemRendererLocker
import yuuto.enhancedinventories.client.renderer.TileImprovedChestRenderer
import yuuto.enhancedinventories.client.renderer.TileRendererCabinet
import yuuto.enhancedinventories.client.renderer.TileRendererLocker
import yuuto.enhancedinventories.compat.CompatLoader
import yuuto.enhancedinventories.tile.TileCabinet
import yuuto.enhancedinventories.tile.TileImprovedChest
import yuuto.enhancedinventories.tile.TileLocker
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.MinecraftForge
import yuuto.enhancedinventories.client.ClientEventHandler

object ProxyClient extends ProxyCommon{
  
}
class ProxyClient extends ProxyCommon{
  
  override def preInit(event:FMLPreInitializationEvent) {
    super.preInit(event);
    CompatLoader.preInitClient();
    MinecraftForge.EVENT_BUS.register(ClientEventHandler);
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileImprovedChest], TileImprovedChestRenderer.instance);
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileLocker], TileRendererLocker.instance);
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileCabinet], TileRendererCabinet.instance);
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProxyCommon.blockImprovedChest), ItemImprovedChestRenderer.instance);
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProxyCommon.blockLocker), ItemRendererLocker.instance);
    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ProxyCommon.blockCabinet), ItemRendererCabinet.instance);
  }

}