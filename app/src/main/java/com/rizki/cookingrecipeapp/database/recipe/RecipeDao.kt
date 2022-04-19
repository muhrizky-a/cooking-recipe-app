package com.rizki.cookingrecipeapp.database.recipe

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAll(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insert(title: String, description: String, ingredients: String, steps: String )
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipe_table WHERE id = :id")
    fun getRecipeById(id: Long): Recipe

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("DELETE FROM recipe_table")
    suspend fun deleteAll()
}