import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import apiService from '../../services/apiService';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalArticles: 0,
    totalUsers: 0,
    pendingRequests: 0,
    activeBigTalks: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      // Simuler le chargement des statistiques
      // En production, vous pourriez créer des endpoints dédiés
      const [articles, requests, bigTalks] = await Promise.all([
        apiService.getArticles({ page: 1, pageSize: 1 }),
        apiService.getBigTalkRequests('Pending'),
        apiService.getActiveBigTalks()
      ]);

      setStats({
        totalArticles: Array.isArray(articles) ? articles.length : 0,
        totalUsers: 0, // À implémenter
        pendingRequests: Array.isArray(requests) ? requests.length : 0,
        activeBigTalks: Array.isArray(bigTalks) ? bigTalks.length : 0
      });
    } catch (error) {
      console.error('Error loading stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    {
      title: 'Articles Publiés',
      value: stats.totalArticles,
      icon: '??',
      color: 'var(--primary-color)',
      bgColor: 'rgba(102, 126, 234, 0.1)'
    },
    {
      title: 'Utilisateurs',
      value: stats.totalUsers,
      icon: '??',
      color: 'var(--success-color)',
      bgColor: 'rgba(81, 207, 102, 0.1)'
    },
    {
      title: 'Demandes en Attente',
      value: stats.pendingRequests,
      icon: '?',
      color: 'var(--warning-color)',
      bgColor: 'rgba(255, 212, 59, 0.1)'
    },
    {
      title: 'BigTalks Actifs',
      value: stats.activeBigTalks,
      icon: '??',
      color: 'var(--accent-color)',
      bgColor: 'rgba(255, 107, 107, 0.1)'
    }
  ];

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <motion.div
      className="admin-dashboard"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
    >
      <div className="dashboard-header">
        <h2>Tableau de Bord</h2>
        <p>Vue d'ensemble de votre plateforme Animerah</p>
      </div>

      <div className="stats-grid">
        {statCards.map((stat, index) => (
          <motion.div
            key={stat.title}
            className="stat-card"
            style={{
              borderLeftColor: stat.color,
              backgroundColor: stat.bgColor
            }}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6, delay: index * 0.1 }}
            whileHover={{ scale: 1.02 }}
          >
            <div className="stat-icon" style={{ color: stat.color }}>
              {stat.icon}
            </div>
            <div className="stat-content">
              <h3>{stat.value}</h3>
              <p>{stat.title}</p>
            </div>
          </motion.div>
        ))}
      </div>

      <div className="dashboard-content">
        <div className="quick-actions">
          <h3>Actions Rapides</h3>
          <div className="actions-grid">
            <motion.button
              className="action-button"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => window.location.href = '/admin/articles'}
            >
              <span className="action-icon">??</span>
              <span>Créer un Article</span>
            </motion.button>
            
            <motion.button
              className="action-button"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => window.location.href = '/admin/requests'}
            >
              <span className="action-icon">??</span>
              <span>Gérer les Demandes</span>
            </motion.button>
            
            <motion.button
              className="action-button"
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => window.location.href = '/admin/users'}
            >
              <span className="action-icon">??</span>
              <span>Gérer les Utilisateurs</span>
            </motion.button>
          </div>
        </div>

        <div className="recent-activity">
          <h3>Activité Récente</h3>
          <div className="activity-list">
            <div className="activity-item">
              <span className="activity-icon">??</span>
              <span>Système initialisé avec succès</span>
              <span className="activity-time">Maintenant</span>
            </div>
            <div className="activity-item">
              <span className="activity-icon">??</span>
              <span>Compte administrateur créé</span>
              <span className="activity-time">À l'installation</span>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default AdminDashboard;