import React, { useState } from 'react';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';

const UserManagement = () => {
  const [users] = useState([
    {
      id: '1',
      displayName: 'Admin Animerah',
      email: 'admin@animerah.com',
      isAdmin: true,
      createdAt: new Date().toISOString(),
      lastLoginAt: new Date().toISOString()
    }
  ]);
  const [loading] = useState(false);

  // Fonction placeholder pour la gestion future des utilisateurs
  const handleToggleAdmin = (userId) => {
    toast.info('Fonctionnalité à venir - Gestion des rôles utilisateur');
  };

  const handleDeleteUser = (userId) => {
    if (!window.confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      return;
    }
    toast.info('Fonctionnalité à venir - Suppression d\'utilisateur');
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
      className="user-management"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
    >
      <div className="management-header">
        <h2>Gestion des Utilisateurs</h2>
        <p>Administrez les comptes utilisateurs et leurs permissions</p>
      </div>

      <div className="users-stats">
        <div className="stat-card">
          <h3>Total Utilisateurs</h3>
          <p className="stat-number">{users.length}</p>
        </div>
        <div className="stat-card">
          <h3>Administrateurs</h3>
          <p className="stat-number">{users.filter(u => u.isAdmin).length}</p>
        </div>
        <div className="stat-card">
          <h3>Utilisateurs Standards</h3>
          <p className="stat-number">{users.filter(u => !u.isAdmin).length}</p>
        </div>
      </div>

      <div className="users-list">
        <div className="users-table">
          <div className="table-header">
            <div className="th">Utilisateur</div>
            <div className="th">Email</div>
            <div className="th">Rôle</div>
            <div className="th">Dernière Connexion</div>
            <div className="th">Actions</div>
          </div>
          
          {users.map((user) => (
            <motion.div
              key={user.id}
              className="table-row"
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.3 }}
            >
              <div className="td">
                <div className="user-info">
                  <div className="user-avatar">
                    {user.displayName[0].toUpperCase()}
                  </div>
                  <div>
                    <div className="user-name">{user.displayName}</div>
                    <div className="user-id">ID: {user.id}</div>
                  </div>
                </div>
              </div>
              
              <div className="td">{user.email}</div>
              
              <div className="td">
                <span className={`badge ${user.isAdmin ? 'admin' : 'user'}`}>
                  {user.isAdmin ? 'Administrateur' : 'Utilisateur'}
                </span>
              </div>
              
              <div className="td">
                {new Date(user.lastLoginAt).toLocaleDateString('fr-FR')}
              </div>
              
              <div className="td">
                <div className="user-actions">
                  <button
                    onClick={() => handleToggleAdmin(user.id)}
                    className={`btn btn-sm ${user.isAdmin ? 'btn-warning' : 'btn-success'}`}
                    disabled={user.email === 'admin@animerah.com'} // Protéger le compte admin principal
                  >
                    {user.isAdmin ? 'Rétrograder' : 'Promouvoir'}
                  </button>
                  
                  <button
                    onClick={() => handleDeleteUser(user.id)}
                    className="btn btn-sm btn-danger"
                    disabled={user.email === 'admin@animerah.com'} // Protéger le compte admin principal
                  >
                    Supprimer
                  </button>
                </div>
              </div>
            </motion.div>
          ))}
        </div>
        
        {users.length === 0 && (
          <div className="no-users">
            <h3>Aucun utilisateur</h3>
            <p>Les utilisateurs inscrits apparaîtront ici</p>
          </div>
        )}
      </div>

      <div className="user-management-info">
        <div className="info-card">
          <h3>?? Fonctionnalités à Venir</h3>
          <ul>
            <li>Gestion complète des rôles utilisateur</li>
            <li>Suspension/Réactivation de comptes</li>
            <li>Historique des activités utilisateur</li>
            <li>Export des données utilisateur</li>
            <li>Statistiques détaillées d'utilisation</li>
          </ul>
        </div>
        
        <div className="info-card">
          <h3>?? Sécurité</h3>
          <p>
            Le compte administrateur principal (admin@animerah.com) est protégé 
            et ne peut pas être supprimé ou rétrogradé pour garantir l'accès administratif.
          </p>
        </div>
      </div>
    </motion.div>
  );
};

export default UserManagement;