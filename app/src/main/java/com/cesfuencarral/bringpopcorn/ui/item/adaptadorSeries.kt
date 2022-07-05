package com.cesfuencarral.bringpopcorn.ui.item

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cesfuencarral.bringpopcorn.Info_series
import com.cesfuencarral.bringpopcorn.R

class adaptadorSeries(val catalogo: List< seriesCatalogo>) : RecyclerView.Adapter<adaptadorSeries.holder>() {

    class holder (val view : View): RecyclerView.ViewHolder(view) {
        // Envia el contenido de los cardview
        fun render(cat:seriesCatalogo){
            view.findViewById<TextView>(R.id.tvTitulo).text = cat.titulo
            Glide
                .with(view.context)
                .load(cat.portada)
                .into(view.findViewById<ImageView>(R.id.imgPortada));
            itemView.setOnClickListener{
                showFilm(it,cat.titulo,cat.descripcion,cat.portada)
            }
        }

        // Te lleva a la pantalla de media
        private fun showFilm(root: View, titulo: String, descripcion: String, portada: String) {
            val info = Intent(root.context, Info_series::class.java).apply {
                putExtra("tipo","series")
                putExtra("titulo", titulo)
                putExtra("descripcion", descripcion)
                putExtra("portada", portada)
            }
            root.context.startActivity(info)
        }
    }

    override fun getItemCount(): Int =catalogo.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val layoutinflater  = LayoutInflater.from(parent.context)
        return holder(layoutinflater.inflate(R.layout.item_list,parent,false))

    }

    override fun onBindViewHolder(holder2: holder, position: Int) {
        holder2.render(catalogo[position])
    }

}