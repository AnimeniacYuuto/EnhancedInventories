package yuuto.enhancedinventories.tile.traits

import yuuto.enhancedinventories.tile.base.TileBaseEI
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.tileentity.TileEntity
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.materials.DecorationInfo
import net.minecraft.item.Item
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

/**
 * @author Jacob
 */
trait TConnective extends TileBaseEI{
  
  protected var partnerDirection:ForgeDirection=ForgeDirection.UNKNOWN;
  protected var partner:TConnective=null;
  
  //Abstracts
  def getValidConnections():Array[ForgeDirection];
  def getMainConnections():Array[ForgeDirection];
  def getItem():Item;
  
  //Definitions
  override def initialize(){
    super.initialize();
    checkConnections();
  }
  def checkConnections(){
    var foundDirection:ForgeDirection = ForgeDirection.UNKNOWN;
    var foundConnection:TConnective = null;
    for(dir <- getValidConnections() if(foundConnection == null)){
      val te:TileEntity = this.getWorldObj().getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
      if(te == null || !isInstanceOfThis(te)){}
      else{
        val potentialConnection:TConnective = te.asInstanceOf[TConnective];
        if(!canConnect(potentialConnection, dir) || !potentialConnection.canConnect(this, dir.getOpposite())){}
        else{
          foundConnection = potentialConnection;
          foundDirection = dir;
        }
      }
    }
    if(foundConnection == null)
      return;
    this.connectTo(foundConnection, foundDirection);
    foundConnection.connectTo(this, foundDirection.getOpposite());
  }
    
  def canConnect(inv:TConnective, dir:ForgeDirection):Boolean={
    if(!initialized || isInvalid())
      return false;
    if(!isValidForConnection(inv))
      return false;
    return true;
  }
  def isValidForConnection(inv:TConnective):Boolean={
    return isInstanceOfThis(inv);
  }
  def isValidForConnection(newChest:ItemStack):Boolean={
    if(newChest == null || newChest.getItem() != getItem())
      return false;
    return true;
  }
  
  def connectTo(tile:TConnective, dir:ForgeDirection){
    this.partnerDirection = dir;
    this.partner = tile;
    if(this.getWorldObj().isRemote)
      this.getWorldObj().markBlockRangeForRenderUpdate(this.xCoord-1, this.yCoord-1, this.zCoord-1,this.xCoord+1, this.yCoord+1, this.zCoord+1);
    else
      this.getWorldObj().addBlockEvent(xCoord, xCoord, zCoord, getBlockType(), 1, this.partnerDirection.ordinal());
  }
  def disconnect(){
    if(!isConnected())
      return;
    this.partnerDirection = ForgeDirection.UNKNOWN;
    val oldPartner:TConnective = this.partner;
    this.partner = null;
    oldPartner.disconnect();
    if(this.getWorldObj().isRemote){
      this.getWorldObj().markBlockRangeForRenderUpdate(this.xCoord-1, this.yCoord-1, this.zCoord-1,this.xCoord+1, this.yCoord+1, this.zCoord+1);
    }else
      this.getWorldObj().addBlockEvent(xCoord, xCoord, zCoord, getBlockType(), 1, -10);
  }
  def isConnected():Boolean={
    return this.partnerDirection != ForgeDirection.UNKNOWN && getPartner() != null;
  }
  def isMain():Boolean={
    return getMainConnections().contains(partnerDirection);
  }
  def getPartner():TConnective={
    return this.partner;
  }
  def getPartnerDirection():ForgeDirection={
    return partnerDirection;
  }
  
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    if(partnerDirection != ForgeDirection.UNKNOWN)
      nbt.setByte("connectedDir", partnerDirection.ordinal().asInstanceOf[Byte]);
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    if(nbt.hasKey("connectedDir")){
      val dir:ForgeDirection = ForgeDirection.getOrientation(nbt.getInteger("connectedDir"));
      if(dir != ForgeDirection.UNKNOWN)
        attemptConnection(dir);
    }
  }
  
  def attemptConnection(dir:ForgeDirection){
    if(this.isConnected() || !initialized)
      return;
    val te:TileEntity = this.getWorldObj().getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
    if(te == null || !isInstanceOfThis(te))
      return;
    val potentialConnection:TConnective = te.asInstanceOf[TConnective];
    if(potentialConnection.isConnected())
      return;
    if(!canConnect(potentialConnection, dir) || !potentialConnection.canConnect(this, dir.getOpposite()))
      return;
    this.connectTo(potentialConnection, dir);
    potentialConnection.connectTo(this, dir.getOpposite());
  }
  
  override def invalidate(){
    super.invalidate();
    if(isConnected()){
      this.partner.disconnect();
      this.disconnect();
    }
  }
  
  override def receiveClientEvent(event:Int, args:Int):Boolean={
      if(event == 1){
        if(args == -10){
          disconnect();
          return true;
        }
        val dir:ForgeDirection = ForgeDirection.getOrientation(args);
        if(dir != ForgeDirection.UNKNOWN)
          attemptConnection(dir);
        return true;
      }
      return super.receiveClientEvent(event, args);
  }
  
  @SideOnly(Side.CLIENT)
  override def getRenderBoundingBox():AxisAlignedBB=AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord-1, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
  
}