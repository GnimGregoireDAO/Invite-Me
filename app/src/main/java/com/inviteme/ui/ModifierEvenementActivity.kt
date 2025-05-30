package com.inviteme.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.inviteme.databinding.ActivityModifierEvenementBinding
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.repos.EvenementRepository
import com.inviteme.viewmodel.EvenementViewModel
import com.inviteme.viewmodel.EvenementViewModelFactory
import java.sql.Timestamp

class ModifierEvenementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifierEvenementBinding
    private lateinit var evenementRepository: EvenementRepository
    private val evenementViewModel: EvenementViewModel by viewModels {
        EvenementViewModelFactory(evenementRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifierEvenementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val evenementDao = AppDatabase.getDatabase(this).evenementDAO()
        evenementRepository = EvenementRepository(evenementDao)

        val evenementId = intent.getIntExtra(EXTRA_EVENEMENT_ID, -1)
        if (evenementId == -1) {
            Toast.makeText(this, "Erreur: événement introuvable", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        evenementViewModel.getEvenementById(evenementId) { evenement ->
            if (evenement == null) {
                Toast.makeText(this, "Erreur: événement introuvable", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.etTitre.setText(evenement.titre)
                binding.etDescription.setText(evenement.description ?: "")
                binding.etDateAjout.setText(evenement.dateAjout.toString())
                binding.etDateModification.setText(evenement.dateModification.toString())
                binding.etLieuId.setText(evenement.lieuId?.toString() ?: "")

                binding.btnSave.setOnClickListener {
                    val updated = evenement.copy(
                        titre = binding.etTitre.text.toString(),
                        description = binding.etDescription.text.toString(),
                        dateAjout = Timestamp.valueOf(binding.etDateAjout.text.toString()),
                        dateModification = Timestamp.valueOf(binding.etDateModification.text.toString()),
                        lieuId = binding.etLieuId.text.toString().toLong()
                    )
                    evenementViewModel.updateEvenement(updated) {
                        runOnUiThread {
                            Toast.makeText(this, getString(com.inviteme.R.string.event_updated), Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
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
