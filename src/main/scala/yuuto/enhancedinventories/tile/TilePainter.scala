package yuuto.enhancedinventories.tile

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import yuuto.enhancedinventories.inventory.IInventoryParent;
import yuuto.enhancedinventories.inventory.InventorySimple;
import yuuto.enhancedinventories.materials.PaintHelper;

class TilePainter extends TileEntity with IInventoryParent{
  val inv:InventorySimple = new InventorySimple(4, this);
  val resultInv:InventorySimple = new InventorySimple(1, this);
  
  def getInventory():IInventory=inv;
  def getResultInventory():IInventory=resultInv;
  override def onInventoryChanged(inventorySimple:IInventory) {
    this.markDirty();
    if(inventorySimple == inv){
      resultInv.setInventorySlotContents(0, PaintHelper.getResult(inv.getStackInSlot(0), inv.getStackInSlot(1), inv.getStackInSlot(2), inv.getStackInSlot(3)));
    }
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    val nbttaglist:NBTTagList = new NBTTagList();
    for(i <- 0 until this.inv.getSizeInventory() if(this.inv.getStackInSlot(i) != null)){
      val nbttagcompound1:NBTTagCompound = new NBTTagCompound();
      nbttagcompound1.setByte("Slot", i.asInstanceOf[Byte]);
      this.inv.getStackInSlot(i).writeToNBT(nbttagcompound1);
      nbttaglist.appendTag(nbttagcompound1);
    }
    nbt.setTag("inv", nbttaglist);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    val nbttaglist:NBTTagList = nbt.getTagList("inv", Constants.NBT.TAG_COMPOUND);
        for (i <- 0 until nbttaglist.tagCount())
        {
            val nbttagcompound1:NBTTagCompound = nbttaglist.getCompoundTagAt(i);
            val j:Int = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < inv.getSizeInventory())
            {
              inv.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
  }

}