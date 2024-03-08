package com.example.menu_app.database.orders

class OrdersRepository (private val ordersDAO: OrdersDAO){

    suspend fun insertOrder(order: OrdersEntity){
        ordersDAO.insertOrder(order)
    }

    suspend fun updateOrder(order: OrdersEntity){
        ordersDAO.updateOrder(order)
    }

    suspend fun deleteOrder(order: OrdersEntity){
        ordersDAO.deleteOrder(order)
    }

    suspend fun getAllOrders(): List<OrdersEntity>{
        return ordersDAO.getAllOrders()
    }
}