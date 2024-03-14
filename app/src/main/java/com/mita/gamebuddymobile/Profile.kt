//package com.mita.gamebuddymobile
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.mita.gamebuddymobile.api.ProfileResponse
//import com.mita.gamebuddymobile.api.RetrofitClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class Profile : AppCompatActivity() {
//
//    private lateinit var emailTextView: TextView
//    private lateinit var usernameTextView: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.nav_header)
//
//        emailTextView = findViewById(R.id.email)
//        usernameTextView = findViewById(R.id.username)
//
//        val token = intent.getStringExtra("TOKEN")
//        if (token != null) {
//            fetchUserProfile(token)
//        }
//    }
//
//    private fun fetchUserProfile(token: String) {
//        val profileCall = RetrofitClient.apiService.getProfile("Bearer $token")
//        profileCall.enqueue(object : Callback<ProfileResponse> {
//            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
//                if (response.isSuccessful) {
//                    val profile = response.body()
//                    if (profile != null) {
//                        emailTextView.text = profile.email
//                        usernameTextView.text = profile.name
//                        Log.d("AccountDisplay", "User profile fetched successfully")
//
//                    }
//                } else {
//                    Toast.makeText(applicationContext, "Failed to fetch user profile", Toast.LENGTH_SHORT).show()
//                    Log.e("AccountDisplay", "Failed to fetch user profile. Error code: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                Toast.makeText(applicationContext, "Failed to communicate with server", Toast.LENGTH_SHORT).show()
//                Log.e("AccountDisplay", "Failed to communicate with server: ${t.message}")
//            }
//        })
//    }
//}
