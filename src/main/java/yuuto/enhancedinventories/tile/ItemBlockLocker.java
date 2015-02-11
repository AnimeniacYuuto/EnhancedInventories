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

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.EWoodType;
import yuuto.yuutolib.item.ModItemBlockMulti;

public class ItemBlockLocker extends ModItemBlockMulti{

	public ItemBlockLocker(Block block) {
		super(block);
	}
	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List results, boolean bool){
		super.addInformation(stack, player, results, bool);
		if(stack.hasTagCompound()){
			results.add(I18n.format(ColorHelper.WOOL_NAMES[stack.getTagCompound().getInteger("wool")]));
			results.add(I18n.format("woodType."+EWoodType.values()[stack.getTagCompound().getInteger("wood")].toString()));
			if(stack.getTagCompound().getBoolean("hopper"))
				results.add(I18n.format("upgrade.hopper"));
			if(stack.getTagCompound().getBoolean("redstone"))
				results.add(I18n.format("upgrade.redstone"));
			if(stack.getTagCompound().getBoolean("alt"))
				results.add(I18n.format("upgrade.alt"));
			if(stack.getTagCompound().getInteger("wool") == 6 &&
					true)
				results.add(I18n.format("easterEgg.pink"));
		}else{
			results.add(I18n.format(ColorHelper.WOOL_NAMES[0]));
			results.add(I18n.format("woodType."+EWoodType.OAK.toString()));
		}
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
		Block block = world.getBlock(x, y, z);

        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            p_77648_7_ = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, z))
        {
            if (p_77648_7_ == 0)
            {
                --y;
            }

            if (p_77648_7_ == 1)
            {
                ++y;
            }

            if (p_77648_7_ == 2)
            {
                --z;
            }

            if (p_77648_7_ == 3)
            {
                ++z;
            }

            if (p_77648_7_ == 4)
            {
                --x;
            }

            if (p_77648_7_ == 5)
            {
                ++x;
            }
        }

        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, p_77648_7_, stack))
        {
            return false;
        }
        else if (y == 255 && this.field_150939_a.getMaterial().isSolid())
        {
            return false;
        }
        else{
        	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        	double angle = 0;
        	if(l == 0)
    			angle = BlockLocker.getRotation(ForgeDirection.getOrientation(2));
    		else if(l == 1)
    			angle = BlockLocker.getRotation(ForgeDirection.getOrientation(5));
    		else if(l == 2)
    			angle = BlockLocker.getRotation(ForgeDirection.getOrientation(3));
    		else if(l == 3)
    			angle = BlockLocker.getRotation(ForgeDirection.getOrientation(4));
        	double yaw = ((player.rotationYaw % 360) + 360) % 360;
        	boolean reversed = (BlockLocker.angleDifference(angle, yaw) > 0);
        	if(!((BlockLocker)field_150939_a).canPlaceBlockAt(stack, world, x, y, z, reversed))
            {
            	return false;
            }
            else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, p_77648_7_, player, stack))
            {
                int i1 = this.getMetadata(stack.getItemDamage());
                int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);

                if (placeBlockAt(stack, player, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

}
