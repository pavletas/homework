package fmi.homework.models;

import static fmi.homework.utils.ValidationMessages.RECIPE_CONTENT_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_COOKING_TIME_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_DESCRIPTION_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_IMAGE_URL_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_NAME_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_PRODUCT_VALIDATION_MESSAGE;
import static fmi.homework.utils.ValidationMessages.RECIPE_TAG_VALIDATION_MESSAGE;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "recipes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotNull
    @Size(min = 2, max = 80, message = RECIPE_NAME_VALIDATION_MESSAGE)
    @Column(name = "name")
    private String name;

    @NonNull
    @NotNull
    @Size(min = 2, max = 256, message = RECIPE_DESCRIPTION_VALIDATION_MESSAGE)
    @Column(name = "description")
    private String description;

    @NonNull
    @NotNull
    @Min(value = 0, message = RECIPE_COOKING_TIME_VALIDATION_MESSAGE)
    @Column(name = "cooking_time")
    private int cookingTime;

    @NonNull
    @NotNull
    @Size(min = 2, max = 2048, message = RECIPE_CONTENT_VALIDATION_MESSAGE)
    @Column(name = "content")
    private String content;

    @NonNull
    @NotNull
    @URL(message = RECIPE_IMAGE_URL_VALIDATION_MESSAGE)
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_recipes_user"))
    private User author;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipes_products")
    @Column(name = "products")
    private Set<@Size(min = 2, max = 80, message = RECIPE_PRODUCT_VALIDATION_MESSAGE) String> products =
            new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipes_tags")
    @Column(name = "tags")
    private Set<@Size(min = 2, max = 80, message = RECIPE_TAG_VALIDATION_MESSAGE) String> tags = new HashSet<>();

    @CreationTimestamp
    @Column(name = "row_created", updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "row_modified")
    private LocalDateTime modified;
}
