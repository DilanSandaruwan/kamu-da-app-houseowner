package com.dilan.kamuda.houseownerapp.common.util

import android.content.Context

class KamuDaSecurePreference {
    companion object {
        private const val PREF_NAME = "FoodHouseAppPreferences"
        private const val FOOD_HOUSE_ID_KEY = "food_house_id"
        private const val IS_LOGGED_HOUSE = "is_logged_house"
        private const val LOAD_MENU_HOUSE = "load_menu_house"
    }

    // Method to store a food house ID in SharedPreferences
    fun setCustomerID(context: Context, customerID: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(FOOD_HOUSE_ID_KEY, customerID)
        editor.apply()
    }

    // Method to retrieve the food house ID from SharedPreferences
    fun getCustomerID(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FOOD_HOUSE_ID_KEY, "0") ?: "0"
    }

    // Method to update the food house ID in SharedPreferences
    fun updateCustomerID(context: Context, newCustomerID: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(FOOD_HOUSE_ID_KEY, newCustomerID)
        editor.apply()
    }

    fun setUserLogged(context: Context, isLogged: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_LOGGED_HOUSE, isLogged)
        editor.apply()
    }

    fun isLoggedUser(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_HOUSE, false)
    }

    fun setLoadMenu(context: Context, isLogged: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOAD_MENU_HOUSE, isLogged)
        editor.apply()
    }

    fun getLoadMenu(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(LOAD_MENU_HOUSE, true)
    }

    fun clearSharedPrefKeys(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(LOAD_MENU_HOUSE)
        editor.apply()
    }

}