package org.acme.getting.started.service;

import org.acme.getting.started.model.Recipe;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class RecipeService {

    @Inject
    EntityManager em;

    @Transactional
    public void createLegume(Recipe recipe) {
        em.persist(recipe);
    }

    public List<Recipe> getAll() {
        return em.createQuery("Select recipe from Recipe recipe", Recipe.class).getResultList();
    }

    @Transactional
    public void delete(Long recipeId) {
        Recipe recipe = this.findById(recipeId);
        em.remove(recipe);
    }

    public Recipe findById(Long recipeId) {
        return em.find(Recipe.class, recipeId);
    }

    @Transactional
    public void updateLegume(Recipe recipe) {
        em.merge(recipe);
    }
}
