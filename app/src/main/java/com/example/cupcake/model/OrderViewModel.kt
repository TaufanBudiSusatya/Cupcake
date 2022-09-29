package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Menentukan harga 1 cupcae */
private const val PRICE_PER_CUPCAKE = 2.00

/** Biaya tambahan untuk pengambilan pesanan di hari yang sama */
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/**
 * [OrderViewModel] menyimpan informasi tentang pesanan cupcake
 * dan menghitung harga total berdasarkan rincian pesanan .
 */
class OrderViewModel : ViewModel() {

    // menentukan jumlah kue
    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    // menentukan rasa
    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    // opsi tanggal
    val dateOptions: List<String> = getPickupOptions()

    // menentukan tanggal pengambilan
    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
    //format mata uang Rp (indonesia)
        NumberFormat.getCurrencyInstance().format(it)
    }

    init {
        resetOrder()
    }

    //menentukan jummlah pesanan
    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    //mengatur rasa pesanan . dan hanya 1 rasa yang dalam pesanan
    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

   //menentapkan tanggal pengambilan
    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }


    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }


    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    //menentukan harga
    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        // If the user selected the first option (today) for pickup, add the surcharge
        if (dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
}