package yuuto.enhancedinventories.tile.upgrades.helpers

import net.minecraft.block.BlockChest
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.{TileEntityChest, TileEntity}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import yuuto.yuutolib.inventory.InventoryHelper

object HopperUpgradeHelper {
	
	def moveItems(hopper:IInventory, world:World, x:Int, y:Int, z:Int, pullDir:ForgeDirection, pushDir:ForgeDirection){
		var tile:TileEntity = world.getTileEntity(x+pullDir.offsetX, y+pullDir.offsetY, z+pullDir.offsetZ);
		if(tile != null && tile.isInstanceOf[IInventory]) {
			if(tile.isInstanceOf[TileEntityChest]){
				val b:BlockChest=tile.getBlockType().asInstanceOf[BlockChest];
				InventoryHelper.pullStacks(b.func_149951_m(world, x+pullDir.offsetX, y+pullDir.offsetY, z+pullDir.offsetZ), hopper, 64, pullDir, false);
			}
			else
				InventoryHelper.pullStacks(tile.asInstanceOf[IInventory], hopper, 64, pullDir, false);
		}
		tile = world.getTileEntity(x+pushDir.offsetX, y+pushDir.offsetY, z+pushDir.offsetZ);
		if(tile != null && tile.isInstanceOf[IInventory]){
			if(tile.isInstanceOf[TileEntityChest]){
				val b:BlockChest=tile.getBlockType().asInstanceOf[BlockChest];
				InventoryHelper.pullStacks(hopper, b.func_149951_m(world, x+pushDir.offsetX, y+pushDir.offsetY, z+pushDir.offsetZ), 64, pushDir, false);
			}
			else
				InventoryHelper.pullStacks(hopper, tile.asInstanceOf[IInventory], 64, pushDir, false);
		}
	}

}
