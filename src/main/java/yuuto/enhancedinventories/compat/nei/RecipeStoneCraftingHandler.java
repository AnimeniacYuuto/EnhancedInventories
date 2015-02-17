package yuuto.enhancedinventories.compat.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yuuto.enhancedinventories.EnhancedInventories;
import yuuto.enhancedinventories.RecipeStoneCrafting;
import yuuto.enhancedinventories.WoodTypes;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.DefaultOverlayRenderer;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.api.IStackPositioner;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class RecipeStoneCraftingHandler extends TemplateRecipeHandler{

	public class CachedStoneCraftingRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
        public int type = 0;
        
        public int woodCount;
        
        public int woodType;
        public int woolType;

        public CachedStoneCraftingRecipe(RecipeStoneCrafting recipe) {
            this.type = recipe.getType();
            result = new PositionedStack(recipe.getRecipeOutput(), 119, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients();
        }

        /**
         * @param width
         * @param height
         * @param items  an ItemStack[] or ItemStack[][]
         */
        public void setIngredients() {
        	ItemStack[] cobbleStacks = getCobbleStacks();
        	ItemStack[] woodStacks = getWoodStacks();
        	ItemStack[] woolStacks = type == 0 ? getWoolStacks() : null;
        	this.woodCount = woodStacks.length;
        	for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int i = (y*3)+x;
                    if(i == 0 || i == 2 || i == 6 || i == 8){
                    	PositionedStack stack = new PositionedStack(cobbleStacks, 25 + x * 18, 6 + y * 18, false);
                    	ingredients.add(stack);
                    }else if(i != 4){
                    	PositionedStack stack = new PositionedStack(woodStacks, 25 + x * 18, 6 + y * 18, false);
                    	ingredients.add(stack);
                    }else{
                    	PositionedStack stack = null;
                    	if(type == 0){
                    		stack = new PositionedStack(woolStacks, 25 + x * 18, 6 + y * 18, false);
                    	}else{
                    		stack = new PositionedStack(new ItemStack(Blocks.trapdoor), 25 + x * 18, 6 + y * 18, false);
                    	}
                    	ingredients.add(stack);
                    }
                }
            }
        }
        ItemStack[] getCobbleStacks(){
        	return OreDictionary.getOres("cobblestone").toArray(new ItemStack[0]);
        }
        ItemStack[] getWoodStacks(){
        	ItemStack[] stacks = new ItemStack[WoodTypes.getWoodTypes().size()];
        	for(int i = 0; i < stacks.length; i++){
        		stacks[i] = WoodTypes.getWoodTypes().get(i).getPlanksStack();
        	}
        	return stacks;
        }
        ItemStack[] getWoolStacks(){
        	ItemStack[] wool = new ItemStack[16];
        	for(int i = 0; i < 16; i++){
        		wool[i] = new ItemStack(Blocks.wool, 1, i);
        	}
        	return wool;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }
        
        @Override
        public List<PositionedStack> getCycledIngredients(int cycle, List<PositionedStack> ingredients) {
        	Random rand = new Random(cycle + 1);
        	this.woodType = rand.nextInt(woodCount);
        	if(this.type == 0)
        		this.woolType = rand.nextInt(16);
        	for (int itemIndex = 0; itemIndex < ingredients.size(); itemIndex++){
        		if(itemIndex == 0 || itemIndex == 2 || itemIndex == 6 || itemIndex == 8){
        			randomRenderPermutation(ingredients.get(itemIndex), cycle + itemIndex);
        		 }else if(itemIndex != 4){
        			 ingredients.get(itemIndex).setPermutationToRender(woodType);
        		 }else{
        			 ingredients.get(itemIndex).setPermutationToRender(woolType);
        		 }
            }

            return ingredients;
        }

        public PositionedStack getResult() {
            result.item.getTagCompound().setString("woodType", WoodTypes.getWoodTypes().get(woodType).id());
            if(type == 0)
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
		return I18n.format("recipe.stoneCrafting");
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
        if (outputId.equals("crafting") && getClass() == RecipeStoneCraftingHandler.class) {
            for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            	CachedStoneCraftingRecipe recipe = null;
                if (irecipe instanceof RecipeStoneCrafting)
                    recipe = new CachedStoneCraftingRecipe((RecipeStoneCrafting) irecipe);

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
        if(result.getItemDamage() != 0)
        	return;
    	for (IRecipe irecipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            if(!(irecipe instanceof RecipeStoneCrafting))
            	continue;
            RecipeStoneCrafting recipe = (RecipeStoneCrafting)irecipe;
            if((recipe.getType() == 0 && result.getItem() == Item.getItemFromBlock(EnhancedInventories.improvedChest)) ||
            		(recipe.getType() == 1 && result.getItem() == Item.getItemFromBlock(EnhancedInventories.locker))){
            	CachedStoneCraftingRecipe cache = new  CachedStoneCraftingRecipe(recipe);
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
        	CachedStoneCraftingRecipe recipe = null;
            if (irecipe instanceof RecipeStoneCrafting)
                recipe = new CachedStoneCraftingRecipe((RecipeStoneCrafting) irecipe);

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
