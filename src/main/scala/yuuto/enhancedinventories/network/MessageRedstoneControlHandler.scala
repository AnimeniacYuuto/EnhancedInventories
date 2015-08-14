/**
 * @author Yuuto
 */
package yuuto.enhancedinventories.network

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cofh.api.tileentity.IRedstoneControl;
import cofh.api.tileentity.IRedstoneControl.ControlMode;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

class MessageRedstoneControlHandler extends IMessageHandler[MessageRedstoneControl, IMessage] {

    override def onMessage(message:MessageRedstoneControl, ctx:MessageContext):IMessage={
      val controlMode:ControlMode = ControlMode.values()(message.id);
      val world:World = DimensionManager.getWorld(message.dim);
      val tile:TileEntity = world.getTileEntity(message.x, message.y, message.z);
      if(tile != null && tile.isInstanceOf[IRedstoneControl]){
        tile.asInstanceOf[IRedstoneControl].setControl(controlMode);
      }
        return null;
    }
}