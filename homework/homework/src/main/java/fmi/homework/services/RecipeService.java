package fmi.homework.services;

import java.util.List;

import fmi.homework.dto.RecipeDto;
import fmi.homework.models.Recipe;

public interface RecipeService {

    Recipe getRecipeById(Long recipeId);

    List<Recipe> getUserRecipes();

    Recipe createRecipe(RecipeDto recipeDto);

    Recipe updateRecipeById(Long recipeId, RecipeDto recipeDto);

    void deleteRecipeById(Long recipeId);
}
