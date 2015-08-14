/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.network

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cofh.api.tileentity.ISecurable;
import cofh.api.tileentity.ISecurable.AccessMode;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

class MessageSecurityControlHandler extends IMessageHandler[MessageSecurityControl, IMessage] {

    override def onMessage(message:MessageSecurityControl, ctx:MessageContext):IMessage={
      val controlMode:AccessMode = AccessMode.values()(message.id);
      val world:World = DimensionManager.getWorld(message.dim);
      val tile:TileEntity = world.getTileEntity(message.x, message.y, message.z);
      if(tile != null && tile.isInstanceOf[ISecurable]){
        tile.asInstanceOf[ISecurable].setAccess(controlMode);
      }
      return null;
    }
}