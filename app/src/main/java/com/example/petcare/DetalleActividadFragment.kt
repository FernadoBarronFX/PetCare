package com.example.petcare

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetalleActividadFragment : Fragment(R.layout.fragment_detalle_actividad) {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etFecha: EditText

    private lateinit var btnEditar: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnEliminar: Button

    private lateinit var db: AppDatabase
    private var actividadId: Int = -1
    private var actividadActual: Actividad? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitulo = view.findViewById(R.id.etTituloActividad)
        etDescripcion = view.findViewById(R.id.etDescripcionActividad)
        etFecha = view.findViewById(R.id.etFechaActividad)

        btnEditar = view.findViewById(R.id.btnEditarActividad)
        btnGuardar = view.findViewById(R.id.btnGuardarActividad)
        btnCancelar = view.findViewById(R.id.btnCancelarActividad)
        btnEliminar = view.findViewById(R.id.btnEliminarActividad)

        db = AppDatabase.getDatabase(requireContext())
        actividadId = arguments?.getInt("actividadId", -1) ?: -1

        cargarDatosActividad()

        btnEditar.setOnClickListener {
            activarEdicion(true)
        }

        btnCancelar.setOnClickListener {
            mostrarDatosActividad()
            activarEdicion(false)
        }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }

        btnEliminar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar actividad?")
                .setMessage("¿Estás seguro de eliminar esta actividad?")
                .setPositiveButton("Sí") { _, _ ->
                    actividadActual?.let {
                        lifecycleScope.launch {
                            db.actividadDao().eliminar(it)
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun cargarDatosActividad() {
        lifecycleScope.launch {
            val actividad = db.actividadDao().obtenerPorId(actividadId)
            actividad?.let {
                actividadActual = it
                mostrarDatosActividad()
            }
        }
    }

    private fun mostrarDatosActividad() {
        actividadActual?.let { actividad ->
            etTitulo.setText(actividad.titulo)
            etDescripcion.setText(actividad.descripcion)
            etFecha.setText(actividad.fecha)
        }
        activarEdicion(false)
    }

    private fun activarEdicion(activo: Boolean) {
        etTitulo.isEnabled = activo
        etDescripcion.isEnabled = activo
        etFecha.isEnabled = activo

        btnGuardar.visibility = if (activo) View.VISIBLE else View.GONE
        btnCancelar.visibility = if (activo) View.VISIBLE else View.GONE
        btnEditar.visibility = if (activo) View.GONE else View.VISIBLE
    }

    private fun guardarCambios() {
        actividadActual?.let {
            val actividadEditada = it.copy(
                titulo = etTitulo.text.toString(),
                descripcion = etDescripcion.text.toString(),
                fecha = etFecha.text.toString()
            )

            lifecycleScope.launch {
                db.actividadDao().actualizar(actividadEditada)
                actividadActual = actividadEditada
                mostrarDatosActividad()
                Toast.makeText(requireContext(), "Cambios guardados", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
