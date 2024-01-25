package com.example.menu_app.classes

class dish {
    lateinit var name: String
    lateinit var cn_name: String
    lateinit var staff_name: String
    lateinit var id: String
    var price: Double = 0.0
}
fun getName(dish: dish): String {
    return dish.name
}

fun setName(dish: dish, name: String) {
    dish.name = name
}

fun getCn_name(dish: dish): String {
    return dish.cn_name
}

fun setCn_name(dish: dish, cn_name: String) {
    dish.cn_name = cn_name
}

fun getStaff_name(dish: dish): String {
    return dish.staff_name
}

fun setStaff_name(dish: dish, staff_name: String) {
    dish.staff_name = staff_name
}

fun getId(dish: dish): String {
    return dish.id
}

fun setId(dish: dish, id: String) {
    dish.id = id
}

fun getPrice(dish: dish): Double {
    return dish.price
}

fun setPrice(dish: dish, price: Double) {
    dish.price = price
}
