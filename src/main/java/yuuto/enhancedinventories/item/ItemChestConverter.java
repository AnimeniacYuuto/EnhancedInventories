package yuuto.enhancedinventories.item;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import yuuto.enhancedinventories.ColorHelper;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.compat.modules.VanillaModule;
import yuuto.enhancedinventories.tile.TileImprovedChest;
import yuuto.yuutolib.item.ModItemMulti;

public class ItemChestConverter extends ModItemMulti{

	public ItemChestConverter() {
		super(EnhancedInventories.tab, "EnhancedInventories", "chestConverter", ".Stone", ".Iron", ".Gold", ".Diamond", ".Emerald", ".Obsidian",
				".Copper", ".Tin",
				".Silver", ".Bronze", ".Steel",
				".Platinum",
				".Alumite", ".Cobalt", ".Ardite", ".Manyullyn");
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
	
	@Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
		System.out.println("item use");
		if(world.isRemote || !player.isSneaking()){
			System.out.println("Client");
			return false;
		}
		System.out.println("Server");
		TileEntity tile = world.getTileEntity(X, Y, Z);
		if(!(tile instanceof TileEntityChest))
			return false;
		System.out.println("passed check");
		TileEntityChest oldTile = (TileEntityChest)tile;
		TileImprovedChest newTile = new TileImprovedChest(EInventoryMaterial.values()[stack.getItemDamage()]);
		newTile.woodType = stack.getTagCompound().getString("woodType");
		newTile.woolType = stack.getTagCompound().getInteger("wool");
		newTile.hopper = stack.getTagCompound().getBoolean("hopper");
		newTile.alt = stack.getTagCompound().getBoolean("alt");
		newTile.redstone = stack.getTagCompound().getBoolean("redstone");
		if(!canUpgrade(newTile, world, X, Y, Z))
			return false;
		ItemStack[] stacks = new ItemStack[oldTile.getSizeInventory()];
		for(int i = 0; i < stacks.length; i++){
			stacks[i] = oldTile.getStackInSlot(i);
			oldTile.setInventorySlotContents(i, null);
		}
		newTile.setContents(stacks, false);
		newTile.setOrientation(ForgeDirection.getOrientation( world.getBlockMetadata(X, Y, Z)));
		boolean trapped = world.getBlock(X, Y, Z).canProvidePower();
		world.setBlockToAir(X, Y, Z);
		world.setBlock(X, Y, Z, EnhancedInventories.improvedChest, 0, 3);
		world.setTileEntity(X, Y, Z, newTile);
		world.setBlockMetadataWithNotify(X, Y, Z, stack.getItemDamage(), 3);
		newTile.checkConnections();
		newTile.markDirty(true);
		if(!player.capabilities.isCreativeMode && stack.stackSize > 0){
			stack.stackSize --;
			if(trapped)
				player.inventory.addItemStackToInventory(new ItemStack(Blocks.trapped_chest));
			else
				player.inventory.addItemStackToInventory(new ItemStack(Blocks.chest));
			player.inventoryContainer.detectAndSendChanges();
		}
		return true;
    }
	
	public static boolean canUpgrade(TileImprovedChest newTile, World world, int x, int y, int z){
		List<ForgeDirection> dirs = newTile.getValidConnectionSides();
    	int chests = 0;
    	for(ForgeDirection dir : dirs){
    		TileEntity tile = world.getTileEntity(x+dir.offsetX, y+dir.offsetY, z+dir.offsetZ);
    		if(tile == null || !(tile instanceof TileImprovedChest))
    			continue;
    		TileImprovedChest inv = (TileImprovedChest)tile;
    		if(!inv.isValidForConnection(newTile))
    			continue;
    		if(inv.getPartner() != null)
    			return false;
    		chests++;
    	}
    	if(chests > 1)
    		return false;
    	return true;
	}
	
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
    	Random rand = new Random();
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setString("woodType", WoodTypes.getWoodTypes().get(rand.nextInt(WoodTypes.getWoodTypes().size())).id());
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

}
