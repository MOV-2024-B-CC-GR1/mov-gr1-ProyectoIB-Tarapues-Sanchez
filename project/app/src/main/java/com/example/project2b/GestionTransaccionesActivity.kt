package com.example.project2b

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class GestionTransaccionesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransaccionAdapter
    private lateinit var btnAgregarTransaccion: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_transacciones)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.rvTransacciones)
        btnAgregarTransaccion = findViewById(R.id.btnAgregarTransaccion)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransaccionAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        btnAgregarTransaccion.setOnClickListener {
            startActivity(Intent(this, AgregarTransaccionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val transacciones = dbHelper.obtenerTransacciones()
        adapter.actualizarTransacciones(transacciones)
    }
}
