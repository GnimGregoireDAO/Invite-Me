import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/apiService';
import toast from 'react-hot-toast';
import './ArticleDetail.css';

const ArticleDetail = () => {
  const { id } = useParams();
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();
  
  const [article, setArticle] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newComment, setNewComment] = useState('');
  const [submittingComment, setSubmittingComment] = useState(false);
  const [showBigTalkForm, setShowBigTalkForm] = useState(false);
  const [bigTalkData, setBigTalkData] = useState({
    title: '',
    reason: ''
  });

  useEffect(() => {
    loadArticle();
    loadComments();
  }, [id]);

  const loadArticle = async () => {
    try {
      const data = await apiService.getArticle(id);
      setArticle(data);
    } catch (error) {
      console.error('Error loading article:', error);
      if (error.response?.status === 404) {
        navigate('/articles');
      }
    }
  };

  const loadComments = async () => {
    try {
      const data = await apiService.getCommentsByArticle(id);
      setComments(data);
    } catch (error) {
      console.error('Error loading comments:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    try {
      setSubmittingComment(true);
      await apiService.createComment({
        content: newComment,
        articleId: parseInt(id)
      });
      
      setNewComment('');
      loadComments();
      toast.success('Commentaire ajouté !');
    } catch (error) {
      toast.error('Erreur lors de l\'ajout du commentaire');
    } finally {
      setSubmittingComment(false);
    }
  };

  const handleBigTalkRequest = async (e) => {
    e.preventDefault();
    if (!bigTalkData.title.trim() || !bigTalkData.reason.trim()) return;

    try {
      await apiService.createBigTalkRequest({
        title: bigTalkData.title,
        reason: bigTalkData.reason,
        articleId: parseInt(id)
      });
      
      setBigTalkData({ title: '', reason: '' });
      setShowBigTalkForm(false);
      toast.success('Demande de BigTalk soumise ! Elle sera examinée par les administrateurs.');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erreur lors de la soumission');
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!article) {
    return (
      <div className="article-not-found">
        <h2>Article non trouvé</h2>
        <Link to="/articles" className="btn btn-primary">
          Retour aux articles
        </Link>
      </div>
    );
  }

  return (
    <div className="article-detail-page">
      <div className="container">
        <motion.article
          className="article-detail"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          {/* Article Header */}
          <div className="article-header">
            {article.imageUrl && (
              <div className="article-hero-image">
                <img src={article.imageUrl} alt={article.title} />
              </div>
            )}
            
            <div className="article-meta-info">
              <div className="breadcrumb">
                <Link to="/articles">Articles</Link> / {article.title}
              </div>
              
              <h1 className="article-title">{article.title}</h1>
              
              {article.animeName && (
                <div className="anime-info">
                  <span className="anime-name">?? {article.animeName}</span>
                  {article.animeGenre && (
                    <span className="tag">{article.animeGenre}</span>
                  )}
                </div>
              )}
              
              <div className="article-meta">
                <span className="author">Par {article.authorName}</span>
                <span className="date">
                  {new Date(article.createdAt).toLocaleDateString('fr-FR', {
                    day: 'numeric',
                    month: 'long',
                    year: 'numeric'
                  })}
                </span>
                <span className="comments-count">
                  ?? {comments.length} commentaire{comments.length !== 1 ? 's' : ''}
                </span>
              </div>
            </div>
          </div>

          {/* Article Content */}
          <div className="article-body">
            <div className="article-content">
              {article.content.split('\n').map((paragraph, index) => (
                <p key={index}>{paragraph}</p>
              ))}
            </div>
          </div>

          {/* BigTalk Request Section */}
          {isAuthenticated && (
            <motion.div
              className="bigtalk-section"
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.3 }}
            >
              <h3>?? Proposer un BigTalk</h3>
              <p>Cet article vous inspire ? Proposez-le pour une discussion approfondie !</p>
              
              {!showBigTalkForm ? (
                <button 
                  className="btn btn-secondary"
                  onClick={() => setShowBigTalkForm(true)}
                >
                  Demander un BigTalk
                </button>
              ) : (
                <form onSubmit={handleBigTalkRequest} className="bigtalk-form">
                  <div className="form-group">
                    <label>Titre du débat</label>
                    <input
                      type="text"
                      value={bigTalkData.title}
                      onChange={(e) => setBigTalkData(prev => ({ ...prev, title: e.target.value }))}
                      placeholder="Ex: Les thèmes philosophiques dans cet anime"
                      required
                    />
                  </div>
                  
                  <div className="form-group">
                    <label>Pourquoi ce sujet mérite-t-il un BigTalk ?</label>
                    <textarea
                      value={bigTalkData.reason}
                      onChange={(e) => setBigTalkData(prev => ({ ...prev, reason: e.target.value }))}
                      placeholder="Expliquez pourquoi ce sujet serait intéressant à débattre..."
                      rows="4"
                      required
                    />
                  </div>
                  
                  <div className="form-actions">
                    <button type="submit" className="btn btn-primary">
                      Soumettre la demande
                    </button>
                    <button 
                      type="button" 
                      className="btn btn-secondary"
                      onClick={() => setShowBigTalkForm(false)}
                    >
                      Annuler
                    </button>
                  </div>
                </form>
              )}
            </motion.div>
          )}
        </motion.article>

        {/* Comments Section */}
        <motion.section
          className="comments-section"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 0.4 }}
        >
          <h2>Commentaires ({comments.length})</h2>
          
          {/* Add Comment Form */}
          {isAuthenticated ? (
            <form onSubmit={handleCommentSubmit} className="comment-form">
              <div className="form-group">
                <textarea
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  placeholder="Partagez votre opinion sur cet article..."
                  rows="4"
                  required
                />
              </div>
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={submittingComment}
              >
                {submittingComment ? 'Publication...' : 'Publier le commentaire'}
              </button>
            </form>
          ) : (
            <div className="login-prompt">
              <p>
                <Link to="/login">Connectez-vous</Link> pour laisser un commentaire
              </p>
            </div>
          )}

          {/* Comments List */}
          <div className="comments-list">
            {comments.length === 0 ? (
              <div className="no-comments">
                <p>Aucun commentaire pour le moment. Soyez le premier à commenter !</p>
              </div>
            ) : (
              comments.map((comment) => (
                <motion.div
                  key={comment.id}
                  className="comment"
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.4 }}
                >
                  <div className="comment-header">
                    <span className="comment-author">{comment.userName}</span>
                    <span className="comment-date">
                      {new Date(comment.createdAt).toLocaleDateString('fr-FR')}
                    </span>
                  </div>
                  <div className="comment-content">
                    {comment.content}
                  </div>
                </motion.div>
              ))
            )}
          </div>
        </motion.section>
      </div>
    </div>
  );
};

export default ArticleDetail;