package com.rizki.cookingrecipeapp.database

import androidx.annotation.WorkerThread
import com.rizki.cookingrecipeapp.database.recipe.Recipe
import com.rizki.cookingrecipeapp.database.recipe.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val allRecipes: Flow<List<Recipe>> = recipeDao.getAll()
    fun getRecipeById(id: Long): Recipe = recipeDao.getRecipeById(id)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }



    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

}