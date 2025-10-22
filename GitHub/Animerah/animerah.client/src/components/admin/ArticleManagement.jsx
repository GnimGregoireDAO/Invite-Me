import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useForm } from 'react-hook-form';
import apiService from '../../services/apiService';
import toast from 'react-hot-toast';

const ArticleManagement = () => {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingArticle, setEditingArticle] = useState(null);

  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm();

  useEffect(() => {
    loadArticles();
  }, []);

  const loadArticles = async () => {
    try {
      setLoading(true);
      const data = await apiService.getArticles({ page: 1, pageSize: 50 });
      setArticles(data);
    } catch (error) {
      console.error('Error loading articles:', error);
      toast.error('Erreur lors du chargement des articles');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateArticle = async (data) => {
    try {
      await apiService.createArticle(data);
      toast.success('Article créé avec succès !');
      setShowCreateForm(false);
      reset();
      loadArticles();
    } catch (error) {
      toast.error('Erreur lors de la création de l\'article');
    }
  };

  const handleUpdateArticle = async (data) => {
    try {
      await apiService.updateArticle(editingArticle.id, data);
      toast.success('Article mis à jour !');
      setEditingArticle(null);
      reset();
      loadArticles();
    } catch (error) {
      toast.error('Erreur lors de la mise à jour');
    }
  };

  const handleDeleteArticle = async (id) => {
    if (!window.confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
      return;
    }

    try {
      await apiService.deleteArticle(id);
      toast.success('Article supprimé');
      loadArticles();
    } catch (error) {
      toast.error('Erreur lors de la suppression');
    }
  };

  const startEdit = (article) => {
    setEditingArticle(article);
    reset(article);
    setShowCreateForm(true);
  };

  const cancelEdit = () => {
    setEditingArticle(null);
    setShowCreateForm(false);
    reset();
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <motion.div
      className="article-management"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
    >
      <div className="management-header">
        <h2>Gestion des Articles</h2>
        <button
          className="btn btn-primary"
          onClick={() => setShowCreateForm(true)}
        >
          + Nouvel Article
        </button>
      </div>

      {/* Create/Edit Form */}
      {showCreateForm && (
        <motion.div
          className="article-form-overlay"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
        >
          <div className="article-form-container">
            <form
              onSubmit={handleSubmit(editingArticle ? handleUpdateArticle : handleCreateArticle)}
              className="article-form"
            >
              <div className="form-header">
                <h3>{editingArticle ? 'Modifier l\'Article' : 'Nouvel Article'}</h3>
                <button type="button" onClick={cancelEdit} className="close-btn">×</button>
              </div>

              <div className="form-grid">
                <div className="form-group">
                  <label>Titre *</label>
                  <input
                    type="text"
                    {...register('title', { required: 'Le titre est requis' })}
                    className={errors.title ? 'error' : ''}
                  />
                  {errors.title && <span className="error-text">{errors.title.message}</span>}
                </div>

                <div className="form-group">
                  <label>Nom de l'Anime</label>
                  <input
                    type="text"
                    {...register('animeName')}
                  />
                </div>

                <div className="form-group">
                  <label>Genre</label>
                  <select {...register('animeGenre')}>
                    <option value="">Sélectionner un genre</option>
                    <option value="Action">Action</option>
                    <option value="Aventure">Aventure</option>
                    <option value="Comédie">Comédie</option>
                    <option value="Drame">Drame</option>
                    <option value="Fantasy">Fantasy</option>
                    <option value="Romance">Romance</option>
                    <option value="Sci-Fi">Sci-Fi</option>
                    <option value="Thriller">Thriller</option>
                    <option value="Horreur">Horreur</option>
                    <option value="Slice of Life">Slice of Life</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Image URL</label>
                  <input
                    type="url"
                    {...register('imageUrl')}
                    placeholder="https://example.com/image.jpg"
                  />
                </div>
              </div>

              <div className="form-group">
                <label>Résumé</label>
                <textarea
                  {...register('summary')}
                  rows="3"
                  placeholder="Résumé court de l'article..."
                />
              </div>

              <div className="form-group">
                <label>Contenu *</label>
                <textarea
                  {...register('content', { required: 'Le contenu est requis' })}
                  rows="10"
                  className={errors.content ? 'error' : ''}
                  placeholder="Contenu de l'article..."
                />
                {errors.content && <span className="error-text">{errors.content.message}</span>}
              </div>

              <div className="form-group">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    {...register('isPublished')}
                  />
                  <span className="checkmark"></span>
                  Publier immédiatement
                </label>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingArticle ? 'Mettre à Jour' : 'Créer l\'Article'}
                </button>
                <button type="button" onClick={cancelEdit} className="btn btn-secondary">
                  Annuler
                </button>
              </div>
            </form>
          </div>
        </motion.div>
      )}

      {/* Articles List */}
      <div className="articles-list">
        {articles.length === 0 ? (
          <div className="no-articles">
            <h3>Aucun article</h3>
            <p>Créez votre premier article pour commencer</p>
          </div>
        ) : (
          <div className="articles-grid">
            {articles.map((article) => (
              <motion.div
                key={article.id}
                className="article-admin-card"
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.3 }}
              >
                {article.imageUrl && (
                  <div className="article-admin-image">
                    <img src={article.imageUrl} alt={article.title} />
                  </div>
                )}
                
                <div className="article-admin-content">
                  <h3>{article.title}</h3>
                  {article.animeName && (
                    <p className="anime-name">?? {article.animeName}</p>
                  )}
                  <p className="article-summary">
                    {article.summary || article.content.substring(0, 100) + '...'}
                  </p>
                  
                  <div className="article-meta">
                    <span className={`status ${article.isPublished ? 'published' : 'draft'}`}>
                      {article.isPublished ? 'Publié' : 'Brouillon'}
                    </span>
                    <span>{article.commentsCount} commentaire(s)</span>
                  </div>
                  
                  <div className="article-actions">
                    <button
                      onClick={() => startEdit(article)}
                      className="btn btn-sm btn-secondary"
                    >
                      Modifier
                    </button>
                    <button
                      onClick={() => handleDeleteArticle(article.id)}
                      className="btn btn-sm btn-danger"
                    >
                      Supprimer
                    </button>
                  </div>
                </div>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </motion.div>
  );
};

export default ArticleManagement;