package com.rizki.cookingrecipeapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileNotFoundException

var imagePath: String = ""

class RecipeFormActivity : AppCompatActivity() {

    private lateinit var titleTxtView: TextInputEditText
    private lateinit var categoryTxtView: TextInputEditText
    private lateinit var descTxtView: TextInputEditText
    private lateinit var previewImgView: ImageView
    private lateinit var ingredientsTxtView: TextInputEditText
    private lateinit var stepsTxtView: TextInputEditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_form)

        var actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        titleTxtView = findViewById(R.id.title_text)
        categoryTxtView = findViewById(R.id.category_text)
        descTxtView = findViewById(R.id.description_text)
        ingredientsTxtView = findViewById(R.id.ingredients_text)
        stepsTxtView = findViewById(R.id.steps_text)

        previewImgView = findViewById(R.id.recipe_form_image_preview)

        val uploadButton = findViewById<Button>(R.id.button_upload)
        val confirmButton = findViewById<Button>(R.id.button_confirm)


        uploadButton.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"

            startActivityForResult(photoPickerIntent, 6969)
        }

        confirmButton.setOnClickListener {
            val replyIntent = Intent()
            val isAllInputEmpty =
                TextUtils.isEmpty(titleTxtView.text) || TextUtils.isEmpty(categoryTxtView.text)
                        || TextUtils.isEmpty(descTxtView.text)
                        || TextUtils.isEmpty(ingredientsTxtView.text)
                        || TextUtils.isEmpty(stepsTxtView.text)

            if (isAllInputEmpty) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {
                replyIntent.putExtra(IntentExtras.EXTRA_TITLE, titleTxtView.text.toString())
                replyIntent.putExtra(IntentExtras.EXTRA_DESCRIPTION, descTxtView.text.toString())
                replyIntent.putExtra(IntentExtras.EXTRA_CATEGORY, categoryTxtView.text.toString())
                replyIntent.putExtra(IntentExtras.EXTRA_IMAGE, imagePath)
                replyIntent.putExtra(IntentExtras.EXTRA_INGREDIENTS, ingredientsTxtView.text.toString())
                replyIntent.putExtra(IntentExtras.EXTRA_STEPS, stepsTxtView.text.toString())

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        if (requestCode === 6969) {
            if (resultCode === RESULT_OK) {
                val selectedImage: Uri? = intentData?.data
                val filePath = getPath(selectedImage)

                val file_extn = filePath.substring(filePath.lastIndexOf(".") + 1)

//                findViewById<TextView>(R.id.uploaded_image_name).text = filePath

                try {
                    if (file_extn == "img" || file_extn == "jpg" || file_extn == "jpeg" || file_extn == "gif" || file_extn == "png") {
                        //FINE
                        imagePath = filePath
                        findViewById<ImageView>(R.id.recipe_form_image_preview).setImageURI(
                            selectedImage
                        )
                    } else {
                        //NOT IN REQUIRED FORMAT
                    }
                } catch (e: FileNotFoundException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
    }

    fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaColumns.DATA)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)

        cursor.moveToFirst()

        return cursor.getString(
            cursor
                .getColumnIndexOrThrow(MediaColumns.DATA)
        )
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