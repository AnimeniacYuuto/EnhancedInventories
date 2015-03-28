package yuuto.enhancedinventories.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.tile.TileEnhancedInventory;
import yuuto.enhancedinventories.tile.TileImprovedChestOld;
import yuuto.yuutolib.block.ModBlockContainerMulti;

public abstract class BlockEnhancedInventory extends ModBlockContainerMulti{

	private final Random random = new Random();
	
	public BlockEnhancedInventory(Material mat, String unlocName) {
		super(mat, EnhancedInventories.tab, "EnhancedInventories", unlocName, 
				".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7");
	}	
	
	@Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }
	@Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int p_149736_5_){
		TileEntity tile = world.getTileEntity(x, y, z);
        if(!(tile instanceof TileEnhancedInventory))
        	return 0;
        return ((TileEnhancedInventory)tile).getComparatorInputOverride();
    }
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
	@Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_){
		TileEnhancedInventory tile = (TileEnhancedInventory)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_);
		return tile.getRedstonePower();
    }
	@Override
    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        //return p_149748_5_ == 1 ? this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_) : 0;
		return this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
    }
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int meta)
    {
    	TileEnhancedInventory tileentitychest = (TileEnhancedInventory) world.getTileEntity(x, y, z);
        ItemStack[] inv = tileentitychest.getContents();
    	
        if (inv != null)
        {
            for (int i1 = 0; i1 < inv.length; ++i1)
            {
                ItemStack itemstack = inv[i1];

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
    public void harvestBlock(World p_149636_1_, EntityPlayer player, int x, int y, int z, int p_149636_6_){}
	@Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);
        
        harvesters.set(player);
        int i1 = EnchantmentHelper.getFortuneModifier(player);
        if(!player.capabilities.isCreativeMode)
        	this.dropBlockAsItem(world, x, y, z, meta, i1);
        harvesters.set(null);
	}

}
