package com.example.practicafirebase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MantenimientosAdapter(
    private val context: Context,
    private val mantenimientosList: List<Mantenimiento>
) : RecyclerView.Adapter<MantenimientosAdapter.MantenimientoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MantenimientoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_mantenimiento, parent, false)
        return MantenimientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MantenimientoViewHolder, position: Int) {
        val mantenimiento = mantenimientosList[position]
        holder.bind(mantenimiento)
    }

    override fun getItemCount(): Int {
        return mantenimientosList.size
    }

    inner class MantenimientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tecnicoTextView: TextView = itemView.findViewById(R.id.textViewTecnico)
        private val establecimientoTextView: TextView = itemView.findViewById(R.id.textViewEstablecimiento)
        private val fechaTextView: TextView = itemView.findViewById(R.id.textViewFecha)
        private val observacionesTextView: TextView = itemView.findViewById(R.id.textViewObservaciones)

        private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(mantenimiento: Mantenimiento) {
            tecnicoTextView.text = mantenimiento.tecnico
            establecimientoTextView.text = mantenimiento.establecimiento
            fechaTextView.text = dateFormatter.format(mantenimiento.fecha)
            observacionesTextView.text = mantenimiento.observaciones
        }
    }
}
