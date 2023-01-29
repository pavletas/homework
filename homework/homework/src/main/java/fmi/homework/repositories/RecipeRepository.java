package fmi.homework.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fmi.homework.models.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> getAllByAuthorId(Long authorId);
}
