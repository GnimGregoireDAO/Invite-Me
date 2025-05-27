package com.inviteme.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Classe utilitaire pour aider avec les opérations liées à la langue et à l'internationalisation
 */
class LanguageHelper {
    companion object {
        /**
         * Obtient la langue actuelle du système
         * à utiliser si possible plus tard pour optimiser l'internationalisation
         */
        fun getCurrentLanguage(): String {
            return Locale.getDefault().language
        }

        /**
         * Obtient le nom complet de la langue actuelle dans la langue locale
         */
        fun getCurrentLanguageDisplayName(): String {
            return Locale.getDefault().getDisplayLanguage(Locale.getDefault())
        }

        /**
         * Change la langue de l'application pour la démonstration utiliser SharedPreferences plutard pour la persistance
         */
        fun changeLanguage(context: Context, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            
            val resources = context.resources
            val config = Configuration(resources.configuration)
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                config.setLocale(locale)
                config.setLocales(android.os.LocaleList(locale))
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
            }
            
            context.resources.updateConfiguration(config, resources.displayMetrics)
            
            // Si le contexte est une activité, recréez-la pour appliquer les changements
            if (context is AppCompatActivity) {
                context.recreate()
            }
            
        }
    }
}
