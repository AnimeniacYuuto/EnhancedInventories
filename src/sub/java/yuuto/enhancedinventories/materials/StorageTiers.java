package yuuto.enhancedinventories.materials;

import java.util.ArrayList;
import java.util.List;

public enum StorageTiers {
	Tier1, Tier2, Tier3, Tier4, Tier5, Tier6, Tier7, Tier8;
	
	List<FrameMaterial> mats = new ArrayList<FrameMaterial>();
	public void addMat(FrameMaterial mat){
		if(!mats.contains(mat))
			mats.add(mat);
	}
	public List<FrameMaterial> getMats(){
		return mats;
	}
	
	public static StorageTiers getTier(int i){
		return StorageTiers.values()[i];
	}
}
