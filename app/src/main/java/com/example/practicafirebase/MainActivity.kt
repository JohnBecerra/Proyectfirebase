package com.example.practicafirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.airbnb.lottie.LottieAnimationView



class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtEmail = findViewById<EditText>(R.id.EditEmail)
        val edtPass = findViewById<EditText>(R.id.EditPass)
        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
        animationView.setAnimation(R.raw.userid)
        animationView.loop(true)
        animationView.playAnimation()
        val btnRegistrar = findViewById<Button>(R.id.BtnRegistrar)
        val btnIngresar = findViewById<Button>(R.id.BtnIngresar)

        btnRegistrar.setOnClickListener{
            if(edtEmail.text.toString().isNotEmpty()&& edtPass.text.toString().isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.text.toString(),edtPass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val ir = Intent(this,Opciones::class.java).apply {
                            putExtra("email",edtEmail.text.toString())
                            putExtra("pass",edtPass.text.toString())
                        }
                        startActivity(ir)
                    }else{
                        Toast.makeText(this, "Error en los datos",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        btnIngresar.setOnClickListener{
            if(edtEmail.text.toString().isNotEmpty()&& edtPass.text.toString().isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.text.toString(),edtPass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val ir = Intent(this,Opciones::class.java).apply {
                            putExtra("email",edtEmail.text.toString())
                            putExtra("pass",edtPass.text.toString())
                        }
                        startActivity(ir)
                    }else{
                        Toast.makeText(this, "Error en los datos",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
}
//Se realizo la implementacion de un programa con inicio de sesion, registro y ingreso de imagenes guardables para cada usuario
//Integrante: John Sebastian Becerra Granados