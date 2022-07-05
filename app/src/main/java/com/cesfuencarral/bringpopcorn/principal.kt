package com.cesfuencarral.bringpopcorn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.cesfuencarral.bringpopcorn.ui.main.SectionsPagerAdapter

class principal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.pink_soft))
        tabs.setupWithViewPager(viewPager)
        session()
    }

    private fun session() {
        // Variables
        val prefs = getSharedPreferences(getString(R.string.pref_files), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        val provider = prefs.getString("provedor",null)
        val name: String? = prefs.getString("nombre",null)
        // Comprobamos que los datos no son nulos
        if (email != null && provider != null) {
            val authLayout = findViewById<LinearLayout>(R.id.authLayout)
            // Quitamos esta pantalla para que directamente inicie sesión
            Toast.makeText(this,"Bienvenido $name ",Toast.LENGTH_LONG).show()
        }else {
            val intent = Intent(this, AuthActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}