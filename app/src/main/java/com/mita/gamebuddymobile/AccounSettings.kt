package com.mita.gamebuddymobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mita.gamebuddymobile.api.Interest
import com.mita.gamebuddymobile.api.InterestsRequest
import com.mita.gamebuddymobile.api.RetrofitClient
import com.mita.gamebuddymobile.api.RetrofitClient.apiService
import com.mita.gamebuddymobile.api.UpdateUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccounSettings : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var saveChangesButton: Button
    private lateinit var accountTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var leagueOfLegendsCheckbox: CheckBox
    private lateinit var valorantCheckbox: CheckBox
    private lateinit var genshinImpactCheckbox: CheckBox
    private lateinit var mobileLegendtsCheckbox: CheckBox
    private lateinit var codmCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accoun_settings)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        saveChangesButton = findViewById(R.id.saveChanges)
        accountTextView = findViewById(R.id.Account)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirmpassword)

        leagueOfLegendsCheckbox = findViewById(R.id.btncheck1)
        valorantCheckbox = findViewById(R.id.btncheck2)
        genshinImpactCheckbox = findViewById(R.id.btncheck3)
        mobileLegendtsCheckbox = findViewById(R.id.btncheck4)
        codmCheckbox = findViewById(R.id.btncheck5)

        val saveButton: Button = findViewById(R.id.submit_button)

        saveButton.setOnClickListener {
            updateUserInterests()
        }

        val deleteButton: Button = findViewById(R.id.DeleteAccount)
        deleteButton.setOnClickListener {
            delete()
        }

        val logoutButton: Button = findViewById(R.id.Logout)
        logoutButton.setOnClickListener {
            logout()
        }



        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_myAccount -> {
                    true
                }

                R.id.bottom_users -> {
                    val intent = Intent(this, UsersAndMatchingPage::class.java)
                    startActivity(intent)
                    true
                }

                R.id.bottom_home -> {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.bottom_myAccount

        val userId = getUserId()

        if (userId != null) {
            saveChangesButton.setOnClickListener {
                val token = sharedPreferences.getString("token", null)
                if (token != null) {
                    val updateUser = UpdateUser(
                        usernameEditText.text.toString(),
                        emailEditText.text.toString(),
                        passwordEditText.text.toString()
                    )
                    updateUser(userId.toInt(), updateUser, token)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Token not found. Please log in again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                "User ID not found. Please log in again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getUserId(): String? {
        return sharedPreferences.getString("userId", null)
    }

    private fun updateUser(userId: Int, updateUser: UpdateUser, token: String) {
        val apiService = RetrofitClient.apiService

        apiService.updateUser(userId, updateUser, "Bearer $token")
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "User updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to update user: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Failed to update user: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun updateUserInterests() {
        val userId = getUserId()

        if (userId.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("UpdateInterest", "User ID: $userId")

        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Token not found. Please log in again.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Log.d("UpdateInterest", "Token: $token")

        val interests = mutableListOf<Interest>()
        if (leagueOfLegendsCheckbox.isChecked) {
            interests.add(Interest("League of Legends"))
        }
        if (valorantCheckbox.isChecked) {
            interests.add(Interest("Valorant"))
        }
        if (genshinImpactCheckbox.isChecked) {
            interests.add(Interest("Genshin Impact"))
        }
        if (mobileLegendtsCheckbox.isChecked) {
            interests.add(Interest("Mobile Legends"))
        }
        if (codmCheckbox.isChecked) {
            interests.add(Interest("Call of Duty Mobile"))
        }

        Log.d("UpdateInterest", "Interests save: $interests")
        val interestsRequest = InterestsRequest(interests)

        apiService.updateInterests(userId.toString(), interestsRequest, "Bearer $token")
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Interests updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("UpdateInterest", "Interests updated successfully")
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to update interests",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("UpdateInterest", "Failed to update interests")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Failed to update interests",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("UpdateInterest", "Failed to update interests", t)
                }
            })
    }

    private fun delete() {
        val userId = getUserId()

        if (userId.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("delete", "User ID: $userId")

        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Token not found. Please log in again.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val apiService = RetrofitClient.apiService

        apiService.deleteUser(userId.toString(), "Bearer $token").enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    clearUserCredentials()
                    Toast.makeText(
                        applicationContext,
                        "User deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("delete", "User deleted successfully")
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Failed to delete user",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("delete", "Failed to delete user")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Failed to delete user",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("delete", "Failed to delete user", t)
            }
        })
    }
    private fun logout() {
        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {

            Toast.makeText(
                applicationContext,
                "User not authenticated",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        val apiService = RetrofitClient.apiService

        apiService.logout("Bearer $token").enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    clearUserCredentials()
                    Toast.makeText(
                        applicationContext,
                        "Logout successful",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Failed to logout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Failed to logout",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("logout", "Failed to logout", t)
            }
        })
    }
    private fun clearUserCredentials() {
        sharedPreferences.edit().clear().apply()

        val intent = Intent(applicationContext, LogInPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}