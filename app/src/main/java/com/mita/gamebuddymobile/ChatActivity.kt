package com.mita.gamebuddymobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mita.gamebuddymobile.api.Conversation
import com.mita.gamebuddymobile.api.ConversationResponse
import com.mita.gamebuddymobile.api.Message
import com.mita.gamebuddymobile.api.MessageResponse
import com.mita.gamebuddymobile.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var userId: String

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    private lateinit var username: String
    private lateinit var userIDReceiver: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var conversationAdapter: ConversationAdapter
    private var conversationIdFinal = "-1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val conversationId = intent.getStringExtra("ConversationId") ?: "-1"
        recyclerView = findViewById(R.id.chatRecycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchConversation(conversationId)
        username = intent.getStringExtra("username") ?: ""
        userIDReceiver = intent.getStringExtra("userIDReceiver") ?: ""
        userId = getUserIdFromSharedPreferences()

        chatRecyclerView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList, userId)


        chatRecyclerView.adapter = messageAdapter
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        sendButton.setOnClickListener {
            val message = messageBox.text.toString().trim()
            if (message.isNotEmpty()) {
                var conversationIdFinal = "-1"
                if (conversationId.isNotEmpty()){
                    conversationIdFinal = conversationId
                }
                sendMessageToServer(message)
                messageBox.text.clear()
            } else {
                Toast.makeText(applicationContext, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }


        if (conversationId.isNotEmpty())
        {
//            loadMessagesFromServer(conversationId)
        }
    }

    private fun loadMessagesFromServer(conversationId: String) {
        val apiService = RetrofitClient.apiService
        val token = getUserTokenFromSharedPreferences()
        apiService.getMessages(conversationId, "Bearer $token").enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    Log.d("responsess", messages?.messages.toString())
                    if (messages != null) {
                        messageList.clear()
                        messages.messages?.let { messageList.addAll(it) }
                        messageAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to load messages!", Toast.LENGTH_SHORT).show()

                    Log.e("response", "$response")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to load messages!", Toast.LENGTH_SHORT).show()

                Log.e("response", "error2: $t")
            }
        })
    }

    private fun sendMessageToServer(message: String) {
        val apiService = RetrofitClient.apiService

        val userIDReceiver = intent.getIntExtra("UserIDReceiver", -1)
        val messageBody = mapOf(
            "receiver_id" to userIDReceiver.toString(),
            "body" to message
        )
        val token = getUserTokenFromSharedPreferences()
        apiService.sendMessage(conversationIdFinal,"Bearer $token", messageBody).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>,
                                    response: Response<Message>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let{
//                        conversationId = conversationId
                        loadMessagesFromServer(conversationIdFinal.toString())
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed to send1 message!", Toast.LENGTH_SHORT
                    ).show()
                    Log.d("response", "responses success: ${response}")
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to send2 message!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserIdFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", "") ?: ""
    }
    private fun getUserTokenFromSharedPreferences(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }

    private fun fetchConversation(conversationId: String) {

        val apiService = RetrofitClient.apiService
        val token = getUserTokenFromSharedPreferences()
        val call = apiService.getConversation(conversationId, "Bearer $token")
        call.enqueue(object : Callback<ConversationResponse> {
            override fun onResponse(
                call: Call<ConversationResponse>,
                response: Response<ConversationResponse>
            ) {
                if (response.isSuccessful) {
                    val conversationResponse = response.body()
                    Log.d("response", "responses success: ${conversationResponse}")
                } else {
                    val userIDReceiver = intent.getIntExtra("UserIDReceiver", -1)
                    Log.d("response", "responses no conversation id: $userIDReceiver token $token")
                    val call2 =  apiService.postConversation(userIDReceiver.toString(), "Bearer $token")
                    call2.enqueue(object : Callback<ConversationResponse> {
                        override fun onResponse(
                            call: Call<ConversationResponse>,
                            response: Response<ConversationResponse>
                        ) {
                            if (response.isSuccessful) {
                                val conversationResponse2 = response.body()
                                conversationResponse2?.conversationId?.let {
                                    loadMessagesFromServer(
                                        it
                                    )
                                }
                                conversationIdFinal = conversationResponse2?.conversationId.toString()
                            } else {
                                Toast.makeText(applicationContext, "Failed to create conversation", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ConversationResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<ConversationResponse>, t: Throwable) {
                Log.e("Conversation", "Failed to fetch conversation", t)
            }
        })

    }

    private fun showConversation(conversation: Conversation) {
        val conversationList = listOf(conversation)
        conversationAdapter = ConversationAdapter(conversationList)
        recyclerView.adapter = conversationAdapter
    }
}
