package com.inviteme.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.inviteme.R
import com.inviteme.viewmodel.InscriptionViewModel


class SignUpActivity : AppCompatActivity() {

    // Déclaration du ViewModel associé à cette activité.
    // 'by viewModels()' est une délégation qui crée et gère le ViewModel automatiquement.
    // Ici, on utilise InscriptionViewModel (celui qui contient la logique d’inscription).
    private val viewModel: InscriptionViewModel by viewModels()

    @SuppressLint("WrongViewCast") // Ignore un avertissement lié au cast des vues (pas important ici)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Charge le layout XML 'activity_sign_up' qui contient l'interface utilisateur
        setContentView(R.layout.activity_sign_up)

        // Permet de gérer correctement les marges pour les zones système (barres, notch)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Récupère les zones occupées par les barres système (haut, bas, gauche, droite)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Ajoute un padding à la vue principale pour éviter que le contenu soit caché derrière les barres
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)

            insets // retourne les insets pour qu’ils soient consommés
        }

        // Récupère les références des éléments graphiques du formulaire dans le layout
        val prenomEditText = findViewById<EditText>(R.id.prenom_signup)
        val emailEditText = findViewById<EditText>(R.id.email_signup)
        val passwordEditText = findViewById<EditText>(R.id.password_signup)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirm_password_login)
        val termsCheckBox = findViewById<CheckBox>(R.id.terms_checkbox)
        val signupButton = findViewById<MaterialButton>(R.id.signup_button)

        // Définit le comportement au clic sur le bouton d’inscription
        signupButton.setOnClickListener {
            // Récupère les textes entrés dans les EditText, en supprimant les espaces inutiles
            val prenom = prenomEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val termsAccepted = termsCheckBox.isChecked

            // --- Validation côté interface utilisateur ---

            // Vérifie que tous les champs sont remplis
            if (prenom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // Affiche un message d’erreur si un champ est vide
                Toast.makeText(this, "Merci de remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // On arrête le traitement ici
            }

            // Vérifie que les mots de passe sont identiques
            if (password != confirmPassword) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vérifie que l’utilisateur a accepté les conditions via la checkbox
            if (!termsAccepted) {
                Toast.makeText(this, "Vous devez accepter les conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- Appel au ViewModel pour traiter l’inscription ---

            viewModel.inscrire(
                prenom = prenom,
                email = email,
                password = password,
                // Callback appelé si l’inscription réussit
                onSuccess = {
                    Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show()
                    finish() // Ferme cette activité pour revenir à l’écran précédent
                },
                // Callback appelé en cas d’erreur (ex : email déjà utilisé)
                onError = { messageErreur ->
                    Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show()
                }
            )
        }
    }
}
