package com.example.altoque.IU

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.altoque.R
import com.example.altoque.models.Ubication
import com.example.altoque.models.UpdateUserRequest
import com.example.altoque.networking.ClientService
import com.example.altoque.networking.UbicationService
import com.example.altoque.networking.UserService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class clientProfileActivity : AppCompatActivity() {

    // Variables globales para el permiso de la cámara y los IDs
    private val CAMERA_PERMISSION_CODE = 0
    private val IMAGE_PICK_CODE = 1
    private var clientId = 1
    private var userId = 1
    private var ubicationId = 1
    private var districtId = 1

    private lateinit var clientService: ClientService
    private lateinit var userService: UserService
    private lateinit var ubicationService: UbicationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_client_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRetrofit()
        loadInformation()
        setupView()
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
                val clientResponse = clientService.getClient(clientId)
                userId = clientResponse.userId
                val userResponse = userService.getUser(userId)
                ubicationId = userResponse.ubicationId
                val ubicationResponse = ubicationService.getUbication(ubicationId)
                runOnUiThread {
                    // Actualizar la UI con la información cargada
                    val etName = findViewById<EditText>(R.id.etName)
                    val etLastname = findViewById<EditText>(R.id.etLastname)
                    val etPhone = findViewById<EditText>(R.id.etPhone)
                    val etBirthday = findViewById<EditText>(R.id.etBirthday)
                    val etDescription = findViewById<EditText>(R.id.etDescription)
                    val etAddress = findViewById<EditText>(R.id.etAddress)
                    val etEmail = findViewById<EditText>(R.id.etEmailAddress)

                    etName.setText(userResponse.firstName)
                    etLastname.setText(userResponse.lastName)
                    etPhone.setText(userResponse.phone)
                    etBirthday.setText(userResponse.birthdate)
                    etDescription.setText(userResponse.description)
                    etAddress.setText(ubicationResponse.address)
                    etEmail.setText(userResponse.email)
                    districtId = ubicationResponse.districtId
                }
            } catch (e: Exception) {
                runOnUiThread {
                    // Manejar errores de carga de información
                    Toast.makeText(this@clientProfileActivity, "Error al cargar la información", Toast.LENGTH_SHORT).show()
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
            updateUserUbication()
            val updateUserRequest = createUpdateUserRequest()
            updateUserInfo(updateUserRequest)
        }
    }

    // Creación de objeto UpdateUserRequest con los datos de la vista
    private fun createUpdateUserRequest(): UpdateUserRequest {
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
            avatar = null, // No estamos actualizando el avatar aquí
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
            startActivity(intent)
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