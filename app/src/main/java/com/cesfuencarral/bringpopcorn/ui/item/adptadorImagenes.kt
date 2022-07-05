package com.cesfuencarral.bringpopcorn.ui.item

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cesfuencarral.bringpopcorn.R
import com.cesfuencarral.bringpopcorn.principal
import com.google.firebase.firestore.FirebaseFirestore

class adptadorImagenes (val catalogo: List< String>) : RecyclerView.Adapter<adptadorImagenes.holder>() {
    //referencia a la base de datos
    class holder(val view: View) : RecyclerView.ViewHolder(view) {
        // Envia el contenido de los cardview
        fun render(cat: String) {
            Glide
                    .with(view.context)
                    .load(cat)
                    .into(view.findViewById<ImageView>(R.id.imgPortada));
            itemView.setOnClickListener {
                showFilm(it, cat)
            }
        }

        // Te lleva a la pantalla de media
        private fun showFilm(root: View, url: String) {

            //enviar al shared, cambiarlo en la db y pintarlo desde aqui todo y luego un toast
            val prefs = root.context.getSharedPreferences(root.context.getString(R.string.pref_files), Context.MODE_PRIVATE)
            val ed = prefs.edit()
            ed.remove("perfil")
            ed.apply()
            ed.putString("perfil", url)
            ed.apply()
            val db = FirebaseFirestore.getInstance()

            db.collection("users")
                    .document(prefs.getString("email","").toString())
                    .set(
                            hashMapOf(
                                    "nombre" to prefs.getString("nombre","").toString(),
                                    "edad" to prefs.getString("edad","").toString(),
                                    "contraseña" to prefs.getString("contraseña","").toString(),
                                    "lista" to prefs.getString("lista","").toString(),
                                    "perfil" to url)
                    )
            Toast.makeText(root.context,"Cambio realizado, solo vuelva atras y si no lo visualiza toque su foto de perfil", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int = catalogo.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val layoutinflater = LayoutInflater.from(parent.context)
        return holder(layoutinflater.inflate(R.layout.item_list, parent, false))

    }

    override fun onBindViewHolder(holder2: holder, position: Int) {
        holder2.render(catalogo[position])
    }
}

