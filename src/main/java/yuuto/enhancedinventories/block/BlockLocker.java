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
package yuuto.enhancedinventories.block;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.compat.SortingUpgradeHelper;
import yuuto.enhancedinventories.compat.TileSortingLocker;
import yuuto.enhancedinventories.item.ItemSizeUpgrade;
import yuuto.enhancedinventories.tile.TileLocker;

public class BlockLocker extends BlockConnectiveInventory{

	private final Random random = new Random();
	
	public BlockLocker() {
		super(Material.wood, EnhancedInventories.tab, "EnhancedInventories", "locker", 
				".Stone", ".Iron", ".Gold", ".Diamond", ".Emerald", ".Obsidian",
				".Copper", ".Tin",
				".Silver", ".Bronze", ".Steel",
				".Platinum",
				".Alumite", ".Cobalt", ".Ardite", ".Manyullyn");
		this.setHardness(2.1f);
	}
	public BlockLocker(String name) {
		super(Material.wood, EnhancedInventories.tab, "EnhancedInventories", name, 
				".Stone", ".Iron", ".Gold", ".Diamond", ".Emerald", ".Obsidian",
				".Copper", ".Tin",
				".Silver", ".Bronze", ".Steel",
				".Platinum",
				".Alumite", ".Cobalt", ".Ardite", ".Manyullyn");
		this.setHardness(2.1f);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
		ItemStack held = player.getHeldItem();
		if(!EnhancedInventories.refinedRelocation || held == null || held.getItem() != SortingUpgradeHelper.getUpgradeItem())
			return super.onBlockActivated(world, x, y, z, player, side, p_149727_7_, p_149727_8_, p_149727_9_);
		if(world.isRemote)
			return true;
		
		TileLocker original = (TileLocker)world.getTileEntity(x, y, z);
		if(!original.canUpgrade(held)){
			return false;
		}
		if(!SortingUpgradeHelper.hasUpgradeMaterials(original, player)){
			player.addChatComponentMessage(new ChatComponentText("Requires 2 "+original.getType().getMaterial()));
			return false;
		}
		try{
			TileSortingLocker newChest = (TileSortingLocker)original.getUpgradeTile(held);
			if(newChest == null || newChest == original || !ItemSizeUpgrade.canUpgrade(original, newChest, world, x, y, z))
				return false;
			newChest.setContents(original.getContents(), true);
			world.setBlockToAir(x, y, z);
			world.setBlock(x, y, z, EnhancedInventories.sortingLocker, original.getType().ordinal(), 3);
			world.setTileEntity(x, y, z, newChest);
			world.setBlockMetadataWithNotify(x, y, z, newChest.getType().ordinal(), 3);
			newChest.checkConnections();
			newChest.markDirty(true);
			if(!player.capabilities.isCreativeMode){
				held.stackSize--;
				SortingUpgradeHelper.removeUpgradeMaterials(newChest, player);
			}
			return true;
		}catch(Exception e){
			return false;
		}
    }
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
		TileEntity tile = world.getTileEntity(x, y, z);
		if(!(tile instanceof TileLocker))
			return;
		TileLocker chest = (TileLocker)tile;
		if(stack.hasTagCompound()){
			chest.woodType = stack.getTagCompound().getString("woodType");
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
		
		double angle = getRotation(chest.getOrientation().getOpposite());
		double yaw = ((player.rotationYaw % 360) + 360) % 360;
		chest.reversed = angleDifference(angle, yaw) > 0;
	}
	
	public static int getRotation(ForgeDirection dir) {
		if (dir == ForgeDirection.WEST) return 90;
		else if (dir == ForgeDirection.NORTH) return 180;
		else if (dir == ForgeDirection.EAST) return 270;
		else return 0;
	}
	public static double angleDifference(double angle1, double angle2) {
		return ((angle2 - angle1) % 360 + 540) % 360 - 180;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileLocker(EInventoryMaterial.values()[meta]);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
    	Random rand = new Random();
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setString("woodType", WoodTypes.getWoodTypes().get(rand.nextInt(WoodTypes.getWoodTypes().size())).id());
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
    public void harvestBlock(World p_149636_1_, EntityPlayer player, int x, int y, int z, int p_149636_6_)
    {
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
            if(tile instanceof TileLocker){
            	stack.setTagCompound(new NBTTagCompound());
            	stack.getTagCompound().setString("woodType", ((TileLocker)tile).woodType);
                stack.getTagCompound().setBoolean("hopper", ((TileLocker)tile).hopper);
                stack.getTagCompound().setBoolean("alt", ((TileLocker)tile).alt);
                stack.getTagCompound().setBoolean("redstone", ((TileLocker)tile).redstone);
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
    	TileLocker tileentitychest = (TileLocker)world.getTileEntity(x, y, z);
        
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
        ret.getTagCompound().setString("woodType", WoodTypes.DEFAULT_WOOD_ID);
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
        	 ret.getTagCompound().setString("woodType", ((TileLocker)tile).woodType);
             ret.getTagCompound().setBoolean("hopper", ((TileLocker)tile).hopper);
             ret.getTagCompound().setBoolean("alt", ((TileLocker)tile).alt);
             ret.getTagCompound().setBoolean("redstone", ((TileLocker)tile).redstone);
             return ret;
        }
        ret.getTagCompound().setString("woodType", WoodTypes.DEFAULT_WOOD_ID);
        ret.getTagCompound().setBoolean("hopper", false);
        ret.getTagCompound().setBoolean("alt", false);
        ret.getTagCompound().setBoolean("redstone", false);
        return ret;
    }
    
    public boolean canPlaceBlockAt(ItemStack itemBlock, World world, int x, int y, int z, boolean reversed){
    	List<ForgeDirection> dirs = TileLocker.conDirs;
    	int chests = 0;
    	for(ForgeDirection dir : dirs){
    		TileEntity tile = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileLocker))
    			continue;
    		TileLocker chest = (TileLocker)tile;
    		if(!chest.isValidForConnection(itemBlock))
    			continue;
    		if(chest.reversed != reversed)
    			continue;
    		if(chest.getPartner() != null)
    			return false;
    		chests++;
    	}
    	if(chests > 1)
    		return false;
    	return true;
    }
    
    /*public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        TileLocker tile = (TileLocker)p_149719_1_.getTileEntity(p_149719_2_, p_149719_3_, p_149719_4_);
    	if(tile.getPartner() == null){
    		this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
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
    //}
}