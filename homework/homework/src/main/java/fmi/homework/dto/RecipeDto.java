package fmi.homework.dto;

import static fmi.homework.utils.ValidationMessages.RECIPE_CONTENT_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_COOKING_TIME_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_DESCRIPTION_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_IMAGE_URL_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_NAME_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_PRODUCT_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_TAG_VALIDATION_MESSAGE;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeDto {

    @NotNull
    @Size(min = 2, max = 80, message = RECIPE_NAME_VALIDATION_MESSAGE)
    private String name;

    @NotNull
    @Size(min = 2, max = 256, message = RECIPE_DESCRIPTION_VALIDATION_MESSAGE)
    private String description;

    @NotNull
    @Min(value = 0, message = RECIPE_COOKING_TIME_VALIDATION_MESSAGE)
    private int cookingTime;

    @NotNull
    @Size(min = 2, max = 2048, message = RECIPE_CONTENT_VALIDATION_MESSAGE)
    private String content;

    @URL(message = RECIPE_IMAGE_URL_VALIDATION_MESSAGE)
    private String imageUrl;

    private Set<@Size(min = 2, max = 80, message = RECIPE_PRODUCT_VALIDATION_MESSAGE) String> products =
            new HashSet<>();

    private Set<@Size(min = 2, max = 80, message = RECIPE_TAG_VALIDATION_MESSAGE) String> tags = new HashSet<>();
}
