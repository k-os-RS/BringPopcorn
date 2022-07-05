package com.cesfuencarral.bringpopcorn.ui.Settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.cesfuencarral.bringpopcorn.AuthActivity
import com.cesfuencarral.bringpopcorn.R
import com.cesfuencarral.bringpopcorn.help
import com.cesfuencarral.bringpopcorn.miLista
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class settings : Fragment() {

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val main = inflater.inflate(R.layout.fragment_settings, container, false)
        val prefs = main.context.getSharedPreferences(main.context.getString(R.string.pref_files), Context.MODE_PRIVATE)

        var nombreUsuario:String? = null
        var edadUsuario:String? = null
        var fotoUsuario:String? = null

        nombreUsuario = prefs.getString("nombre","")
        edadUsuario = prefs.getString("edad","")
        fotoUsuario = prefs.getString("perfil","")
        val userName = main.findViewById<TextView>(R.id.profile_name)
        userName.text = nombreUsuario
        val userPhoto = main.findViewById<CircleImageView>(R.id.profile_image)


        userPhoto.setOnClickListener {
            fotoUsuario = prefs.getString("perfil","")
            Glide
                    .with(this)
                    .load(fotoUsuario)
                    .into(userPhoto)
        }
        Glide
                .with(this)
                .load(fotoUsuario)
                .into(userPhoto)

        val userAge = main.findViewById<TextView>(R.id.profile_age)
        userAge.text = edadUsuario
        val logoutbtn = main.findViewById<Button>(R.id.logoutbtn)
        val helpbtn = main.findViewById<Button>(R.id.helpbtn)
        val myListbtn = main.findViewById<Button>(R.id.listbtn)


        // Cerrar sesión
        logoutbtn.setOnClickListener{
             // Borrado de datos de la memoria interna
             val prefs = main.context.getSharedPreferences(getString(R.string.pref_files), Context.MODE_PRIVATE).edit()
             prefs.clear()
             prefs.apply()
             // Cierrra sesión en la base de datos
             FirebaseAuth.getInstance().signOut()
             // Vuelve a la pantalla de inicio
            //activity?.finish()
            val intent = Intent(it.context, AuthActivity::class.java)
            activity?.finish()
            startActivity(intent)
        }
        // Abrir ayuda
          helpbtn.setOnClickListener {
              val intent = Intent(it.context, help::class.java)
              startActivity(intent)
          }
        // Abrir menu general
        myListbtn.setOnClickListener {
            val homeIntent = Intent(it.context, miLista::class.java).apply {
                //putExtra("ser",email)
            }
            startActivity(homeIntent)
        }
        return main
    }
}