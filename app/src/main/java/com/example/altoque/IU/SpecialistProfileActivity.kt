package com.example.altoque.IU

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.altoque.R

class SpecialistProfileActivity : AppCompatActivity() {

    //Variable global para el permiso de la cámara
    private val CAMERA_PERMISSION_CODE = 0
    private val IMAGE_PICK_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_specialist_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Cuando el usuario selecciona una imagen de la galería, obtén la URI de la imagen seleccionada
            val imageUri: Uri? = data.data

            val ivUser = findViewById<ImageView>(R.id.ivUser)
            ivUser.setImageURI(imageUri)
        }
    }

    private fun setupView() {
        val btBack = findViewById<Button>(R.id.btBack)
        val tvName = findViewById<TextView>(R.id.tvName)
        val etName = findViewById<EditText>(R.id.etName)
        val tvLastname = findViewById<TextView>(R.id.tvLastname)
        val etLastname = findViewById<EditText>(R.id.etLastname)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val tvBirthday = findViewById<TextView>(R.id.tvBirthday)
        val etBirthday = findViewById<EditText>(R.id.etBirthday)
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        val etDescription = findViewById<EditText>(R.id.etDescription)

        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val etPrice = findViewById<EditText>(R.id.etPrice)


        val btSave = findViewById<Button>(R.id.btSave)
        val ibCamera = findViewById<ImageButton>(R.id.ibCamera)

        ibCamera.setOnClickListener {
            //Verifico si tengo permiso
            checkCameraPermission()
        }

        btSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val lastName = etLastname.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val birthday = etBirthday.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val price = etPrice.text.toString().trim()

            if (name.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && phone.length == 9 && birthday.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty()) {
                // Si todos los datos están ingresados, mostrar confirmación
                showConfirmationDialog()
            } else {
                // Si algunos datos faltan, mostrar un mensaje de error
                Toast.makeText(this, "Por favor, ingrese todos los datos solicitados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //METODO QUE MUESTRA UN DIALOGO DE CONFIRMACION
    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("Se guardaron los cambios con éxito.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            //Si no tengo permiso, solicito permiso
            requestCameraPermission()
        }
        else{
            Toast.makeText(this, "Tienes permisos para usar la cámara", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }
    }

    //METODO PARA SOLICITAR PERMISO
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            //Si el usuario ha rechazado el permiso antes, ingrese manualmente
            Toast.makeText(this, "Ya se rechazó el permiso antes, habilítelo manualmente", Toast.LENGTH_SHORT).show()
        }
        else{
            //Si el usuario no hay rechazado el permiso antes, solicito permiso
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    //METODO QUE ESCUCHA RESPUESTA SI EL USUARIO ACEPTA O RECHAZA LA SOLICITUD DE PERMISO
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
                    //Acá se pondría toda la lógica
                }
                else{
                    //Si el usuario rechaza el permiso
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}