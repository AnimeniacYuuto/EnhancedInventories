/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.network

import net.minecraft.entity.player.EntityPlayerMP;
import yuuto.enhancedinventories.gui.ContainerWorktable;
import yuuto.enhancedinventories.tile.TileWorktable;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

class MessageWorktableHandler extends IMessageHandler[MessageWorktable, IMessage] {

    override def onMessage(message:MessageWorktable, ctx:MessageContext):IMessage={
        message.id match{
            case 0 => emptyCraftMatrix(ctx.getServerHandler().playerEntity);
            case 1 =>{
                val tile:TileWorktable = ctx.getServerHandler().playerEntity.openContainer.asInstanceOf[ContainerWorktable].tile;
                tile.saveSchematic();
            }
        }
        return null;
    }

    private def emptyCraftMatrix(thePlayer:EntityPlayerMP) {
        if(!thePlayer.openContainer.isInstanceOf[ContainerWorktable])
            return;
        for(i <-0 until 9) {
            thePlayer.openContainer.transferStackInSlot(thePlayer, thePlayer.openContainer.asInstanceOf[ContainerWorktable].craftMatrixStart + i);
        }
    }
}