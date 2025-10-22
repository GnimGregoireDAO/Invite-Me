document.addEventListener("DOMContentLoaded", function () {
    const navLinks = document.querySelectorAll(".navbar-nav li a");

    // Récupérer l'URL actuelle de la page
    const currentPath = window.location.pathname.split("/").pop(); // Nom du fichier

    // Vérifier si une page est déjà stockée dans localStorage
    let storedPage = localStorage.getItem("activeNav");

    // Si aucune page n'est stockée, on met la page actuelle
    if (!storedPage) {
        storedPage = currentPath;
        localStorage.setItem("activeNav", storedPage);
    }

    // Supprimer la classe active de tous les liens
    navLinks.forEach(link => link.parentElement.classList.remove("active"));

    // Ajouter la classe active au bon élément
    navLinks.forEach(link => {
        if (link.getAttribute("href") === storedPage) {
            link.parentElement.classList.add("active");
        }
    });

    // Gérer le clic sur les liens
    navLinks.forEach(link => {
        link.addEventListener("click", function (event) {
            // Supprimer la classe active de tous les éléments
            navLinks.forEach(link => link.parentElement.classList.remove("active"));

            // Ajouter la classe active au lien cliqué
            this.parentElement.classList.add("active");

            // Stocker la page actuelle
            localStorage.setItem("activeNav", this.getAttribute("href"));
        });
    });
});
