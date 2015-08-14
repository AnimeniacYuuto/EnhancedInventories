package yuuto.enhancedinventories.item

import java.util.List
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IIcon
import net.minecraft.item.ItemStack
import net.minecraft.inventory.InventoryCrafting
import cpw.mods.fml.relauncher.SideOnly
import yuuto.enhancedinventories.item.base.ItemBaseEI
import net.minecraft.inventory.Container
import cpw.mods.fml.relauncher.Side
import net.minecraftforge.common.util.Constants

object ItemSchematic{
  final val KEY_RESULT:String = "craftResult";
  final val KEY_CRAFTING_MATRIX:String= "craftMatrix";
  
  def getResult(schematic:ItemStack):ItemStack={
    if(schematic == null || !schematic.hasTagCompound())
      return null;
    return ItemStack.loadItemStackFromNBT(schematic.getTagCompound().getCompoundTag(KEY_RESULT));
  }
  def getCraftingMatrix(schematic:ItemStack):InventoryCrafting={
    if(schematic == null || !schematic.hasTagCompound())
      return null;
    val tagList:NBTTagList = schematic.getTagCompound().getTagList(KEY_CRAFTING_MATRIX, Constants.NBT.TAG_COMPOUND);
    val craftMatrix:InventoryCrafting = new InventoryCrafting(new Container(){
      override def canInteractWith(player:EntityPlayer):Boolean=false;
    }, 3, 3);
    for(i <-0 until tagList.tagCount()){
      val nbttagcompound1:NBTTagCompound = tagList.getCompoundTagAt(i);
      val j:Int = nbttagcompound1.getByte("Slot") & 0xff;
      if (j >= 0 && j < craftMatrix.getSizeInventory())
      {
          craftMatrix.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
      }
    }
    return craftMatrix;
  }
  
  def writeSchematic(schematic:ItemStack, craftMatrix:InventoryCrafting, result:ItemStack){
    if(schematic == null)
      return;
    if(!schematic.hasTagCompound())
      schematic.setTagCompound(new NBTTagCompound());
    schematic.getTagCompound().setTag(KEY_RESULT, result.writeToNBT(new NBTTagCompound()));
    val tagList:NBTTagList = new NBTTagList();
    for(i <- 0 until craftMatrix.getSizeInventory() if(craftMatrix.getStackInSlot(i) != null)){
      val nbt:NBTTagCompound = new NBTTagCompound();
      nbt.setByte("Slot", i.asInstanceOf[Byte]);
      craftMatrix.getStackInSlot(i).writeToNBT(nbt);
      nbt.setByte("Count", 1.asInstanceOf[Byte]);
      tagList.appendTag(nbt);
    }
    schematic.getTagCompound().setTag(KEY_CRAFTING_MATRIX, tagList);
  }
}
class ItemSchematic(name:String) extends ItemBaseEI(name){
  var writtenIcon:IIcon = null;
  
  @SideOnly(Side.CLIENT)
  override def registerIcons(reg:IIconRegister)
  {
      this.itemIcon = reg.registerIcon(this.getIconString());
      this.writtenIcon = reg.registerIcon(this.getIconString()+"Written");
  }
  
  @SideOnly(Side.CLIENT)
  override def getIconFromDamage(damage:Int):IIcon=if(damage==1){writtenIcon}else{itemIcon};
  @SideOnly(Side.CLIENT)
  override def getIconIndex(stack:ItemStack):IIcon={
      return this.getIconFromDamage(if(stack.hasTagCompound()){1}else{0});        
  }
    
    @Override
  @SideOnly(Side.CLIENT)
  override def addInformation(stack:ItemStack, player:EntityPlayer, info:List[_], bool:Boolean) {
    super.addInformation(stack, player, info, bool);
    if(!stack.hasTagCompound())
      return;
    info.asInstanceOf[List[String]].add(ItemSchematic.getResult(stack).getDisplayName());
  }
}