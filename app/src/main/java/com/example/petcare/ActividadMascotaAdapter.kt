package com.example.petcare

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ActividadMascotaAdapter(
    private val mascotas: List<Mascota>,
    private val onAgregarActividadClick: (Mascota) -> Unit,
    private val onVerActividadesClick: (Mascota) -> Unit
) : RecyclerView.Adapter<ActividadMascotaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMascota: ImageView = view.findViewById(R.id.imgMascotaCard)
        val txtNombre: TextView = view.findViewById(R.id.txtNombreCard)
        val btnAgregarActividad: Button = view.findViewById(R.id.btnAgregarActividad)
        val btnVerActividades: Button = view.findViewById(R.id.btnVerActividades)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota_actividad, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mascota = mascotas[position]
        holder.txtNombre.text = mascota.nombre

        // ✅ Cargar imagen desde almacenamiento interno (ruta completa o relativa)
        if (mascota.imagenUri.isNotBlank()) {
            val context = holder.itemView.context
            val archivo = File(mascota.imagenUri)
            val imagenFile = if (archivo.exists()) {
                archivo
            } else {
                File(context.filesDir, mascota.imagenUri)
            }

            if (imagenFile.exists()) {
                holder.imgMascota.setImageURI(Uri.fromFile(imagenFile))
            } else {
                holder.imgMascota.setImageResource(R.drawable.ic_pet_placeholder)
            }
        } else {
            holder.imgMascota.setImageResource(R.drawable.ic_pet_placeholder)
        }

        holder.btnAgregarActividad.setOnClickListener {
            onAgregarActividadClick(mascota)
        }

        holder.btnVerActividades.setOnClickListener {
            onVerActividadesClick(mascota)
        }
    }

    override fun getItemCount(): Int = mascotas.size
}
