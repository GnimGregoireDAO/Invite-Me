package com.inviteme.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.inviteme.R
import com.inviteme.databinding.ActivityAjouterEvenementBinding
import com.inviteme.exception.EvenementNonConformeException
import com.inviteme.states.EvenmentState
import com.inviteme.states.LieuState
import com.inviteme.vuemodel.AjouterEvementVueModel
import java.sql.Timestamp

class AjouterEvenementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjouterEvenementBinding


    // vue model:
    private val vueModel: AjouterEvementVueModel by viewModels()


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


    @SuppressLint("SuspiciousIndentation")
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


        //vueModel = AjouterEvementVueModel(EvenmentState(), LieuState(), )

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btnAjouter.setOnClickListener{
            //Toast.makeText(this, "bouton ajouté cliquer" , Toast.LENGTH_SHORT).show()
            try {
            // initialisation du vuemodel.eventState
            vueModel.evenementState.titre = titreEventEntry.text.toString()
            vueModel.evenementState.type = eventTypeEntry.text.toString()
            vueModel.evenementState.description = descriptionEntry.text.toString()
            vueModel.lieuState.adresse = adresseEntry.text.toString()
            vueModel.evenementState.lieu = null
            vueModel.evenementState.date = Timestamp(getTimestampFromPickers())
            Toast.makeText(this, vueModel.evenementState.date.toString() , Toast.LENGTH_SHORT).show()
            vueModel.evenementState.dateModification = Timestamp(getTimestampFromPickers()) // donc oui, ce n'est pas la date de modification haha

                vueModel.ajouterEvenement(
                    onSuccess = {
                        Toast.makeText(this, "Nouvel evenement ajouté" , Toast.LENGTH_SHORT).show()
                    },
                    onError = { errorMessage ->
                        //Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        btnAjouter.text = errorMessage
                    }
                )
            }catch (error: Exception){
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Nouvel evenement ajouté" , Toast.LENGTH_SHORT).show()
        }


        // l'orsqu'on click sur annuler, on reviens vers la page de liste evenement
        binding.btnAnnuler.setOnClickListener{
            val intent = Intent(this, EvenementListActivity::class.java)

            startActivity(intent)
        }
    }

    private fun getTimestampFromPickers(): Long {
        // Récupération des valeurs des pickers
        val year = dateEntry.year
        val month = dateEntry.month + 1 // DatePicker mois 0-11 → converti en 1-12
        val day = dateEntry.dayOfMonth

        val hour = timePicker.hour
        val minute = timePicker.minute

        // Création du Calendar et conversion en timestamp
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // On reconvertit en format Calendar (0-11)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calendar.timeInMillis
    }



}