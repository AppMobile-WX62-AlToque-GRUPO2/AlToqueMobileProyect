package com.example.altoque.IU

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
import com.example.altoque.networking.PostService
import com.example.altoque.networking.UserService
import com.example.altoque.networking.SpecialistService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuExpertActivity : AppCompatActivity(), OnItemClickListenerPost {

    lateinit var post : Post
    lateinit var posts : List<Post>
    lateinit var postAdapter : PostAdapter
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_expert)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MenuExpertActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SpecialistNotification::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ShowSpecialistProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

        loadPublications()

        GoToAgenda()
        GoToServices()
        GoToNotifications()
    }

    @SuppressLint("SetTextI18n")
    private fun loadPublications() {
        val rvPosts = findViewById<RecyclerView>(R.id.rvSpeHome)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PostService::class.java)

        val request = service.getPosts()

        request.enqueue(object : retrofit2.Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
                if (response.isSuccessful) {
                    posts = response.body()!!
                    postAdapter = PostAdapter(posts, this@MenuExpertActivity)
                    rvPosts.adapter = postAdapter
                    rvPosts.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@MenuExpertActivity)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        val specialistService = retrofit.create(SpecialistService::class.java)
        val userService = retrofit.create(UserService::class.java)

        lifecycleScope.launch {
            try {
                val specialistResponse = specialistService.getSpecialist(1)

                val userId = specialistResponse.userId
                val userResponse = userService.getUser(userId)

                runOnUiThread {
                    val tvName = findViewById<TextView>(R.id.tvSpeHomMenuName)
                    tvName.setText(userResponse.firstName + " " + userResponse.lastName)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MenuExpertActivity, "Error al cargar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun GoToAgenda() {
        val ivAgenda = findViewById<ImageView>(R.id.ivSpeHomAgenda)

        ivAgenda.setOnClickListener {
            startActivity(Intent(this, SpecialistAgendaActivity::class.java))
        }
    }

    private fun GoToServices() {
        val ivProfile = findViewById<ImageView>(R.id.ivSpeHomServices)

        ivProfile.setOnClickListener {
            startActivity(Intent(this, SpecialistServicesActivity::class.java))
        }
    }
    private fun GoToNotifications() {
        val ivExpNotification = findViewById<ImageView>(R.id.ivSpeHomNotification)

        ivExpNotification.setOnClickListener {
            startActivity(Intent(this, SpecialistNotification::class.java))
        }
    }

    override fun onItemClicked(post: Post) {
        val intent = Intent(this, SpecialistPublicationDetailActivity::class.java)
        intent.putExtra("ID_POST", post.id.toString())
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        postAdapter.cancelUpdates() // Llama a cancelUpdates del adaptador
    }
}