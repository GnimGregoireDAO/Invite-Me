package com.inviteme.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.inviteme.R
import com.inviteme.databinding.ActivityEvenementListBinding
import com.inviteme.model.database.AppDatabase
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu
import com.inviteme.model.repos.EvenementRepository
import com.inviteme.model.repos.LieuRepository
import com.inviteme.utils.LanguageHelper
import com.inviteme.viewmodel.EvenementViewModel
import com.inviteme.viewmodel.EvenementViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp

class EvenementListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEvenementListBinding
    private lateinit var evenementRepository: EvenementRepository
    private lateinit var lieuRepository: LieuRepository // Added
    private val evenementViewModel: EvenementViewModel by viewModels {
        EvenementViewModelFactory(evenementRepository)
    }
    // private var sampleDataCreated = false // Removed: data will be refreshed on each launch

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load and apply saved language before anything else, especially super.onCreate()
        val sharedPreferences = getSharedPreferences("GetTogetherPrefs", MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", null)
        if (savedLanguage != null && savedLanguage != LanguageHelper.getCurrentLanguage(this)) {
            LanguageHelper.changeLanguage(this, savedLanguage, false) // Don't recreate yet, activity will handle it
        }

        super.onCreate(savedInstanceState)
        binding = ActivityEvenementListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation du repository
        val evenementDao = AppDatabase.getDatabase(this).evenementDAO()
        val lieuDao = AppDatabase.getDatabase(this).lieuDao() // Added
        evenementRepository = EvenementRepository(evenementDao)
        lieuRepository = LieuRepository(lieuDao) // Added

        // Clear existing data and create fresh sample data on each launch
        clearAndCreateFreshSampleData()

        // Configurer le menu hamburger
        binding.btnMenu.setOnClickListener {
            showLanguageSelectionPopup()
        }

        binding.fabAjouterEvenement.setOnClickListener { // Added FAB click listener
            val intent = Intent(this, AjouterEvenementActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val adapter = EvenementAdapter(
            emptyList(),
            onEdit = { evenement ->
                // Naviguer vers l'écran de modification
                val intent = ModifierEvenementActivity.newIntent(this, evenement.id)
                startActivity(intent)
            },
            onDelete = { evenement ->
                showDeleteConfirmationDialog(evenement)
            },
            lieuRepository, // Pass lieuRepository
            lifecycleScope // Pass lifecycleScope for coroutines
        )
        binding.rvEvenements.layoutManager = LinearLayoutManager(this)
        binding.rvEvenements.adapter = adapter

        // Observe les evenements de la database
        evenementViewModel.evenements.observe(this, Observer<List<Evenement>> { events ->
            android.util.Log.d("EvenementListActivity", "Événements observés: ${events?.size ?: 0}")
            // Data is now managed by clearAndCreateFreshSampleData() on launch
            if (events != null) {
                android.util.Log.d("EvenementListActivity", "Mise à jour de l'adaptateur avec ${events.size} événements")
                adapter.updateEvents(events)
            }
        })
    }

    private fun clearAndCreateFreshSampleData() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // Clear existing data
                lieuRepository.deleteAllLieux()
                evenementRepository.deleteAllEvenements()
                android.util.Log.d("EvenementListActivity", "Anciennes données supprimées.")

                // Create sample Lieux first
                val lieux = mutableListOf<Lieu>()
                val pays = listOf("France", "USA", "Canada", "Allemagne", "Japon", "Brésil", "Australie")
                val villes = listOf("Paris", "New York", "Toronto", "Berlin", "Tokyo", "Rio", "Sydney")
                for (i in 1..7) {
                    lieux.add(Lieu(adresse = "${i * 100} Rue Principale, ${villes[i-1]}", pays = pays[i-1]))
                }
                val lieuIds = lieux.map { lieuRepository.saveLieu(it) }
                android.util.Log.d("EvenementListActivity", "${lieuIds.size} lieux de test créés.")


                val sampleEvents = mutableListOf<Evenement>()
                val titles = listOf(
                    "Conférence Tech Innovante", "Atelier Développement Kotlin", "Grand Meetup Android", "Hackathon IA & Futur", "Séminaire Cloud Computing Avancé",
                    "Webinar sur DevOps Pratiques", "Table Ronde UI/UX Design", "Cours Intensif Flutter", "Présentation Méthodes Agile", "Démo Interactive VR/AR",
                    "Forum International Cybersécurité", "Compétition eSport Majeure", "Exposition Robotique et Automatisation", "Lancement Exclusif Produit Alpha", "Soirée Networking Développeurs",
                    "Masterclass Big Data", "Symposium Blockchain", "Formation Sécurité Applicative", "Challenge Code & Café", "Pitch Night Startups"
                )
                val descriptions = listOf(
                    "Explorez les dernières innovations technologiques.",
                    "Maîtrisez Kotlin pour des applications robustes.",
                    "Connectez-vous avec la communauté Android locale.",
                    "Relevez des défis et construisez le futur avec l'IA.",
                    "Approfondissez vos connaissances sur les services cloud.",
                    "Optimisez vos cycles de développement et déploiement.",
                    "Discutez des meilleures pratiques en design d'interface.",
                    "Créez des applications natives performantes pour iOS et Android.",
                    "Adoptez l'agilité pour une gestion de projet efficace.",
                    "Vivez des expériences immersives révolutionnaires.",
                    "Protégez vos actifs numériques contre les cybermenaces.",
                    "Assistez à des affrontements épiques entre pro-gamers.",
                    "Découvrez les avancées en robotique et IA.",
                    "Soyez parmi les premiers à découvrir notre nouveau produit.",
                    "Échangez avec des professionnels et recruteurs du secteur.",
                    "Devenez un expert en analyse de données massives.",
                    "Comprendre les enjeux et applications de la Blockchain.",
                    "Apprenez à développer des applications sécurisées dès la conception.",
                    "Codez, débuggez et partagez autour d'un bon café.",
                    "Découvrez les idées novatrices des jeunes pousses."
                )
                val currentTime = System.currentTimeMillis()
                val dayInMillis = 24 * 60 * 60 * 1000

                for (i in 0 until 15) {
                    val lieuId = if (lieuIds.isNotEmpty()) lieuIds[i % lieuIds.size] else null
                    // Vary dates: some past, some future
                    val dateOffset = (i - 7) * dayInMillis // Events from 7 days ago to 7 days in future
                    val eventTime = currentTime + dateOffset
                    sampleEvents.add(
                        Evenement(
                            titre = titles[i % titles.size], // Cycle through titles if more events than titles
                            description = descriptions[i % descriptions.size], // Cycle through descriptions
                            dateAjout = Timestamp(eventTime),
                            dateModification = Timestamp(eventTime),
                            lieuId = lieuId
                        )
                    )
                }
                
                // Ajouter les événements à la base de données
                sampleEvents.forEach { event ->
                    try {
                        evenementRepository.ajouterEvenement(event)
                        android.util.Log.d("CreateSampleEvents", "Événement de test créé: ${event.titre}")
                    } catch (e: Exception) {
                        android.util.Log.e("CreateSampleEvents", "Erreur lors de la création de l'événement de test: ${e.message}")
                        e.printStackTrace()
                    }
                }
                android.util.Log.d("EvenementListActivity", "${sampleEvents.size} événements de test créés.")
            }
        }
    }

    private fun showLanguageSelectionPopup() {
        val popupMenu = PopupMenu(this, binding.btnMenu)
        val currentLang = LanguageHelper.getCurrentLanguage(this)
        val languages = mapOf("fr" to "Français", "en" to "English", "es" to "Español")

        languages.forEach { (code, name) ->
            if (code != currentLang) {
                popupMenu.menu.add(name).setOnMenuItemClickListener {
                    // Save selected language before changing it
                    val sharedPreferences = getSharedPreferences("GetTogetherPrefs", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("language", code)
                        apply()
                    }
                    LanguageHelper.changeLanguage(this, code, true) // Now recreate
                    true
                }
            }
        }
        popupMenu.show()
    }

    private fun showDeleteConfirmationDialog(evenement: Evenement) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message, evenement.titre))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                evenementViewModel.supprimerEvenement(evenement)
                Toast.makeText(this, getString(R.string.event_deleted_toast, evenement.titre), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_language, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // This menu is now redundant due to the hamburger menu language selection.
        // Kept for now, can be removed if desired.
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
