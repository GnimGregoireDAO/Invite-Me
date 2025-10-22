import React from 'react';
import { Link } from 'react-router-dom';
import './Footer.css';

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="container">
          <div className="footer-grid">
            {/* Section Logo et Description */}
            <div className="footer-section">
              <div className="footer-brand">
                <img 
                  src="/AnimeRahLogo.png" 
                  alt="AnimeRah Logo" 
                  className="footer-logo"
                />
                <h3 className="footer-title">AnimeRah</h3>
              </div>
              <p className="footer-description">
                Votre destination ultime pour découvrir, analyser et discuter 
                de l'univers passionnant des animes et mangas.
              </p>
              <div className="social-links">
                <a href="https://www.tiktok.com/@ranymh0" className="social-link" target="_blank" rel="noopener noreferrer">
                  <i className="fab fa-tiktok"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-twitter"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-instagram"></i>
                </a>
                <a href="#" className="social-link">
                  <i className="fab fa-discord"></i>
                </a>
              </div>
            </div>

            {/* Section Navigation */}
            <div className="footer-section">
              <h4 className="footer-section-title">Navigation</h4>
              <ul className="footer-links">
                <li><Link to="/">Accueil</Link></li>
                <li><Link to="/articles">Articles</Link></li>
                <li><Link to="/discussions">BigTalks</Link></li>
                <li><Link to="/about">À propos</Link></li>
              </ul>
            </div>

            {/* Section Communauté */}
            <div className="footer-section">
              <h4 className="footer-section-title">Communauté</h4>
              <ul className="footer-links">
                <li><Link to="/register">Rejoindre</Link></li>
                <li><Link to="/login">Se connecter</Link></li>
                <li><a href="#">Guide du débutant</a></li>
                <li><a href="#">Règles de la communauté</a></li>
              </ul>
            </div>

            {/* Section Contact */}
            <div className="footer-section">
              <h4 className="footer-section-title">Contact</h4>
              <div className="contact-info">
                <div className="contact-item">
                  <i className="fas fa-map-marker-alt"></i>
                  <span>Lomé, Togo</span>
                </div>
                <div className="contact-item">
                  <i className="fas fa-envelope"></i>
                  <span>ranymh000@gmail.com</span>
                </div>
                <div className="contact-item">
                  <i className="fas fa-phone"></i>
                  <span>+228 97 78 70 19</span>
                </div>
              </div>
              <div className="newsletter">
                <h5>Newsletter</h5>
                <div className="newsletter-form">
                  <input 
                    type="email" 
                    placeholder="Votre email"
                    className="newsletter-input"
                  />
                  <button className="newsletter-btn">
                    <i className="fas fa-paper-plane"></i>
                  </button>
                </div>
                <span className="newsletter-note">
                  <i className="fas fa-shield-alt"></i>
                  Vous ne serez pas victime de spam
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Section Copyright */}
      <div className="footer-bottom">
        <div className="container">
          <div className="footer-bottom-content">
            <div className="copyright">
              <p>&copy; {currentYear} AnimeRah. Tous droits réservés.</p>
              <p>Créé avec <i className="fas fa-heart"></i> par RanymH</p>
            </div>
            <div className="footer-legal">
              <Link to="/privacy">Politique de confidentialité</Link>
              <Link to="/terms">Conditions d'utilisation</Link>
              <Link to="/cookies">Cookies</Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;