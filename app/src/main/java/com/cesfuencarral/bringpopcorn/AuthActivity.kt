 package com.cesfuencarral.bringpopcorn

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
/*
import android.graphics.Color
import android.text.Editable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
*/
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

 class AuthActivity : AppCompatActivity() {

     // private val GOOGLE_SIGN_IN = 100
     // referencia a la base de datos
     private val db = FirebaseFirestore.getInstance()

     @RequiresApi(Build.VERSION_CODES.M)
     override fun onCreate(savedInstanceState: Bundle?) {

         // Tiempo que le dejo a la pantalla de carga
        Thread.sleep(1500)
         // Splash o pantalla de carga
         setTheme(R.style.Theme_BringPopcorn)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Analytics event
         // Sirve para la que la base de datos pueda tener un analisis de la app (usuarios, datos cargados, errores...)
        val analytics =  FirebaseAnalytics.getInstance(this)
        val bundle  = Bundle()
        bundle.putString("message", "Integración completa")
        analytics.logEvent("InitScreen",bundle)

        // Setup
        setup()

        //Comprueba si hay una sesión abierta
        //session()
     }

     override fun onStart() {
         super.onStart()
         // Método por si no se llegase a mostrar el contenido ( posible fallo de inicio de sesión )
         val authLayout = findViewById<LinearLayout>(R.id.authLayout)
         authLayout.visibility = View.VISIBLE
     }

     @RequiresApi(Build.VERSION_CODES.M)
     private fun setup(){

         title = "Autenticación"
         // Variables
         val signbtn = findViewById<Button>(R.id.signbtn)
         val loginbtn = findViewById<Button>(R.id.loginbtn)
         val googlebtn = findViewById<Button>(R.id.googlebtn)
         val olvido = findViewById<TextView>(R.id.forgottv)
         val email = findViewById<TextInputLayout>(R.id.email)
         val pass = findViewById<TextInputLayout>(R.id.pssw)
            email.boxBackgroundColor = getColor(R.color.pink_soft2)
            pass.boxBackgroundColor = getColor(R.color.pink_soft2)

         //borra el error cuando vas a corregirlo
         val a = findViewById<TextInputEditText>(R.id.a)
         a.setOnFocusChangeListener { v, hasFocus ->
             email.isErrorEnabled = false
         }
         val b = findViewById<TextInputEditText>(R.id.b)
         b.setOnFocusChangeListener { v, hasFocus ->
             pass.isErrorEnabled = false
         }

         // Registrarse
         signbtn.setOnClickListener{
             // Te lleva a la pantalla de registro
             showSignIn()
         }
         // Olvidó su contraseña
         olvido.setOnClickListener{
             // Te lleva a la pantalla de registro
             showReset()
         }

         //inicio de sesión
         loginbtn.setOnClickListener{
            // Comprueba que los campos de texto no esten vacios
             if (email.editText!!.text.toString() != ""  && pass.editText!!.text.toString() != ""){

                val pssValidate = contra(pass.editText!!.text.toString())
                 if (pssValidate){
                     FirebaseAuth.getInstance().signInWithEmailAndPassword(
                             email.editText?.text.toString(),
                             pass.editText?.text.toString()).addOnCompleteListener{
                             if(it.isSuccessful){
                                 showHome(it.result?.user?.email?:"", "BASIC")
                             }else {
                                 showAlert()
                             }
                         }
                 } else {
                     pass.error = "La contraseña debe tener 6 caracteres mínimos"
                 }
             // Muestra un mensaje si no tienes nada escrito
             } else {
                    email.error = "Debe escribir su email"
                     pass.error = "Debe escribir la contraseña"
             }
         }

         //inicio de sesión con google
         googlebtn.setOnClickListener{
             //configuración
            /* val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestIdToken(getString(R.string.default_web_client_id))
                 .requestEmail()
                 .build()

             val googleClient  = GoogleSignIn.getClient(this,googleConf)

             startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)*/
            Toast.makeText(this,"Arreglando errores",Toast.LENGTH_SHORT).show()
         }


     }

     //contador
     private fun contra( pss:String): Boolean{
        return pss.length>=6
     }

     // Indica un error al acceder a la base de datos
     private fun showAlert(){
        val builder = AlertDialog.Builder(this)
         builder.setTitle("Error")
         builder.setMessage("Se ha producido un error autenticando al usuario")
         builder.setPositiveButton("Aceptar",null)
         val dialog: AlertDialog = builder.create()
         dialog.show()
     }

     // Te lleva a la pantalla principal
     @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
     private fun showHome(email: String, proveedor: String){

         db.collection("users").document(email).get().addOnSuccessListener {
             val prefs = getSharedPreferences(getString(R.string.pref_files), Context.MODE_PRIVATE)
             //Guardado de datos
             val ad = prefs.edit()

             val nombre = it.get("nombre") as String
             val age = it.get("edad") as String
             val contra = it.get("contraseña") as String
             val cadena = it.get("lista") as String
             val perfil = it.get("perfil") as String

             ad.putString("lista",cadena)
             ad.putString("email", email)
             ad.putString("contraseña", contra)
             ad.putString("edad", age)
             ad.putString("nombre", nombre)
             ad.putString("provedor", proveedor)
             ad.putString("perfil", perfil)
             ad.apply()
             Toast.makeText(this,"Guardado ${prefs.getString("nombre","")}",Toast.LENGTH_LONG).show()
             val homeIntent = Intent(this,principal::class.java)
             finish()
             startActivity(homeIntent)
         }
     }

     // Te lleva a la pantalla de registro
     private fun showSignIn(email: String = "", proveedor: String = "BASIC"){

        val signIn_intent = Intent(this,sign_in::class.java).apply {
            putExtra("email",email)
            putExtra("proveedor",proveedor)
        }
         startActivity(signIn_intent)
     }

     // Te lleva a la pantalla de registro
     private fun showReset(){

         val signIn_intent = Intent(this,forgot_password::class.java)
         startActivity(signIn_intent)
     }
     // Accion que realiza para recuperar los datos de google y usarlo en el registro o inicio directo de sesion
    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

         if (requestCode == GOOGLE_SIGN_IN) {
             val task = GoogleSignIn.getSignedInAccountFromIntent(data)
             try {
                 val account = task.getResult(ApiException::class.java)

                 if (account != null) {
                     val userEmail:String =account.email.toString()
                     val userName = account.displayName
                     db.collection("users").document(userEmail).get()
                         .addOnSuccessListener {
                             val nn:String? = it.getString("nombre") as String
                             if ( nn.equals(null)){
                                 val signIn_intent = Intent(this,sign_in::class.java).apply {
                                     putExtra("email",userEmail.toString())
                                     putExtra("proveedor","GOOGLE")
                                     putExtra("nombre",userName.toString())
                                 }
                                 Toast.makeText(this,"Si hay algo mal corrigelo y pon una contraseña por seguridad",Toast.LENGTH_LONG).show()
                                 startActivity(signIn_intent)
                             }
                             val array = listOf<String>(it.getString("contraseña")as String,it.getString("edad")as String)
                             array.forEach{
                                 Toast.makeText(this,it,Toast.LENGTH_SHORT).show()

                             }
                         //showHome(userEmail!!,"GOOGLE")
                        }.addOnFailureListener {


                        }
                 }
             } catch (e:ApiException) {
                 showAlert()
             }


         }
     }*/
}