package yuuto.enhancedinventories.tile.traits

import yuuto.enhancedinventories.tile.upgrades.Upgrade
import yuuto.enhancedinventories.tile.upgrades.helpers.HopperUpgradeHelper
import net.minecraftforge.common.util.ForgeDirection
import net.minecraft.item.ItemStack
import yuuto.enhancedinventories.materials.DecorationHelper
import net.minecraft.util.MathHelper
import net.minecraft.nbt.NBTTagCompound
import yuuto.enhancedinventories.proxy.ProxyCommon
import yuuto.enhancedinventories.compat.refinedrelocation.RefinedRelocationHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import yuuto.enhancedinventories.config.EIConfiguration

/**
 * @author Jacob
 */
trait TInventoryConnectiveUpgradeable extends TInventoryConnective with TUpgradeable{
  var tier:Int=0;
  var numberOfPlayers:Int=0;
  var alternate:Boolean=false;
  
  var prevLidAngle:Float=0f;
  var lidAngle:Float=0;
  
  def getPullDirection():ForgeDirection;
  def getPushDirection():ForgeDirection;
  protected def checkUpgradeValidity(chest:TInventoryConnectiveUpgradeable, newChest:ItemStack):Boolean;
  
  override def getArraySize():Int=27*(tier+1)
  
  //adds the hopper operation and checks lid angle
  override def updateEntity(){
    super.updateEntity();
    if(isHopper()){
      HopperUpgradeHelper.moveItems(this, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, getPullDirection(), getPushDirection());
    }
    prevLidAngle = lidAngle;
    val f:Float = 0.1F;
    if (getNumberOfPlayers() > 0 && lidAngle == 0.0F && (!this.isConnected() || this.isMain()))
    {
        playSoundOpen();
    }
    if (getNumberOfPlayers() == 0 && lidAngle > 0.0F || getNumberOfPlayers() > 0 && lidAngle < 1.0F)
    {
        val f1:Float = lidAngle;
        if (getNumberOfPlayers() > 0)
        {
            lidAngle += f;
        }
        else
        {
            lidAngle -= f;
        }
        if (lidAngle > 1.0F)
        {
            lidAngle = 1.0F;
        }
        val f2:Float = 0.5F;
        if (lidAngle < f2 && f1 >= f2 && (!this.isConnected() || this.isMain()))
        {
            playSoundClosed();
        }
        if (lidAngle < 0.0F)
        {
            lidAngle = 0.0F;
        }
        
        //this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
  }
  /**
   * Checks the number of players accessing the tile, or its partner
   */
  def getNumberOfPlayers():Int={
    if(this.isConnected()){
      val tile:TInventoryConnectiveUpgradeable = getPartner().asInstanceOf[TInventoryConnectiveUpgradeable];
      if(tile != null)
        return this.numberOfPlayers + tile.numberOfPlayers;
    }
    return numberOfPlayers;
  }
  /**
   * get the level of redstone power that should be emitted from this tile
   */
  def getRedstonePower():Int={
    return if(this.isTrapped()){MathHelper.clamp_int(this.getNumberOfPlayers(), 0, 15)}else{0};
  }
  /**
   * tells the tile that someone has opened it's inventory
   */
  override def onOpenInventory() {
    if(numberOfPlayers < 0){
      numberOfPlayers = 0;
    }
    numberOfPlayers +=1;
    this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord-1, this.zCoord, this.getBlockType());
        if(this.isConnected()){
          val d:ForgeDirection = this.getPartnerDirection();
          this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord+d.offsetX, this.yCoord+d.offsetY, this.zCoord+d.offsetZ, this.getBlockType());
            this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord+d.offsetX, this.yCoord+d.offsetY-1, this.zCoord+d.offsetZ, this.getBlockType());
        }
    if(!getWorldObj().isRemote)
      this.getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 2, numberOfPlayers);
  }
  /**
   * tells the tile that someone closed it's inventory
   */
  override def onCloseInventory() {
    numberOfPlayers -= 1;
    if(numberOfPlayers < 0){
      numberOfPlayers = 0;
    }
    this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord, this.yCoord-1, this.zCoord, this.getBlockType());
        if(this.isConnected()){
          val d:ForgeDirection = this.getPartnerDirection();
          this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord+d.offsetX, this.yCoord+d.offsetY, this.zCoord+d.offsetZ, this.getBlockType());
            this.getWorldObj().notifyBlocksOfNeighborChange(this.xCoord+d.offsetX, this.yCoord+d.offsetY-1, this.zCoord+d.offsetZ, this.getBlockType());
        }
    if(!getWorldObj().isRemote)
      this.getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 2, numberOfPlayers);
  }
  
  //makes sure connected tiles of the right tier and alternative settings
  override def isValidForConnection(inv:TConnective):Boolean={
    if(!super.isValidForConnection(inv))
      return false;
    val t:TInventoryConnectiveUpgradeable = inv.asInstanceOf[TInventoryConnectiveUpgradeable];
    return this.tier == t.tier && this.alternate == t.alternate;
  }
  override def isValidForConnection(newChest:ItemStack):Boolean={
    if(!super.isValidForConnection(newChest))
      return false;
    if(!newChest.hasTagCompound() && this.alternate)
      return false;
    if(newChest.hasTagCompound()){
      return newChest.getItemDamage() == this.tier && this.alternate == newChest.getTagCompound().getBoolean(DecorationHelper.KEY_ALTERNATE);
    }
    return newChest.getItemDamage() == this.tier;
  }
  //Save/transfer data
  override def writeToNBT(nbt:NBTTagCompound){
    nbt.setByte("tier", tier.asInstanceOf[Byte]);
    nbt.setBoolean(DecorationHelper.KEY_ALTERNATE, alternate);
    super.writeToNBT(nbt);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    this.tier = nbt.getInteger("tier");
    this.resetInventory();
    this.alternate = nbt.getBoolean(DecorationHelper.KEY_ALTERNATE);
    super.readFromNBT(nbt);
  }
  override def writeToNBTPacket(nbt:NBTTagCompound){
    super.writeToNBTPacket(nbt);
    nbt.setBoolean(DecorationHelper.KEY_ALTERNATE, alternate);
    nbt.setInteger("numberOfPlayers", numberOfPlayers);
  }
  override def readFromNBTPacket(nbt:NBTTagCompound){
    super.readFromNBTPacket(nbt);
    this.alternate = nbt.getBoolean(DecorationHelper.KEY_ALTERNATE);
    numberOfPlayers = nbt.getInteger("numberOfPlayers");
  }
  //Upgrade application
  override def isUpgradeValid(stack:ItemStack, player:EntityPlayer):Boolean={
    if(stack.getItem() == ProxyCommon.sizeUpgrades)
      return isSizeUpgradeValid(stack, player);
    if(EIConfiguration.moduleRefinedRelocation && RefinedRelocationHelper.isSortingUpgrade(stack)){
      return RefinedRelocationHelper.canUpgradeTile(this);
    }
    return super.isUpgradeValid(stack, player);
    
  }
  def isSizeUpgradeValid(stack:ItemStack, player:EntityPlayer):Boolean={
    if(stack.getItemDamage() != tier)
      return false;
    val newChest:ItemStack = getItemStack();
    newChest.setItemDamage(stack.getItemDamage()+1);
    if(this.isInstanceOf[TDecorative] && !this.asInstanceOf[TDecorative].isPainted() && stack.hasTagCompound() && newChest.hasTagCompound())
      newChest.getTagCompound().setString(DecorationHelper.KEY_FRAME_NAME, stack.getTagCompound().getString(DecorationHelper.KEY_FRAME_NAME));
    var chests:Int = 0;
    for(dir <- getValidConnections()){
      val tile:TileEntity = getWorldObj().getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
      if(tile == null || !isInstanceOfThis(tile)){}
      else{
        val chest:TInventoryConnectiveUpgradeable = tile.asInstanceOf[TInventoryConnectiveUpgradeable];
        if(dir == this.getPartnerDirection()){}
        else if(!checkUpgradeValidity(chest, newChest)){}
        else if(chest.isConnected()){
          return false;
        }else{
          chests+=1;
        }
      }
    }
    if(chests > 1)
      return false;
    return true;
  }
  override def addUpgrade(stack:ItemStack, player:EntityPlayer):Boolean={
    if(stack.getItem() == ProxyCommon.sizeUpgrades){
      return addSizeUpgrade(stack, player);
    }
    if(EIConfiguration.moduleRefinedRelocation && RefinedRelocationHelper.isSortingUpgrade(stack)){
      return RefinedRelocationHelper.upgradeTile(this);
    }
    return super.addUpgrade(stack, player);
  }
  def addSizeUpgrade(stack:ItemStack, player:EntityPlayer):Boolean={
    if(this.tier != stack.getItemDamage())
        return false;
    this.getWorldObj().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, stack.getItemDamage()+1, 3);
    return true;
  }
  override def updateContainingBlockInfo(){
      super.updateContainingBlockInfo();
      val newMetaData:Int = getBlockMetadata();
      if(newMetaData != tier){
        setTier(newMetaData);
      }
  }
  def setTier(t:Int){
    this.tier = t;
    if(inv == null){
      resetInventory();
      return;
    }
    val stacks:Array[ItemStack] = inv;
    resetInventory();
    for(i <-0 until Math.min(inv.length, stacks.length) ){
      inv(i) = stacks(i);
    }
    if(!this.getWorldObj().isRemote)
      this.getWorldObj().addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 4, t);
  }
  
  def playSoundOpen(){
    val d:Double = xCoord + 0.5D;
    val d1:Double = zCoord + 0.5D;
    getWorldObj().playSoundEffect(d, yCoord + 0.5D, d1, "random.chestopen", 0.5F, getWorldObj().rand.nextFloat() * 0.1F + 0.9F);
  }
  def playSoundClosed(){
    val d2:Double = xCoord + 0.5D;
    val d3:Double = zCoord + 0.5D;
    getWorldObj().playSoundEffect(d2, yCoord + 0.5D, d3, "random.chestclosed", 0.5F, getWorldObj().rand.nextFloat() * 0.1F + 0.9F);
  }
  override def getItemStack():ItemStack={
    val stack=new ItemStack(getItem(), 1, tier);
    return getItemStack(stack);
  }
  override def getItemStack(stack:ItemStack):ItemStack={
    stack.setItemDamage(tier);
    if(alternate)
      DecorationHelper.setAlternate(stack, alternate);
    return super.getItemStack(stack);
  }
  
  def setUninitialized(){
    this.initialized = false;
  }
}