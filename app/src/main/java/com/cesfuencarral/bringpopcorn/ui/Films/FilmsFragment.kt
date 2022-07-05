package com.cesfuencarral.bringpopcorn.ui.Films

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.cesfuencarral.bringpopcorn.R
import com.cesfuencarral.bringpopcorn.ui.item.adaptadorPelis
//import com.cesfuencarral.bringpopcorn.ui.item.adaptadorSeries
import com.cesfuencarral.bringpopcorn.ui.item.pelisCatalogo
//import com.cesfuencarral.bringpopcorn.ui.item.seriesCatalogo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FilmsFragment : Fragment() {
    //referencia a la base de datos
    private val db = FirebaseFirestore.getInstance()
    var catalogo:  List<pelisCatalogo> = listOf(pelisCatalogo("a","a","a","a"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_films, container, false)
        val storage = FirebaseStorage.getInstance().getReference()
        val pelis = storage.child("peliculas")

        pelis.listAll().addOnSuccessListener {
            for (item in it.getItems()) {
                db.collection("videos/peliculas/${item.name}")
                    .document("datos").get()
                    .addOnSuccessListener {
                        //variables de la base de datos
                        val titulo = it.get("titulo") as String
                        val url = it.get("url") as String
                        val desc = it.get("descripción") as String
                        val portada = it.get("portada") as String
                        // Comprobación de que no se repita y añade las peliculas
                        var contador = 0
                        if (find(titulo) && contador<=5){
                            contador++

                            if (catalogo[0].titulo=="a"){
                                    catalogo = listOf(
                                        pelisCatalogo(titulo,url,desc,portada))
                                }else{
                                    catalogo += listOf(
                                        pelisCatalogo(titulo,url,desc,portada))
                                }
                            }else{
                                if(find(titulo)){
                                    catalogo += listOf(
                                            pelisCatalogo(titulo,url,desc,portada))
                                    //Guardado de datos
                                    initrecycler2(root)
                                }
                        }
                        //Guardado de datos
                        initrecycler(root)

                    }.addOnFailureListener {
                    println("No se ha podido recuperar la serie/pelicula")
                }
            }

        }.addOnFailureListener {
            showAlert(root)
        }
        return root

    }

    // Añade la vista al layout 2

    fun initrecycler2(root:View){
        // Hace que la lista se vea en horizontal
        root.findViewById<RecyclerView>(R.id.rv2).layoutManager  = LinearLayoutManager(
                root.context, RecyclerView.HORIZONTAL,false)
        val adapt  = adaptadorPelis(catalogo)
        root.findViewById<RecyclerView>(R.id.rv2).adapter = adapt
    }

    // Comprueba que los videos no se repitan
    private fun find (tt:String):Boolean {
        var peli = true
        catalogo.forEach {
            if(tt.equals(it.titulo)){
                peli = false
            }
        }
        return peli
        }

    // Indica un error al acceder a la base de datos
    private fun showAlert(root: View) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(root.context)
        builder.setTitle("Carga fallida")
        builder.setMessage("Se ha producido un error al intentar cargar el contenido")
        builder.setPositiveButton("Aceptar", null)
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }

    // Añade la vista al layout
    fun initrecycler(root:View){
        // Hace que la lista se vea en horizontal
        root.findViewById<RecyclerView>(R.id.rv).layoutManager  = LinearLayoutManager(
            root.context,RecyclerView.HORIZONTAL,false)
        val adapt  = adaptadorPelis(catalogo)
        root.findViewById<RecyclerView>(R.id.rv).adapter = adapt
    }
}