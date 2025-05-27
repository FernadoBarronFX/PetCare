package com.example.petcare

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment

class AjustesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ajustes, container, false)

        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)
        val btnAcercaDe = view.findViewById<Button>(R.id.btnAcercaDe)

        // 👉 Cerrar sesión
        btnCerrarSesion.setOnClickListener {
            SessionManager.cerrarSesion(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 👉 Acerca de
        btnAcercaDe.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Acerca de PetCare")
                .setMessage("Versión 1.0\nDesarrollado por [Fernando de Jesus Barron y Joana Areli Lopez Sanchez].\nEsta app permite gestionar y monitorear tus mascotas de manera facil y eficiente.")
                .setPositiveButton("Aceptar", null)
                .show()
        }
        return view
    }
}
