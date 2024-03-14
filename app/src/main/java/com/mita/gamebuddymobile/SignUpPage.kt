package com.mita.gamebuddymobile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mita.gamebuddymobile.api.RetrofitClient
import com.mita.gamebuddymobile.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPage : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var age: EditText
    private lateinit var maleCheckbox: CheckBox
    private lateinit var femaleCheckbox: CheckBox
    private lateinit var othersCheckbox: CheckBox
    private lateinit var signupbutton: Button
    private lateinit var checkbox: CheckBox
    private lateinit var gender:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        age = findViewById(R.id.age)
        password = findViewById(R.id.password)
        confirmPassword = findViewById(R.id.confirmpassword)
        signupbutton = findViewById(R.id.signupbutton)
        checkbox = findViewById(R.id.checkBox)
        maleCheckbox = findViewById(R.id.male)
        femaleCheckbox = findViewById(R.id.female)
        othersCheckbox = findViewById(R.id.others)

        signupbutton.isEnabled = false

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            signupbutton.isEnabled = isChecked
            if (isChecked) {
                showCustomDialogBox()
            }
        }

        maleCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                femaleCheckbox.isChecked = false
                othersCheckbox.isChecked = false
                gender = "Male"
            }
        }
        femaleCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                maleCheckbox.isChecked = false
                othersCheckbox.isChecked = false
                gender = "Female"
            }
        }
        othersCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                maleCheckbox.isChecked = false
                femaleCheckbox.isChecked = false
                gender = "Other"
            }
        }

        val logInRed: TextView = findViewById(R.id.AlreadyHaveAccLogIn)
        logInRed.setOnClickListener {
            val intent = Intent(this, LogInPage::class.java)
            startActivity(intent)
        }

        signupbutton.setOnClickListener {
            val name = username.text.toString().trim()
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()
            val age = age.text.toString().trim()
            val confirmPassword = confirmPassword.text.toString().trim()

            if (password == confirmPassword) {
                // Passwords match, proceed with registration
                registerUser(name, email, password,age.toInt())
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showCustomDialogBox() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConfirm: Button = dialog.findViewById(R.id.confirmdialog)
        val btnCancel: Button = dialog.findViewById(R.id.canceldialog)

        btnConfirm.setOnClickListener {
            checkbox.isChecked = true
            Toast.makeText(this, "Confirm", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            checkbox.isChecked = false
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun registerUser(name: String, email: String, password: String, age:Int) {
        val apiService = RetrofitClient.apiService

        val user = User(id = 0, name = name, email = email, password = password, age = age, gender = gender)

        apiService.register(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpPage, "Registration successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignUpPage, HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@SignUpPage, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("Registration", "Failed to execute registration request", t)
                Toast.makeText(this@SignUpPage, "Error.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}