package com.example.petcare

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import com.example.petcare.ActividadMascotaAdapter

class FragmentActividades : Fragment(R.layout.fragment_actividades) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: AppDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerActividades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        db = AppDatabase.getDatabase(requireContext())

        cargarMascotas()
    }

    private fun cargarMascotas() {
        val prefs = requireContext().getSharedPreferences("sesion", Context.MODE_PRIVATE)
        val usuarioId = prefs.getInt("usuarioId", -1)

        lifecycleScope.launch {
            val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)
            recyclerView.adapter = ActividadMascotaAdapter(
                mascotas,
                onAgregarActividadClick = { mascota ->
                    val fragment = AgregarActividadFragment()
                    val bundle = Bundle()
                    bundle.putInt("mascotaId", mascota.id)
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onVerActividadesClick = { mascota ->
                    val fragment = VerActividadesFragment()
                    val args = Bundle()
                    args.putInt("mascotaId", mascota.id)
                    fragment.arguments = args

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contenedorFragment, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            )
        }
    }
}
