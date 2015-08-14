/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.compat.nei

import yuuto.enhancedinventories.client.gui.GuiContainerAutoAssembler
import yuuto.enhancedinventories.client.gui.GuiContainerWorkbench
import yuuto.enhancedinventories.client.gui.GuiContainerWorktable
import codechicken.nei.api.API
import codechicken.nei.api.IConfigureNEI
import codechicken.nei.recipe.DefaultOverlayHandler;
import yuuto.enhancedinventories.ref.ReferenceEI

class NEIConfig extends IConfigureNEI{

  override def loadConfig() {
    API.registerRecipeHandler(new EIShapedHandlerBasic());
    API.registerUsageHandler(new EIShapedHandlerBasic());
    API.registerGuiOverlay(classOf[GuiContainerWorkbench], "crafting");
    API.registerGuiOverlayHandler(classOf[GuiContainerWorkbench], new DefaultOverlayHandler(), "crafting");
    API.registerGuiOverlay(classOf[GuiContainerWorktable], "crafting");
    API.registerGuiOverlayHandler(classOf[GuiContainerWorktable], new WorktableOverlayHandler(), "crafting");
    API.registerGuiOverlay(classOf[GuiContainerAutoAssembler], "crafting", -17, 11);
    API.registerGuiOverlayHandler(classOf[GuiContainerAutoAssembler], new AutoAssemblerOverlayHandler(), "crafting");
  }

  override def getName():String="EnhancedInventoriesNEIHandler";

  override def getVersion():String=ReferenceEI.VERSION;

}