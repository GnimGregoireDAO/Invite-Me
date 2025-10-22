import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/apiService';
import toast from 'react-hot-toast';
import './Discussions.css';

const Discussions = () => {
  const { isAuthenticated } = useAuth();
  const [activeTab, setActiveTab] = useState('bigtalks');
  const [bigTalks, setBigTalks] = useState([]);
  const [myRequests, setMyRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadBigTalks();
    if (isAuthenticated) {
      loadMyRequests();
    }
  }, [isAuthenticated]);

  const loadBigTalks = async () => {
    try {
      const data = await apiService.getActiveBigTalks();
      setBigTalks(data);
    } catch (error) {
      console.error('Error loading BigTalks:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadMyRequests = async () => {
    try {
      const data = await apiService.getBigTalkRequests();
      setMyRequests(data);
    } catch (error) {
      console.error('Error loading requests:', error);
    }
  };

  return (
    <div className="discussions-page">
      <div className="container">
        <motion.div
          className="page-header"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <h1 className="page-title">BigTalks</h1>
          <p className="page-subtitle">
            Participez aux discussions approfondies sur vos animes préférés
          </p>
        </motion.div>

        {/* Navigation Tabs */}
        <div className="tabs">
          <button
            className={`tab ${activeTab === 'bigtalks' ? 'active' : ''}`}
            onClick={() => setActiveTab('bigtalks')}
          >
            Discussions Actives
          </button>
          {isAuthenticated && (
            <button
              className={`tab ${activeTab === 'requests' ? 'active' : ''}`}
              onClick={() => setActiveTab('requests')}
            >
              Mes Demandes
            </button>
          )}
        </div>

        {/* Content */}
        {loading ? (
          <div className="loading">
            <div className="spinner"></div>
          </div>
        ) : (
          <div className="tab-content">
            {activeTab === 'bigtalks' && (
              <motion.div
                className="bigtalks-list"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.6 }}
              >
                {bigTalks.length === 0 ? (
                  <div className="no-content">
                    <h3>Aucune discussion active</h3>
                    <p>Les BigTalks approuvés apparaîtront ici</p>
                  </div>
                ) : (
                  bigTalks.map((bigTalk) => (
                    <div key={bigTalk.id} className="bigtalk-card">
                      <h3>{bigTalk.title}</h3>
                      <p>{bigTalk.description}</p>
                      <div className="bigtalk-meta">
                        <span>Article: {bigTalk.article.title}</span>
                        <span>Par {bigTalk.createdBy}</span>
                        <span>{bigTalk.messagesCount} message(s)</span>
                      </div>
                    </div>
                  ))
                )}
              </motion.div>
            )}

            {activeTab === 'requests' && isAuthenticated && (
              <motion.div
                className="requests-list"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.6 }}
              >
                {myRequests.length === 0 ? (
                  <div className="no-content">
                    <h3>Aucune demande</h3>
                    <p>Vos demandes de BigTalk apparaîtront ici</p>
                  </div>
                ) : (
                  myRequests.map((request) => (
                    <div key={request.id} className="request-card">
                      <h3>{request.title}</h3>
                      <p>{request.reason}</p>
                      <div className="request-meta">
                        <span>Article: {request.articleTitle}</span>
                        <span className={`status ${request.status.toLowerCase()}`}>
                          {request.status === 'Pending' && 'En attente'}
                          {request.status === 'Approved' && 'Approuvé'}
                          {request.status === 'Rejected' && 'Rejeté'}
                        </span>
                      </div>
                      {request.adminResponse && (
                        <div className="admin-response">
                          <strong>Réponse admin:</strong> {request.adminResponse}
                        </div>
                      )}
                    </div>
                  ))
                )}
              </motion.div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default Discussions;