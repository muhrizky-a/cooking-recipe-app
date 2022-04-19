package com.rizki.cookingrecipeapp.adapter

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rizki.cookingrecipeapp.DetailActivity
import com.rizki.cookingrecipeapp.IntentExtras
import com.rizki.cookingrecipeapp.R
import com.rizki.cookingrecipeapp.database.recipe.Recipe
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


//class CookingRecipeAdapter(private val onItemClicked: (Recipe) -> Unit) :
class RecipeAdapter :
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val current = getItem(position)
        // holder.bind(current.title, current.image)
        holder.bind(current)
    }

    class RecipeViewHolder(private var itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val titleItemView: TextView = itemView.findViewById(R.id.item_title)
        private val categoryItemView: TextView = itemView.findViewById(R.id.item_category)
        private val imageItemView: ImageView = itemView.findViewById(R.id.item_image)

        lateinit private var data: Recipe;
        // fun bind(title: String?, imagePath: String) {
        fun bind(current: Recipe) {
            data = current
            titleItemView.text = current.title
            categoryItemView.text = current.category
            var uri: Uri = Uri.fromFile(File(current.image))

            try {
                if (!File(current.image).exists()) throw FileNotFoundException()
                imageItemView.setImageURI(uri)
            } catch (f: FileNotFoundException) {
                imageItemView.setImageResource(R.drawable.no_image)
            } catch (io: IOException) {
                Toast.makeText(
                    this.imageItemView.context,
                    R.string.access_denied,
                    Toast.LENGTH_LONG
                ).show()
            }

            itemView.setOnClickListener(this)

        }

        companion object {
            fun create(parent: ViewGroup): RecipeViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return RecipeViewHolder(view)
            }
        }


        override fun onClick(v: View?) {
            Log.d(TAG,"DONE")

            val intent = Intent( v?.context, DetailActivity::class.java)
            intent.putExtra(IntentExtras.EXTRA_ID, data.id)
            v!!.context.startActivity(intent)

        }


    }
}