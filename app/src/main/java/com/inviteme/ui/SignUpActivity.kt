package com.inviteme.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.inviteme.R

class SignUpActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Récupération des vues
        val username = findViewById<EditText>(R.id.prenom_signup)
        val regbtn = findViewById<MaterialButton>(R.id.signup_button)

        // Listener du bouton
        regbtn.setOnClickListener {
            val usernameText = username.text.toString()
            Toast.makeText(this, "Username is $usernameText", Toast.LENGTH_SHORT).show()
        }
    }
}