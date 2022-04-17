package com.rizki.cookingrecipeapp.database.recipe

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_table")
data class Recipe (
    @PrimaryKey(autoGenerate = true) val id: Long,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "category") val category: String,
    @NonNull @ColumnInfo(name = "description") val description: String,
    @NonNull @ColumnInfo(name = "image") val image: String,
    @NonNull @ColumnInfo(name = "ingredients") val ingredients: String,
    @NonNull @ColumnInfo(name = "steps") val steps: String
//    @NonNull @ColumnInfo(name = "timestamp") val timestamp: Int
)