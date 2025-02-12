package com.example.project2b

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CuentaAdapter(private var cuentas: List<Cuenta>, private val context: Context) :
    RecyclerView.Adapter<CuentaAdapter.CuentaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuentaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cuenta, parent, false)
        return CuentaViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarCuentas(nuevasCuentas: List<Cuenta>) {
        cuentas = nuevasCuentas
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CuentaViewHolder, position: Int) {
        val cuenta = cuentas[position]
        holder.tvNombreCuenta.text = cuenta.nombre
        holder.tvSaldoCuenta.text = "$${cuenta.saldo}"

        holder.btnEliminarCuenta.setOnClickListener {
            val dbHelper = DatabaseHelper(context)
            if (dbHelper.eliminarCuenta(cuenta.id)) {
                val nuevasCuentas = dbHelper.obtenerCuentas()
                actualizarCuentas(nuevasCuentas)
                Toast.makeText(context, "Cuenta eliminada exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return cuentas.size
    }

    class CuentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCuenta: TextView = itemView.findViewById(R.id.tvNombreCuenta)
        val tvSaldoCuenta: TextView = itemView.findViewById(R.id.tvSaldoCuenta)
        val btnEliminarCuenta: Button = itemView.findViewById(R.id.btnEliminarCuenta)
    }
}
