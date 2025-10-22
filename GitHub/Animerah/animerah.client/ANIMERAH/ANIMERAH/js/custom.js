
    $(document).ready(function(){
        $("#custom-carousel").owlCarousel({
            loop: true, 
            margin: 0, 
            nav: false, 
            dots: true, 
            autoplay: true, 
            autoplayTimeout: 4000,  // Changement toutes les 4 secondes
            autoplayHoverPause: false, 
            items: 1,  // Toujours une seule image à la fois
            animateOut: 'fadeOut',  // Effet de fondu au changement
            animateIn: 'fadeIn'
        });
    });




document.addEventListener("DOMContentLoaded", function () {
  const navLinks = document.querySelectorAll(".navbar-nav li a");

  // Récupérer la page active stockée
  const activePage = localStorage.getItem("activeNav");

  if (activePage) {
      // Supprime la classe active de tous les éléments
      navLinks.forEach(link => link.parentElement.classList.remove("active"));

      // Ajoute la classe active au bon élément
      navLinks.forEach(link => {
          if (link.getAttribute("href") === activePage) {
              link.parentElement.classList.add("active");
          }
      });
  }

  // Gérer le clic sur les liens
  navLinks.forEach(link => {
      link.addEventListener("click", function (event) {
          // Supprime l'ancienne classe active
          navLinks.forEach(link => link.parentElement.classList.remove("active"));

          // Ajoute la classe active sur l'élément cliqué
          this.parentElement.classList.add("active");

          // Enregistrer le lien cliqué
          localStorage.setItem("activeNav", this.getAttribute("href"));
      });
  });
});
