import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import apiService from '../services/apiService';
import './Articles.css';

const Articles = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [selectedGenre, setSelectedGenre] = useState('');

  useEffect(() => {
    loadArticles();
  }, [search, selectedGenre]);

  const loadArticles = async () => {
    try {
      setLoading(true);
      const params = {};
      if (search) params.search = search;
      if (selectedGenre) params.genre = selectedGenre;
      
      const data = await apiService.getArticles(params);
      setArticles(data);
    } catch (error) {
      console.error('Error loading articles:', error);
    } finally {
      setLoading(false);
    }
  };

  const genres = [
    'Action', 'Aventure', 'Comédie', 'Drame', 'Fantasy', 
    'Romance', 'Sci-Fi', 'Thriller', 'Horreur', 'Slice of Life'
  ];

  return (
    <div className="articles-page">
      <div className="container">
        <motion.div
          className="page-header"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <h1 className="page-title">Articles Anime</h1>
          <p className="page-subtitle">
            Découvrez les dernières analyses, critiques et actualités du monde de l'anime
          </p>
        </motion.div>

        {/* Filters */}
        <motion.div
          className="filters"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.2 }}
        >
          <div className="search-box">
            <input
              type="text"
              placeholder="Rechercher un article..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="search-input"
            />
          </div>
          
          <div className="genre-filter">
            <select
              value={selectedGenre}
              onChange={(e) => setSelectedGenre(e.target.value)}
              className="genre-select"
            >
              <option value="">Tous les genres</option>
              {genres.map(genre => (
                <option key={genre} value={genre}>{genre}</option>
              ))}
            </select>
          </div>
        </motion.div>

        {/* Articles Grid */}
        {loading ? (
          <div className="loading">
            <div className="spinner"></div>
          </div>
        ) : (
          <motion.div
            className="articles-grid"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.4 }}
          >
            {articles.length === 0 ? (
              <div className="no-articles">
                <h3>Aucun article trouvé</h3>
                <p>Essayez de modifier vos critères de recherche</p>
              </div>
            ) : (
              articles.map((article, index) => (
                <motion.div
                  key={article.id}
                  className="article-card"
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.6, delay: index * 0.1 }}
                  whileHover={{ scale: 1.02 }}
                >
                  <Link to={`/articles/${article.id}`} className="article-link">
                    {article.imageUrl && (
                      <div className="article-image-container">
                        <img
                          src={article.imageUrl}
                          alt={article.title}
                          className="article-image"
                          onError={(e) => {
                            e.target.style.display = 'none';
                          }}
                        />
                      </div>
                    )}
                    
                    <div className="article-content">
                      <div className="article-meta">
                        <span className="article-author">Par {article.authorName}</span>
                        <span className="article-date">
                          {new Date(article.createdAt).toLocaleDateString('fr-FR')}
                        </span>
                      </div>
                      
                      <h3 className="article-title">{article.title}</h3>
                      
                      {article.animeName && (
                        <div className="anime-info">
                          <span className="anime-name">{article.animeName}</span>
                          {article.animeGenre && (
                            <span className="tag">{article.animeGenre}</span>
                          )}
                        </div>
                      )}
                      
                      <p className="article-summary">
                        {article.summary || article.content.substring(0, 150) + '...'}
                      </p>
                      
                      <div className="article-footer">
                        <span className="comments-count">
                          ?? {article.commentsCount} commentaire{article.commentsCount !== 1 ? 's' : ''}
                        </span>
                        <span className="read-more">Lire plus ?</span>
                      </div>
                    </div>
                  </Link>
                </motion.div>
              ))
            )}
          </motion.div>
        )}
      </div>
    </div>
  );
};

export default Articles;