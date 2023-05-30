package com.example.practicafirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Registro : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mantenimientosList: MutableList<Mantenimiento>
    private lateinit var mantenimientosAdapter: MantenimientosAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mantenimientosList = mutableListOf()
        mantenimientosAdapter = MantenimientosAdapter(this, mantenimientosList)
        recyclerView.adapter = mantenimientosAdapter

        // Inicializar Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        databaseRef = database.reference.child("informes")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mantenimientosList.clear()
                for (dataSnapshot in snapshot.children) {
                    val mantenimiento = dataSnapshot.getValue(Mantenimiento::class.java)
                    mantenimiento?.let {
                        mantenimientosList.add(it)
                    }
                }
                mantenimientosAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error al leer los datos de Firebase
            }
        })
    }
}
