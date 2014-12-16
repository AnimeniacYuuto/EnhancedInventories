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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.EWoodType;
import yuuto.yuutolib.item.ModItemBlockMulti;

public class ItemBlockImprovedChest extends ModItemBlockMulti{

	public ItemBlockImprovedChest(Block block) {
		super(block);
	}
	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List results, boolean bool){
		super.addInformation(stack, player, results, bool);
		if(stack.hasTagCompound()){
			results.add(ColorHelper.WOOL_NAMES[stack.getTagCompound().getInteger("wool")].toString());
			results.add(EWoodType.values()[stack.getTagCompound().getInteger("wood")].toString());
			if(stack.getTagCompound().getBoolean("hopper"))
				results.add("Hopper");
			if(stack.getTagCompound().getBoolean("redstone"))
				results.add("Trapped");
			if(stack.getTagCompound().getBoolean("alt"))
				results.add("Alternate");
			if(stack.getTagCompound().getInteger("wool") == 6 &&
					true)
				results.add("Pink Power");
		}else{
			results.add(ColorHelper.WOOL_NAMES[0].toString());
			results.add(EWoodType.OAK.toString());
		}
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer p_77648_2_, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
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
        else if (!p_77648_2_.canPlayerEdit(x, y, z, p_77648_7_, stack))
        {
            return false;
        }
        else if (y == 255 && this.field_150939_a.getMaterial().isSolid())
        {
            return false;
        }
        else if(!((BlockImprovedChest)field_150939_a).canPlaceBlockAt(stack, world, x, y, z))
        {
        	return false;
        }
        else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, p_77648_7_, p_77648_2_, stack))
        {
            int i1 = this.getMetadata(stack.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, i1);

            if (placeBlockAt(stack, p_77648_2_, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, j1))
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
