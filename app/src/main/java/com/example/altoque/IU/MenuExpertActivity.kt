package com.example.altoque.IU

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.OnItemClickListenerPost
import com.example.altoque.adapter.PostAdapter
import com.example.altoque.models.Post
import com.example.altoque.networking.PostService
import com.example.altoque.networking.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

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

    private fun loadPublications() {
        val rvPublication = findViewById<RecyclerView>(R.id.rvSpeHome)

        val service = RetrofitClient.createService<PostService>()

        lifecycleScope.launch {
            try {
                val response = service.getAll()
                if (response.isSuccessful) {
                    val publications = response.body() ?: emptyList()
                    val limitedPublications = publications.take(4)
                    postAdapter = PostAdapter(limitedPublications, this@MenuExpertActivity)
                    rvPublication.adapter = postAdapter
                    rvPublication.layoutManager = LinearLayoutManager(this@MenuExpertActivity)
                } else {
                    showToast("Failed to load publications")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClicked(post: Post) {
        val intent = Intent(this, SpecialistPublicationDetailActivity::class.java)
        intent.putExtra("ID_POST", post.id.toString())
        startActivity(intent)
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
}