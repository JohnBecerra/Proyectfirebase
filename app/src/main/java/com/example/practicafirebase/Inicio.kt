package com.example.practicafirebase

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Inicio : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1

    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 1000
        private const val PERMISSION_CODE = 1000
        private const val REQUEST_CODE_PERMISSIONS = 1001
    }

    private lateinit var storageRef: StorageReference
    private lateinit var edtTecnico: EditText
    private lateinit var edtEstablecimiento: EditText
    private lateinit var edtFecha: EditText
    private lateinit var edtObservaciones: EditText
    private lateinit var databaseRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Inicializar Firebase Storage
        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val auth = FirebaseAuth.getInstance()

        val imageView = findViewById<ImageView>(R.id.imageView)
        val storageReference =
            FirebaseStorage.getInstance().getReference("${auth.currentUser?.uid}/profile.jpg")
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(imageView)
        }.addOnFailureListener { exception ->
            // Manejar el error al cargar la imagen
        }

        val txtEmail = findViewById<TextView>(R.id.email)
        val btnCerrar = findViewById<Button>(R.id.cerrarsesion)
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        txtEmail.text = email

        edtTecnico = findViewById(R.id.datos_tecnico)
        edtEstablecimiento = findViewById(R.id.datos_establecimiento)
        edtFecha = findViewById(R.id.datos_fecha)
        edtObservaciones = findViewById(R.id.datos_observaciones)

        val btnAgregarImagen = findViewById<Button>(R.id.btnAgregarImagen)
        btnAgregarImagen.setOnClickListener {
            // Verificar si se tienen los permisos necesarios para acceder a la galería
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // Si no se tienen los permisos, solicitarlos al usuario
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                // Si se tienen los permisos, abrir la galería
                openGallery()
            }
        }

        // Inicializar Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        databaseRef = database.reference

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            val tecnico = edtTecnico.text.toString().trim()
            val establecimiento = edtEstablecimiento.text.toString().trim()
            val fechaString = edtFecha.text.toString().trim()
            val observaciones = edtObservaciones.text.toString().trim()

            if (tecnico.isEmpty() || establecimiento.isEmpty() || fechaString.isEmpty() || observaciones.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                try {
                    val fecha = dateFormatter.parse(fechaString)
                    val informe = hashMapOf(
                        "tecnico" to tecnico,
                        "establecimiento" to establecimiento,
                        "fecha" to fecha,
                        "observaciones" to observaciones
                    )

                    databaseRef.child("informes").push().setValue(informe)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Informe guardado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al guardar el informe",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } catch (e: ParseException) {
                    Toast.makeText(
                        this,
                        "La fecha no está en el formato esperado (dd/MM/yyyy)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnCerrar.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_REQUEST_CODE) {
            data?.data?.let { uri ->
                val imageView = findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load(uri).into(imageView)

                uploadImage(uri)
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val auth = FirebaseAuth.getInstance()
        val storageReference =
            FirebaseStorage.getInstance().getReference("${auth.currentUser?.uid}/profile.jpg")
        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                Toast.makeText(this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception: Exception ->
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
    }
}


