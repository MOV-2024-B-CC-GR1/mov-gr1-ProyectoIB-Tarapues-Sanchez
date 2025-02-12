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

class TransaccionAdapter(private var transacciones: List<Transaccion>, private val context: Context) :
    RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transaccion, parent, false)
        return TransaccionViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun actualizarTransacciones(nuevasTransacciones: List<Transaccion>) {
        transacciones = nuevasTransacciones
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        val transaccion = transacciones[position]
        holder.tvTipo.text = transaccion.tipo.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        holder.tvMonto.text = "$" + String.format("%.2f", transaccion.monto)
        holder.tvFecha.text = transaccion.fechaTransaccion
        holder.tvDescripcion.text = transaccion.descripcion ?: ""

        holder.btnEliminarTransaccion.setOnClickListener {
            val dbHelper = DatabaseHelper(context)
            if (dbHelper.eliminarTransaccion(transaccion.id)) {
                Toast.makeText(context, "Transacción eliminada", Toast.LENGTH_SHORT).show()
                val nuevasTransacciones = dbHelper.obtenerTransacciones()
                actualizarTransacciones(nuevasTransacciones)
            } else {
                Toast.makeText(context, "Error al eliminar la transacción", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = transacciones.size

    class TransaccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val tvMonto: TextView = itemView.findViewById(R.id.tvMonto)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val btnEliminarTransaccion: Button = itemView.findViewById(R.id.btnEliminarTransaccion)
    }
}
