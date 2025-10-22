import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import apiService from '../../services/apiService';
import toast from 'react-hot-toast';

const RequestManagement = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('Pending');
  const [reviewingRequest, setReviewingRequest] = useState(null);
  const [reviewData, setReviewData] = useState({
    approve: true,
    adminResponse: ''
  });

  useEffect(() => {
    loadRequests();
  }, [filter]);

  const loadRequests = async () => {
    try {
      setLoading(true);
      const data = await apiService.getBigTalkRequests(filter);
      setRequests(data);
    } catch (error) {
      console.error('Error loading requests:', error);
      toast.error('Erreur lors du chargement des demandes');
    } finally {
      setLoading(false);
    }
  };

  const handleReview = async () => {
    if (!reviewingRequest) return;

    try {
      await apiService.reviewBigTalkRequest(reviewingRequest.id, {
        approve: reviewData.approve,
        adminResponse: reviewData.adminResponse
      });

      toast.success(
        reviewData.approve 
          ? 'Demande approuvée et BigTalk créé !'
          : 'Demande rejetée'
      );
      
      setReviewingRequest(null);
      setReviewData({ approve: true, adminResponse: '' });
      loadRequests();
    } catch (error) {
      toast.error('Erreur lors de la révision');
    }
  };

  const startReview = (request) => {
    setReviewingRequest(request);
    setReviewData({ approve: true, adminResponse: '' });
  };

  const cancelReview = () => {
    setReviewingRequest(null);
    setReviewData({ approve: true, adminResponse: '' });
  };

  const getStatusBadge = (status) => {
    const statusMap = {
      'Pending': { text: 'En attente', class: 'pending' },
      'Approved': { text: 'Approuvé', class: 'approved' },
      'Rejected': { text: 'Rejeté', class: 'rejected' }
    };
    
    const statusInfo = statusMap[status] || { text: status, class: 'pending' };
    
    return (
      <span className={`badge ${statusInfo.class}`}>
        {statusInfo.text}
      </span>
    );
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
      className="request-management"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
    >
      <div className="management-header">
        <h2>Gestion des Demandes BigTalk</h2>
        <div className="filter-tabs">
          {['Pending', 'Approved', 'Rejected'].map((status) => (
            <button
              key={status}
              className={`filter-tab ${filter === status ? 'active' : ''}`}
              onClick={() => setFilter(status)}
            >
              {status === 'Pending' && 'En attente'}
              {status === 'Approved' && 'Approuvées'}
              {status === 'Rejected' && 'Rejetées'}
            </button>
          ))}
        </div>
      </div>

      {/* Review Modal */}
      {reviewingRequest && (
        <motion.div
          className="review-modal-overlay"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
        >
          <div className="review-modal">
            <div className="modal-header">
              <h3>Réviser la Demande</h3>
              <button onClick={cancelReview} className="close-btn">×</button>
            </div>
            
            <div className="modal-content">
              <div className="request-details">
                <h4>{reviewingRequest.title}</h4>
                <p><strong>Article:</strong> {reviewingRequest.articleTitle}</p>
                <p><strong>Demandeur:</strong> {reviewingRequest.requesterName}</p>
                <p><strong>Raison:</strong></p>
                <div className="reason-text">{reviewingRequest.reason}</div>
              </div>
              
              <div className="review-form">
                <div className="form-group">
                  <label>Décision</label>
                  <div className="radio-group">
                    <label className="radio-label">
                      <input
                        type="radio"
                        checked={reviewData.approve}
                        onChange={() => setReviewData(prev => ({ ...prev, approve: true }))}
                      />
                      <span>Approuver</span>
                    </label>
                    <label className="radio-label">
                      <input
                        type="radio"
                        checked={!reviewData.approve}
                        onChange={() => setReviewData(prev => ({ ...prev, approve: false }))}
                      />
                      <span>Rejeter</span>
                    </label>
                  </div>
                </div>
                
                <div className="form-group">
                  <label>Réponse à l'utilisateur (optionnel)</label>
                  <textarea
                    value={reviewData.adminResponse}
                    onChange={(e) => setReviewData(prev => ({ ...prev, adminResponse: e.target.value }))}
                    placeholder="Expliquez votre décision..."
                    rows="4"
                  />
                </div>
              </div>
            </div>
            
            <div className="modal-actions">
              <button onClick={handleReview} className="btn btn-primary">
                {reviewData.approve ? 'Approuver' : 'Rejeter'}
              </button>
              <button onClick={cancelReview} className="btn btn-secondary">
                Annuler
              </button>
            </div>
          </div>
        </motion.div>
      )}

      {/* Requests List */}
      <div className="requests-list">
        {requests.length === 0 ? (
          <div className="no-requests">
            <h3>Aucune demande</h3>
            <p>
              {filter === 'Pending' && 'Aucune demande en attente'}
              {filter === 'Approved' && 'Aucune demande approuvée'}
              {filter === 'Rejected' && 'Aucune demande rejetée'}
            </p>
          </div>
        ) : (
          <div className="requests-grid">
            {requests.map((request) => (
              <motion.div
                key={request.id}
                className="request-card"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.3 }}
              >
                <div className="request-header">
                  <h3>{request.title}</h3>
                  {getStatusBadge(request.status)}
                </div>
                
                <div className="request-meta">
                  <p><strong>Article:</strong> {request.articleTitle}</p>
                  <p><strong>Demandeur:</strong> {request.requesterName}</p>
                  <p><strong>Date:</strong> {new Date(request.createdAt).toLocaleDateString('fr-FR')}</p>
                  {request.reviewedByName && (
                    <p><strong>Révisé par:</strong> {request.reviewedByName}</p>
                  )}
                </div>
                
                <div className="request-reason">
                  <h4>Raison:</h4>
                  <p>{request.reason}</p>
                </div>
                
                {request.adminResponse && (
                  <div className="admin-response">
                    <h4>Réponse admin:</h4>
                    <p>{request.adminResponse}</p>
                  </div>
                )}
                
                {request.status === 'Pending' && (
                  <div className="request-actions">
                    <button
                      onClick={() => startReview(request)}
                      className="btn btn-primary"
                    >
                      Réviser
                    </button>
                  </div>
                )}
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </motion.div>
  );
};

export default RequestManagement;