package com.rizki.cookingrecipeapp

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.*
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rizki.cookingrecipeapp.adapter.RecipeAdapter
import com.rizki.cookingrecipeapp.database.recipe.Recipe
import com.rizki.cookingrecipeapp.databinding.ActivityMainBinding
import com.rizki.cookingrecipeapp.viewmodels.RecipeViewModel
import com.rizki.cookingrecipeapp.viewmodels.RecipeViewModelFactory

// Storage Permissions
val REQUEST_EXTERNAL_STORAGE = 1
val PERMISSIONS_STORAGE = arrayOf<String>(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

class MainActivity : AppCompatActivity() {
    private val newRecipeActivityRequestCode = 1
    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val display_mode = resources.configuration.orientation
        this.verifyStoragePermissions(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RecipeAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = if (display_mode == ORIENTATION_PORTRAIT) LinearLayoutManager(this) else GridLayoutManager(this, 3)


        recipeViewModel.allLiveRecipe.observe(this, Observer { recipes ->
            // Update the cached copy of the words in the adapter.
            recipes?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, RecipeFormActivity::class.java)
            intent.putExtra(IntentExtras.EXTRA_ID, -1L)
            startActivityForResult(intent, newRecipeActivityRequestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newRecipeActivityRequestCode && resultCode == Activity.RESULT_OK) {
            lateinit var title: String
            lateinit var category: String
            lateinit var desc: String
            lateinit var imagePath: String
            lateinit var ingredients: String
            lateinit var steps: String
            data?.getStringExtra(IntentExtras.EXTRA_TITLE)?.let {
                title = it
            }
            data?.getStringExtra(IntentExtras.EXTRA_DESCRIPTION)?.let {
                desc = it
            }
            data?.getStringExtra(IntentExtras.EXTRA_CATEGORY)?.let {
                category = it
            }
            data?.getStringExtra(IntentExtras.EXTRA_IMAGE)?.let {
                imagePath = it
            }
            data?.getStringExtra(IntentExtras.EXTRA_INGREDIENTS)?.let {
                ingredients = it
            }
            data?.getStringExtra(IntentExtras.EXTRA_STEPS)?.let {
                steps = it
            }

            val recipe = Recipe(System.currentTimeMillis(), title, category, desc, imagePath, ingredients, steps)
            recipeViewModel.insert(recipe)

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            )
        }
    }

    private fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
