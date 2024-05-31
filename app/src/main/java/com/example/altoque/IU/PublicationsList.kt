package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.PublicationAdapter
import com.example.altoque.models.Publication
import com.example.altoque.networking.PublicationService
import com.example.altoque.adapter.OnItemClickListener
import retrofit2.Retrofit
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class PublicationsList : AppCompatActivity(), OnItemClickListener {
    lateinit var publications: List<Publication>
    lateinit var publicationAdapter: PublicationAdapter
    lateinit var rvPublications: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_publications_list)

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
                    publicationAdapter = PublicationAdapter(publications, this@PublicationsList)
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
        val intent = Intent(this, DetailPublication::class.java).apply {
            putExtra("TITLE", publication.title)
            putExtra("ADDRESS", publication.address)
            putExtra("DESCRIPTION", publication.description)
            putExtra("IMAGE", publication.image)
            putExtra("POST_ID", publication.id)
        }
        startActivity(intent)
    }
}