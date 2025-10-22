# Guide de Style AnimeRah

## ?? Palette de Couleurs

### Couleurs Principales
- **Rouge Principal** : `#ff6b6b` - Passion, énergie
- **Turquoise** : `#4ecdc4` - Calme, créativité
- **Jaune** : `#ffd93d` - Joie, optimisme
- **Vert** : `#6bcf7f` - Croissance, harmonie
- **Bleu** : `#45b7d1` - Confiance, stabilité

### Couleurs de Fond
- **Fond Principal** : `#ffffff`
- **Fond Secondaire** : `#f8fafc`
- **Fond Sombre** : `#1a1a2e`
- **Fond Plus Sombre** : `#16213e`

### Dégradés
- **Principal** : `linear-gradient(135deg, #ff6b6b, #4ecdc4)`
- **Secondaire** : `linear-gradient(135deg, #ffd93d, #6bcf7f)`
- **Sombre** : `linear-gradient(135deg, #1a1a2e, #16213e, #0f3460)`

## ?? Typographie

### Polices
- **Principale** : `Poppins` - Moderne, lisible
- **Secondaire** : `Inter` - Clean, fonctionnel
- **Monospace** : `JetBrains Mono` - Code

### Hiérarchie
- **H1** : 3rem (48px) - Titres principaux
- **H2** : 2.25rem (36px) - Titres de section
- **H3** : 1.875rem (30px) - Sous-titres
- **Body** : 1rem (16px) - Texte principal
- **Small** : 0.875rem (14px) - Texte secondaire

## ?? Logo et Branding

### Logo Principal
- **Fichier** : `/AnimeRahLogo.png`
- **Taille recommandée** : 45px × 45px (navbar)
- **Taille footer** : 50px × 50px
- **Effet** : Drop-shadow rouge #ff6b6b

### Utilisation du Logo
```jsx
// Dans la navbar
<img 
  src="/AnimeRahLogo.png" 
  alt="AnimeRah" 
  className="logo-image"
/>

// Avec texte
<div className="logo-container">
  <img src="/AnimeRahLogo.png" alt="AnimeRah" />
  <div className="logo-text-container">
    <span className="logo-text">AnimeRah</span>
    <span className="logo-subtitle">Votre passion anime</span>
  </div>
</div>
```

## ??? Composants Réutilisables

### Boutons
```jsx
// Bouton principal
<button className="btn btn-primary">
  <i className="fas fa-icon"></i>
  Texte
</button>

// Bouton secondaire
<button className="btn btn-secondary">Action</button>

// Bouton anime avec effet
<button className="btn btn-anime">Découvrir</button>
```

### Cartes
```jsx
// Carte avec effet brillant
<div className="card card-shine">
  <div className="card-header">
    <h3>Titre</h3>
  </div>
  <div className="card-body">
    <p>Contenu</p>
  </div>
</div>
```

### Badges
```jsx
<span className="badge badge-primary">Nouveau</span>
<span className="badge badge-success">Publié</span>
```

## ?? Effets et Animations

### Effet de Brillance (Shine)
```css
.card-shine::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s;
}

.card-shine:hover::before {
  left: 100%;
}
```

### Animations au Hover
- **Boutons** : `translateY(-2px)` + shadow
- **Cartes** : `translateY(-4px)` + shadow élargie
- **Logo** : `scale(1.05)` + rotation légère

### Transitions
- **Rapide** : `0.15s ease`
- **Normale** : `0.3s ease`
- **Lente** : `0.5s ease`

## ?? Responsive Design

### Breakpoints
- **Mobile** : `max-width: 640px`
- **Tablette** : `max-width: 768px`
- **Desktop** : `max-width: 1024px`
- **Large** : `max-width: 1280px`

### Adaptations Mobile
```css
@media (max-width: 768px) {
  .logo-text {
    font-size: 20px;
  }
  
  .logo-image {
    width: 35px;
    height: 35px;
  }
  
  .desktop-nav {
    display: none;
  }
}
```

## ?? Variables CSS

### Utilisation
```css
/* Couleurs */
color: var(--primary-color);
background: var(--primary-gradient);

/* Espacements */
padding: var(--spacing-md);
margin: var(--spacing-lg);

/* Ombres */
box-shadow: var(--shadow-anime);

/* Transitions */
transition: all var(--transition-normal);
```

## ?? Checklist Design

### ? Éléments à vérifier
- [ ] Logo AnimeRah utilisé partout
- [ ] Couleurs cohérentes avec la palette
- [ ] Dégradés appliqués aux éléments principaux
- [ ] Animations fluides (hover, focus)
- [ ] Responsive sur tous les écrans
- [ ] Font Awesome pour les icônes
- [ ] Effets de brillance sur les cardes
- [ ] Ombres et profondeur appropriées

### ?? À éviter
- ? Couleurs hors palette
- ? Polices différentes de Poppins/Inter
- ? Boutons sans hover effects
- ? Images sans border-radius
- ? Textes non lisibles
- ? Animations trop rapides/lentes

## ?? Exemples d'Application

### Page d'Accueil
- Hero avec dégradé sombre et particules
- Cartes avec effet shine
- Boutons avec animations
- Stats avec compteurs animés

### Articles
- Cards avec images arrondies
- Badges de catégories colorés
- Boutons "Lire plus" avec effet
- Métadonnées avec icônes

### Navigation
- Logo avec effet hover
- Menu avec underline animé
- Dropdown avec backdrop-filter
- Mobile menu avec hamburger animé

## ?? Outils et Ressources

### Couleurs
- [Coolors.co](https://coolors.co) pour générer des palettes
- Extension VSCode : Color Highlight

### Polices
- [Google Fonts](https://fonts.google.com/specimen/Poppins)
- Poppins : weights 300, 400, 500, 600, 700, 800

### Icônes
- [Font Awesome](https://fontawesome.com/icons)
- Version 6.0.0 ou plus récente

### Animations
- [Framer Motion](https://www.framer.com/motion/) pour React
- CSS animations custom pour les effets

---

**Maintenu par** : RanymH  
**Version** : 1.0  
**Dernière mise à jour** : Septembre 2024