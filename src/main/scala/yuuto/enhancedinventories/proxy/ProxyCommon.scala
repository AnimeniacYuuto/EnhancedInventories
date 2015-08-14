/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.proxy

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.block.BlockAutoAssembler
import yuuto.enhancedinventories.block.BlockCabinet
import yuuto.enhancedinventories.block.BlockImprovedChest
import yuuto.enhancedinventories.block.BlockLocker
import yuuto.enhancedinventories.block.BlockPainter
import yuuto.enhancedinventories.block.BlockWorktable
import yuuto.enhancedinventories.compat.CompatLoader
import yuuto.enhancedinventories.config.EIConfiguration
import yuuto.enhancedinventories.gui.GuiHandler
import yuuto.enhancedinventories.item.ItemBlockCabinet
import yuuto.enhancedinventories.item.ItemBlockImprovedChest
import yuuto.enhancedinventories.item.ItemBlockLocker
import yuuto.enhancedinventories.item.ItemChestConverter
import yuuto.enhancedinventories.item.ItemFunctionUpgrade
import yuuto.enhancedinventories.item.ItemPaintBrush
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.item.ItemSizeUpgrade
import yuuto.enhancedinventories.tile.TileAutoAssembler
import yuuto.enhancedinventories.tile.TileCabinet
import yuuto.enhancedinventories.tile.TileImprovedChest
import yuuto.enhancedinventories.tile.TileLocker
import yuuto.enhancedinventories.tile.TilePainter
import yuuto.enhancedinventories.tile.TileWorktable
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.GameRegistry;
import yuuto.enhancedinventories.config.json.JsonRecipeFactory

object ProxyCommon{
  final val blockImprovedChest:Block = new BlockImprovedChest("improvedChest");
  final val blockLocker:Block = new BlockLocker("locker");
  final val blockCabinet:Block = new BlockCabinet("cabinet");
  final val blockWorktable:Block = new BlockWorktable("worktable");
  final val blockAutoAssembler:Block = new BlockAutoAssembler("autoAssembler");
  final val blockPainter:Block = new BlockPainter("painter");
  
  final val functionUpgrades:Item = new ItemFunctionUpgrade("upgradeFunction").setTextureNames(
      "Basic", "Hopper", "Trapped", "Security", "Reinforced"
      );
  final val sizeUpgrades:Item = new ItemSizeUpgrade("sizeUpgrade");
  final val chestConverter:Item = new ItemChestConverter("chestConverter");
  final val schematic:Item = new ItemSchematic("schematic");
  final val paintBrush:Item = new ItemPaintBrush("paintbrush");
}
class ProxyCommon {
  
  def preInit(event:FMLPreInitializationEvent) {
    EIConfiguration.init(event);
    NetworkRegistry.INSTANCE.registerGuiHandler(EnhancedInventories, new GuiHandler());
    CompatLoader.preInit();
    GameRegistry.registerBlock(ProxyCommon.blockImprovedChest, classOf[ItemBlockImprovedChest], "improvedChest");
    GameRegistry.registerBlock(ProxyCommon.blockLocker, classOf[ItemBlockLocker], "locker");
    GameRegistry.registerBlock(ProxyCommon.blockCabinet, classOf[ItemBlockCabinet], "blockCabinet");
    GameRegistry.registerBlock(ProxyCommon.blockWorktable, "blockWorktable");
    GameRegistry.registerBlock(ProxyCommon.blockAutoAssembler, "blockAutoAssembler");
    GameRegistry.registerBlock(ProxyCommon.blockPainter, "blockPainter");
    GameRegistry.registerTileEntity(classOf[TileImprovedChest], "container.ImprovedChests:ImprovedChest");
    GameRegistry.registerTileEntity(classOf[TileLocker], "container.ImprovedChests:locker");
    GameRegistry.registerTileEntity(classOf[TileCabinet], "tileCabinet");
    GameRegistry.registerTileEntity(classOf[TileWorktable], "tileWorktable");
    GameRegistry.registerTileEntity(classOf[TileAutoAssembler], "tileAutoAssembler");
    GameRegistry.registerTileEntity(classOf[TilePainter], "tilePainter");
    GameRegistry.registerItem(ProxyCommon.functionUpgrades, "functionUpgrade");
    GameRegistry.registerItem(ProxyCommon.sizeUpgrades, "sizeUpgrade");
    GameRegistry.registerItem(ProxyCommon.chestConverter, "chestConverter");
    GameRegistry.registerItem(ProxyCommon.schematic, "schematic");
    GameRegistry.registerItem(ProxyCommon.paintBrush, "paintbrush");
    //RecipeFactory.init();
  }

  def init(event:FMLInitializationEvent) {
    
  }

  def postInit(event:FMLPostInitializationEvent) {
    CompatLoader.postInit();
  }

}