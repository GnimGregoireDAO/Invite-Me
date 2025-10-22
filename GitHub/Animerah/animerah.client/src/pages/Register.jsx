import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useForm } from 'react-hook-form';
import toast from 'react-hot-toast';
import { useAuth } from '../contexts/AuthContext';
import './Auth.css';

const Register = () => {
  const { register: registerUser, loading } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm();

  const password = watch('password');

  const onSubmit = async (data) => {
    try {
      setIsLoading(true);
      await registerUser({
        email: data.email,
        password: data.password,
        displayName: data.displayName
      });
      toast.success('Compte créé avec succès !');
      navigate('/');
    } catch (error) {
      toast.error(error.response?.data?.message || 'Erreur lors de la création du compte');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-background">
        <div className="floating-shapes">
          <div className="shape shape-1"></div>
          <div className="shape shape-2"></div>
          <div className="shape shape-3"></div>
          <div className="shape shape-4"></div>
        </div>
      </div>
      
      <div className="container">
        <motion.div
          className="auth-container"
          initial={{ opacity: 0, y: 50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <div className="auth-card">
            <div className="auth-header">
              <motion.h1
                className="auth-title"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2 }}
              >
                Inscription
              </motion.h1>
              <motion.p
                className="auth-subtitle"
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.3 }}
              >
                Rejoignez la communauté Animerah !
              </motion.p>
            </div>

            <motion.form
              className="auth-form"
              onSubmit={handleSubmit(onSubmit)}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.4 }}
            >
              <div className="form-group">
                <label className="form-label">Nom d'affichage</label>
                <input
                  type="text"
                  className={`form-input ${errors.displayName ? 'error' : ''}`}
                  placeholder="Votre nom d'utilisateur"
                  {...register('displayName', {
                    required: 'Le nom d\'affichage est requis',
                    minLength: {
                      value: 2,
                      message: 'Le nom doit contenir au moins 2 caractères'
                    },
                    maxLength: {
                      value: 50,
                      message: 'Le nom ne peut pas dépasser 50 caractères'
                    }
                  })}
                />
                {errors.displayName && (
                  <span className="error-text">{errors.displayName.message}</span>
                )}
              </div>

              <div className="form-group">
                <label className="form-label">Email</label>
                <input
                  type="email"
                  className={`form-input ${errors.email ? 'error' : ''}`}
                  placeholder="votre@email.com"
                  {...register('email', {
                    required: 'L\'email est requis',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: 'Format d\'email invalide'
                    }
                  })}
                />
                {errors.email && (
                  <span className="error-text">{errors.email.message}</span>
                )}
              </div>

              <div className="form-group">
                <label className="form-label">Mot de passe</label>
                <input
                  type="password"
                  className={`form-input ${errors.password ? 'error' : ''}`}
                  placeholder="••••••••"
                  {...register('password', {
                    required: 'Le mot de passe est requis',
                    minLength: {
                      value: 6,
                      message: 'Le mot de passe doit contenir au moins 6 caractères'
                    }
                  })}
                />
                {errors.password && (
                  <span className="error-text">{errors.password.message}</span>
                )}
              </div>

              <div className="form-group">
                <label className="form-label">Confirmer le mot de passe</label>
                <input
                  type="password"
                  className={`form-input ${errors.confirmPassword ? 'error' : ''}`}
                  placeholder="••••••••"
                  {...register('confirmPassword', {
                    required: 'Veuillez confirmer votre mot de passe',
                    validate: value =>
                      value === password || 'Les mots de passe ne correspondent pas'
                  })}
                />
                {errors.confirmPassword && (
                  <span className="error-text">{errors.confirmPassword.message}</span>
                )}
              </div>

              <div className="form-group">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    {...register('acceptTerms', {
                      required: 'Vous devez accepter les conditions d\'utilisation'
                    })}
                  />
                  <span className="checkmark"></span>
                  J'accepte les{' '}
                  <a href="/terms" target="_blank" className="auth-link">
                    conditions d'utilisation
                  </a>{' '}
                  et la{' '}
                  <a href="/privacy" target="_blank" className="auth-link">
                    politique de confidentialité
                  </a>
                </label>
                {errors.acceptTerms && (
                  <span className="error-text">{errors.acceptTerms.message}</span>
                )}
              </div>

              <motion.button
                type="submit"
                className="btn btn-primary btn-full"
                disabled={isLoading}
                whileHover={{ scale: 1.02 }}
                whileTap={{ scale: 0.98 }}
              >
                {isLoading ? (
                  <div className="spinner-small"></div>
                ) : (
                  'Créer mon compte'
                )}
              </motion.button>
            </motion.form>

            <motion.div
              className="auth-footer"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: 0.5 }}
            >
              <p>
                Déjà un compte ?{' '}
                <Link to="/login" className="auth-link">
                  Se connecter
                </Link>
              </p>
            </motion.div>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default Register;