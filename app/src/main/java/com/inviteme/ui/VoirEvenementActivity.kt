package com.inviteme.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import com.inviteme.R
import com.inviteme.databinding.ActivityAjouterEvenementBinding
import com.inviteme.databinding.ActivityVoirEvenementBinding
import com.inviteme.exception.EvenementNonTrouveException
import com.inviteme.vuemodel.VoirEvenementViewModel

class VoirEvenementActivity : AppCompatActivity() {

    // vuemodel
    private val vueModel: VoirEvenementViewModel by viewModels()

    private lateinit var binding: ActivityVoirEvenementBinding

    private lateinit var tvTitre : TextView
    private lateinit var tvDate: TextView
    private lateinit var tvAdresse: TextView
    private lateinit var tvDescription: TextView




    // variables xml nécéssaores

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVoirEvenementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialisation des variables
        tvTitre = binding.tvTitre
        tvDate = binding.tvDate
        tvAdresse = binding.tvAdresse
        tvDescription = binding.tvDescription


        // initialisation de l'évenement
        val evenementId:Int = intent.getIntExtra("evenementId", -1)

        if(evenementId == -1){
            Toast.makeText(this, "Erreur: événement introuvable", Toast.LENGTH_SHORT).show()
        }else{
            vueModel.chargerEvenement(
                evenementId,
                onSuccess = {
                    tvTitre.text = vueModel.evenement?.titre
                    tvDate.text = vueModel.evenement?.dateAjout.toString()
                    tvAdresse.text = vueModel.lieu?.adresse
                    tvDescription.text = vueModel.evenement?.description
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
            Toast.makeText(this, "id de l'event:: $evenementId ", Toast.LENGTH_SHORT).show()
        }



        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}