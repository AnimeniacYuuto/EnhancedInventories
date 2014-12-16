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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.EnhancedInventories;
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

}
