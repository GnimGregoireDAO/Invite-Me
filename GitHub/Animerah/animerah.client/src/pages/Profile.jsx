import React from 'react';
import { motion } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';
import './Profile.css';

const Profile = () => {
  const { user } = useAuth();

  return (
    <div className="profile-page">
      <div className="container">
        <motion.div
          className="profile-content"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <h1>Mon Profil</h1>
          
          <div className="profile-card">
            <div className="profile-avatar">
              {user?.displayName?.[0]?.toUpperCase() || 'U'}
            </div>
            
            <div className="profile-info">
              <h2>{user?.displayName}</h2>
              <p>{user?.email}</p>
              {user?.isAdmin && (
                <span className="badge admin">Administrateur</span>
              )}
            </div>
          </div>
          
          <div className="profile-stats">
            <h3>Statistiques</h3>
            <p>Fonctionnalité à venir...</p>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default Profile;