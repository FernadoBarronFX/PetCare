package com.example.petcare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class FragmentMascotas : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAgregar: ImageView
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mascotas, container, false)

        recyclerView = view.findViewById(R.id.recyclerMascotas)
        btnAgregar = view.findViewById(R.id.btnAgregarMascota)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        db = AppDatabase.getDatabase(requireContext())

        btnAgregar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, AgregarMascotaFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        cargarMascotas()
    }

    private fun cargarMascotas() {
        val prefs = requireContext().getSharedPreferences("sesion", android.content.Context.MODE_PRIVATE)
        val usuarioId = prefs.getInt("usuarioId", -1)

        if (usuarioId == -1) {
            Toast.makeText(requireContext(), "Usuario no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId)
            val adapter = MascotaAdapter(mascotas) { mascota ->
                val fragment = DetalleMascotaFragment().apply {
                    arguments = Bundle().apply {
                        putInt("mascotaId", mascota.id)
                    }
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.contenedorFragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            recyclerView.adapter = adapter
        }
    }
}
