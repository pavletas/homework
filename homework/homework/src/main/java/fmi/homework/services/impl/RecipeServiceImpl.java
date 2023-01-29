package fmi.homework.services.impl;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fmi.homework.dto.RecipeDto;
import fmi.homework.enums.Role;
import fmi.homework.exceptions.EntityNotFoundException;
import fmi.homework.models.Recipe;
import fmi.homework.models.User;
import fmi.homework.repositories.RecipeRepository;
import fmi.homework.services.RecipeService;
import fmi.homework.services.UserService;
import fmi.homework.utils.JwtUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final UserService userService;
    private final RecipeRepository recipeRepository;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional(readOnly = true)
    public Recipe getRecipeById(Long recipeId) {
        Recipe foundRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe", "id", recipeId.toString()));

       // checkIfAuthorIsSameAsTheLoggedUserOrIsAdmin(foundRecipe, "You cannot delete a recipe created from %s.");
        return foundRecipe;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recipe> getUserRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional
    public Recipe createRecipe(RecipeDto recipeDto) {
        User loggedUser = userService.getByUsername(jwtUtils.getCurrentUsername());

        Recipe recipe = new Recipe(recipeDto.getName(), recipeDto.getDescription(), recipeDto.getCookingTime(),
                recipeDto.getContent(), recipeDto.getImageUrl());
        recipe.setAuthor(loggedUser);
        recipe.setProducts(recipeDto.getProducts());
        recipe.setTags(recipeDto.getTags());

        return recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    //@PreAuthorize("(# == authentication.principal.id and #authorId == #article.authorId )" +
    //        " or hasAuthority('ALL_ARTICLE_UPDATE')")
    public Recipe updateRecipeById(Long recipeId, RecipeDto recipeDto) {
        Recipe recipe = getRecipeById(recipeId);

        checkIfAuthorIsSameAsTheLoggedUserOrIsAdmin(recipe, "You cannot update a recipe created from %s.");

        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setContent(recipeDto.getContent());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setImageUrl(recipeDto.getImageUrl());
        recipe.setProducts(recipeDto.getProducts());
        recipe.setTags(recipeDto.getTags());

        return recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipeById(Long recipeId) {
        Recipe foundRecipe = getRecipeById(recipeId);
        checkIfAuthorIsSameAsTheLoggedUserOrIsAdmin(foundRecipe, "You cannot delete a recipe created from %s.");
        recipeRepository.delete(foundRecipe);
    }

    private void checkIfAuthorIsSameAsTheLoggedUserOrIsAdmin(Recipe recipe, String message) {
        User loggedUser = userService.getByUsername(jwtUtils.getCurrentUsername());
        boolean isAdmin = loggedUser.getRole().equals(Role.ADMIN);

        if (!isAdmin && !loggedUser.getId().equals(recipe.getAuthor().getId())) {
            throw new IllegalArgumentException(String.format(message,
                    recipe.getAuthor().getUsername()));
        }
    }
}
