package com.example.altoque.IU

import android.annotation.SuppressLint
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
import com.example.altoque.adapter.AgendaAdapter
import com.example.altoque.adapter.OnItemClickListenerContract
import com.example.altoque.models.Contract
import com.example.altoque.networking.ContractService
import com.example.altoque.networking.PostService
import com.example.altoque.networking.RetrofitClient
import kotlinx.coroutines.launch

class SpecialistAgendaActivity : AppCompatActivity(), OnItemClickListenerContract {

    lateinit var agendaAdapter : AgendaAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_agenda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = findViewById(R.id.tbrSpeAgenda)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        loadAgenda()
    }
    private fun loadAgenda() {
        val rvAgenda = findViewById<RecyclerView>(R.id.rvSpeAgenda)

        val servicePost = RetrofitClient.createService<PostService>()
        val serviceContract = RetrofitClient.createService<ContractService>()

        lifecycleScope.launch {
            try {
                val responseContract = serviceContract.getAll()
                val responsePost = servicePost.getAll()

                if (responsePost.isSuccessful && responseContract.isSuccessful) {
                    val posts = responsePost.body() ?: emptyList()
                    val contracts = responseContract.body() ?: emptyList()

                    agendaAdapter = AgendaAdapter(posts, contracts, this@SpecialistAgendaActivity)
                    rvAgenda.adapter = agendaAdapter
                    rvAgenda.layoutManager = LinearLayoutManager(this@SpecialistAgendaActivity)
                } else {
                    showToast("Failed to load contracts")
                }
            } catch (e: Exception) {
                showToast("Error: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(contract: Contract) {

    }
}