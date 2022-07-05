package com.cesfuencarral.bringpopcorn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class help : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        val back = findViewById<Button>(R.id.back)
        val ayuda = findViewById<TextView>(R.id.helptv)
        ayuda.text = getText(R.string.helpcode)
        back.setOnClickListener {
            onBackPressed()
        }

    }
}