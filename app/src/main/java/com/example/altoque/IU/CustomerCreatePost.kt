package com.example.altoque.IU

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.altoque.R
import com.example.altoque.adapter.ScheduleAdapter
import com.example.altoque.models.Post
import com.example.altoque.models.PostResponse
import com.example.altoque.models.PostUpload
import com.example.altoque.models.Schedule
import com.example.altoque.networking.PostService
import com.example.altoque.repository.ScheduleDatabase
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerCreatePost : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 0
    private val IMAGE_PICK_CODE = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customer_create_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val ibCameraToCreatePost = findViewById<ImageButton>(R.id.ibCameraToCreatePost)
        ibCameraToCreatePost.setOnClickListener {
            checkCameraPermission()
        }
        
        val btPost = findViewById<Button>(R.id.btPost)
        btPost.setOnClickListener{
            // Agregar los horarios al retrofit, solo los que esten rellenados
            val retrofit = Retrofit.Builder()
                .baseUrl("https://altoquebackendapi.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            
            val service = retrofit.create(PostService::class.java)
            
            var clientId = 1
            var is_publish = true
            
            var etTitle = findViewById<TextInputEditText>(R.id.etTitle)
            var etDesc = findViewById<TextInputEditText>(R.id.etDesc)
            var etDistrict = findViewById<TextInputEditText>(R.id.etDistrict)
            //var etSpc = findViewById<TextInputEditText>(R.id.etSpc)
            var ivPost = findViewById<ImageView>(R.id.ivPost)
            
            
            var title = etTitle.text.toString()
            var desc = etDesc.text.toString()
            var district = etDistrict.text.toString()
            //var speciality = etSpc.text.toString()
            //var image = ivPost.text.toString()
            
            
            var post = PostUpload(title, desc, district, "image", is_publish, clientId)
            
            var request = service.insertPost(post)
            
            request.enqueue(object : Callback<PostResponse>{
                override fun onResponse(p0: Call<PostResponse>, p1: Response<PostResponse>) {
                    Toast.makeText(this@CustomerCreatePost, "Error de red 1",
                        Toast.LENGTH_LONG)
                        .show()
                }
                
                override fun onFailure(p0: Call<PostResponse>, p1: Throwable) {
                    Toast.makeText(this@CustomerCreatePost, "Error de red 2: ${p1.message}",
                        Toast.LENGTH_LONG)
                        .show()
                }
                
            })
            
            // Mensaje de confirmación TOAST
            //Toast.makeText(this, "Se creo correctamente la publicación", Toast.LENGTH_LONG).show()
            redirectToHome()
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Cuando el usuario selecciona una imagen de la galería, obtén la URI de la imagen seleccionada
            val imageUri: Uri? = data.data
            
            val ivUser = findViewById<ImageView>(R.id.ivPost)
            ivUser.setImageURI(imageUri)
        }
    }
    
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager
            .PERMISSION_GRANTED){
            // Si no tiene permiso
            requestCameraPermission()
        } else {
            // Tengo permiso
            Toast.makeText(this, "Ya tiene permiso", Toast.LENGTH_LONG).show()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }
    
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //Si el usuario ha rechazado el permiso antes, informar que vaya a ajustes
            Toast.makeText(this, "Ya se rechazó el permiso antes, habilítelo manualmente", Toast.LENGTH_SHORT).show()
        }
        else{
            //Si el usuario nunca a aceptado ni rechazado, entonces solicito permiso
            Toast.makeText(this, "Debe aceptar el permiso", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Si el usuario acepta el permiso
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                    //Acá se pondría toda la lógica
                }
                else{
                    //Si el usuario rechaza el permiso
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun redirectToHome() {
        val intent = Intent(this, ScheduleListActivity::class.java)
        startActivity(intent)
    }
}