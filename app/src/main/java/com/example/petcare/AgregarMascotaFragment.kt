package com.example.petcare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class AgregarMascotaFragment : Fragment(R.layout.fragment_agregar_mascota) {

    private lateinit var imagenMascota: ImageView
    private lateinit var btnSeleccionarImagen: Button
    private var imagenUri: String = ""

    private lateinit var nombre: EditText
    private lateinit var edad: EditText
    private lateinit var especie: EditText
    private lateinit var sexo: EditText
    private lateinit var raza: EditText
    private lateinit var color: EditText
    private lateinit var peso: EditText
    private lateinit var enfermedades: EditText
    private lateinit var btnGuardar: Button
    private lateinit var db: AppDatabase

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            // Guardar imagen internamente
            val path = guardarImagenEnInterno(it)
            imagenUri = path
            // Si imagenMascota ya está listo, cargarla
            if (this::imagenMascota.isInitialized) {
                imagenMascota.setImageURI(Uri.fromFile(File(imagenUri)))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nombre = view.findViewById(R.id.editNombre)
        edad = view.findViewById(R.id.editEdad)
        especie = view.findViewById(R.id.editEspecie)
        sexo = view.findViewById(R.id.editSexo)
        raza = view.findViewById(R.id.editRaza)
        color = view.findViewById(R.id.editColor)
        peso = view.findViewById(R.id.editPeso)
        enfermedades = view.findViewById(R.id.editEnfermedades)
        imagenMascota = view.findViewById(R.id.imagenMascota)
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen)
        btnGuardar = view.findViewById(R.id.btnGuardarMascota)

        db = AppDatabase.getDatabase(requireContext())

        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagenLauncher.launch("image/*")
        }

        btnGuardar.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("sesion", android.content.Context.MODE_PRIVATE)
            val usuarioId = prefs.getInt("usuarioId", -1)

            if (usuarioId == -1) {
                Toast.makeText(requireContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mascota = Mascota(
                nombre = nombre.text.toString(),
                edad = edad.text.toString(),
                especie = especie.text.toString(),
                sexo = sexo.text.toString(),
                raza = raza.text.toString(),
                color = color.text.toString(),
                peso = peso.text.toString(),
                enfermedades = enfermedades.text.toString(),
                imagenUri = imagenUri,
                usuarioId = usuarioId
            )

            lifecycleScope.launch {
                val id = db.mascotaDao().insertar(mascota).toInt()

                // Obtener fecha actual
                val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    .format(java.util.Date())

                // Crear notificación
                val notificacion = Notificacion(
                    usuarioId = usuarioId,
                    titulo = "Nueva mascota agregada",
                    mensaje = "Registraste a '${mascota.nombre}' el día $fechaHoy",
                    fecha = fechaHoy
                )

                db.notificacionDao().insertar(notificacion)

                Toast.makeText(requireContext(), "Mascota registrada", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

    }

    private fun guardarImagenEnInterno(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "mascota_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)

        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        return file.absolutePath
    }
}
