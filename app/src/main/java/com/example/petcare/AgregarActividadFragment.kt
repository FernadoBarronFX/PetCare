package com.example.petcare

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AgregarActividadFragment : Fragment(R.layout.fragment_agregar_actividad) {

    private lateinit var editTitulo: EditText
    private lateinit var editDescripcion: EditText
    private lateinit var editFecha: EditText
    private lateinit var btnGuardar: Button
    private lateinit var db: AppDatabase
    private var mascotaId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTitulo = view.findViewById(R.id.editTitulo)
        editDescripcion = view.findViewById(R.id.editDescripcion)
        editFecha = view.findViewById(R.id.editFecha)
        btnGuardar = view.findViewById(R.id.btnGuardarActividad)

        db = AppDatabase.getDatabase(requireContext())
        mascotaId = arguments?.getInt("mascotaId", -1) ?: -1

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        editFecha.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    editFecha.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnGuardar.setOnClickListener {
            val titulo = editTitulo.text.toString()
            val descripcion = editDescripcion.text.toString()
            val fecha = editFecha.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty() && fecha.isNotEmpty()) {
                val actividad = Actividad(
                    mascotaId = mascotaId,
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = fecha
                )

                lifecycleScope.launch {
                    db.actividadDao().insertar(actividad)

                    val mascota = db.mascotaDao().obtenerPorId(mascotaId)
                    if (mascota != null) {
                        val notificacion = Notificacion(
                            usuarioId = mascota.usuarioId,
                            titulo = "Nueva actividad registrada",
                            mensaje = "Se agregó '${titulo}' para ${mascota.nombre} el día $fecha",
                            fecha = fecha
                        )
                        db.notificacionDao().insertar(notificacion)
                    }

                    Toast.makeText(requireContext(), "Actividad guardada", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
