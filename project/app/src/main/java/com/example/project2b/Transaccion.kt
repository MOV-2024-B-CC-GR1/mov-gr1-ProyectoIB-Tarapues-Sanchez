package com.example.project2b

data class Transaccion(
    val id: Long,
    val cuentaId: Long,
    val categoriaId: Long,
    val monto: Double,
    val tipo: String,
    val fechaTransaccion: String,
    val descripcion: String?,
    val esRecurrente: Boolean = false,
    val frecuenciaRecurrencia: String? = null,
    val comprobanteUrl: String? = null
)
