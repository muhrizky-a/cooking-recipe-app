package com.rizki.cookingrecipeapp

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        var actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        showData()
    }


    private fun showData(){
        supportActionBar?.title = intent.getStringExtra(IntentExtras.EXTRA_TITLE)

        val tvTitle: TextView = findViewById(R.id.tv_detail_recipe_title)
        val imgThumbnail: ImageView = findViewById(R.id.tv_detail_recipe_image)
        val tvCategory: TextView = findViewById(R.id.tv_recipe_category)
        val tvDesc: TextView = findViewById(R.id.tv_detail_recipe_description)
        val tvIngredients: TextView = findViewById(R.id.tv_detail_recipe_ingredients)
        val tvSteps: TextView = findViewById(R.id.tv_detail_recipe_steps)

        val title = intent.getStringExtra(IntentExtras.EXTRA_TITLE)
        val thumbnail = intent.getStringExtra(IntentExtras.EXTRA_IMAGE)
        val category = intent.getStringExtra(IntentExtras.EXTRA_CATEGORY)
        val description = intent.getStringExtra(IntentExtras.EXTRA_DESCRIPTION)
        val ingredients = intent.getStringExtra(IntentExtras.EXTRA_INGREDIENTS)
        val steps = intent.getStringExtra(IntentExtras.EXTRA_STEPS)

        var uri: Uri = Uri.fromFile(File(thumbnail))

        try {
            if (!File(thumbnail).exists()) throw FileNotFoundException()
            imgThumbnail.setImageURI(uri)
        } catch (f: FileNotFoundException) {
            imgThumbnail.setImageResource(R.drawable.no_image)
        }

        tvTitle.text = title
        tvCategory.text = category
        tvDesc.text = description
        tvIngredients.text = ingredients
        tvSteps.text = steps
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                // startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}

