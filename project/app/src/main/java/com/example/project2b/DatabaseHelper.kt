package com.example.project2b

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USUARIO)
        db.execSQL(CREATE_TABLE_CUENTAS)
        db.execSQL(CREATE_TABLE_CATEGORIA)
        db.execSQL(CREATE_TABLE_TRANSACCION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS transaccion")
        db.execSQL("DROP TABLE IF EXISTS categoria")
        db.execSQL("DROP TABLE IF EXISTS cuentas")
        db.execSQL("DROP TABLE IF EXISTS cuenta")
        db.execSQL("DROP TABLE IF EXISTS usuario")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "sistema_financiero.db"
        private const val DATABASE_VERSION = 1

        private const val CREATE_TABLE_USUARIO = """
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                nombre_completo TEXT NOT NULL,
                password_hash TEXT NOT NULL,
                fecha_registro TEXT DEFAULT (datetime('now')),
                estado_activo INTEGER DEFAULT 1,
                configuracion_notificaciones TEXT,
                created_at TEXT DEFAULT (datetime('now')),
                updated_at TEXT DEFAULT (datetime('now'))
            )
        """

        private const val CREATE_TABLE_CUENTAS = """
            CREATE TABLE cuenta (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                tipo TEXT CHECK(tipo IN ('ahorro', 'corriente')) NOT NULL,
                saldo REAL DEFAULT 0.00,
                moneda TEXT DEFAULT 'USD',
                activa INTEGER DEFAULT 1,
                fecha_creacion TEXT DEFAULT (datetime('now')),
                created_at TEXT DEFAULT (datetime('now')),
                updated_at TEXT DEFAULT (datetime('now')),
                FOREIGN KEY (usuario_id) REFERENCES usuario(id)
            )
        """

        private const val CREATE_TABLE_CATEGORIA = """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                nombre TEXT NOT NULL,
                tipo TEXT CHECK(tipo IN ('ingreso', 'gasto')) NOT NULL,
                icono TEXT,
                color TEXT,
                created_at TEXT DEFAULT (datetime('now')),
                updated_at TEXT DEFAULT (datetime('now')),
                FOREIGN KEY (usuario_id) REFERENCES usuario(id)
            )
        """

        private const val CREATE_TABLE_TRANSACCION = """
            CREATE TABLE transaccion (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cuenta_id INTEGER NOT NULL,
                categoria_id INTEGER NOT NULL,
                monto REAL NOT NULL,
                tipo TEXT CHECK(tipo IN ('ingreso', 'gasto')) NOT NULL,
                fecha_transaccion TEXT NOT NULL,
                descripcion TEXT,
                es_recurrente INTEGER DEFAULT 0,
                frecuencia_recurrencia TEXT,
                comprobante_url TEXT,
                created_at TEXT DEFAULT (datetime('now')),
                updated_at TEXT DEFAULT (datetime('now')),
                FOREIGN KEY (cuenta_id) REFERENCES cuenta(id),
                FOREIGN KEY (categoria_id) REFERENCES categoria(id)
            )
        """
    }

    fun insertarUsuario(email: String, nombre: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("nombre_completo", nombre)
            put("password_hash", password)
        }
        return db.insert("usuario", null, values)
    }

    fun obtenerUsuarios(): List<String> {
        val usuarios = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuario", null)
        while (cursor.moveToNext()) {
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre_completo"))
            usuarios.add(nombre)
        }
        cursor.close()
        return usuarios
    }

    fun obtenerSaldoTotal(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(saldo) as total FROM cuenta WHERE activa = 1", null)
        var saldoTotal = 0.0
        if (cursor.moveToFirst() && !cursor.isNull(0)) {  // Verificamos que no sea NULL
            saldoTotal = cursor.getDouble(0)
        }
        cursor.close()
        return saldoTotal
    }
    fun actualizarSaldoCuenta(id: Long, nuevoSaldo: Double): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("saldo", nuevoSaldo)
            put("updated_at", "datetime('now')")
        }
        val filasActualizadas = db.update("cuenta", values, "id = ?", arrayOf(id.toString()))
        db.close()
        return filasActualizadas > 0
    }



    fun obtenerTotalIngresos(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(monto) FROM transaccion WHERE tipo = 'ingreso'", null)
        var totalIngresos = 0.0
        if (cursor.moveToFirst()) {
            totalIngresos = cursor.getDouble(0)
        }
        cursor.close()
        return totalIngresos
    }

    fun obtenerTotalGastos(): Double {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(monto) FROM transaccion WHERE tipo = 'gasto'", null)
        var totalGastos = 0.0
        if (cursor.moveToFirst()) {
            totalGastos = cursor.getDouble(0)
        }
        cursor.close()
        return totalGastos
    }

// DatabaseHelper.kt

    // Añadir la tabla de cuentas
//    val CREATE_TABLE_CUENTAS = "CREATE TABLE cuentas (" +
//            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "nombre TEXT," +
//            "saldo REAL)"

    fun insertarCuenta(nombre: String, saldo: Double): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("usuario_id", 1)  // Asumiendo un usuario por defecto
            put("nombre", nombre)
            put("tipo", "corriente")
            put("saldo", saldo)
            put("moneda", "USD")
            put("activa", 1)
            put("fecha_creacion", "datetime('now')")
            put("created_at", "datetime('now')")
            put("updated_at", "datetime('now')")
        }
        val id = db.insert("cuenta", null, values)
        db.close()
        return id
    }


    fun obtenerCuentas(): List<Cuenta> {
        val cuentas = mutableListOf<Cuenta>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM cuenta", null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            val nombreIndex = cursor.getColumnIndex("nombre")
            val saldoIndex = cursor.getColumnIndex("saldo")

            if (idIndex != -1 && nombreIndex != -1 && saldoIndex != -1) {
                do {
                    val id = cursor.getLong(idIndex)
                    val nombre = cursor.getString(nombreIndex)
                    val saldo = cursor.getDouble(saldoIndex)
                    cuentas.add(Cuenta(id, nombre, saldo))
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return cuentas
    }



    fun insertarTransaccion(cuentaId: Long, monto: Double, tipo: String, fecha: String, descripcion: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("cuenta_id", cuentaId)
            put("monto", monto)
            put("tipo", tipo)
            put("fecha_transaccion", fecha)
            put("descripcion", descripcion)
        }
        return db.insert("transaccion", null, values)
    }
    fun eliminarCuenta(id: Long): Boolean {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // Primero eliminamos las transacciones asociadas
            db.delete("transaccion", "cuenta_id = ?", arrayOf(id.toString()))
            // Luego eliminamos la cuenta
            val cuentaEliminada = db.delete("cuenta", "id = ?", arrayOf(id.toString()))
            db.setTransactionSuccessful()
            return cuentaEliminada > 0
        } catch (e: Exception) {
            return false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun insertarTransaccion(
        cuentaId: Long,
        categoriaId: Long,
        monto: Double,
        tipo: String,
        fecha: String,
        descripcion: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("cuenta_id", cuentaId)
            put("categoria_id", categoriaId)
            put("monto", monto)
            put("tipo", tipo)
            put("fecha_transaccion", fecha)
            put("descripcion", descripcion)
        }
        val id = db.insert("transaccion", null, values)
        db.close()
        return id
    }

    fun obtenerTransacciones(): List<Transaccion> {
        val transacciones = mutableListOf<Transaccion>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM transaccion", null)

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex("id")
            val cuentaIdIndex = cursor.getColumnIndex("cuenta_id")
            val categoriaIdIndex = cursor.getColumnIndex("categoria_id")
            val montoIndex = cursor.getColumnIndex("monto")
            val tipoIndex = cursor.getColumnIndex("tipo")
            val fechaIndex = cursor.getColumnIndex("fecha_transaccion")
            val descripcionIndex = cursor.getColumnIndex("descripcion")
            val esRecurrenteIndex = cursor.getColumnIndex("es_recurrente")
            val frecuenciaIndex = cursor.getColumnIndex("frecuencia_recurrencia")
            val comprobanteIndex = cursor.getColumnIndex("comprobante_url")

            do {
                val transaccion = Transaccion(
                    id = cursor.getLong(idIndex),
                    cuentaId = cursor.getLong(cuentaIdIndex),
                    categoriaId = cursor.getLong(categoriaIdIndex),
                    monto = cursor.getDouble(montoIndex),
                    tipo = cursor.getString(tipoIndex),
                    fechaTransaccion = cursor.getString(fechaIndex),
                    descripcion = cursor.getString(descripcionIndex),
                    esRecurrente = cursor.getInt(esRecurrenteIndex) == 1,
                    frecuenciaRecurrencia = cursor.getString(frecuenciaIndex),
                    comprobanteUrl = cursor.getString(comprobanteIndex)
                )
                transacciones.add(transaccion)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transacciones
    }

    fun eliminarTransaccion(id: Long): Boolean {
        val db = this.writableDatabase
        val result = db.delete("transaccion", "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }














}
