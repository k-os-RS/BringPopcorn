package com.cesfuencarral.bringpopcorn.ui.item

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cesfuencarral.bringpopcorn.Info_films
import com.cesfuencarral.bringpopcorn.R

class adaptadorPelis(val catalogo: List< pelisCatalogo>) : RecyclerView.Adapter<adaptadorPelis.holder>() {

    class holder (val view : View): RecyclerView.ViewHolder(view) {

        fun render(cat:pelisCatalogo){
            view.findViewById<TextView>(R.id.tvTitulo).text = cat.titulo
            Glide
                .with(view.context)
                .load(cat.portada)
                .into(view.findViewById<ImageView>(R.id.imgPortada));
            itemView.setOnClickListener{
                showFilm(it,cat.titulo,cat.descripcion,cat.portada,cat.url)
            }
        }

        // Te lleva a la pantalla de media
        private fun showFilm(root: View, titulo: String, descripcion: String, portada: String, url: String) {
            val info = Intent(root.context, Info_films::class.java).apply {
                putExtra("tipo","peliculas")
                putExtra("titulo", titulo)
                putExtra("descripcion", descripcion)
                putExtra("portada", portada)
                putExtra("url", url)
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