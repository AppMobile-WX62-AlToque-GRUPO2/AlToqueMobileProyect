package com.example.test2.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test2.R
import com.example.test2.adapter.OnItemClickListener
import com.example.test2.adapter.PublicationAdapter
import com.example.test2.detail_publication
import com.example.test2.models.Publication
import com.example.test2.networking.PublicationService
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var publications: List<Publication>
    lateinit var publicationAdapter: PublicationAdapter
    lateinit var rvPublications: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar RecyclerView
        rvPublications = findViewById(R.id.rvPublication)
        rvPublications.layoutManager = LinearLayoutManager(this)

        loadPublications()
    }

    private fun loadPublications() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8001/")  // Para emulador
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PublicationService::class.java)

        val request = service.getAllPublications()

        request.enqueue(object : Callback<List<Publication>> {
            override fun onResponse(call: Call<List<Publication>>, response: Response<List<Publication>>) {
                if (response.isSuccessful) {
                    publications = response.body() ?: emptyList()
                    publicationAdapter = PublicationAdapter(publications, this@MainActivity)
                    rvPublications.adapter = publicationAdapter
                } else {
                    Log.e("MainActivity", "Error en la respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Publication>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    override fun onItemClicked(publication: Publication) {
        val intent = Intent(this, detail_publication::class.java).apply {
            putExtra("TITLE", publication.title)
            putExtra("ADDRESS", publication.address)
            putExtra("DESCRIPTION", publication.description)
            putExtra("IMAGE", publication.image)
            putExtra("POST_ID", publication.id)  // Agregar el postId aquí
        }
        startActivity(intent)
    }
}
