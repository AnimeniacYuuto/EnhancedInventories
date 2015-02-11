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
package yuuto.enhancedinventories.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.client.RendererImprovedChest;
import yuuto.enhancedinventories.client.RendererImprovedChestItem;
import yuuto.enhancedinventories.tile.TileImprovedChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends ProxyCommon {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientRegistry.bindTileEntitySpecialRenderer(TileImprovedChest.class, new RendererImprovedChest());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(EnhancedInventories.improvedChest), new RendererImprovedChestItem());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}

}
