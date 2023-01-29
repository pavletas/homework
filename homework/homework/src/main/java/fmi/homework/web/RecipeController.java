package fmi.homework.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fmi.homework.dto.RecipeDto;
import fmi.homework.exceptions.InvalidEntityDataException;
import fmi.homework.models.Recipe;
import fmi.homework.services.RecipeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/{recipeId}")
    public Recipe getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return recipeService.getRecipeById(recipeId);
    }

    @GetMapping
    public List<Recipe> getRecipes() {
        return recipeService.getUserRecipes();
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeDto recipeDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        Recipe createdRecipe = recipeService.createRecipe(recipeDto);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                .build(createdRecipe.getId())).body(createdRecipe);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable("recipeId") Long recipeId,
            @Valid @RequestBody RecipeDto recipeDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid recipe data", errors.getAllErrors().stream()
                    .map(ObjectError::toString)
                    .collect(Collectors.toList()));
        }

        Recipe createdRecipe = recipeService.updateRecipeById(recipeId, recipeDto);
        return ResponseEntity.ok().body(createdRecipe);
    }

    @DeleteMapping("/{recipeId}")
    public void deleteRecipeById(@PathVariable("recipeId") Long recipeId) {
        recipeService.deleteRecipeById(recipeId);
    }
}
