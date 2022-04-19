package com.rizki.cookingrecipeapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rizki.cookingrecipeapp.database.recipe.Recipe
import com.rizki.cookingrecipeapp.viewmodels.RecipeViewModel
import com.rizki.cookingrecipeapp.viewmodels.RecipeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity() {
    private lateinit var recipe: Recipe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        val id = intent.getLongExtra(IntentExtras.EXTRA_ID, 0L)
        Log.d(TAG, "ID: $id")
        lifecycleScope.launch(Dispatchers.IO) {
            recipe = recipeViewModel.getRecipeById(id)
            showData()
        }

    }


    private fun showData() {
        supportActionBar?.title = intent.getStringExtra(IntentExtras.EXTRA_TITLE)

        val tvTitle: TextView = findViewById(R.id.tv_detail_recipe_title)
        val imgThumbnail: ImageView = findViewById(R.id.tv_detail_recipe_image)
        val tvCategory: TextView = findViewById(R.id.tv_recipe_category)
        val tvDesc: TextView = findViewById(R.id.tv_detail_recipe_description)
        val tvIngredients: TextView = findViewById(R.id.tv_detail_recipe_ingredients)
        val tvSteps: TextView = findViewById(R.id.tv_detail_recipe_steps)

        var uri: Uri = Uri.fromFile(File(recipe.image))

        try {
            if (!File(recipe.image).exists()) throw FileNotFoundException()
            imgThumbnail.setImageURI(uri)
        } catch (f: FileNotFoundException) {
            imgThumbnail.setImageResource(R.drawable.no_image)
        }

        supportActionBar?.title = recipe.title
        tvTitle.text = recipe.title
        tvCategory.text = recipe.category
        tvDesc.text = recipe.description
        tvIngredients.text = recipe.ingredients
        tvSteps.text = recipe.steps
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val recipeViewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory((application as RecipeApplication).repository)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_edit -> {
                val intent = Intent(this, RecipeFormActivity::class.java)
                intent.putExtra(IntentExtras.EXTRA_ID, recipe.id)
                intent.putExtra(IntentExtras.EXTRA_TITLE, recipe.title)
                intent.putExtra(IntentExtras.EXTRA_DESCRIPTION, recipe.description)
                intent.putExtra(IntentExtras.EXTRA_CATEGORY, recipe.category)
                intent.putExtra(IntentExtras.EXTRA_IMAGE, recipe.image)
                intent.putExtra(
                    IntentExtras.EXTRA_INGREDIENTS,
                    recipe.ingredients
                )
                Log.d(TAG,"DETAIL_ID: ${intent.getLongExtra(IntentExtras.EXTRA_ID, 0L)}")
                intent.putExtra(IntentExtras.EXTRA_STEPS, recipe.steps)
                startActivityForResult(intent, 420)
                return true
            }

            R.id.action_delete -> {
                MaterialAlertDialogBuilder(this).setTitle(getString(android.R.string.dialog_alert_title))
                    .setMessage("Apakah anda yakin ingin menghapus resep ini?")
                    .setCancelable(false)
                    .setNegativeButton("Tidak") { _, _ -> }
                    .setPositiveButton("Ya") { _, _ ->
                        recipeViewModel.delete(recipe)
                        finish()
                    }
                    .show()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 420 && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "UPDATEE")
            var id: Long = data!!.getLongExtra(IntentExtras.EXTRA_ID, 0L)
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


            lifecycleScope.launch(Dispatchers.IO) {
                Log.d(TAG, "ID:$id")
                Log.d(TAG, "TITLE:$imagePath")

                recipeViewModel.update( Recipe(id, title, category, desc, imagePath, ingredients, steps) )
            }

            finish()
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            )
        }
    }
}

