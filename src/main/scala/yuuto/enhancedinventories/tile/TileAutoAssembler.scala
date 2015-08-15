package yuuto.enhancedinventories.tile

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.WorldServer
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.FakePlayerFactory
import net.minecraftforge.common.util.ForgeDirection
import yuuto.enhancedinventories.EnhancedInventories
import yuuto.enhancedinventories.gui.slot.SlotCraftingExtendedAuto
import yuuto.enhancedinventories.inventory.IInventoryParent
import yuuto.enhancedinventories.inventory.InventorySimple
import yuuto.enhancedinventories.item.ItemSchematic
import yuuto.enhancedinventories.network.MessageRedstoneControl
import yuuto.enhancedinventories.tile.base.TileCrafter
import cofh.api.tileentity.IRedstoneControl
import cofh.api.tileentity.IRedstoneControl.ControlMode
import scala.collection.mutable.MutableList
import yuuto.enhancedinventories.tile.traits.TInventorySimple
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.yuutolib.inventory.IInventoryExtended
import yuuto.yuutolib.inventory.InventoryWrapper
import yuuto.enhancedinventories.config.EIConfiguration

object TileAutoAssembler{
  private val schematicSlot:Int = 9;
}

class TileAutoAssembler extends TileCrafter with IInventoryParent with TInventorySimple with IRedstoneControl{
  var syncronizing:Boolean=false;
  
  protected var usingSchematic:Boolean=false;
  protected var fakePlayer:EntityPlayer=null;
  protected var slotCrafting:SlotCraftingExtendedAuto=null;
  
  protected var craftingTicks:Int=0;
  protected var powered:Boolean=false;
  protected var pulsed:Boolean=false;
  protected var redstoneControl:ControlMode = ControlMode.HIGH;
  resetInventory();
  
  override def initialize(){
    super.initialize();
    if(this.fakePlayer == null){
      fakePlayer = FakePlayerFactory.getMinecraft(this.getWorldObj().asInstanceOf[WorldServer]);
      slotCrafting = new SlotCraftingExtendedAuto(this, fakePlayer, craftingMatrix, craftResult, 0, 0, 0);
      this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
    }
  }
  
  override def resetInventory(){
    this.inv = new Array[ItemStack](getArraySize());
    setInventory();
  }
  override def setInventory(){
    invHandler = new InventorySimple(this.inv, this);
  }
  override def getArraySize():Int={
    return 10;
  }
  override def getSizeInventory():Int=getInventory.getSizeInventory()-1;
  
  override def updateEntity(){
    if(this.getWorldObj().isRemote)
      return;
    super.updateEntity();
    
    this.setPowered(this.worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0);
    craftingTicks-=1;
    if(craftingTicks < 1){
      if(isActive()){
        craftingTicks = getDelay();
        autoCraft();
        pulsed = false;
      }
    }
    
  }
  
  def getDelay():Int={
    return EIConfiguration.autoAssemblerTickRate;
  }
  
  def autoCraft(){
    if(!slotCrafting.getHasStack())
      return;
    if(!slotCrafting.canTakeStack(fakePlayer))
      return;
    val stack:ItemStack = slotCrafting.getStack().copy();
    if(mergeItemStack(stack))
      slotCrafting.onPickupFromSlot(fakePlayer, slotCrafting.getStack());
  }
  def mergeItemStack(stack:ItemStack):Boolean={
    if(mergeItemStack(stack.copy(), true))
      return mergeItemStack(stack, false);
    return false;
  }
  def mergeItemStack(stack:ItemStack, simulate:Boolean):Boolean={
    var emptySlot:Int= -1;
    for(i <-0 until 9 if(stack.stackSize > 0)){
      val stack1:ItemStack = this.invHandler.getStackInSlot(i);
      if(stack1 == null){
        if(emptySlot == -1)
          emptySlot =i;
      }
      else if (stack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stack1)){
        val amt:Int = stack1.stackSize + stack.stackSize;
        if (amt <= stack.getMaxStackSize())
        {
          if(simulate)
            return true;
            stack.stackSize = 0;
            stack1.stackSize = amt;
            this.invHandler.markDirty();
            return true;
        }
        else if (stack1.stackSize < stack.getMaxStackSize())
        {
            stack.stackSize -= stack.getMaxStackSize() - stack1.stackSize;
            if(!simulate){
              stack1.stackSize = stack.getMaxStackSize();
              this.invHandler.markDirty();
            }
        }
      }
    }
    if(stack.stackSize < 1)
      return true;
    if(emptySlot == -1)
      return false;
    if(simulate)
      return true;
    this.invHandler.setInventorySlotContents(emptySlot, stack.copy());
    stack.stackSize = 0;
    return true;
  }
  
  override def getSubInventories():Array[IInventoryExtended]={
    val invs:MutableList[IInventoryExtended] = MutableList[IInventoryExtended]();
    invs+=InventoryWrapper.getWrapper(this.invHandler);
    for(dir <- ForgeDirection.VALID_DIRECTIONS){
      val tile:TileEntity = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
      if(tile == null || !tile.isInstanceOf[IInventory]){}
      else {
        invs+=InventoryWrapper.getWrapper(tile.asInstanceOf[IInventory], dir.getOpposite());
      }
    }
    return invs.toArray;
  }
  
  override def onCraftMatrixChanged(inv:IInventory) {
    if(this.syncronizing)
      return;
    this.markDirty();
    if(isCraftingMatrixEmpty() && getSchematic() != null){
      usingSchematic = true;
      this.craftResult.setInventorySlotContents(0, ItemSchematic.getResult(getSchematic()));
    }else{
      usingSchematic = false;
      this.craftResult.setInventorySlotContents(0, findMatchingRecipe(this.craftingMatrix, this.worldObj));
    }
  }

  override def isUsingSchematic():Boolean=usingSchematic;

  override def getSchematic():ItemStack={
    return this.invHandler.getStackInSlot(TileAutoAssembler.schematicSlot);
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    nbt.setByte("redstoneControl", redstoneControl.ordinal().asInstanceOf[Byte]);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    syncronizing=true;
    super.readFromNBT(nbt);
    this.redstoneControl = ControlMode.values()(nbt.getInteger("redstoneControl"));
    syncronizing=false;
    this.onCraftMatrixChanged(this.getCraftingMatrix());
  }

  override def onInventoryChanged(inventorySimple:IInventory)=this.onCraftMatrixChanged(inventorySimple);

  override def isPowered():Boolean=powered;
  def hasPulsed():Boolean=pulsed;
  override def setPowered(powered:Boolean){
    if(this.powered == false && powered == true)
      pulsed = true;
    this.powered = powered;
  }
  override def getControl():ControlMode=this.redstoneControl;

  override def setControl(controlMode:ControlMode){
    this.redstoneControl = controlMode;
    if(this.worldObj.isRemote){
      EnhancedInventories.network.sendToServer(new MessageRedstoneControl(redstoneControl.ordinal(), this.getWorldObj().provider.dimensionId, xCoord, yCoord, zCoord));
    }else{
      this.markDirty();
    }
  }
  protected def isActive():Boolean={
    if(getControl().isHigh() && isPowered())
      return true;
    else if(getControl().isLow() && !isPowered())
      return true;
    return this.getControl().isDisabled() && hasPulsed();
  }
  
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    nbt.setByte("redstoneControl", redstoneControl.ordinal().asInstanceOf[Byte]);
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    this.redstoneControl = ControlMode.values()(nbt.getInteger("redstoneControl"));
  }

  override def getItemStack():ItemStack={
    return new ItemStack(ProxyCommon.blockAutoAssembler);
  }

}