package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.NotificationAdapter
import com.example.altoque.adapter.PostAdapter
import com.example.altoque.models.Notification
import com.example.altoque.models.Post
import com.example.altoque.networking.AlToqueService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuCustomerActivity : AppCompatActivity() {

    lateinit var posts : List<Post>
    lateinit var postsAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_customer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        GoToCreatePost()
        GoToProfile()
        GoToMyPosts2()

        postsAdapter = PostAdapter(emptyList())

        loadPublications()
    }

    private fun loadPublications() {
        val rvPosts = findViewById<RecyclerView>(R.id.rvPostCustomer)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/posts?=client_id=")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(AlToqueService::class.java)

        

        val request = service.getPostsbyClient()

        request.enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
                if (response.isSuccessful) {
                    posts = response.body()!!
                    postsAdapter = PostAdapter(posts)
                    rvPosts.adapter = postsAdapter
                    rvPosts.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MenuCustomerActivity)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun GoToCreatePost() {
        val llCreatePosts = findViewById<LinearLayout>(R.id.llCustCreatePosts)
        
        llCreatePosts.setOnClickListener {
            startActivity(Intent(this, CustomerCreatePost::class.java))
        }
    }
    
    private fun GoToProfile() {
        val llMyProfile = findViewById<LinearLayout>(R.id.llCustMyProfile)
        
        llMyProfile.setOnClickListener {
            startActivity(Intent(this, clientProfileActivity::class.java))
        }
    }

    
    private fun GoToMyPosts2() {
        val tvPosts = findViewById<TextView>(R.id.tvCustPosts)
        
        tvPosts.setOnClickListener {
            startActivity(Intent(this, PublicationList::class.java))
        }
    }
    
}