package com.example.menu_app.database.dishes

import androidx.room.*

@Dao
interface DishesDAO {
    @Insert
    suspend fun insertDish(item: DishesEntity)

    @Update
    suspend fun updateDish(item: DishesEntity)

    @Delete
    suspend fun deleteDish(item: DishesEntity)

    @Query("SELECT * FROM dishes")
    suspend fun getAllDishes(): List<DishesEntity>

    @Query("SELECT * FROM dishes WHERE IDNumber = :number")
    suspend fun getDishByID(number: Long): DishesEntity?

//    @Query("SELECT dishEnglishName,dishPrice FROM dishes")
//    fun getDishesWithPrice(): List<DishWithPrice>
}