package com.cesfuencarral.bringpopcorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class sign_in : AppCompatActivity() {

    //referencia a la base de datos
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //setup
        setup()
    }

    fun setup(){
        title = "Registro"
        //variables
        val savebtn = findViewById<Button>(R.id.savebtn)
        val nombre = findViewById<TextInputLayout>(R.id.nameEt)
        val edad = findViewById<TextInputLayout>(R.id.ageEt)
        val email = findViewById<TextInputLayout>(R.id.emailET)
        val contra = findViewById<TextInputLayout>(R.id.passEt)
        val contra2 = findViewById<TextInputLayout>(R.id.pass2Et)

        //color
        nombre.boxBackgroundColor = getColor(R.color.pink_soft2)
        edad.boxBackgroundColor = getColor(R.color.pink_soft2)
        email.boxBackgroundColor = getColor(R.color.pink_soft2)
        contra.boxBackgroundColor = getColor(R.color.pink_soft2)
        contra2.boxBackgroundColor = getColor(R.color.pink_soft2)

        //borra el error cuando vas a corregirlo
        val a = findViewById<TextInputEditText>(R.id.a)
        a.setOnFocusChangeListener { v, hasFocus ->
            email.isErrorEnabled = false
        }
        val b = findViewById<TextInputEditText>(R.id.b)
        b.setOnFocusChangeListener { v, hasFocus ->
            nombre.isErrorEnabled = false
        }
        val c = findViewById<TextInputEditText>(R.id.c)
        c.setOnFocusChangeListener { v, hasFocus ->
            edad.isErrorEnabled = false
        }
        val d = findViewById<TextInputEditText>(R.id.d)
        d.setOnFocusChangeListener { v, hasFocus ->
            contra.isErrorEnabled = false
        }
        val e = findViewById<TextInputEditText>(R.id.e)
        e.setOnFocusChangeListener { v, hasFocus ->
            contra2.isErrorEnabled = false
        }

        //accion
        savebtn.setOnClickListener {

            //comprobamos que todo este escrito
            if (email.editText!!.text.isNotEmpty() &&
                contra.editText!!.text.isNotEmpty() &&
                nombre.editText!!.text.isNotEmpty() &&
                edad.editText!!.text.isNotEmpty() &&
                contra2.editText!!.text.isNotEmpty()
            ){
                val pssValidate = contra(contra.editText!!.text.toString())
                val pssValidate2 = contra(contra2.editText!!.text.toString())
                if (pssValidate && pssValidate2){
                    //comprobamos que se repite la contraseña
                    if (contra.editText!!.text.toString().equals(contra2.editText!!.text.toString())){
                        // Comprobamos que no existe el usuario
                        // Te registra el email y contraseña
                        FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email.editText!!.text.toString(),contra.editText!!.text.toString())
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    // Mostramos un mensaje por pantalla de que fue exitoso
                                    Toast.makeText(this,"Registro exitoso",Toast.LENGTH_LONG).show()
                                    //Añade a la coleccion los datos del usuario registrado
                                    db.collection("users")
                                        .document(email.editText!!.text.toString())
                                        .set(
                                        hashMapOf(
                                            "nombre" to nombre.editText!!.text.toString(),
                                            "edad" to edad.editText!!.text.toString(),
                                            "contraseña" to contra.editText!!.text.toString(),
                                        "lista" to "",
                                        "perfil" to "https://firebasestorage.googleapis.com/v0/b/bring-popcorn.appspot.com/o/perfil%2Fno%20soy%20un%20perro.jpg?alt=media&token=3eb99d9e-160c-44f1-8b1e-ad9d214a9ba3")
                                    )
                                    // Vuelve a inicio de sesión
                                    finish()
                                }else {
                                    // Avisa de que el email ya está en uso
                                    email.error = "Este email ya esta en uso"
                                }
                            }
                    } else {
                        // Mostramos un mensaje por pantalla  para que escriba la contraseña igual
                        contra2.error = "No coincide con la anterior contraseñas"
                    }
                }else if(!pssValidate){
                    // Mostramos un mensaje por pantalla  para que escriba la contraseña igual
                    contra.error = "Mínimo de 6 caracteres por favor"
                }else if (!pssValidate2){
                    // Mostramos un mensaje por pantalla  para que escriba la contraseña igual
                    contra2.error = "Mínimo de 6 caracteres por favor"
                }else if (!pssValidate && !pssValidate2){
                    contra.error = "Mínimo de 6 caracteres por favor"
                    contra2.error = "Mínimo de 6 caracteres por favor"
                }
             } else {
                val array = listOf<TextInputLayout>(nombre,edad,email,contra,contra2)
                 array.forEach{
                     if (it.editText!!.text.toString() == ""){
                        it.error = "Escribe tu ${it.hint.toString()}"
                     }
                 }
                // Mostramos un mensaje por pantalla  para que escriba todos los datos

            }
        }

    }
    //contador
    private fun contra( pss:String): Boolean{
        return pss.length>=6
    }
}