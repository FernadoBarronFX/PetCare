package com.example.petcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.graphics.Color



class NotificacionAdapter(
    private val notificaciones: List<Notificacion>,
    private val onAccionClick: (Notificacion, String) -> Unit
) : RecyclerView.Adapter<NotificacionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtContenido: TextView = view.findViewById(R.id.txtContenidoNotificacion)
        val txtFecha: TextView = view.findViewById(R.id.txtFechaNotificacion)
        val txtMensaje: TextView = view.findViewById(R.id.txtMensajeNoti) // 🆕
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnCompletada: Button = view.findViewById(R.id.btnCompletada)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notificacion = notificaciones[position]
        holder.txtContenido.text = notificacion.mensaje
        holder.txtFecha.text = notificacion.fecha

        if (notificacion.completada) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.azulFuerte)
            )
            holder.btnCompletada.visibility = View.GONE
            holder.btnEliminar.text = "Eliminar"
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.btnCompletada.visibility = View.VISIBLE
            holder.btnEliminar.text = "Eliminar"
        }

        holder.btnEliminar.setOnClickListener {
            onAccionClick(notificacion, "eliminar")
        }

        holder.btnCompletada.setOnClickListener {
            onAccionClick(notificacion, "completada")
        }
    }



    override fun getItemCount(): Int = notificaciones.size
}
