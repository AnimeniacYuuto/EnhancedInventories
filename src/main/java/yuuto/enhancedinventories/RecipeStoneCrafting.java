package yuuto.enhancedinventories;

import java.util.Random;

import yuuto.enhancedinventories.item.ItemBlockImprovedChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeStoneCrafting implements IRecipe{

	Random rand = new Random();
	
	int type = 0;
	ItemStack result;
	
	public RecipeStoneCrafting(int type){
		this.type = type;
		switch(type){
		case 0:
			result = new ItemStack(EnhancedInventories.improvedChest);
			break;
		case 1:
			result = new ItemStack(EnhancedInventories.locker);
			break;
		}
	}
	
	@Override
    public boolean matches(InventoryCrafting inv, World world)
    {
		String wood = null;
		for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; ++y)
            {
            	ItemStack mat = inv.getStackInRowAndColumn(x, y);
            	if(mat == null)
            		return false;
            	int i = (x*3)+y;
                if(i == 0 || i == 2 || i == 6 || i == 8){
                	int id = OreDictionary.getOreID("cobblestone");
                	int ids[] = OreDictionary.getOreIDs(mat);
                	if(ids == null || ids.length < 1)
                		return false;
                	boolean flag = false;
                	for(int j = 0; j < ids.length; j++){
                		if(ids[j] == id){
                			flag = true;
                			break;
                		}
                	}
                	if(!flag)
                		return false;
                }else if(i != 4){
                	String s = WoodTypes.getId(mat);
                	if(s == null || s.isEmpty())
                		return false;
                	if(wood == null)
                		wood = s;
                	else if(!wood.matches(s))
                		return false;
                }else{
                	if(type == 0 && mat.getItem() != Item.getItemFromBlock(Blocks.wool))
                		return false;
                	if(type == 1 && mat.getItem() != Item.getItemFromBlock(Blocks.trapdoor))
                		return false;
                }
            }
        }

        return true;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack ret = getRecipeOutput();
		ret.getTagCompound().setString("woodType", WoodTypes.getId(inv.getStackInRowAndColumn(0, 1)));
		if(type == 0)
			ret.getTagCompound().setByte("wool", (byte)inv.getStackInRowAndColumn(1, 1).getItemDamage());
		return ret;
	}

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack ret = result.copy();
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("woodType", WoodTypes.getWoodTypes().get(rand.nextInt(WoodTypes.getWoodTypes().size())).id());
		if(type == 0)
			nbt.setByte("wool", (byte)rand.nextInt(16));
		nbt.setBoolean("hopper", false);
		nbt.setBoolean("alt", false);
		nbt.setBoolean("redstone", false);
		ret.setTagCompound(nbt);
		return ret;
	}

}
