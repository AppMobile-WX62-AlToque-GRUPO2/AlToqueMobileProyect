package com.example.altoque.IU

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

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


        findViewById<ImageButton>(R.id.btBackPublication).setOnClickListener {
            onBackPressed()
        }
        
        rvPublications = findViewById(R.id.rvPublication)
        rvPublications.layoutManager = LinearLayoutManager(this)

        loadPublications()
    }

    private fun loadPublications() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")  // Para emulador
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
                    Toast.makeText(this@PublicationsList, "Error en la respuesta: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Publication>>, t: Throwable) {
                Toast.makeText(this@PublicationsList, "Error: ${t.message}", Toast.LENGTH_LONG).show()
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

    override fun onItemDeleteClicked(publication: Publication) {
        deletePublication(publication)
    }

    private fun deletePublication(publication: Publication) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PublicationService::class.java)

        val request = service.deletePublication(publication.id!!)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    publications = publications.filter { it.id != publication.id }
                    publicationAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@PublicationsList, "Error al eliminar la publicación", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PublicationsList, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}