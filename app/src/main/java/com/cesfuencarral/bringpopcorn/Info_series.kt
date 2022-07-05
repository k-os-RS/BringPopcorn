package com.cesfuencarral.bringpopcorn

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text

class Info_series : AppCompatActivity() {
    //referencia a la base de datos
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_series)

        //variables
        val bundle = intent.extras
        val titulo = bundle?.getString("titulo")
        val descripcion = bundle?.getString("descripcion")
        val portada = bundle?.getString("portada")
        val url = bundle?.getString("url")

        val img = findViewById<ImageView>(R.id.img)
        val tit = findViewById<TextView>(R.id.titulo)
        val descript = findViewById<TextView>(R.id.descripcion)
        val caps = findViewById<ListView>(R.id.caps)

        // Asigno los valores
        tit.text = titulo
        descript.text = descripcion
        Glide
            .with(this)
            .load(portada)
            .into(img);


        //sacar la lista de caps
        val storage = FirebaseStorage.getInstance().getReference()
        val series = storage.child("series/$titulo")
        val array: ArrayList<String> = mutableListOf<String>("") as ArrayList<String>

        series.listAll().addOnSuccessListener {
            for (item in it.getItems()) {
                array.add(item.getName())
            }
            val adaptador: ArrayAdapter<*> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, array)

            if (caps != null) {
                caps.adapter = adaptador

                caps.setOnItemClickListener { parent, view, position, id ->
                    val name = parent.getItemAtPosition(position).toString()
                    //lanza nueva pantalla
                    db.collection("videos/series/$titulo").document(name).get().addOnSuccessListener {
                        val url = it.get("url")as String
                        showHome(url)
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Uff cuesta ",Toast.LENGTH_SHORT).show()
        }

        // Añade a la lista de favoritos
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

        // Comparte la información sobre lo que estas viendo y el nombre de la app
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
    private fun showHome(url:String?){
        val homeIntent = Intent(this, media::class.java).apply {
            putExtra("url",url)
        }
        startActivity(homeIntent)
    }
}