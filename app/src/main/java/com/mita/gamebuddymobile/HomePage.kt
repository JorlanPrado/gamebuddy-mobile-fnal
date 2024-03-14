package com.mita.gamebuddymobile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.mita.gamebuddymobile.api.ApiService
import com.mita.gamebuddymobile.api.ReportRequest
import com.mita.gamebuddymobile.api.ReportResponse
import com.mita.gamebuddymobile.api.RetrofitClient
import com.mita.gamebuddymobile.api.UserDataClass
import com.mita.gamebuddymobile.databinding.ActivityHomePageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView : RecyclerView
    private lateinit var userList : ArrayList<UserDataClass>
    private lateinit var userAdapter : UserAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        val reportButton: Button = findViewById(R.id.ReportButton)

        reportButton.setOnClickListener {
            showReportDialog()
        }

        apiService = RetrofitClient.apiService

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    true
                }
                R.id.bottom_users -> {
                    val intent = Intent(this, UsersAndMatchingPage::class.java)
                    startActivity(intent)
                    true
                }

                R.id.bottom_myAccount -> {
                    val intent = Intent(this, AccounSettings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.bottom_home

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()

        fetchUserData()

        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter
    }

    private fun fetchUserData() {
        apiService.getUser().enqueue(object : Callback<List<UserDataClass>> {
            override fun onResponse(
                call: Call<List<UserDataClass>>,
                response: Response<List<UserDataClass>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        userList.addAll(it)
                        userAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@HomePage,
                        "Failed to fetch user data from server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<UserDataClass>>, t: Throwable) {
                Toast.makeText(
                    this@HomePage,
                    "Error occurred while fetching user data",
                    Toast.LENGTH_SHORT
                ).show()
                t.printStackTrace()
            }
        })
    }
    private fun showReportDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.report_dialog_layout)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConfirm: Button = dialog.findViewById(R.id.Submit)
        val btnCancel: Button = dialog.findViewById(R.id.Cancel)
        val editTextReportName: EditText = dialog.findViewById(R.id.editTextReportName)
        val editTextReportType: EditText = dialog.findViewById(R.id.editTextReportType)

        btnConfirm.setOnClickListener {
            val reportName = editTextReportName.text.toString()
            val reportType = editTextReportType.text.toString()
            reported(reportName, reportType, sharedPreferences)
            dialog.dismiss()
        }


        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun reported(reportName: String, reportType: String, sharedPreferences: SharedPreferences) {
        val apiService = RetrofitClient.apiService

        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Token not found. Please log in again.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        apiService.submitReport(ReportRequest(reportName, reportType), "Bearer $token")
            .enqueue(object : Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "User submitted report successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to submit report: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Failed to submit report: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}

