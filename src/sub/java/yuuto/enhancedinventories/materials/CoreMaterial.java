package yuuto.enhancedinventories.materials;

import net.minecraft.block.Block;

public class CoreMaterial {
	
	public Block block;
	public int metadata;
	
	public CoreMaterial(Block block, int meta){
		this.block = block;
		this.metadata = meta;
	}
	
	public boolean matches(CoreMaterial mat){
		return this.block == mat.block && this.metadata == mat.metadata;
	}

}
