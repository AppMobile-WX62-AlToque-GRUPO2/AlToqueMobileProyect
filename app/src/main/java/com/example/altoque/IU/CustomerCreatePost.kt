package com.example.altoque.IU

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.lifecycle.lifecycleScope
import com.example.altoque.R
import com.example.altoque.models.PostResponse
import com.example.altoque.models.PostUpload
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.PostService
import com.example.altoque.networking.SharedPreferences
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerCreatePost : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 0
    private val IMAGE_PICK_CODE = 1

    private var selectedImageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    private var isUploadingImage = false

    private var userId = -1
    private var role = -1
    private var clientId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customer_create_post)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar referencia de almacenamiento de Firebase
        storageRef = FirebaseStorage.getInstance().reference

        val ibCameraToCreatePost = findViewById<ImageButton>(R.id.ibCameraToCreatePost)
        ibCameraToCreatePost.setOnClickListener {
            checkCameraPermission()
        }

        val btPost = findViewById<Button>(R.id.btPost)
        btPost.setOnClickListener {
            if (!isUploadingImage) {
                isUploadingImage = true
                selectedImageUri?.let {
                    uploadImageToFirebase(it) { imageUrl ->
                        createPost(imageUrl)
                        isUploadingImage = false // Habilitar el botón después de la actualización
                    }
                } ?: run {
                    createPost(null)
                    isUploadingImage = false // Habilitar el botón después de la actualización
                }
            }
        }

        val btCancel = findViewById<Button>(R.id.btCancelPost)
        btCancel.setOnClickListener {
            redirectToHome()
        }

        val sharedPreference = SharedPreferences(this)
        val datosUser = sharedPreference.getData("UserData")
        if (datosUser != null) {
            userId = datosUser.id
            role = if (datosUser.role) 1 else 0
        } else {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Cuando el usuario selecciona una imagen de la galería, obtén la URI de la imagen seleccionada
            selectedImageUri = data.data

            val ivUser = findViewById<ImageView>(R.id.ivPost)
            ivUser.setImageURI(selectedImageUri)
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Si el usuario ha rechazado el permiso antes, informar que vaya a ajustes
            Toast.makeText(this, "Ya se rechazó el permiso antes, habilítelo manualmente", Toast.LENGTH_SHORT).show()
        } else {
            // Si el usuario nunca ha aceptado ni rechazado, entonces solicito permiso
            Toast.makeText(this, "Debe aceptar el permiso", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Si el usuario acepta el permiso
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                    // Acá se pondría toda la lógica
                } else {
                    // Si el usuario rechaza el permiso
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, callback: (String) -> Unit) {
        val ref = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        ref.putFile(imageUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createPost(imageUrl: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://altoquebackendapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PostService::class.java)

        val serviceClient = retrofit.create(ClientService::class.java)

        lifecycleScope.launch {
            val clientResponse = serviceClient.getClientIdByUserAndRole(userId, role.toString())

            if (clientResponse.isSuccessful) {
                val clientIdResponse = clientResponse.body()
                if (clientIdResponse != null) {
                    clientId = clientIdResponse.client_id
                }
            }

            val clientId = clientId
            val is_publish = true

            val etTitle = findViewById<TextInputEditText>(R.id.etTitle)
            val etDesc = findViewById<TextInputEditText>(R.id.etDesc)
            val etDistrict = findViewById<TextInputEditText>(R.id.etDistrict)

            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            val district = etDistrict.text.toString()

            val post = PostUpload(title, desc, district, imageUrl ?: "", is_publish, clientId)

            val request = service.insertPost(post)

            request.enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CustomerCreatePost, "Publicación creada correctamente", Toast.LENGTH_LONG).show()
                        redirectToSchedule()
                    } else {
                        Toast.makeText(this@CustomerCreatePost, "Error al crear la publicación", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    Toast.makeText(this@CustomerCreatePost, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }


    }

    private fun redirectToHome() {
        val intent = Intent(this, MenuCustomerActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToSchedule() {
        val intent = Intent(this, ScheduleListActivity::class.java)
        startActivity(intent)
    }
}
