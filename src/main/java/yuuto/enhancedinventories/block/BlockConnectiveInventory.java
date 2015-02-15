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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.tile.TileConnectiveInventory;
import yuuto.yuutolib.block.ModBlockContainerMulti;
import yuuto.yuutolib.block.tile.IRotatable;

public abstract class BlockConnectiveInventory extends ModBlockContainerMulti{

	public BlockConnectiveInventory(Material mat, CreativeTabs tab, String mod,
			String unlocName, String ... subNames) {
		super(mat, tab, mod, unlocName, subNames);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
                return false;
        }
        //code to open gui explained later
        player.openGui(EnhancedInventories.instance, 0, world, x, y, z);
        	return true;
    }
	
	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis){
		TileEntity tile = worldObj.getTileEntity(x, y, z);
		if(!(tile instanceof IRotatable))
			return false;
		if(axis != ForgeDirection.UP && axis != ForgeDirection.DOWN)
			return false;
		((IRotatable)tile).rotateAround(axis);
		return true;
    }
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("planks_oak");
        for(int i = 0; i < subNames.length; i++){
        	icons[i] = p_149651_1_.registerIcon("planks_oak");
        }
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
    public int damageDropped(int meta)
    {
        return meta;
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
        if(tile == null || !(tile instanceof TileConnectiveInventory))
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
    	TileConnectiveInventory chest = (TileConnectiveInventory)p_149709_1_.getTileEntity(p_149709_2_, p_149709_3_, p_149709_4_);
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

}
