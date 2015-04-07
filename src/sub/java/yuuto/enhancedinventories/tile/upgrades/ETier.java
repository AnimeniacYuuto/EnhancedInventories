package yuuto.enhancedinventories.tile.upgrades;

import java.util.List;

import yuuto.enhancedinventories.materials.FrameMaterial;

public enum ETier {
	Tier1, Tier2, Tier3, Tier4, Tier5, Tier6, Tier7, Tier8;
	
	
	public List<FrameMaterial> getFrames(){
		return null;
	}
	public static ETier get(int i){
		return values()[i];
	}

}
