package com.cesfuencarral.bringpopcorn

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Info_films : AppCompatActivity() {
    //referencia a la base de datos
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_films)

        //variables
        val bundle = intent.extras
        val titulo = bundle?.getString("titulo")
        val descripcion = bundle?.getString("descripcion")
        val portada = bundle?.getString("portada")
        val url = bundle?.getString("url")

        val img = findViewById<ImageView>(R.id.img)
        val tit = findViewById<TextView>(R.id.titulo)
        tit.text = titulo
        val descriptor = findViewById<TextView>(R.id.descripcion)
        descriptor.text = descripcion
            Glide
                .with(this)
                .load(portada)
                .into(img);

        val verbtn = findViewById<Button>(R.id.ver)
        verbtn.setOnClickListener {
            ver(url)
        }

        val addbtn = findViewById<Button>(R.id.addbtn)
        addbtn.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.pref_files), Context.MODE_PRIVATE)
            db.collection("users")
                .document(prefs.getString("email",null).toString()).set(

                    if (prefs.getString("lista","") != "") {
                        hashMapOf(
                            "nombre" to prefs.getString("nombre",""),
                            "edad" to prefs.getString("edad","0"),
                            "contraseña" to prefs.getString("contraseña",""),
                            "lista" to "${prefs.getString("lista","")},$titulo"
                        )
                    } else {
                        hashMapOf(
                            "nombre" to prefs.getString("nombre",""),
                            "edad" to prefs.getString("edad","0"),
                            "contraseña" to prefs.getString("contraseña",""),
                            "lista" to titulo
                        )
                    }

                )
            Toast.makeText(this,"Añadido a tu lista",Toast.LENGTH_SHORT).show()
        }

        // Boton de compartir
        val sharebtn = findViewById<Button>(R.id.sharebtn)
        sharebtn.setOnClickListener {
            val share = Intent()
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT,"Estoy viendo ${tit.text} en bring popcorn y es una pasada!!\n Pruebalo")
            share.action = Intent.ACTION_SEND
            val choose = Intent.createChooser(share,"Elija una opción")
            startActivity(choose)
        }


    }
    // Te lleva a la pantalla de media
    private fun ver(url:String?){
        val homeIntent = Intent(this, media::class.java).apply {
            putExtra("url",url)
        }
        startActivity(homeIntent)
    }
}