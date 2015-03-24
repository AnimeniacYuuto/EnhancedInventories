package yuuto.enhancedinventories.compat.nei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yuuto.enhancedinventories.recipe.EnhancedShapelessRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.ShapelessRecipeHandler.CachedShapelessRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;

public class EnhancedShapelessRecipeHandler extends ShapedRecipeHandler
{
    public int[][] stackorder = new int[][]{
            {0, 0},
            {1, 0},
            {0, 1},
            {1, 1},
            {0, 2},
            {1, 2},
            {2, 0},
            {2, 1},
            {2, 2}};

    public class CachedEnhancedShapelessRecipe extends CachedRecipe
    {
        public CachedEnhancedShapelessRecipe() {
            ingredients = new ArrayList<PositionedStack>();
        }

        public CachedEnhancedShapelessRecipe(ItemStack output) {
            this();
            setResult(output);
        }

        public CachedEnhancedShapelessRecipe(Object[] input, ItemStack output) {
            this(Arrays.asList(input), output);
        }

        public CachedEnhancedShapelessRecipe(List<?> input, ItemStack output) {
            this(output);
            setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            ingredients.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + stackorder[ingred][0] * 18, 6 + stackorder[ingred][1] * 18);
                stack.setMaxSize(1);
                ingredients.add(stack);
            }
        }

        public void setResult(ItemStack output) {
            result = new PositionedStack(output, 119, 24);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
    }

    public String getRecipeName() {
        return NEIClientUtils.translate("recipe.shapeless");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == EnhancedShapelessRecipeHandler.class) {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                if(!(irecipe instanceof EnhancedShapelessRecipe))
                	continue;
            	CachedEnhancedShapelessRecipe recipe = forgeShapelessRecipe((EnhancedShapelessRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                if(!(irecipe instanceof EnhancedShapelessRecipe))
                	continue;
            	CachedEnhancedShapelessRecipe recipe = forgeShapelessRecipe((EnhancedShapelessRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if(!(irecipe instanceof EnhancedShapelessRecipe))
            	continue;
        	CachedEnhancedShapelessRecipe recipe = forgeShapelessRecipe((EnhancedShapelessRecipe) irecipe);

            if (recipe == null)
                continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

/*    private CachedEnhancedShapelessRecipe shapelessRecipe(EnhancedShapelessRecipe recipe) {
        if(recipe.recipeItems == null) //because some mod subclasses actually do this
            return null;

        return new CachedEnhancedShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
    }*/

    public CachedEnhancedShapelessRecipe forgeShapelessRecipe(EnhancedShapelessRecipe recipe) {
        ArrayList<Object> items = recipe.getInput();

        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedEnhancedShapelessRecipe(items, recipe.getRecipeOutput());
    }

    @Override
    public boolean isRecipe2x2(int recipe) {
        return getIngredientStacks(recipe).size() <= 4;
    }
}