package com.example.menu_app.database.dishes

/* Note
* For stupid old me to understand:
* Repository is the middleman between the database and the view model.
* The manager and coordinator of the many (librarians) DAOs.
 */
class dishRepository(private val dishesDAO: dishesDAO) {
    suspend fun getAllDishes(): List<dishesEntity> {
        return dishesDAO.getAllDishes()
    }

    suspend fun insertDish(item: dishesEntity) {
        dishesDAO.insertDish(item)
    }

    suspend fun updateDish(item: dishesEntity) {
        dishesDAO.updateDish(item)
    }

    suspend fun delete(item: dishesEntity) {
        dishesDAO.deleteDish(item)
    }

//    fun getDishesWithPrice(): List<dishesDatabase> {
//        return dishesDAO.getDishesWithPrice()
//    }
}