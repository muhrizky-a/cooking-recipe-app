package com.rizki.cookingrecipeapp.viewmodels

import androidx.lifecycle.*
import com.rizki.cookingrecipeapp.database.RecipeRepository
import com.rizki.cookingrecipeapp.database.recipe.Recipe
import com.rizki.cookingrecipeapp.database.recipe.RecipeDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RecipeViewModel(private val repository: RecipeRepository): ViewModel() {
    val allLiveRecipe: LiveData<List<Recipe>> = repository.allRecipes.asLiveData()
    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }
}


class RecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}