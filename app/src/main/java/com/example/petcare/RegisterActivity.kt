package com.example.petcare

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editCorreo: EditText
    private lateinit var editContrasena: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editNombre = findViewById(R.id.editNombre)
        editCorreo = findViewById(R.id.editCorreo)
        editContrasena = findViewById(R.id.editContraseña)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        db = AppDatabase.getDatabase(this)

        btnRegistrar.setOnClickListener {
            val nombre = editNombre.text.toString()
            val correo = editCorreo.text.toString()
            val contrasena = editContrasena.text.toString()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contraseña = contrasena)

                CoroutineScope(Dispatchers.IO).launch {
                    val id = db.usuarioDao().insertar(nuevoUsuario).toInt()

                    withContext(Dispatchers.Main) {
                        val prefs = getSharedPreferences("sesion", MODE_PRIVATE)
                        prefs.edit().putInt("usuarioId", id).apply()

                        // 🔔 Notificación de registro
                        CoroutineScope(Dispatchers.IO).launch {
                            val notificacion = Notificacion(
                                usuarioId = id,
                                titulo = "¡Registro exitoso!",
                                mensaje = "Tu cuenta ha sido creada correctamente. ¡Bienvenido a PetCare!",
                                fecha = obtenerFechaActual()
                            )
                            db.notificacionDao().insertar(notificacion)
                        }

                        Toast.makeText(this@RegisterActivity, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerFechaActual(): String {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return java.time.LocalDate.now().format(formatter)
    }
}
