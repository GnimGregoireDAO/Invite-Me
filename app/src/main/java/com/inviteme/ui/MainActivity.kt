package com.inviteme.ui

// Importations nécessaires pour l'activité, les widgets, les thèmes, etc.
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.inviteme.R
import com.inviteme.viewmodel.ConnexionViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Appliquer le thème sombre ou clair selon les préférences enregistrées
        val prefs = getSharedPreferences("settings", MODE_PRIVATE) // accès aux préférences
        val isNightMode = prefs.getBoolean("night_mode", false) // récupère l'état du mode nuit
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // 2. Initialisation de l'activité
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // permet l’affichage plein écran (sans marges inutiles)
        setContentView(R.layout.activity_main) // lie l'activité à son layout XML

        // 3. Ajuster les marges pour éviter que le contenu ne soit coupé (en mode plein écran)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 4. Gérer le switch de thème dans l’interface (claire ↔ sombre)
        val switch = findViewById<SwitchCompat>(R.id.themeSwitch)
        switch.isChecked = isNightMode // applique l’état actuel au switch
        switch.setOnCheckedChangeListener { _, isChecked ->
            // sauvegarde le choix de l’utilisateur
            prefs.edit().putBoolean("night_mode", isChecked).apply()
            // applique le nouveau thème
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate() // recharge l'activité pour appliquer le thème immédiatement
        }

        // 5. Initialiser le ViewModel pour gérer la logique de connexion
        val viewModel = ViewModelProvider(this)[ConnexionViewModel::class.java]

        // 6. Récupérer les références aux champs email et mot de passe + bouton de connexion
        val emailEditText = findViewById<EditText>(R.id.nom_signup) // champ pour l’email
        val passwordEditText = findViewById<EditText>(R.id.confirm_password_login) // champ pour le mot de passe
        val loginButton = findViewById<Button>(R.id.signup_button) // bouton de connexion

        // 7. Gérer le clic sur le bouton de connexion
        loginButton.setOnClickListener {
            // Récupérer le contenu des champs
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Vérifie si un des champs est vide
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // stoppe la fonction ici
            }

            // Appel au ViewModel pour tenter de se connecter
            viewModel.connecter(
                email = email,
                password = password,
                // Si la connexion est un succès
                onSuccess = { utilisateur ->
                    Toast.makeText(this, "Connexion réussie ! Bienvenue ${utilisateur.prenom}", Toast.LENGTH_SHORT).show()
                    // Redirection possible vers une autre activité (à décommenter si besoin)
                    // startActivity(Intent(this, DashboardActivity::class.java))
                    finish() // termine cette activité
                },
                // Si une erreur se produit (email inexistant ou mot de passe incorrect)
                onError = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
