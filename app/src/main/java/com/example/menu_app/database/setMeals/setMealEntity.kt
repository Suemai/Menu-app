package com.example.menu_app.database.setMeals

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "set_meals")
data class setMealEntity (
    @PrimaryKey(autoGenerate = true)
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "min_per_person")
    val minPerPerson: Int,

    @ColumnInfo(name = "max_per_person")
    val maxPerPerson: Int
)