package com.inviteme.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.inviteme.R
import com.inviteme.databinding.ActivityAjouterEvenementBinding

class AjouterEvenementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterEvenementBinding


    // vue model:



    // ui elements qui concernent l'interface
    private lateinit var titreEventEntry : TextInputEditText
    private lateinit var eventTypeEntry: TextInputEditText
    private lateinit var adresseEntry :TextInputEditText
    private lateinit var descriptionEntry :EditText
    private lateinit var dateEntry :DatePicker
    private lateinit var timePicker :TimePicker

    private lateinit var btnAjouter: Button
    private lateinit var btnAnnuler: Button


    // tag
    private val TAG = "AJOUTER_EVENEMENT"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAjouterEvenementBinding.inflate(layoutInflater) // plug du lien avec la vue xml;
        setContentView(binding.root)

        // assignation des variables:

        titreEventEntry = binding.tvTitre
        eventTypeEntry = binding.tvTypeEvenement
        adresseEntry = binding.tvLieuEvenemnt
        descriptionEntry = binding.edittextDescription
        dateEntry = binding.datepickerDate
        timePicker = binding.timepickerTemp

        btnAjouter = binding.btnAjouter
        btnAnnuler = binding.btnAnnuler



        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnAjouter.setOnClickListener{
            Toast.makeText(this, "bouton ajouté cliquer" , Toast.LENGTH_SHORT).show()
        }

        binding.btnAnnuler.setOnClickListener{
            Toast.makeText(this, "bouton ajouter cliquer" , Toast.LENGTH_SHORT).show()
        }
    }
}