package yuuto.enhancedinventories.compat.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import yuuto.enhancedinventories.EInventoryMaterial;
import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.RecipeFunctionUpgrades;
import yuuto.enhancedinventories.WoodTypes;
import yuuto.enhancedinventories.WoolUpgradeHelper;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeFunctionUpgradesHandler extends TemplateRecipeHandler{

	public class CachedUpgradeRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
        public int type = 0;
        
        public int woodCount;
        
        public int woodType;
        public int woolType;
        public int matType;
        
        public boolean resultCache = false;
        
        int chestIndex;
        int dyeIndex;

        public CachedUpgradeRecipe(RecipeFunctionUpgrades recipe) {
            this.type = recipe.getType();
            chestIndex = type == 0 || type == 3 ? 4: type == 2 || type == 5 ? 0 : 1;
            result = new PositionedStack(this.getCoreStacks(true), 119, 24, true);
            result.setPermutationToRender(0);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients();
        }
        public CachedUpgradeRecipe(RecipeFunctionUpgrades recipe, int dye) {
            this.type = recipe.getType();
            this.resultCache = true;
            this.woolType = dye;
            chestIndex = type == 0 || type == 3 ? 4: type == 2 || type == 5 ? 0 : 1;
            result = new PositionedStack(this.getCoreStacks(true), 119, 24, true);
            result.setPermutationToRender(0);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients();
        }

        /**
         * @param width
         * @param height
         * @param items  an ItemStack[] or ItemStack[][]
         */
        public void setIngredients() {
        	this.woodCount = WoodTypes.getWoodTypes().size();
        	ItemStack[] coreStacks = getCoreStacks(false);
        	if(type == 2 || type == 5){
        		PositionedStack stack = new PositionedStack(coreStacks, 25 + 0 * 18, 6 + 0 * 18, false);
        		this.chestIndex = ingredients.size();
        		ingredients.add(stack);
        		return;
        	}
        	for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int i = (y*3)+x;
                    if(type == 0 || type == 3){
	                    if(i == 0 || i == 2 || i == 6 || i == 8){
	                    	continue;
	                    }else if(i != 4){
	                    	PositionedStack stack = new PositionedStack(new ItemStack(Blocks.hopper), 25 + x * 18, 6 + y * 18, false);
	                    	ingredients.add(stack);
	                    }else{
	                    	PositionedStack stack = new PositionedStack(coreStacks, 25 + x * 18, 6 + y * 18, false);
	                    	this.chestIndex = ingredients.size();
	                    	ingredients.add(stack);
	                    }
	                    continue;
                    }
                    if(type == 1 || type == 4){
                    	if(i == 0){
                    		PositionedStack stack = new PositionedStack(new ItemStack(Blocks.tripwire_hook), 25 + x * 18, 6 + y * 18, false);
	                    	ingredients.add(stack);
                    	}else if(i == 1){
                    		PositionedStack stack = new PositionedStack(coreStacks, 25 + x * 18, 6 + y * 18, false);
                    		this.chestIndex = ingredients.size();
                    		ingredients.add(stack);
                    	}else{
                    		break;
                    	}
                    	continue;
                    }
                    if(type == 6){
                    	if(i == 0){
                    		PositionedStack stack = new PositionedStack(getDyeStacks(), 25 + x * 18, 6 + y * 18, false);
                    		this.dyeIndex = ingredients.size();
                    		ingredients.add(stack);
                    	}else if(i == 1){
                    		PositionedStack stack = new PositionedStack(coreStacks, 25 + x * 18, 6 + y * 18, false);
                    		this.chestIndex = ingredients.size();
                    		ingredients.add(stack);
                    	}else{
                    		break;
                    	}
                    	continue;
                    }
                    
                }
            }
        }
        ItemStack[] getCoreStacks(boolean output){
        	ItemStack[] stacks = new ItemStack[EInventoryMaterial.values().length];
        	for(int i = 0; i < stacks.length; i++){
        		ItemStack s = new ItemStack(type < 3|| type == 6 ? EnhancedInventories.improvedChest : EnhancedInventories.locker, 1, i);
        		NBTTagCompound nbt = new NBTTagCompound();
        		nbt.setString("woodType", WoodTypes.DEFAULT_WOOD_ID);
        		if(type == 0 || type == 1 || type == 2 || type == 6)
        			nbt.setByte("wool", (byte) (resultCache ? woolType : 0));
        		nbt.setBoolean("hopper", (type == 0 || type == 3) && output);
        		nbt.setBoolean("redstone", (type == 1 || type == 4) && output);
        		nbt.setBoolean("alt", (type == 2 || type == 5) && output);
        		s.setTagCompound(nbt);
        		stacks[i] = s;
        	}
        	return stacks;
        }
        ItemStack[] getDyeStacks(){
        	List<String> dyeNames = WoolUpgradeHelper.getDyeNames();
        	ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        	if(!resultCache){
	        	for(String id : dyeNames){
	        		stacks.addAll(OreDictionary.getOres(id));
	        	}
        	}else{
        		stacks.addAll(OreDictionary.getOres(dyeNames.get(woolType)));
        	}
        	return stacks.toArray(new ItemStack[0]);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }
        
        @Override
        public List<PositionedStack> getCycledIngredients(int cycle, List<PositionedStack> ingredients) {
        	Random rand = new Random(cycle + 1);
        	this.woodType = rand.nextInt(woodCount);
        	this.matType = rand.nextInt(EInventoryMaterial.values().length);
        	if(this.type < 3)
        		this.woolType = rand.nextInt(16);
        	//System.out.println(ingredients.size()+"/"+this.chestIndex+":"+this.type);
        	PositionedStack chest = ingredients.get(this.chestIndex);
        	//System.out.println(chest.items.length+"/"+EInventoryMaterial.values().length);
        	if(chest.items.length > 1){
        		chest.setPermutationToRender(matType);
        	}else{
        		this.matType = chest.item.getItemDamage();
        	}
        	chest.item.getTagCompound().setString("woodType", WoodTypes.getWoodTypes().get(woodType).id());
        	if(this.type < 3)
        		chest.item.getTagCompound().setByte("wool", (byte)woolType);
        	else if(type == 6){
        		if(!resultCache){
	        		PositionedStack dye = ingredients.get(this.dyeIndex);
	        		dye.setPermutationToRender(rand.nextInt(dye.items.length));
	        		int index = WoolUpgradeHelper.getDyeId(dye.item);
	        		woolType = WoolUpgradeHelper.getCollorValue(index);
	        		//chest.item.getTagCompound().setByte("wool", (byte)woolType);
        		}else{
        			PositionedStack dye = ingredients.get(this.dyeIndex);
	        		dye.setPermutationToRender(rand.nextInt(dye.items.length));
        		}
        	}
        	/*for (int itemIndex = 0; itemIndex < ingredients.size(); itemIndex++){
        		if(itemIndex == 0 || itemIndex == 2 || itemIndex == 6 || itemIndex == 8){
        			randomRenderPermutation(ingredients.get(itemIndex), cycle + itemIndex);
        		 }else if(itemIndex != 4){
        			 ingredients.get(itemIndex).setPermutationToRender(woodType);
        		 }else{
        			 ingredients.get(itemIndex).setPermutationToRender(woolType);
        		 }
            }*/

            return ingredients;
        }

        public PositionedStack getResult() {
            result.setPermutationToRender(matType);
        	result.item.getTagCompound().setString("woodType", WoodTypes.getWoodTypes().get(woodType).id());
            if(type < 3)
            	result.item.getTagCompound().setByte("wool", (byte)woolType);
        	return result;
        }

        public void computeVisuals() {
            for (PositionedStack p : ingredients)
                p.generatePermutations();
        }
    }
	
	
	@Override
	public String getRecipeName() {
		return I18n.format("recipe.functionUpgrade");
	}

	@Override
	public String getGuiTexture() {
		return "textures/gui/container/crafting_table.png";
	}
	
	@Override
    public String getOverlayIdentifier() {
        return "crafting";
    }
	
	@Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrafting.class;
    }
	
	@Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
    }
	
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
        return super.hasOverlay(gui, container, recipe) ||
                isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "crafting2x2");
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe) {
        IRecipeOverlayRenderer renderer = super.getOverlayRenderer(gui, recipe);
        if (renderer != null)
            return renderer;

        IStackPositioner positioner = RecipeInfo.getStackPositioner(gui, "crafting2x2");
        if (positioner == null)
            return null;
        return new DefaultOverlayRenderer(getIngredientStacks(recipe), positioner);
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
        IOverlayHandler handler = super.getOverlayHandler(gui, recipe);
        if (handler != null)
            return handler;

        return RecipeInfo.getOverlayHandler(gui, "crafting2x2");
    }
    
    public boolean isRecipe2x2(int recipe) {
        for (PositionedStack stack : getIngredientStacks(recipe))
            if (stack.relx > 43 || stack.rely > 24)
                return false;

        return true;
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == RecipeFunctionUpgradesHandler.class) {
            for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            	CachedUpgradeRecipe recipe = null;
                if (irecipe instanceof RecipeFunctionUpgrades)
                    recipe = new CachedUpgradeRecipe((RecipeFunctionUpgrades) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void loadCraftingRecipes(ItemStack result) {
    	for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            if(!(irecipe instanceof RecipeFunctionUpgrades))
            	continue;
            RecipeFunctionUpgrades recipe = (RecipeFunctionUpgrades)irecipe;
            if(recipe.getType() < 3 || recipe.getType() == 6){
            	if(result.getItem() != Item.getItemFromBlock(EnhancedInventories.improvedChest))
            		return;
            	CachedUpgradeRecipe cache = new  CachedUpgradeRecipe(recipe);
            	cache.computeVisuals();
            	arecipes.add(cache);
            	/*if(((recipe.getType() < 3 || recipe.getType() == 6 )&& result.getItem() == Item.getItemFromBlock(EnhancedInventories.improvedChest)) ||
            		((recipe.getType() > 2 && recipe.getType() < 6)&& result.getItem() == Item.getItemFromBlock(EnhancedInventories.locker))){
            	CachedUpgradeRecipe cache = new  CachedUpgradeRecipe(recipe);
            	cache.computeVisuals();
            	arecipes.add(cache);*/
            }else if(recipe.getType() > 2 && recipe.getType() < 6){
            	if(result.getItem() != Item.getItemFromBlock(EnhancedInventories.locker))
            		return;
            	CachedUpgradeRecipe cache = new  CachedUpgradeRecipe(recipe, result.getTagCompound().getInteger("wool"));
            	
            	cache.computeVisuals();
            	arecipes.add(cache);
            }else{
            	return;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
        	CachedUpgradeRecipe recipe = null;
            if (irecipe instanceof RecipeFunctionUpgrades)
                recipe = new CachedUpgradeRecipe((RecipeFunctionUpgrades) irecipe);

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

}