package com.example.project2b

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarCuentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cuenta)

        val etNombreCuenta = findViewById<EditText>(R.id.etNombreCuenta)
        val etSaldoCuenta = findViewById<EditText>(R.id.etSaldoCuenta)
        val btnGuardarCuenta = findViewById<Button>(R.id.btnGuardarCuenta)

        btnGuardarCuenta.setOnClickListener {
            val nombreCuenta = etNombreCuenta.text.toString()
            val saldoCuenta = etSaldoCuenta.text.toString().toDoubleOrNull()

            if (nombreCuenta.isNotEmpty() && saldoCuenta != null) {
                val dbHelper = DatabaseHelper(this)
                dbHelper.insertarCuenta(nombreCuenta, saldoCuenta)
                Toast.makeText(this, "com.example.project2b.Cuenta guardada", Toast.LENGTH_SHORT).show()
                finish() // Cerrar la actividad y regresar a la actividad anterior
            } else {
                Toast.makeText(this, "Por favor ingresa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
