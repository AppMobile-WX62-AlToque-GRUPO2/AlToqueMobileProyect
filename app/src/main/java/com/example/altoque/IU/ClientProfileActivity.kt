package com.example.altoque.IU

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
import com.bumptech.glide.Glide
import com.example.altoque.R
import com.example.altoque.models.Ubication
import com.example.altoque.models.UpdateUserRequest
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.SharedPreferences
import com.example.altoque.networking.UbicationService
import com.example.altoque.networking.UserService
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clientProfileActivity : AppCompatActivity() {

    // Variables globales para el permiso de la cámara y los IDs
    private val CAMERA_PERMISSION_CODE = 0
    private val IMAGE_PICK_CODE = 1

    // IDs de cliente, usuario, ubicación y distrito
    private var clientId = 1
    private var userId = 1
    private var role = 1
    private var ubicationId = 1
    private var districtId = 1

    private lateinit var clientService: ClientService
    private lateinit var userService: UserService
    private lateinit var ubicationService: UbicationService

    // Create a storage reference from our app
    private var selectedImageUri: Uri? = null
    private lateinit var storageRef: StorageReference
    private var isUploadingImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar referencia de almacenamiento de Firebase
        storageRef = FirebaseStorage.getInstance().reference

        //cargar las preferencias
        val sharedPreference = SharedPreferences(this)
        val datosUser = sharedPreference.getData("UserData")
        if (datosUser != null) {
            userId = datosUser.id
            role = if (datosUser.role) 1 else 0
        } else {
            Toast.makeText(this, "No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
        }


        setupRetrofit()
        loadInformation()
        setupView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            val ivUser = findViewById<ImageView>(R.id.ivUserShowClientActivity)
            ivUser.setImageURI(selectedImageUri)
        }
    }

    // Configuración de Retrofit para las solicitudes de red
    private fun setupRetrofit() {
        val urlBase = "https://altoquebackendapi.onrender.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        clientService = retrofit.create(ClientService::class.java)
        userService = retrofit.create(UserService::class.java)
        ubicationService = retrofit.create(UbicationService::class.java)
    }

    // Carga de la información del cliente, usuario y ubicación
    private fun loadInformation() {
        lifecycleScope.launch {
            try {
                val response = clientService.getClientIdByUserAndRole(userId, role.toString())
                if (response.isSuccessful) {
                    val clientIdResponse = response.body()
                    if (clientIdResponse != null) {
                        clientId = clientIdResponse.client_id
                        val clientResponse = clientService.getClient(clientId)
                        userId = clientResponse.userId
                        val userResponse = userService.getUser(userId)
                        ubicationId = userResponse.ubicationId
                        val ubicationResponse = ubicationService.getUbication(ubicationId)

                        runOnUiThread {
                            val etName = findViewById<EditText>(R.id.etName)
                            val etLastname = findViewById<EditText>(R.id.etLastname)
                            val etPhone = findViewById<EditText>(R.id.etPhone)
                            val etBirthday = findViewById<EditText>(R.id.etBirthday)
                            val etDescription = findViewById<EditText>(R.id.etDescription)
                            val etAddress = findViewById<EditText>(R.id.etAddress)
                            val etEmail = findViewById<EditText>(R.id.etEmailAddress)
                            val ivUser = findViewById<ImageView>(R.id.ivUserShowClientActivity)

                            etName.setText(userResponse.firstName)
                            etLastname.setText(userResponse.lastName)
                            etPhone.setText(userResponse.phone)
                            etBirthday.setText(userResponse.birthdate)
                            etDescription.setText(userResponse.description)
                            etAddress.setText(ubicationResponse.address)
                            etEmail.setText(userResponse.email)
                            districtId = ubicationResponse.districtId

                            // Cargar la imagen del usuario utilizando Glide
                            if (userResponse.avatar != null) {
                                Glide.with(this@clientProfileActivity)
                                    .load(userResponse.avatar)
                                    .placeholder(R.drawable.default_user)
                                    .into(ivUser)
                            } else {
                                // Si no hay imagen, mostrar imagen default
                                ivUser.setImageResource(R.drawable.default_user)
                            }
                        }
                    } else {
                        throw Exception("Client ID response body is null")
                    }
                } else {
                    throw Exception("Failed to fetch client ID: ${response.code()}")
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@clientProfileActivity, "Error al cargar la información: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ClientProfileActivity", "Error loading information", e)
                }
            }
        }
    }


    // Configuración de la vista y manejo de eventos
    private fun setupView() {
        val ibCamera = findViewById<ImageButton>(R.id.ibCamera)
        ibCamera.setOnClickListener {
            checkCameraPermission()
        }
        val btSave = findViewById<Button>(R.id.btSave)
        btSave.setOnClickListener {
            if (!isUploadingImage) {
                isUploadingImage = true
                updateUserUbication()
                selectedImageUri?.let {
                    uploadImageToFirebase(it) { imageUrl ->
                        val updateUserRequest = createUpdateUserRequest(imageUrl)
                        updateUserInfo(updateUserRequest)
                        isUploadingImage = false // Habilitar el botón después de la actualización
                    }
                } ?: run {
                    val updateUserRequest = createUpdateUserRequest(null)
                    updateUserInfo(updateUserRequest)
                    isUploadingImage = false // Habilitar el botón después de la actualización
                }
            }
        }

        // Regresar
        val btBack = findViewById<Button>(R.id.btBack)
        btBack.setOnClickListener {
            finish()
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

    // Creación de objeto UpdateUserRequest con los datos de la vista
    private fun createUpdateUserRequest(imageUrl: String?): UpdateUserRequest {
        val etName = findViewById<EditText>(R.id.etName)
        val etLastname = findViewById<EditText>(R.id.etLastname)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etEmail = findViewById<EditText>(R.id.etEmailAddress)
        return UpdateUserRequest(
            email = etEmail.text.toString(),
            role = true,
            firstName = etName.text.toString(),
            lastName = etLastname.text.toString(),
            phone = etPhone.text.toString(),
            birthdate = etBirthday.text.toString(),
            avatar = imageUrl,
            description = etDescription.text.toString(),
            ubicationId = ubicationId
        )
    }



    // Actualización de la ubicación del usuario en el servidor
    private fun updateUserUbication() {
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val address = etAddress.text.toString()
        lifecycleScope.launch {
            try {
                ubicationService.updateUbication(ubicationId, Ubication(ubicationId, address, districtId))
            } catch (e: Exception) {
                runOnUiThread {
                    // Manejar errores de actualización de ubicación
                    Toast.makeText(this@clientProfileActivity, "Error al actualizar la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Actualización de la información del usuario en el servidor
    private fun updateUserInfo(updateUserRequest: UpdateUserRequest) {
        lifecycleScope.launch {
            try {
                userService.updateUser(userId, updateUserRequest)
                runOnUiThread {
                    // Mostrar diálogo de confirmación después de la actualización exitosa
                    showConfirmationDialog()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    // Manejar errores de actualización de información del usuario
                    Toast.makeText(this@clientProfileActivity, "Error al actualizar la información", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Mostrar un diálogo de confirmación después de la actualización exitosa
    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("Se guardaron los cambios con éxito.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            // Regresar al perfil del cliente después de la confirmación
            val intent = Intent(this, ShowClientProfileActivity::class.java)
            // Limpiar el stack de tareas
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        builder.show()
    }

    // Verificar el permiso de la cámara y solicitarlo si es necesario
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission()
        } else {
            Toast.makeText(this, "Tienes permisos para usar la cámara", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    // Solicitar permiso de la cámara al usuario
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            // Explicar al usuario por qué se necesita el permiso
            Toast.makeText(this, "Ya se rechazó el permiso antes, habilítelo manualmente", Toast.LENGTH_SHORT).show()
        } else {
            // Solicitar el permiso al usuario
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    // Manejar la respuesta del usuario a la solicitud de permiso
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Si el usuario acepta el permiso
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                }
                else{
                    //Si el usuario rechaza el permiso
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}