package com.example.menu_app.database.dishes

import androidx.room.*

@Dao
interface dishesDAO {
    @Insert
    suspend fun insertDish(item: dishesEntity)

    @Update
    suspend fun updateDish(item: dishesEntity)

    @Delete
    suspend fun deleteDish(item: dishesEntity)

    @Query("SELECT * FROM dishes")
    suspend fun getAllDishes(): List<dishesEntity>

//    @Query("SELECT dishEnglishName,dishPrice FROM dishes")
//    fun getDishesWithPrice(): List<DishWithPrice>
}