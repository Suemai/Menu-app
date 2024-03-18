package com.example.menu_app.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.menu_app.database.basket.CartDAO
import com.example.menu_app.database.basket.CartItem
import com.example.menu_app.database.basket.CartRepository
import com.example.menu_app.database.dishes.dishRepository
import com.example.menu_app.database.dishes.dishesEntity
import com.example.menu_app.database.orders.OrdersEntity
import com.example.menu_app.database.orders.OrdersRepository
import kotlinx.coroutines.launch

class mainViewModel (private val cartRepo: CartRepository, private val dishRepo: dishRepository) : ViewModel() {

    private val dishData = MutableLiveData<dishesEntity>()

    //Define LiveData properties
    val textItemCount: LiveData<Int> = MutableLiveData()
    val totalBasketPrice: LiveData<Double> = MutableLiveData()

    val filteredDishes: LiveData<List<dishesEntity>> = MutableLiveData()
    val isCartEmpty: LiveData<Boolean> = MutableLiveData()

    private val _isEditModeEnabled = MutableLiveData<Boolean>()
    val isEditModeEnabled: LiveData<Boolean> = _isEditModeEnabled

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _changesMade = MutableLiveData<Boolean>()
    val changesMade: LiveData<Boolean> = _changesMade

    private val _databaseChanged = MutableLiveData<Boolean>()
    val databaseChanged: LiveData<Boolean> = _databaseChanged

    private val _mainChanged = MutableLiveData<Boolean>()
    val mainChanged: LiveData<Boolean> = _mainChanged

    // When the app is initialised
    init {
        //Immediately clears the cart when app is opened
        viewModelScope.launch {
            cartRepo.clearCart()
            (isCartEmpty as MutableLiveData).value = true
            (isEditModeEnabled as MutableLiveData).value = false
            (changesMade as MutableLiveData).value = false
            (databaseChanged as MutableLiveData).value = false
            (mainChanged as MutableLiveData).value = false
        }
    }

    suspend fun addToCart(position: dishesEntity){
        val existingItem = cartRepo.getCartItemByName(position.dishEnglishName)
        if (existingItem == null){
            val cartItem = CartItem(
                name = position.dishEnglishName,
                price = position.dishPrice,
                quantity = 1,
                notes = ""
            )
            cartRepo.insertCartItem(cartItem)
        }else{
            existingItem.quantity++
            cartRepo.updateCartItem(existingItem)
        }

        (isCartEmpty as MutableLiveData).value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    suspend fun addNoteToCart(dish: dishesEntity, note: String){
        Log.d("MainViewModel", "addNoteToCart: $note")
        val existingItem = cartRepo.getCartItemByName(dish.dishEnglishName)
        if (existingItem == null){
            val cartItem = CartItem(
                name = dish.dishEnglishName,
                price = dish.dishPrice,
                quantity = 1,
                notes = note
            )
            cartRepo.insertCartItem(cartItem)
        } else {
            existingItem.quantity++
            existingItem.notes = note
            cartRepo.updateCartItem(existingItem)
        }
        (isCartEmpty as MutableLiveData).value = false
        updateItemCount()
        updateTotalBasketPrice()
    }

    private suspend fun isCartEmpty(){
        viewModelScope.launch {
            (isCartEmpty as MutableLiveData).value = cartRepo.getAllCartItems().isEmpty()
        }
    }

    fun filterDishes(dishes: List<dishesEntity>, newText: String): List<dishesEntity>{
        // Filter dishes based on search text
        return dishes.filter { dish ->
            dish.dishEnglishName.contains(newText, ignoreCase = true) ||
                    dish.dishId.toString().contains(newText, ignoreCase = true)
        }
    }

    // Define a function to update the state of your UI
    private suspend fun updateItemCount() {
        val totalItems = cartRepo.getAllCartItems().sumOf { it.quantity }
        Log.d("mainViewModel", "updateItemCount: $totalItems")
        (textItemCount as MutableLiveData).value = totalItems
        isCartEmpty()
    }

    private suspend fun updateTotalBasketPrice() {
        val totalPrice = cartRepo.getTotalPrice()
        (totalBasketPrice as MutableLiveData).value = totalPrice
    }

    fun updateCart(){
        viewModelScope.launch {
            updateItemCount()
            updateTotalBasketPrice()
        }
    }

    fun setEditMode(enabled: Boolean) {
        _isEditModeEnabled.value = enabled
    }

    fun updateIndividualTotalPrice() {
        viewModelScope.launch {
            val cartItems = cartRepo.getAllCartItems()
            val totalPrice = cartItems.sumOf { it.price * it.quantity }
            (totalBasketPrice as MutableLiveData).value = totalPrice
        }
    }

    fun triggerRefresh() {
        _changesMade.value = true
        Log.d("mainViewModel", "Trigger refresh: Refresh triggering")
    }

    fun refreshDone(fragment: String){
        when (fragment){
            "database" -> _databaseChanged.value = true
            "main" -> _mainChanged.value = true
        }
        if (_databaseChanged.value == true && _mainChanged.value == true){
            _changesMade.value = false
            Log.d("mainViewModel", "Refresh done: changesMade set to false")
        }
    }

    fun setDishesData(dishes: dishesEntity){
        dishData.value = dishes
    }

    fun getDishesData(): LiveData<dishesEntity> {
        return dishData
    }

    // Generally for the dish page //////////////////////////////////////////
    suspend fun saveChanges(
        dishNumber: String,
        dishName: String,
        dishCnName: String,
        dishStaffName: String,
        dishPrice: Double
    ){
        try {
            val updatedDish = dishesEntity(
                dishId = dishNumber,
                dishEnglishName = dishName,
                dishChineseName = dishCnName,
                dishStaffName = dishStaffName,
                dishPrice = dishPrice
            )
            dishRepo.updateDish(updatedDish)
            setDishesData(updatedDish)
            Log.d("Updated dish", updatedDish.toString())
            Log.d("mainViewModel", changesMade.value.toString())
            _changesMade.value = true
        }catch (e: Exception){
            Log.d("Error updating dish", e.message.toString())
            _changesMade.value = false
        }finally {
            setEditMode(false)
            Log.d("mainViewModel", "saveChanges: finally - edit mode off")
            Log.d("mainViewModel", "Changes made: "+changesMade.value.toString())
        }
    }

    suspend fun deleteDish(dish: dishesEntity){
        try {
            dishRepo.delete(dish)
        }catch (e: Exception){
            Log.d("Error deleting dish", e.message.toString())
            _changesMade.value = false
        }
    }


//    fun placeOrder(order: OrdersEntity){
//        viewModelScope.launch {
//            ordersRepo.insertOrder(order)
//        }
//    }
}