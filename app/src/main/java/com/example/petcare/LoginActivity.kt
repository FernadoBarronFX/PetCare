package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var editCorreo: EditText
    private lateinit var editContrasena: EditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editCorreo = findViewById(R.id.editCorreo)
        editContrasena = findViewById(R.id.editContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        val btnIrRegistro = findViewById<TextView>(R.id.btnIrRegistro)
        btnIrRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        db = AppDatabase.getDatabase(this)

        btnIniciarSesion.setOnClickListener {
            val correo = editCorreo.text.toString()
            val contrasena = editContrasena.text.toString()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val usuario = db.usuarioDao().autenticar(correo, contrasena)
                    runOnUiThread {
                        if (usuario != null) {
                            val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
                            prefs.edit().putInt("usuarioId", usuario.id).apply()

                            // 🔔 Notificación de inicio de sesión
                            CoroutineScope(Dispatchers.IO).launch {
                                val notificacion = Notificacion(
                                    usuarioId = usuario.id,
                                    titulo = "Inicio de sesión",
                                    mensaje = "Bienvenido de nuevo a PetCare, ${usuario.nombre}!",
                                    fecha = obtenerFechaActual()
                                )
                                db.notificacionDao().insertar(notificacion)

                                val actividadesResumen = generarResumenDiario(usuario.id)
                                val notificacionResumen = Notificacion(
                                    usuarioId = usuario.id,
                                    titulo = "Resumen de actividades de hoy",
                                    mensaje = actividadesResumen,
                                    fecha = obtenerFechaActual()
                                )
                                db.notificacionDao().insertar(notificacionResumen)
                            }

                            Toast.makeText(this@LoginActivity, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }


                    }
                }
            } else {
                Toast.makeText(this, "Por favor completa los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerFechaActual(): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }


    private suspend fun generarResumenDiario(usuarioId: Int): String {
        val db = AppDatabase.getDatabase(this)
        val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)
        val hoy = obtenerFechaActual()

        val resumen = mascotas.mapNotNull { mascota ->
            val actividades = db.actividadDao().obtenerPorMascota(mascota.id)
                .filter { it.fecha == hoy }

            if (actividades.isNotEmpty()) {
                val detalles = actividades.joinToString { "- ${it.titulo}" }
                "🐾 ${mascota.nombre}:\n$detalles"
            } else null
        }

        return if (resumen.isNotEmpty())
            resumen.joinToString("\n\n")
        else "No hay actividades programadas para hoy."
    }

}
