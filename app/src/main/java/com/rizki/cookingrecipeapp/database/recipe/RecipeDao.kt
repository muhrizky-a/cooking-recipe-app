package com.rizki.cookingrecipeapp.database.recipe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAll(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insert(title: String, description: String, ingredients: String, steps: String )
    suspend fun insert(recipe: Recipe)

    @Query("SELECT * FROM recipe_table WHERE id = :id")
    fun getRecipeById(id: Int): Flow<List<Recipe>>

    @Query("DELETE FROM recipe_table")
    suspend fun deleteAll()
}