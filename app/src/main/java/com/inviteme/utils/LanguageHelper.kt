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
         * Obtient le code de langue actuel de la configuration de l'application pour un contexte donné.
         */
        fun getCurrentLanguage(context: Context): String {
            val currentLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                @Suppress("DEPRECATION")
                context.resources.configuration.locale
            }
            return currentLocale?.language ?: Locale.getDefault().language
        }

        /**
         * Obtient la langue actuelle du système
         * à utiliser si possible plus tard pour optimiser l\'internationalisation
         */
        fun getCurrentSystemLanguage(): String { // Renamed from getCurrentLanguage to avoid conflict
            return Locale.getDefault().language
        }

        /**
         * Obtient le nom complet de la langue actuelle dans la langue locale
         */
        fun getCurrentLanguageDisplayName(context: Context): String { // Added context parameter
            val currentLocale = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                context.resources.configuration.locales.get(0)
            } else {
                @Suppress("DEPRECATION")
                context.resources.configuration.locale
            }
            return currentLocale?.getDisplayLanguage(currentLocale) ?: Locale.getDefault().getDisplayLanguage(Locale.getDefault())
        }

        /**
         * Change la langue de l\'application pour la démonstration utiliser SharedPreferences plutard pour la persistance
         */
        fun changeLanguage(context: Context, languageCode: String, recreateActivity: Boolean = true) { // Added recreateActivity parameter
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
            
            // Si le contexte est une activité et recreateActivity est vrai, recréez-la pour appliquer les changements
            if (recreateActivity && context is AppCompatActivity) {
                context.recreate()
            }
            
        }
    }
}
