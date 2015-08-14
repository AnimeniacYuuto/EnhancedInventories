package yuuto.enhancedinventories.util

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.compat.modules.ModuleRefinedRelocation;
import yuuto.enhancedinventories.materials.CoreMaterialHelper;
import yuuto.enhancedinventories.materials.DecorationHelper;
import yuuto.enhancedinventories.materials.FrameMaterial;
import yuuto.enhancedinventories.materials.FrameMaterials;
import yuuto.enhancedinventories.proxy.ProxyCommon;
import yuuto.enhancedinventories.tile.TileImprovedChest;
import yuuto.enhancedinventories.tile.TileLocker;
import yuuto.enhancedinventories.tile.base.TileConnectiveUpgradeable;
import yuuto.enhancedinventories.tile.upgrades.Upgrade;
import cpw.mods.fml.common.registry.GameRegistry;

object UpdateHelper {
  
  def updateSizeUpgrade(stack:ItemStack){
    if(stack.getItemDamage() == 4){
      stack.func_150996_a(ProxyCommon.functionUpgrades);
      stack.setItemDamage(4);
    }else{
      stack.setTagCompound(new NBTTagCompound());
      DecorationHelper.setFrame(stack.getTagCompound(), getFrameMaterial(stack.getItemDamage()+1));
      stack.setItemDamage(getNewTier(stack.getItemDamage()+1)-1);
    }
  }
  
  def updateInventory(stack:ItemStack){
    var coreStack:ItemStack = null;
    var colorStack:ItemStack = null;
    var alternate:Boolean = false;
    
    if(stack.hasTagCompound()){
      val s:String = stack.getTagCompound().getString("woodType");
      coreStack = getCoreStack(s);
      alternate = stack.getTagCompound().getBoolean("alt");
      if(stack.getTagCompound().hasKey("wool"))
        colorStack = new ItemStack(Blocks.wool, 1, stack.getTagCompound().getInteger("wool"));
    }else{
      coreStack = new ItemStack(Blocks.planks);
      if(stack.getItem() == Item.getItemFromBlock(ProxyCommon.blockImprovedChest) || stack.getItem() == Item.getItemFromBlock(ModuleRefinedRelocation.sortingImprovedChest))
        colorStack = new ItemStack(Blocks.wool);
    }
    
    stack.setTagCompound(new NBTTagCompound());
    DecorationHelper.setCoreBlock(stack.getTagCompound(), coreStack);
    DecorationHelper.setWool(stack.getTagCompound(), colorStack);
    DecorationHelper.setAlternate(stack.getTagCompound(), alternate);
    DecorationHelper.setFrame(stack.getTagCompound(), getFrameMaterial(stack.getItemDamage()+1));
    stack.setItemDamage(getNewTier(stack.getItemDamage()+1)-1);
  }

  def updateTile(tile:TileConnectiveUpgradeable, oldTag:NBTTagCompound){
    val woodType:String = oldTag.getString("woodType");
    val oldTier:Int = oldTag.getInteger("MatType");
    val woolType:Int = oldTag.getInteger("wool");
    val alt:Boolean = oldTag.getBoolean("alt");
    
    val newTier:Int = getNewTier(oldTier);
    val coreStack:ItemStack = getCoreStack(woodType)
    
    tile.decor.coreBlock = CoreMaterialHelper.getCoreMaterialBlock(coreStack);
    tile.decor.coreMetadata = CoreMaterialHelper.getCoreMaterialMetadata(coreStack);;
    tile.decor.frameMaterial = getFrameMaterial(oldTier);
    tile.alternate = alt;
    tile.setTier(newTier);
    if(tile.isInstanceOf[TileImprovedChest]){
      tile.decor.decColor = MinecraftColors.wool(woolType);
    }else if(tile.isInstanceOf[TileLocker]){
      tile.asInstanceOf[TileLocker].reversed=oldTag.getBoolean("reversed");
    }
    
    //rotation
    tile.setRotation(ForgeDirection.getOrientation(oldTag.getInteger("Orientation")));
    
    //Check upgrades
    if(oldTag.getBoolean("redstone"))
      tile.addUpgrade(Upgrade.TRAPPED);
    if(oldTag.getBoolean("hopper"))
      tile.addUpgrade(Upgrade.HOPPER);
    if(oldTier == 4)
      tile.addUpgrade(Upgrade.REINFORCED);
    
    //Set inventory
    val chestContents:Array[ItemStack] = tile.getContents();
    val nbttaglist:NBTTagList= oldTag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (i <-0 until nbttaglist.tagCount()){
            val nbttagcompound1:NBTTagCompound = nbttaglist.getCompoundTagAt(i);
            val j:Int = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < chestContents.length)
            {
                chestContents(j) = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
        tile.getWorldObj().setBlockMetadataWithNotify(tile.xCoord, tile.yCoord, tile.zCoord, newTier, 3);
  }
  private def getCoreStack(woodType:String):ItemStack={
    val parts:Array[String] = woodType.split(":");
    var coreStack:ItemStack = null;
    if(parts.length < 4){
      coreStack = new ItemStack(Blocks.planks);
    }else{
      coreStack = GameRegistry.findItemStack(parts(1),parts(2), 1);
      if(coreStack == null){
        coreStack = new ItemStack(Blocks.planks);
      }else{
        coreStack = coreStack.copy();
        coreStack.setItemDamage(Integer.parseInt(parts(3)));
      }
    }
    return coreStack;
  }
  private def getFrameMaterial(oldTier:Int):FrameMaterial={
    oldTier match {
    case 1 => return FrameMaterials.Instance.getMaterial("frame.mc.iron");
    case 2 => return FrameMaterials.Instance.getMaterial("frame.mc.gold");
    case 3 => return FrameMaterials.Instance.getMaterial("frame.mc.diamond");
    case 4 => return FrameMaterials.Instance.getMaterial("frame.mc.emerald");
    case 5 => return FrameMaterials.Obsidian;
    case 6 => return FrameMaterials.Instance.getMaterial("frame.ore.copper");
    case 7 => return FrameMaterials.Instance.getMaterial("frame.ore.tin");
    case 8 => return FrameMaterials.Instance.getMaterial("frame.ore.silver");
    case 9 => return FrameMaterials.Instance.getMaterial("frame.ore.bronze");
    case 10 => return FrameMaterials.Instance.getMaterial("frame.ore.steel");
    case 11 => return FrameMaterials.Instance.getMaterial("frame.ore.platinum");
    case 12 => return FrameMaterials.Instance.getMaterial("frame.ore.alumite");
    case 13 => return FrameMaterials.Instance.getMaterial("frame.ore.cobalt");
    case 14 => return FrameMaterials.Instance.getMaterial("frame.ore.ardite");
    case 15 => return FrameMaterials.Instance.getMaterial("frame.ore.manyullyn");
    case i=>return FrameMaterials.Stone;
    }
    return FrameMaterials.Stone;
  }
  def getNewTier(oldTier:Int):Int={
    oldTier match{
    case 1 | 6 | 7=>
      return 1;
    case 2 | 8 | 9 | 10 =>
      return 2;
    case 3 | 5 | 11 | 12 =>
      return 5;
    case 4 | 13 | 14 =>
      return 6;
    case 15 =>
      return 7;
    case i=>return 0;
    }
    return 0;
  }
}