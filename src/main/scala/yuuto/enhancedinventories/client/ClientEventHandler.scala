/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.client

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.TextureStitchEvent
import cofh.core.render.IconRegistry
import yuuto.enhancedinventories.ref.ReferenceEI

object ClientEventHandler {
  @SubscribeEvent
  def onTextureStitch(event:TextureStitchEvent.Pre){
    if(event.map.getTextureType() == 1){
      IconRegistry.addIcon("IconRepeater", getButtonName("repeater"), event.map);
      IconRegistry.addIcon("IconNever", getButtonName("neverActive"), event.map);
    }
  }
  
  private def getButtonName(name:String):String=getIconName("buttons/", name);
  private def getIconName(dir:String, name:String):String=getIconName(dir+name);
  private def getIconName(name:String):String={
    return ReferenceEI.MOD_ID.toLowerCase()+":"+name;
  }
  
}