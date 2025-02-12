package com.example.project2b

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GestionCuentasActivity : AppCompatActivity() {

    private lateinit var adapter: CuentaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_cuentas)

        val recyclerView = findViewById<RecyclerView>(R.id.rvCuentas)
        val btnAgregarCuenta = findViewById<Button>(R.id.btnAgregarCuenta)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener y mostrar cuentas
        val dbHelper = DatabaseHelper(this)
        val cuentas = dbHelper.obtenerCuentas()
        adapter = CuentaAdapter(cuentas, this) // Inicializar el adapter
        recyclerView.adapter = adapter

        btnAgregarCuenta.setOnClickListener {
            val intent = Intent(this, AgregarCuentaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val dbHelper = DatabaseHelper(this)
        val nuevasCuentas = dbHelper.obtenerCuentas()
        adapter.actualizarCuentas(nuevasCuentas)
    }
}