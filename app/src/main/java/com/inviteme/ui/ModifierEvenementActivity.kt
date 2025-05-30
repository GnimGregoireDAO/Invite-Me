package com.inviteme.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.inviteme.R
import com.inviteme.databinding.ActivityModifierEvenementBinding
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu // Added import
import com.inviteme.model.repos.EvenementRepository
import com.inviteme.model.repos.LieuRepository // Added import
import com.inviteme.viewmodel.EvenementViewModel
import com.inviteme.viewmodel.EvenementViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class ModifierEvenementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifierEvenementBinding
    private lateinit var evenementRepository: EvenementRepository
    private lateinit var lieuRepository: LieuRepository // Added
    private lateinit var evenementViewModel: EvenementViewModel
    // private var currentLieu: Lieu? = null // To store the loaded or new lieu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifierEvenementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val evenementDao = AppDatabase.getDatabase(this).evenementDAO()
        val lieuDao = AppDatabase.getDatabase(this).lieuDao() // Added
        evenementRepository = EvenementRepository(evenementDao)
        lieuRepository = LieuRepository(lieuDao) // Added
        val factory = EvenementViewModelFactory(evenementRepository) // ViewModel factory might need adjustment if LieuViewModel is used
        evenementViewModel = factory.create(EvenementViewModel::class.java)

        val evenementId = intent.getIntExtra(EXTRA_EVENEMENT_ID, -1)
        if (evenementId == -1) {
            Toast.makeText(this, "Erreur: événement introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadEvenementAndLieu(evenementId) // Renamed and logic will be updated
    }

    private fun loadEvenementAndLieu(evenementId: Int) {
        lifecycleScope.launch {
            try {
                val evenement = withContext(Dispatchers.IO) {
                    evenementRepository.getEvenementById(evenementId)
                }
                
                if (evenement == null) {
                    Toast.makeText(this@ModifierEvenementActivity, "Erreur: événement introuvable", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    binding.etTitre.setText(evenement.titre)
                    binding.etDescription.setText(evenement.description ?: "")
                    binding.etDateAjout.setText(evenement.dateAjout.toString().split(" ")[0])
                    binding.etDateModification.setText(evenement.dateModification.toString().split(" ")[0])

                    // Load Lieu details if lieuId exists
                    evenement.lieuId?.let { lieuId ->
                        val lieu = withContext(Dispatchers.IO) {
                            lieuRepository.getLieuById(lieuId.toInt())
                        }
                        if (lieu != null) {
                            // currentLieu = lieu // Store current lieu
                            binding.etLieuAdresse.setText(lieu.adresse)
                            binding.etLieuPays.setText(lieu.pays)
                        } else {
                            // Handle case where lieuId is present but Lieu not found (optional)
                            binding.etLieuAdresse.setText("")
                            binding.etLieuPays.setText("")
                        }
                    } ?: run {
                        // currentLieu = null // No lieu associated
                        binding.etLieuAdresse.setText("")
                        binding.etLieuPays.setText("")
                    }

                    binding.btnSave.setOnClickListener {
                        saveEvenementAndLieu(evenement) // Renamed and logic will be updated
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ModifierEvenementActivity, "Erreur lors du chargement: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun saveEvenementAndLieu(originalEvenement: Evenement) {
        lifecycleScope.launch {
            try {
                val adresse = binding.etLieuAdresse.text.toString()
                val pays = binding.etLieuPays.text.toString()
                var newLieuId: Long? = originalEvenement.lieuId // Keep original lieuId by default

                // Save or update Lieu if details are provided
                if (adresse.isNotBlank() && pays.isNotBlank()) {
                    // Attempt to find existing Lieu first or create a new one.
                    // For simplicity, we'll create a new one or update based on originalEvenement.lieuId.
                    // A more robust approach would be to check if a Lieu with the same address/pays already exists.
                    val lieuToSave = originalEvenement.lieuId?.let {
                        // If there was an original lieu, fetch it to update, or create new if not found
                        val existingLieu = lieuRepository.getLieuById(it.toInt())
                        existingLieu?.copy(adresse = adresse, pays = pays) ?: Lieu(adresse = adresse, pays = pays)
                    } ?: Lieu(adresse = adresse, pays = pays) // No original lieuId, create new

                    newLieuId = withContext(Dispatchers.IO) {
                        lieuRepository.saveLieu(lieuToSave) // saveLieu handles insert or update
                    }
                } else if (originalEvenement.lieuId != null && (adresse.isBlank() || pays.isBlank())) {
                    // If details are cleared and there was an original lieu, we might want to disassociate or delete it.
                    // For now, we'll just set newLieuId to null, effectively disassociating.
                    // Deleting the Lieu object if no other events reference it would be a more complex operation.
                    newLieuId = null
                }


                val updatedEvenement = originalEvenement.copy(
                    titre = binding.etTitre.text.toString(),
                    description = binding.etDescription.text.toString(),
                    dateModification = Timestamp(System.currentTimeMillis()),
                    lieuId = newLieuId
                )

                withContext(Dispatchers.IO) {
                    evenementRepository.updateEvenement(updatedEvenement)
                }
                
                Toast.makeText(this@ModifierEvenementActivity, "Événement modifié avec succès", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@ModifierEvenementActivity, "Erreur lors de la sauvegarde: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val EXTRA_EVENEMENT_ID = "evenement_id"
        fun newIntent(context: Context, evenementId: Int): Intent {
            val intent = Intent(context, ModifierEvenementActivity::class.java)
            intent.putExtra(EXTRA_EVENEMENT_ID, evenementId)
            return intent
        }
    }
}
