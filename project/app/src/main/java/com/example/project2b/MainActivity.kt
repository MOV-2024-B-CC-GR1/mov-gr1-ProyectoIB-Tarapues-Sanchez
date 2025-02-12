package com.example.project2b

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvSaldoActual: TextView
    private lateinit var tvResumenFinanciero: TextView
    private lateinit var btnGestionCuentas: Button
    private lateinit var btnGestionTransacciones: Button
    private lateinit var btnGestionMetas: Button

    @SuppressLint("SetTextI18n")
    private fun cargarDatosFinancieros() {
        // Obtiene el saldo total de la base de datos (suma de todos los saldos de cuentas activas)
        val saldoTotal = dbHelper.obtenerSaldoTotal()
        val ingresos = dbHelper.obtenerTotalIngresos()
        val gastos = dbHelper.obtenerTotalGastos()

        // Actualiza el TextView con el saldo total, formateándolo a dos decimales
        tvSaldoActual.text = "Saldo total: $" + String.format("%.2f", saldoTotal)
        tvResumenFinanciero.text = "Resumen: Ingresos $" + String.format("%.2f", ingresos) +
                " | Gastos $" + String.format("%.2f", gastos)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa el DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Inicializa las vistas
        tvSaldoActual = findViewById(R.id.tvSaldoActual)
        tvResumenFinanciero = findViewById(R.id.tvResumenFinanciero)
        btnGestionCuentas = findViewById(R.id.btnGestionCuentas)
        btnGestionTransacciones = findViewById(R.id.btnGestionTransacciones)
        btnGestionMetas = findViewById(R.id.btnGestionMetas)

        // Carga y muestra los datos financieros (saldo total, ingresos y gastos)
        cargarDatosFinancieros()

        // Configura el botón para gestionar cuentas
        btnGestionCuentas.setOnClickListener {
            startActivity(Intent(this, GestionCuentasActivity::class.java))
        }

        // Configura el botón para gestionar transacciones
        btnGestionTransacciones.setOnClickListener {
            startActivity(Intent(this, GestionTransaccionesActivity::class.java))
        }

        // Puedes configurar el botón de metas si es necesario
        btnGestionMetas.setOnClickListener {
            // Lógica para la actividad de metas
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosFinancieros()  // Actualiza la información financiera al volver a primer plano
    }
}
