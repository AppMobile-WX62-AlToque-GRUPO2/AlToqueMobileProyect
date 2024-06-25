package com.example.altoque.IU

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
import kotlinx.coroutines.launch

class SpecialistServicesActivity : AppCompatActivity(), OnItemClickListenerPost {

    lateinit var postAdapter : PostAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_services)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.tbrSpeServices)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        loadPublications()
    }

    private fun loadPublications() {
        val rvServices = findViewById<RecyclerView>(R.id.rvSpeServices)

        val service = RetrofitClient.createService<PostService>()

        lifecycleScope.launch {
            try {
                val response = service.getAll()
                if (response.isSuccessful) {
                    val publications = response.body() ?: emptyList()

                    postAdapter = PostAdapter(publications, this@SpecialistServicesActivity)
                    rvServices.adapter = postAdapter
                    rvServices.layoutManager = LinearLayoutManager(this@SpecialistServicesActivity)
                } else {
                    showToast("Failed to load publications")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
                loadPublications()
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
    override fun onDestroy() {
        super.onDestroy()
        postAdapter.cancelUpdates() // Llama a cancelUpdates del adaptador
    }
}