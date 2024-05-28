package com.example.altoque.IU

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.altoque.R
import com.example.altoque.models.Post
import com.example.altoque.models.User
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.PostService
import com.example.altoque.networking.RetrofitClient
import com.example.altoque.networking.UserService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class SpecialistPublicationDetailActivity : AppCompatActivity(), MessageFragment.OnNotificationRejectedListener {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_publication_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.tbrSpePubDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val postId = intent.getStringExtra("ID_POST")
        val fragmentManager: FragmentManager = supportFragmentManager

        if (postId != null) loadPostDetails(postId)
        else Toast.makeText(this, "No post ID found", Toast.LENGTH_SHORT).show()

        val radioGroup = findViewById<RadioGroup>(R.id.rgSpePubDetHours)
        val btnAccept = findViewById<Button>(R.id.btnSpePubDetAccept)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            btnAccept.isEnabled = true
        }

        findViewById<Button>(R.id.btnSpePubDetAccept).setOnClickListener {
            showMessage(fragmentManager)
        }
    }

    private fun loadPostDetails(postId: String) {

        val postService = RetrofitClient.createService<PostService>()
        val clientService = RetrofitClient.createService<ClientService>()
        val userService = RetrofitClient.createService<UserService>()

        lifecycleScope.launch {
            try {
                val postResponse = postService.getById(postId)
                val post = postResponse.body()

                if (post != null) {
                    val clientResponse = clientService.getById(post.clientId.toString())
                    val client = clientResponse.body()

                    if (client != null) {
                        val userResponse = userService.getById(client.userId.toString())
                        val user = userResponse.body()
                        if (user != null) displayPostDetails(post, user)
                        else showToast("User not found")

                    } else showToast("Client not found")

                } else showToast("post not found")

            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayPostDetails(post: Post, user: User) {

        val tvTitle = findViewById<TextView>(R.id.tvSpePubDetTitle)
        val tvAddress = findViewById<TextView>(R.id.tvSpePubDetAddress)
        val ivImagePost = findViewById<ImageView>(R.id.ivSpePubDetPost)
        val tvIsPublish = findViewById<TextView>(R.id.tvSpePubDetIsPublish)
        val tvDescription = findViewById<TextView>(R.id.tvSpePubDetDescription)
        val tvName = findViewById<TextView>(R.id.tvSpePubDetName)
        val tvEmail = findViewById<TextView>(R.id.tvSpePubDetEmail)
        val tvPhone = findViewById<TextView>(R.id.tvSpePubDetPhone)
        val ivAvatarClient = findViewById<ImageView>(R.id.ivSpePubDetClient)

        tvTitle.text = post.title
        tvAddress.text = post.address
        Picasso.get().load(post.image).into(ivImagePost)

        if (post.is_publish) tvIsPublish.text = "Hace 20 minutos" else tvIsPublish.text = "Inactivo"
        tvDescription.text = post.description
        tvName.text = user.firstName + " " + user.lastName
        tvEmail.text = user.email
        tvPhone.text = user.phone
        Picasso.get().load(user.avatar).into(ivAvatarClient)
    }

    private fun showMessage(fragmentManager: FragmentManager) {
        val messageFragment = MessageFragment()
        messageFragment.show(fragmentManager, "message")
    }

    override fun onNotificationRejected() {
        findViewById<CardView>(R.id.cvSuccessMessage).visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}