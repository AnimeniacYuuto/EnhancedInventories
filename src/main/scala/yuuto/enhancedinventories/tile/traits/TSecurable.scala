package yuuto.enhancedinventories.tile.traits

import java.util.UUID
import com.mojang.authlib.GameProfile
import cofh.api.tileentity.ISecurable
import cofh.api.tileentity.ISecurable.AccessMode
import cofh.lib.util.helpers.SecurityHelper
import cofh.core.util.SocialRegistry
import net.minecraft.entity.player.EntityPlayer
import yuuto.enhancedinventories.tile.base.TileBaseEI
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.network.MessageSecurityControl
import net.minecraft.nbt.NBTTagCompound

/**
 * @author Jacob
 */
trait TSecurable extends TileBaseEI with ISecurable{
  
  protected var owner:GameProfile=null;
  protected var ownerId:UUID=null;
  protected var ownerName:String=null;
  protected var accessMode:AccessMode = AccessMode.PUBLIC;
  
  def isSecured():Boolean=true;
  override def canPlayerAccess(player:EntityPlayer):Boolean={
    val accessMode:AccessMode = getAccess();
    if(accessMode.isPublic())
      return true;
    val owner:GameProfile = getOwner();
    val p1:UUID = owner.getId();
    if(SecurityHelper.isDefaultUUID(p1))
      return true;
    val p2:UUID = SecurityHelper.getID(player);
    if(p1.equals(p2))
      return true;
    return accessMode.isRestricted() && SocialRegistry.playerHasAccess(player.getCommandSenderName(), owner);
  }
  
  override def getAccess():AccessMode={
    return if(!isSecured() || !hasOwner()){AccessMode.PUBLIC}else{accessMode};
  }

  override def getOwner():GameProfile={
    if(owner != null)
      return owner;
    if(ownerId != null && ownerName != null && !ownerName.trim().isEmpty()){
      owner = SecurityHelper.getProfile(ownerId, ownerName);
    }
    return owner;
  }
  protected def hasOwner():Boolean={
    return this.owner != null || (this.ownerId != null && this.ownerName != null && !this.ownerName.trim().isEmpty());
  }

  override def getOwnerName():String={
    if(ownerName == null || ownerName.trim().isEmpty())
      return "[None]";
    return ownerName;
  }

  override def setAccess(mode:AccessMode):Boolean={
    accessMode = mode;
    if(this.getWorldObj() != null){
      //this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
      //this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
      if(!this.getWorldObj().isRemote)
        this.markDirty();
      else
        EnhancedInventories.network.sendToServer(new MessageSecurityControl(accessMode.ordinal(), this.getWorldObj().provider.dimensionId, xCoord, yCoord, zCoord));
    }
    return accessMode == mode;
  }

  override def setOwner(profile:GameProfile):Boolean={
    owner = profile;
    if(profile == null){
      this.ownerId = null;
      this.ownerName = null;
    }else{
      this.ownerId = profile.getId();
      this.ownerName = profile.getName();
    }
    if(this.getWorldObj() != null && !this.getWorldObj().isRemote){
      this.getWorldObj().func_147479_m(xCoord, yCoord, zCoord);
      this.getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
      this.markDirty();
    }
    return owner == profile;
  }

  override def setOwnerName(name:String):Boolean={
    if(name == null || name.trim().isEmpty())
      return false;
    if(!hasOwner())
      return false;
    if(owner != null)
      owner = null;
    this.ownerName = name;
    return this.ownerName.matches(name);
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    nbt.setByte("securityMode", this.accessMode.ordinal().asInstanceOf[Byte]);
    if(this.hasOwner()){
      nbt.setString("ownerID", ownerId.toString());
      nbt.setString("ownerName", ownerName);
    }
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    this.setAccess(AccessMode.values()(nbt.getInteger("securityMode")));
    if(nbt.hasKey("ownerID") && nbt.hasKey("ownerName")){
      val id:String = nbt.getString("ownerID");
      ownerName = nbt.getString("ownerName");
      ownerId = UUID.fromString(id);
    }
  }
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    nbt.setByte("securityMode", this.accessMode.ordinal().asInstanceOf[Byte]);
    if(this.hasOwner()){
      nbt.setString("ownerID", ownerId.toString());
      nbt.setString("ownerName", ownerName);
    }
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    this.setAccess(AccessMode.values()(nbt.getInteger("securityMode")));
    if(nbt.hasKey("ownerID") && nbt.hasKey("ownerName")){
      val id:String = nbt.getString("ownerID");
      ownerName = nbt.getString("ownerName");
      ownerId = UUID.fromString(id);
    }
  }
  
}