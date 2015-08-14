package yuuto.enhancedinventories.tile.base

import java.util.Iterator
import net.minecraft.inventory.IInventory
import yuuto.enhancedinventories.gui.ICraftingTable
import yuuto.enhancedinventories.gui.ContainerCraftingDummy
import net.minecraft.item.crafting.IRecipe
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.world.World
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants

abstract class TileCrafter extends TileBaseEI with ICraftingTable{
  protected val dummyContainer:ContainerCraftingDummy = new ContainerCraftingDummy(this);
  protected val craftingMatrix:InventoryCrafting = this.dummyContainer.craftingMatrix;
  protected val craftResult:IInventory = this.dummyContainer.craftResult;
  
  protected var currentRecipe:IRecipe=null;
  
  override def getCraftingMatrix():InventoryCrafting=craftingMatrix;
  override def getCraftResult():IInventory=return craftResult;
  
  def isCraftingMatrixEmpty():Boolean={
    for(i <- 0 until 9){
      if(craftingMatrix.getStackInSlot(i) != null)
        return false;
    }
    return true;
  }
  
  override def getCurrentRecipe():IRecipe=currentRecipe;
  def findMatchingRecipe(inventoryCrafting:InventoryCrafting, world:World):ItemStack={
      val iterator:Iterator[_] = CraftingManager.getInstance().getRecipeList().iterator();
      var irecipe:IRecipe = null;

      do
      {
          if (!iterator.hasNext())
          {
              return null;
          }

          irecipe = iterator.next().asInstanceOf[IRecipe];
      }
      while (!irecipe.matches(inventoryCrafting, world));

      currentRecipe = irecipe;

      return irecipe.getCraftingResult(inventoryCrafting);
  }
  
  override def writeToNBT(nbt:NBTTagCompound){
    super.writeToNBT(nbt);
    val nbttaglist:NBTTagList = new NBTTagList();
    for(i <- 0 until this.craftingMatrix.getSizeInventory() if(this.craftingMatrix.getStackInSlot(i) != null)){
      val nbttagcompound1:NBTTagCompound = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", i.asInstanceOf[Byte]);
            this.craftingMatrix.getStackInSlot(i).writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
    }
    nbt.setTag("craftingMatrix", nbttaglist);
  }
  override def readFromNBT(nbt:NBTTagCompound){
    super.readFromNBT(nbt);
    val nbttaglist:NBTTagList = nbt.getTagList("craftingMatrix", Constants.NBT.TAG_COMPOUND);
        for (i <-0 until nbttaglist.tagCount())
        {
            val nbttagcompound1:NBTTagCompound = nbttaglist.getCompoundTagAt(i);
            val j:Int = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < craftingMatrix.getSizeInventory())
            {
              craftingMatrix.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
        this.onCraftMatrixChanged(craftingMatrix);
  }

}