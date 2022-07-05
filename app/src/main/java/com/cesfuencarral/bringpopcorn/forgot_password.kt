package com.cesfuencarral.bringpopcorn

import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class forgot_password : AppCompatActivity() {

    private val fire: FirebaseAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        //variables
        val email = findViewById<TextInputLayout>(R.id.emaileEdit)
        val boton = findViewById<Button>(R.id.reset)
        email.boxBackgroundColor = getColor(R.color.pink_soft2)

        // Tratamos de enviar el email para su posterior cambio
        boton.setOnClickListener {
            val dialog = ProgressDialog(this)

            if (email.editText?.text?.toString() != ""){
                dialog.setMessage("Espere un momento...")
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
                reset(email.editText?.text?.toString())
            }else {
                email.error = "Debe escribir su email!!"
            }
            dialog.dismiss()

        }
    }

    //envio de email
    private fun reset(em:String?) {
        // Estabecemos el idioma y tratamos de enviarlo
        fire.setLanguageCode("es")
        fire.sendPasswordResetEmail(em.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast
                        .makeText(
                            this,
                            "Se ha enviado el correo para restablecer la contraseña",
                            Toast.LENGTH_LONG)
                        .show()

                }else{
                    Toast
                        .makeText(this,
                            "No se pudo enviar el correo",
                            Toast.LENGTH_LONG)
                        .show()
                }

            }


    }
}