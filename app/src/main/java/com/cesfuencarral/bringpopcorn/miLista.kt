package com.cesfuencarral.bringpopcorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.cesfuencarral.bringpopcorn.ui.item.adaptadorPelis
import com.cesfuencarral.bringpopcorn.ui.item.adaptadorSeries
import com.cesfuencarral.bringpopcorn.ui.item.adptadorImagenes
import com.cesfuencarral.bringpopcorn.ui.item.seriesCatalogo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class miLista : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    var catalogo:  List<String> = listOf("a")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_lista)

        val storage = FirebaseStorage.getInstance().getReference()
        val perfil = storage.child("perfil")

        perfil.listAll().addOnSuccessListener {
            for (item in it.items) {
                db.collection("perfil")
                        .document(item.name).get()
                        .addOnSuccessListener {

                            //variables de la base de datos
                            val url = it.get("url") as String

                            // Comprobación de que no se repita y añade las peliculas
                            if (find(url)){
                                if (catalogo[0]=="a"){
                                    catalogo = listOf(url)
                                }else{
                                    catalogo += url
                                }
                            }
                            //Guardado de datos
                            initrecycler()

                        }.addOnFailureListener {
                            println("F")
                        }
            }

        }
    }
    // Comprueba que los videos no se repitan
    private fun find (tt:String):Boolean {
        var peli = true
        catalogo.forEach {
            if(tt.equals(it)){
                peli = false
            }
        }
        return peli
    }

    // Añade la vista al layout
    fun initrecycler(){
        // Hace que la lista se vea en horizontal
        findViewById<RecyclerView>(R.id.perfiles).layoutManager  = LinearLayoutManager(
                this, OrientationHelper.HORIZONTAL,false)
        val adapt  = adptadorImagenes(catalogo)
        findViewById<RecyclerView>(R.id.perfiles).adapter = adapt
    }
}