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
package yuuto.enhancedinventories.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.block.BlockImprovedChest;
import yuuto.enhancedinventories.compat.modules.VanillaModule;
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
			results.add(I18n.format(ColorHelper.WOOL_NAMES[stack.getTagCompound().getInteger("wool")]));
			//results.add(WoodTypes.getWoodType(stack.getTagCompound().getString("woodType")).name());
			WoodTypes.getWoodType(stack.getTagCompound().getString("woodType")).addInformation(stack, player, results, bool);
			
			if(stack.getTagCompound().getBoolean("hopper"))
				results.add(I18n.format("upgrade.hopper"));
			if(stack.getTagCompound().getBoolean("redstone"))
				results.add(I18n.format("upgrade.redstone"));
			if(stack.getTagCompound().getBoolean("alt"))
				results.add(I18n.format("upgrade.alt"));
			if(stack.getTagCompound().getInteger("wool") == 6 && isPinkWood(stack))
				results.add(I18n.format("easterEgg.pink"));
		}else{
			results.add(I18n.format(ColorHelper.WOOL_NAMES[0]));
			//results.add(VanillaModule.OAK.name());
			VanillaModule.OAK.addInformation(stack, player, results, bool);
		}
	}
	public boolean isPinkWood(ItemStack stack){
		return stack.getTagCompound().getString("woodType").matches("wood:Natura:planks:0");
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer p_77648_2_, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("wood")){
			int w = stack.getTagCompound().getInteger("wood");
			stack.getTagCompound().removeTag("wood");
			ItemStack stack1 = new ItemStack(Blocks.planks, 1, w);
			stack.getTagCompound().setString("woodType", WoodTypes.getId(stack1));
		}
		
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
	
	@Override
	public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		super.onUpdate(stack, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("wood")){
			int w = stack.getTagCompound().getInteger("wood");
			stack.getTagCompound().removeTag("wood");
			ItemStack stack1 = new ItemStack(Blocks.planks, 1, w);
			stack.getTagCompound().setString("woodType", WoodTypes.getId(stack1));
		}
	}
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
    {
		if(entityItem.getEntityItem().hasTagCompound() && entityItem.getEntityItem().getTagCompound().hasKey("wood")){
			ItemStack stack = entityItem.getEntityItem();
			int w = stack.getTagCompound().getInteger("wood");
			stack.getTagCompound().removeTag("wood");
			ItemStack stack1 = new ItemStack(Blocks.planks, 1, w);
			stack.getTagCompound().setString("woodType", WoodTypes.getId(stack1));
		}
        return super.onEntityItemUpdate(entityItem);
    }

}
