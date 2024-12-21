package com.example.budgetshield.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref (context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    // Save email and name
    fun saveUserDetails(email: String?, name: String?) {
        sharedPreferences.edit().apply {
            putString("userEmail", email)
            putString("userName", name)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
    fun getUserEmail(): String? {
        return sharedPreferences.getString("userEmail", "No Email")
    }
    fun getUserName(): String? {
        return sharedPreferences.getString("userName", "No Name")
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }
    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}
