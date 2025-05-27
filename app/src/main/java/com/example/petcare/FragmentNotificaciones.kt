package com.example.petcare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FragmentNotificaciones : Fragment(R.layout.fragment_notificaciones) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerNotificaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getDatabase(requireContext())

        cargarNotificaciones()
    }

    private fun cargarNotificaciones() {
        lifecycleScope.launch {
            val usuarioId = requireContext()
                .getSharedPreferences("sesion", android.content.Context.MODE_PRIVATE)
                .getInt("usuarioId", -1)

            val notificaciones = db.notificacionDao().obtenerPorUsuario(usuarioId)
            recyclerView.adapter = NotificacionAdapter(notificaciones) { notificacion, accion ->
                when (accion) {
                    "completada" -> marcarComoCompletada(notificacion)
                    "eliminar" -> eliminarNotificacion(notificacion)
                }
            }
        }
    }

    private fun marcarComoCompletada(notificacion: Notificacion) {
        lifecycleScope.launch {
            db.notificacionDao().marcarComoCompletada(notificacion.id) // pasa solo el ID
            cargarNotificaciones()
        }
    }

    private fun eliminarNotificacion(notificacion: Notificacion) {
        lifecycleScope.launch {
            db.notificacionDao().eliminar(notificacion.id) // pasa solo el ID
            cargarNotificaciones()
        }
    }

}
