package com.example.menu_app.database.dishes

/* Note
* For stupid old me to understand:
* Repository is the middleman between the database and the view model.
* The manager and coordinator of the many (librarians) DAOs.
 */
class DishRepository(private val dishesDAO: DishesDAO) {
    suspend fun getAllDishes(): List<DishesEntity> {
        return dishesDAO.getAllDishes()
    }

    suspend fun insertDish(item: DishesEntity) {
        dishesDAO.insertDish(item)
    }

    suspend fun updateDish(item: DishesEntity) {
        dishesDAO.updateDish(item)
    }

    suspend fun delete(item: DishesEntity) {
        dishesDAO.deleteDish(item)
    }

    suspend fun getDishByID(number: Long): DishesEntity? {
        return dishesDAO.getDishByID(number)
    }

//    fun getDishesWithPrice(): List<dishesDatabase> {
//        return dishesDAO.getDishesWithPrice()
//    }
}