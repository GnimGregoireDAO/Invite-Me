package com.inviteme.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.inviteme.R
import com.inviteme.databinding.ActivityEvenementListBinding
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Evenement
import com.inviteme.model.repos.EvenementRepository
import com.inviteme.utils.LanguageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class EvenementListActivity : AppCompatActivity() {
      private lateinit var binding: ActivityEvenementListBinding
    private lateinit var evenementRepository: EvenementRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvenementListBinding.inflate(layoutInflater)
        setContentView(binding.root)
          
        // Initialisation du repository
        val evenementDao = AppDatabase.getDatabase(this).evenementDAO()
        evenementRepository = EvenementRepository(evenementDao)
        
        // Configurer le menu hamburger
        binding.btnMenu.setOnClickListener {
            // Affiche un message indiquant la langue actuelle de l'application
            val currentLanguage = LanguageHelper.getCurrentLanguageDisplayName()
            Toast.makeText(this, getString(R.string.current_language, currentLanguage), Toast.LENGTH_SHORT).show()
        }
        
        setupRecyclerView()
    }    private fun setupRecyclerView() {
        // Configure le RecyclerView avec adaptateur vide
        val adapter = EvenementAdapter(emptyList())
        binding.rvEvenements.layoutManager = LinearLayoutManager(this)
        binding.rvEvenements.adapter = adapter
        
        // Observe les evenements de la database
        evenementRepository.getAll().observe(this, Observer { events ->
            // If database is empty, create sample events
            if (events.isEmpty()) {
                createSampleEvents()
            } else {
                // Update adapter with events from database
                (binding.rvEvenements.adapter as EvenementAdapter).updateEvents(events)
            }
        })
    }
      private fun createSampleEvents() {
        // Utiliser lifecycleScope pour lancer une coroutine liée au cycle de vie de l'activité
        lifecycleScope.launch {
            // Utiliser withContext(Dispatchers.IO) pour exécuter les opérations de base de données
            withContext(Dispatchers.IO) {
                val sampleEvents = listOf(
                    Evenement(0, "Formation avancée", "Formation sur les technologies mobiles", 
                            Timestamp(System.currentTimeMillis()), 
                            Timestamp(System.currentTimeMillis()), 1),
                    Evenement(0, "Atelier débutant", "Atelier d'introduction à Android", 
                            Timestamp(System.currentTimeMillis() + 86400_000), 
                            Timestamp(System.currentTimeMillis() + 86400_000), 1),
                    Evenement(0, "Soirée quiz", "Soirée conviviale avec quiz technique", 
                            Timestamp(System.currentTimeMillis() + 2 * 86400_000), 
                            Timestamp(System.currentTimeMillis() + 2 * 86400_000), 1)
                )
                
                // Ajouter les événements à la base de données
                sampleEvents.forEach { event ->
                    try {
                        evenementRepository.ajouterEvenement(event)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_language, menu)
        return true
    }
      override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language_fr -> {
                LanguageHelper.changeLanguage(this, "fr")
                Toast.makeText(this, getString(R.string.language_changed, "Français"), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_language_en -> {
                LanguageHelper.changeLanguage(this, "en")
                Toast.makeText(this, getString(R.string.language_changed, "English"), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_language_es -> {
                LanguageHelper.changeLanguage(this, "es")
                Toast.makeText(this, getString(R.string.language_changed, "Español"), Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
