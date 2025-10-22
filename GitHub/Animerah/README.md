# AnimeRah - Plateforme de Blog Anime & Manga

AnimeRah est une plateforme moderne dédiée aux passionnés d'anime et de manga, offrant un espace pour découvrir, analyser et discuter de l'univers de l'animation japonaise.

## ?? Design & Identité Visuelle

Le design d'AnimeRah s'inspire directement du logo de la plateforme, créant une identité visuelle cohérente et moderne :

### Palette de Couleurs
- **Gradient Principal** : `#667eea` ? `#764ba2`
- **Couleurs Primaires** : Violet dégradé inspiré du logo
- **Couleurs Secondaires** : Blanc, gris avec teintes violettes
- **Couleurs d'État** : Vert (succès), Rouge (erreur), Orange (attention)

### Typographie
- **Police Principale** : Noto Sans JP (pour l'inspiration japonaise)
- **Police Secondaire** : Roboto (pour la lisibilité)

### Style Visuel
- **Inspiration Anime** : Éléments visuels inspirés de l'esthétique anime/manga
- **Cartes Modernes** : Design card-based avec ombres et gradients
- **Animations Fluides** : Transitions et animations subtiles
- **Responsive Design** : Adapté à tous les écrans

## ?? Fonctionnalités

### Frontend (React + Vite)
- **Authentification Complète** : Connexion, inscription, gestion de profil
- **Gestion d'Articles** : Lecture, filtrage, recherche
- **Système de Discussions** : BigTalk pour échanges communautaires
- **Interface Admin** : Gestion articles, utilisateurs, demandes
- **Design Responsif** : Mobile-first approach

### Backend (ASP.NET Core)
- **API RESTful** : Architecture moderne et scalable
- **Authentification JWT** : Sécurité robuste
- **Entity Framework** : ORM pour base de données
- **Système de Rôles** : Admin/Utilisateur
- **Validation Complète** : Sécurité des données

## ??? Technologies Utilisées

### Frontend
- **React 18** avec Hooks
- **Vite** pour le build rapide
- **React Router** pour la navigation
- **Framer Motion** pour les animations
- **Axios** pour les appels API
- **CSS Variables** pour le theming

### Backend
- **ASP.NET Core 9**
- **Entity Framework Core**
- **SQL Server**
- **JWT Authentication**
- **AutoMapper**
- **FluentValidation**

## ?? Structure du Projet

```
Animerah/
??? animerah.client/                 # Application React
?   ??? src/
?   ?   ??? components/             # Composants réutilisables
?   ?   ?   ??? Navbar.jsx         # Navigation principale
?   ?   ?   ??? Footer.jsx         # Pied de page
?   ?   ?   ??? admin/             # Composants admin
?   ?   ??? pages/                 # Pages de l'application
?   ?   ?   ??? Home.jsx          # Page d'accueil
?   ?   ?   ??? Articles.jsx      # Liste articles
?   ?   ?   ??? Login.jsx         # Connexion
?   ?   ?   ??? Admin.jsx         # Panel admin
?   ?   ??? contexts/             # Contextes React
?   ?   ??? services/             # Services API
?   ?   ??? index.css             # Styles globaux
?   ??? public/                   # Assets statiques
??? Animerah.Server/             # API ASP.NET Core
    ??? Controllers/             # Contrôleurs API
    ??? Models/                 # Modèles de données
    ??? Services/               # Services métier
    ??? DTOs/                   # Objects de transfert
    ??? Data/                   # Contexte base de données
```

## ?? Installation & Démarrage

### Prérequis
- **.NET 9 SDK**
- **Node.js 18+**
- **SQL Server** (ou SQL Server Express)

### Configuration Backend
1. Clonez le repository
2. Configurez la chaîne de connexion dans `appsettings.json`
3. Exécutez les migrations :
   ```bash
   dotnet ef database update
   ```
4. Démarrez l'API :
   ```bash
   dotnet run --project Animerah.Server
   ```

### Configuration Frontend
1. Installez les dépendances :
   ```bash
   cd animerah.client
   npm install
   ```
2. Démarrez le serveur de développement :
   ```bash
   npm run dev
   ```

## ?? Système de Design

Le projet utilise un système de design basé sur des **variables CSS** pour assurer la cohérence :

```css
:root {
  /* Couleurs principales du logo */
  --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --primary-color: #667eea;
  --secondary-color: #764ba2;
  
  /* Typographie inspirée du style anime */
  --font-family-primary: 'Noto Sans JP', sans-serif;
  
  /* Espacements cohérents */
  --spacing-sm: 0.5rem;
  --spacing-md: 1rem;
  --spacing-lg: 1.5rem;
  --spacing-xl: 2rem;
}
```

### Composants Stylisés
- **Boutons** : Effets de brillance et animations
- **Cartes** : Design moderne avec gradients
- **Formulaires** : Inputs stylisés avec focus states
- **Navigation** : Design glassmorphism

## ?? Utilisateur par Défaut

Un compte administrateur est créé automatiquement :
- **Email** : `admin@animerah.com`
- **Mot de passe** : `Admin123!`

## ?? Fonctionnalités Clés

### Pour les Utilisateurs
- ? Inscription/Connexion sécurisée
- ? Lecture d'articles avec filtres
- ? Profil utilisateur personnalisé
- ? Participation aux discussions BigTalk
- ? Interface responsive et moderne

### Pour les Administrateurs
- ? Gestion complète des articles
- ? Modération des utilisateurs
- ? Validation des demandes BigTalk
- ? Dashboard avec statistiques
- ? Interface d'administration intuitive

## ?? Inspiration & Style

Le design s'inspire de l'esthétique anime/manga moderne :
- **Couleurs vibrantes** du logo AnimeRah
- **Typographie japonaise** (Noto Sans JP)
- **Animations fluides** rappelant l'animation
- **Éléments visuels** inspirés des interfaces d'anime
- **Particules flottantes** pour l'immersion

## ?? Responsive Design

L'application est entièrement responsive :
- **Mobile First** : Optimisé pour mobile
- **Tablette** : Interface adaptée
- **Desktop** : Expérience complète
- **Navigation** : Menu hamburger sur mobile

## ?? Sécurité

- **JWT Authentication** : Tokens sécurisés
- **Validation côté serveur** : Protection des données
- **Hachage des mots de passe** : Sécurité utilisateur
- **Protection CORS** : API sécurisée
- **Validation frontend** : UX améliorée

## ?? Développements Futurs

- [ ] Système de notifications en temps réel
- [ ] Upload d'images pour articles
- [ ] Système de votes/likes
- [ ] Mode sombre complet
- [ ] PWA (Progressive Web App)
- [ ] Multilingue (JP/EN/FR)

## ?? Contributeurs

**RanymH** - Créateur et développeur principal
- Passionné d'anime et de développement web
- Vision créative et technique du projet

## ?? Contact

- **Email** : admin@animerah.com
- **TikTok** : [@ranymh0](https://www.tiktok.com/@ranymh0)
- **Localisation** : Lomé, Togo

## ?? Licence

Ce projet est développé avec ?? pour la communauté anime francophone.

---

*"Que tu sois fan d'anime ou amateur de web, bienvenue sur AnimeRah !"* - RanymH