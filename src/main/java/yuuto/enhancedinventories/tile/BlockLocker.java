/*******************************************************************************
 * Copyright (c) 2014 Yuuto.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * 	   cpw - src reference from Iron Chests
 * 	   doku/Dokucraft staff - base chest texture
 *     Yuuto - initial API and implementation
 ******************************************************************************/
package yuuto.enhancedinventories.tile;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.yuutolib.block.ModBlockContainerMulti;
import yuuto.yuutolib.block.tile.IRotatable;

public class BlockLocker extends BlockConnectiveInventory{

	public BlockLocker() {
		super(Material.wood, EnhancedInventories.tab, "EnhancedInventories", "improvedLocker", 
				".Stone", ".Iron", ".Gold", ".Diamond", ".Emerald", ".Obsidian",
				".Copper", ".Tin",
				".Silver", ".Bronze", ".Steel",
				".Platinum");
	}	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof TileLocker))
			return;
		TileLocker locker = (TileLocker)tile;
		if(stack.hasTagCompound()){
			locker.woodType = stack.getTagCompound().getInteger("wood");
			locker.woolType = stack.getTagCompound().getInteger("wool");
			locker.hopper = stack.getTagCompound().getBoolean("hopper");
			locker.alt = stack.getTagCompound().getBoolean("alt");
			locker.redstone = stack.getTagCompound().getBoolean("redstone");
		}
		
		int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0)
			locker.setOrientation(ForgeDirection.getOrientation(2));
		else if(l == 1)
			locker.setOrientation(ForgeDirection.getOrientation(5));
		else if(l == 2)
			locker.setOrientation(ForgeDirection.getOrientation(3));
		else if(l == 3)
			locker.setOrientation(ForgeDirection.getOrientation(4));
		double angle = getRotation(locker.getOrientation());
		double yaw = ((player.rotationYaw % 360) + 360) % 360;
    	locker.reversed = (angleDifference(angle, yaw) > 0);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileLocker(EInventoryMaterial.values()[meta]);
	}
	
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
    	Random rand = new Random();
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setByte("wood", (byte)rand.nextInt(6));
    	nbt.setByte("wool", (byte) rand.nextInt(16));
    	NBTTagCompound h = (NBTTagCompound)nbt.copy();
    	h.setBoolean("hopper", true);
    	NBTTagCompound r = (NBTTagCompound)nbt.copy();
    	r.setBoolean("redstone", true);
    	NBTTagCompound a = (NBTTagCompound)nbt.copy();
    	a.setBoolean("alt", true);
    	for (int ix = 0; ix < subNames.length; ix++) {
    		ItemStack stack = new ItemStack(this, 1, ix);
    		stack.setTagCompound((NBTTagCompound)nbt.copy());
			subItems.add(stack);
			stack = stack.copy();
			stack.setTagCompound((NBTTagCompound)h.copy());
			subItems.add(stack);
			stack = stack.copy();
			stack.setTagCompound((NBTTagCompound)r.copy());
			subItems.add(stack);
			stack = stack.copy();
			stack.setTagCompound((NBTTagCompound)a.copy());
			subItems.add(stack);
		}
    	
    }
    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack){
    	if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            if (captureDrops.get())
            {
                capturedDrops.get().add(stack);
                return;
            }
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileLocker){
            	stack.getTagCompound().setByte("wood", (byte)((TileLocker)tile).woodType);
                stack.getTagCompound().setByte("wool", (byte)((TileLocker)tile).woolType);
                stack.getTagCompound().setBoolean("hopper", ((TileLocker)tile).hopper);
                stack.getTagCompound().setBoolean("alt", ((TileLocker)tile).alt);
                stack.getTagCompound().setBoolean("redstone", ((TileLocker)tile).redstone);
            }
            float f = 0.7F;
            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityitem);
        }
    }
    
    @Override
    protected ItemStack createStackedBlock(int meta){
    	int j = meta;
        Item item = Item.getItemFromBlock(this);
        ItemStack ret = new ItemStack(item, 1, j);
        ret.setTagCompound(new NBTTagCompound());
        ret.getTagCompound().setByte("wood", (byte)0);
        ret.getTagCompound().setByte("wool", (byte)0);
        ret.getTagCompound().setBoolean("hopper", false);
        ret.getTagCompound().setBoolean("alt", false);
        ret.getTagCompound().setBoolean("redstone", false);
        return ret;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
    	Item item = getItem(world, x, y, z);

        if (item == null)
        {
            return null;
        }

        //Block block = item instanceof ItemBlock && !isFlowerPot() ? Block.getBlockFromItem(item) : this;
        ItemStack ret = new ItemStack(item, 1, world.getBlockMetadata(x, y, z));
        TileEntity tile = world.getTileEntity(x, y, z);
        ret.setTagCompound(new NBTTagCompound());
        if(tile != null && tile instanceof TileLocker){
        	 ret.getTagCompound().setByte("wood", (byte)((TileLocker)tile).woodType);
             ret.getTagCompound().setByte("wool", (byte)((TileLocker)tile).woolType);
             ret.getTagCompound().setBoolean("hopper", ((TileLocker)tile).hopper);
             ret.getTagCompound().setBoolean("alt", ((TileLocker)tile).alt);
             ret.getTagCompound().setBoolean("redstone", ((TileLocker)tile).redstone);
             return ret;
        }
        ret.getTagCompound().setByte("wood", (byte)0);
        ret.getTagCompound().setByte("wool", (byte)0);
        ret.getTagCompound().setBoolean("hopper", false);
        ret.getTagCompound().setBoolean("alt", false);
        ret.getTagCompound().setBoolean("redstone", false);
        return ret;
    }
    
    public boolean canPlaceBlockAt(ItemStack itemBlock, World world, int x, int y, int z, boolean reversed){
    	List<ForgeDirection> dirs = TileLocker.conDirs;
    	int lockers = 0;
    	for(ForgeDirection dir : dirs){
    		TileEntity tile = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileLocker))
    			continue;
    		TileLocker locker = (TileLocker)tile;
    		if(!locker.isValidForConnection(itemBlock) || locker.reversed != reversed)
    			continue;
    		if(locker.getPartner() != null)
    			return false;
    		lockers++;
    	}
    	if(lockers > 1)
    		return false;
    	return true;
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile == null || !(tile instanceof TileLocker))
        	return 0;
    	return Container.calcRedstoneFromInventory((IInventory) tile);
    }
    
    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
    	TileLocker locker = (TileLocker)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_);
    	if(!locker.redstone){
    		return 0;
    	}
    	int i1 = locker.getTotalUsingPlayers();
    	return MathHelper.clamp_int(i1, 0, 15);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return p_149748_5_ == 1 ? this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_) : 0;
    }
    
    /*public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        TileLocker tile = (TileLocker)p_149719_1_.getTileEntity(p_149719_2_, p_149719_3_, p_149719_4_);
    	if(tile.getPartner() == null){
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		return;
    	}
    	switch(tile.partnerDir){
    	case NORTH:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	case SOUTH:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
    		break;
    	case WEST:
    		this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	case EAST:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
    		break;
    	default:
    		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    		break;
    	}
    }*/
    
    public static int getRotation(ForgeDirection dir) {
		if (dir == ForgeDirection.WEST) return 90;
		else if (dir == ForgeDirection.NORTH) return 180;
		else if (dir == ForgeDirection.EAST) return 270;
		else return 0;
	}
    public static double angleDifference(double angle1, double angle2) {
		return ((angle2 - angle1) % 360 + 540) % 360 - 180;
	}
}
