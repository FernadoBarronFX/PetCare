package com.example.petcare

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File

class DetalleMascotaFragment : Fragment(R.layout.fragment_detalle_mascota) {

    private lateinit var imgMascota: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etEspecie: EditText
    private lateinit var etSexo: EditText
    private lateinit var etRaza: EditText
    private lateinit var etColor: EditText
    private lateinit var etPeso: EditText
    private lateinit var etEnfermedades: EditText

    private lateinit var btnEditar: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnEliminar: Button

    private lateinit var db: AppDatabase
    private var mascotaId: Int = -1
    private var mascotaActual: Mascota? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgMascota = view.findViewById(R.id.imgDetalleMascota)
        etNombre = view.findViewById(R.id.etNombre)
        etEdad = view.findViewById(R.id.etEdad)
        etEspecie = view.findViewById(R.id.etEspecie)
        etSexo = view.findViewById(R.id.etSexo)
        etRaza = view.findViewById(R.id.etRaza)
        etColor = view.findViewById(R.id.etColor)
        etPeso = view.findViewById(R.id.etPeso)
        etEnfermedades = view.findViewById(R.id.etEnfermedades)

        btnEditar = view.findViewById(R.id.btnEditar)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
        btnEliminar = view.findViewById(R.id.btnEliminar)

        db = AppDatabase.getDatabase(requireContext())
        mascotaId = arguments?.getInt("mascotaId", -1) ?: -1

        cargarDatosMascota()

        btnEditar.setOnClickListener { activarEdicion(true) }

        btnCancelar.setOnClickListener {
            mostrarDatosMascota()
            activarEdicion(false)
        }

        btnGuardar.setOnClickListener { guardarCambios() }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar mascota?")
                .setMessage("¿Estás seguro de eliminar esta mascota?")
                .setPositiveButton("Sí") { _, _ ->
                    mascotaActual?.let {
                        lifecycleScope.launch {
                            db.mascotaDao().eliminar(it)
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun cargarDatosMascota() {
        lifecycleScope.launch {
            val mascota = db.mascotaDao().obtenerPorId(mascotaId)
            mascota?.let {
                mascotaActual = it
                mostrarDatosMascota()
            }
        }
    }

    private fun mostrarDatosMascota() {
        mascotaActual?.let { mascota ->
            etNombre.setText(mascota.nombre)
            etEdad.setText(mascota.edad)
            etEspecie.setText(mascota.especie)
            etSexo.setText(mascota.sexo)
            etRaza.setText(mascota.raza)
            etColor.setText(mascota.color)
            etPeso.setText(mascota.peso)
            etEnfermedades.setText(mascota.enfermedades)

            // Mostrar imagen desde almacenamiento interno
            if (mascota.imagenUri.isNotEmpty()) {
                val imagenFile = File(mascota.imagenUri)
                if (imagenFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(imagenFile.absolutePath)
                    imgMascota.setImageBitmap(bitmap)
                } else {
                    imgMascota.setImageResource(R.drawable.ic_pet_placeholder)
                }
            } else {
                imgMascota.setImageResource(R.drawable.ic_pet_placeholder)
            }
        }
        activarEdicion(false)
    }

    private fun activarEdicion(activo: Boolean) {
        etNombre.isEnabled = activo
        etEdad.isEnabled = activo
        etEspecie.isEnabled = activo
        etSexo.isEnabled = activo
        etRaza.isEnabled = activo
        etColor.isEnabled = activo
        etPeso.isEnabled = activo
        etEnfermedades.isEnabled = activo

        btnGuardar.visibility = if (activo) View.VISIBLE else View.GONE
        btnCancelar.visibility = if (activo) View.VISIBLE else View.GONE
        btnEditar.visibility = if (activo) View.GONE else View.VISIBLE
    }

    private fun guardarCambios() {
        mascotaActual?.let {
            val mascotaEditada = it.copy(
                nombre = etNombre.text.toString(),
                edad = etEdad.text.toString(),
                especie = etEspecie.text.toString(),
                sexo = etSexo.text.toString(),
                raza = etRaza.text.toString(),
                color = etColor.text.toString(),
                peso = etPeso.text.toString(),
                enfermedades = etEnfermedades.text.toString()
            )

            lifecycleScope.launch {
                db.mascotaDao().actualizar(mascotaEditada)
                mascotaActual = mascotaEditada
                mostrarDatosMascota()
                Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
