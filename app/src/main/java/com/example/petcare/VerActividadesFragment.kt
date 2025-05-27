package com.example.petcare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class VerActividadesFragment : Fragment(R.layout.fragment_ver_actividades) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase
    private var mascotaId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerVerActividades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getDatabase(requireContext())

        mascotaId = arguments?.getInt("mascotaId", -1) ?: -1
        if (mascotaId != -1) {
            cargarActividades()
        }
    }

    private fun cargarActividades() {
        lifecycleScope.launch {
            val actividades = db.actividadDao().obtenerPorMascota(mascotaId)
            recyclerView.adapter = ActividadAdapter(actividades) { actividad ->
                val fragment = DetalleActividadFragment().apply {
                    arguments = Bundle().apply {
                        putInt("actividadId", actividad.id)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
