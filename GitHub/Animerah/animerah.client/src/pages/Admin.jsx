import React from 'react';
import { Routes, Route, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';
import AdminDashboard from '../components/admin/AdminDashboard';
import ArticleManagement from '../components/admin/ArticleManagement';
import RequestManagement from '../components/admin/RequestManagement';
import UserManagement from '../components/admin/UserManagement';
import './admin/Admin.css';

const Admin = () => {
  const { user } = useAuth();

  if (!user?.isAdmin) {
    return (
      <div className="unauthorized">
        <h2>Accès non autorisé</h2>
        <p>Vous n'avez pas les permissions pour accéder à cette page.</p>
      </div>
    );
  }

  return (
    <div className="admin-page">
      <div className="container">
        <motion.div
          className="admin-header"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <h1>Administration Animerah</h1>
          <p>Panneau de controle administrateur</p>
        </motion.div>

        <div className="admin-layout">
          <nav className="admin-sidebar">
            <Link to="/admin" className="admin-nav-link">
              Dashboard
            </Link>
            <Link to="/admin/articles" className="admin-nav-link">
              Articles
            </Link>
            <Link to="/admin/requests" className="admin-nav-link">
              Demandes BigTalk
            </Link>
            <Link to="/admin/users" className="admin-nav-link">
              Utilisateurs
            </Link>
          </nav>

          <main className="admin-content">
            <Routes>
              <Route index element={<AdminDashboard />} />
              <Route path="articles" element={<ArticleManagement />} />
              <Route path="requests" element={<RequestManagement />} />
              <Route path="users" element={<UserManagement />} />
            </Routes>
          </main>
        </div>
      </div>
    </div>
  );
};

export default Admin;