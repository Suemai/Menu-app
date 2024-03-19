package com.example.menu_app.database.dishes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/* Note
  - The dishes in the menu sometimes has an ID and sometimes doesn't.
    So in the pre-made database not all dishes have an ID and so blank cells.
    dishId:String = "" is to ensure a non-null primary key even if the original data doesn't provide one.
  - The iDNumber does not correspond to the number/ID of the dish on the actual physical menu.
    It is just a unique identifier for the database.
 */

@Entity(tableName = "dishes")
data class DishesEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "IDNumber")
    val iDNumber: Long = 0,

    @ColumnInfo(name = "number")
    var dishId:String?,

    @ColumnInfo(name = "name")
    var dishEnglishName: String,

    @ColumnInfo(name = "cnName")
    var dishChineseName: String,

    @ColumnInfo(name = "cnStaffName")
    var dishStaffName: String,

    @ColumnInfo(name = "price")
    var dishPrice: Double)

