//package com.mita.gamebuddymobile
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.yourapp.adapters.ConversationAdapter
//import com.mita.gamebuddymobile.api.Conversation
//import com.mita.gamebuddymobile.api.ConversationResponse
//import com.mita.gamebuddymobile.api.RetrofitClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class ConversationActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var conversationAdapter: ConversationAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_conversation)
//
//        recyclerView = findViewById(R.id.chatRecycleView)
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        fetchConversation()
//
//
//    }
//
//    private fun fetchConversation() {
//
//        val apiService = RetrofitClient.apiService
//        val conversationId = intent.getStringExtra("ConversationId")
//        val call = conversationId?.let { apiService.getConversation(it.toInt()) }
//
//        call?.enqueue(object : Callback<ConversationResponse> {
//            override fun onResponse(
//                call: Call<ConversationResponse>,
//                response: Response<ConversationResponse>
//            ) {
//                Log.e("conversationId", "conversationId: ${response}")
//                if (response.isSuccessful) {
//                    val conversationResponse = response.body()
//                    conversationResponse?.let { conversationResponse ->
//                        showConversation(conversationResponse.conversation)
//
//
//                    }
//                } else {
//                    val userIDReceiver = intent.getStringExtra("userIDReceiver") ?: ""
//                    val call2 =  apiService.postConversation(userIDReceiver.toInt())
//                    Log.e("Conversation", "Failed to fetch conversation: ${response.message()}")
//                    call2.enqueue(object : Callback<ConversationResponse> {
//                        override fun onResponse(
//                            call: Call<ConversationResponse>,
//                            response: Response<ConversationResponse>
//                        ) {
//                            if (response.isSuccessful) {
//                                val conversationResponse = response.body()
//                                val intent = Intent(this@ConversationActivity, ChatActivity::class.java)
//                                conversationResponse?.let { intent.putExtra("ConversationId", it.conversationId) }
//                                startActivity(intent)
//                            } else {
//                                Toast.makeText(applicationContext, "Failed to create conversation", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<ConversationResponse>, t: Throwable) {
//                            Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_SHORT).show()
//
//                        }
//                    })
//                }
//            }
//
//            override fun onFailure(call: Call<ConversationResponse>, t: Throwable) {
//                Log.e("Conversation", "Failed to fetch conversation", t)
//            }
//        })
//    }
//
//    private fun showConversation(conversation: Conversation) {
//        val conversationList = listOf(conversation)
//        conversationAdapter = ConversationAdapter(conversationList)
//        recyclerView.adapter = conversationAdapter
//    }
//}