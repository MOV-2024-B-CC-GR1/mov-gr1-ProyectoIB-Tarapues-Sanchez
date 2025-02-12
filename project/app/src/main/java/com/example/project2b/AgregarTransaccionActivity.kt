package com.example.project2b

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AgregarTransaccionActivity : AppCompatActivity() {

    private lateinit var etCuentaId: EditText
    private lateinit var etCategoriaId: EditText
    private lateinit var etMonto: EditText
    private lateinit var spTipo: Spinner
    private lateinit var etFechaTransaccion: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnGuardarTransaccion: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_transaccion)

        dbHelper = DatabaseHelper(this)

        etCuentaId = findViewById(R.id.etCuentaId)
        etCategoriaId = findViewById(R.id.etCategoriaId)
        etMonto = findViewById(R.id.etMonto)
        spTipo = findViewById(R.id.spTipo)
        etFechaTransaccion = findViewById(R.id.etFechaTransaccion)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnGuardarTransaccion = findViewById(R.id.btnGuardarTransaccion)

        // Configurar el Spinner con los tipos "ingreso" y "gasto"
        val tipos = arrayOf("ingreso", "gasto")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipo.adapter = spinnerAdapter

        btnGuardarTransaccion.setOnClickListener {
            val cuentaId = etCuentaId.text.toString().toLongOrNull()
            val categoriaId = etCategoriaId.text.toString().toLongOrNull()
            val monto = etMonto.text.toString().toDoubleOrNull()
            val tipo = spTipo.selectedItem.toString()
            val fechaTransaccion = etFechaTransaccion.text.toString()
            val descripcion = etDescripcion.text.toString()

            if (cuentaId != null && categoriaId != null && monto != null && fechaTransaccion.isNotEmpty()) {
                dbHelper.insertarTransaccion(cuentaId, categoriaId, monto, tipo, fechaTransaccion, descripcion)
                Toast.makeText(this, "Transacción guardada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
