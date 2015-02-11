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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.yuutolib.block.ModBlockContainerMulti;
import yuuto.yuutolib.block.tile.IRotatable;

public class BlockImprovedChest extends BlockConnectiveInventory{

	private final Random random = new Random();
	
	public BlockImprovedChest() {
		super(Material.wood, EnhancedInventories.tab, "EnhancedInventories", "improvedChest", 
				".Stone", ".Iron", ".Gold", ".Diamond", ".Emerald", ".Obsidian",
				".Copper", ".Tin",
				".Silver", ".Bronze", ".Steel",
				".Platinum");
		this.setHardness(2.1f);
	}	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof TileImprovedChest))
			return;
		TileImprovedChest chest = (TileImprovedChest)tile;
		if(stack.hasTagCompound()){
			chest.woodType = stack.getTagCompound().getInteger("wood");
			chest.woolType = stack.getTagCompound().getInteger("wool");
			chest.hopper = stack.getTagCompound().getBoolean("hopper");
			chest.alt = stack.getTagCompound().getBoolean("alt");
			chest.redstone = stack.getTagCompound().getBoolean("redstone");
		}
		
		int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(l == 0)
			chest.setOrientation(ForgeDirection.getOrientation(2));
		else if(l == 1)
			chest.setOrientation(ForgeDirection.getOrientation(5));
		else if(l == 2)
			chest.setOrientation(ForgeDirection.getOrientation(3));
		else if(l == 3)
			chest.setOrientation(ForgeDirection.getOrientation(4));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileImprovedChest(EInventoryMaterial.values()[meta]);
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
    public int damageDropped(int meta)
    {
        return meta;
    }
    
    @Override
    public void harvestBlock(World p_149636_1_, EntityPlayer player, int x, int y, int z, int p_149636_6_)
    {
    	//System.out.println("Distance: " +player.getDistance(x, y, z));
    }
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
    	if(player.capabilities.isCreativeMode)
    		return;
    	player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        harvesters.set(player);
        int i1 = EnchantmentHelper.getFortuneModifier(player);
        this.dropBlockAsItem(world, x, y, z, meta, i1);
        harvesters.set(null);
    }
    @Override
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack){
    	if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            TileEntity tile = world.getTileEntity(x, y, z);
            if(tile instanceof TileImprovedChest){
            	stack.setTagCompound(new NBTTagCompound());
            	stack.getTagCompound().setByte("wood", (byte)((TileImprovedChest)tile).woodType);
                stack.getTagCompound().setByte("wool", (byte)((TileImprovedChest)tile).woolType);
                stack.getTagCompound().setBoolean("hopper", ((TileImprovedChest)tile).hopper);
                stack.getTagCompound().setBoolean("alt", ((TileImprovedChest)tile).alt);
                stack.getTagCompound().setBoolean("redstone", ((TileImprovedChest)tile).redstone);
            }
            if (captureDrops.get())
            {
                capturedDrops.get().add(stack);
                return;
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
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int meta)
    {
    	TileImprovedChest tileentitychest = (TileImprovedChest)world.getTileEntity(x, y, z);
        
    	if(tileentitychest.getPartner() != null){
    		tileentitychest.getPartner().disconnect();
    		tileentitychest.disconnect();
    	}
    	
        if (tileentitychest != null)
        {
            for (int i1 = 0; i1 < tileentitychest.getSizeInventory(); ++i1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(i1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                    {
                        int j1 = this.random.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, p_149749_5_);
        }

        super.breakBlock(world, x, y, z, p_149749_5_, meta);
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
        if(tile != null && tile instanceof TileImprovedChest){
        	 ret.getTagCompound().setByte("wood", (byte)((TileImprovedChest)tile).woodType);
             ret.getTagCompound().setByte("wool", (byte)((TileImprovedChest)tile).woolType);
             ret.getTagCompound().setBoolean("hopper", ((TileImprovedChest)tile).hopper);
             ret.getTagCompound().setBoolean("alt", ((TileImprovedChest)tile).alt);
             ret.getTagCompound().setBoolean("redstone", ((TileImprovedChest)tile).redstone);
             return ret;
        }
        ret.getTagCompound().setByte("wood", (byte)0);
        ret.getTagCompound().setByte("wool", (byte)0);
        ret.getTagCompound().setBoolean("hopper", false);
        ret.getTagCompound().setBoolean("alt", false);
        ret.getTagCompound().setBoolean("redstone", false);
        return ret;
    }
    
    public boolean canPlaceBlockAt(ItemStack itemBlock, World world, int x, int y, int z){
    	List<ForgeDirection> dirs = TileImprovedChest.conDirs;
    	int chests = 0;
    	for(ForgeDirection dir : dirs){
    		TileEntity tile = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileImprovedChest))
    			continue;
    		TileImprovedChest chest = (TileImprovedChest)tile;
    		if(!chest.isValidForConnection(itemBlock))
    			continue;
    		if(chest.getPartner() != null)
    			return false;
    		chests++;
    	}
    	if(chests > 1)
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
        if(tile == null || !(tile instanceof TileImprovedChest))
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
    	TileImprovedChest chest = (TileImprovedChest)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_);
    	if(!chest.redstone){
    		return 0;
    	}
    	int i1 = chest.getTotalUsingPlayers();
    	return MathHelper.clamp_int(i1, 0, 15);
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return p_149748_5_ == 1 ? this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_) : 0;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        TileImprovedChest tile = (TileImprovedChest)p_149719_1_.getTileEntity(p_149719_2_, p_149719_3_, p_149719_4_);
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
        /*if (p_149719_1_.getBlock(p_149719_2_, p_149719_3_, p_149719_4_ - 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (p_149719_1_.getBlock(p_149719_2_, p_149719_3_, p_149719_4_ + 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        }
        else if (p_149719_1_.getBlock(p_149719_2_ - 1, p_149719_3_, p_149719_4_) == this)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (p_149719_1_.getBlock(p_149719_2_ + 1, p_149719_3_, p_149719_4_) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        }
        else
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }*/
    }
}