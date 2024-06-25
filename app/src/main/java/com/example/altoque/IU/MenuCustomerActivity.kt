package com.example.altoque.IU

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.OnItemClickListenerPost
import com.example.altoque.adapter.PostAdapter
import com.example.altoque.models.Post
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.PostService
import com.example.altoque.networking.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuCustomerActivity : AppCompatActivity(), OnItemClickListenerPost {

    lateinit var posts : List<Post>
    lateinit var postsAdapter: PostAdapter
    var clientId: Int = 1 //tiene q recibirlo del login

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_customer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MenuCustomerActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, ClientNotification::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ShowClientProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val btClientNotification = findViewById<ImageView>(R.id.ivClientNotification)
        btClientNotification.setOnClickListener {
            startActivity(Intent(this, ClientNotification::class.java))
        }
        
        GoToCreatePost()
        GoToMyPosts2()
        clientId = intent.getIntExtra("clientId", 1)

        loadPublications(clientId)
    }

    private fun loadPublications(clientId: Int) {
        val rvPosts = findViewById<RecyclerView>(R.id.rvPostCustomer)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PostService::class.java)
        val request = service.getPostsbyClient(clientId)

        request.enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
                if (response.isSuccessful) {
                    posts = response.body()!!
                    postsAdapter = PostAdapter(posts, this@MenuCustomerActivity)
                    rvPosts.adapter = postsAdapter
                    rvPosts.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MenuCustomerActivity)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        val clientService = retrofit.create(ClientService::class.java)
        val userService = retrofit.create(UserService::class.java)

        lifecycleScope.launch {
            try {
                val clientResponse = clientService.getClient(1)

                val userId = clientResponse.userId
                val userResponse = userService.getUser(userId)

                runOnUiThread {
                    val tvName = findViewById<TextView>(R.id.tvCustMenuName)
                    tvName.setText(userResponse.firstName + " " + userResponse.lastName)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MenuCustomerActivity, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    private fun GoToCreatePost() {
        val llCreatePosts = findViewById<LinearLayout>(R.id.llCustCreatePosts)
        
        llCreatePosts.setOnClickListener {
            startActivity(Intent(this, CustomerCreatePost::class.java))
        }
    }

    private fun GoToMyPosts2() {
        val tvPosts = findViewById<LinearLayout>(R.id.ivMyClientPosts)

        tvPosts.setOnClickListener {
            startActivity(Intent(this, PublicationsList::class.java))
        }
    }

    override fun onItemClicked(post: Post) {
        TODO("Not yet implemented")
    }
}
