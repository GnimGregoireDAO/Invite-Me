import axios from 'axios';

const API_BASE_URL = 'https://localhost:7207/api';

class ApiService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Intercepteur pour ajouter le token JWT
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Intercepteur pour gérer les erreurs de réponse
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Auth endpoints
  async register(userData) {
    const response = await this.api.post('/auth/register', userData);
    return response.data;
  }

  async login(credentials) {
    const response = await this.api.post('/auth/login', credentials);
    return response.data;
  }

  async getCurrentUser() {
    const response = await this.api.get('/auth/me');
    return response.data;
  }

  // Articles endpoints
  async getArticles(params = {}) {
    const response = await this.api.get('/articles', { params });
    return response.data;
  }

  async getArticle(id) {
    const response = await this.api.get(`/articles/${id}`);
    return response.data;
  }

  async createArticle(articleData) {
    const response = await this.api.post('/articles', articleData);
    return response.data;
  }

  async updateArticle(id, articleData) {
    const response = await this.api.put(`/articles/${id}`, articleData);
    return response.data;
  }

  async deleteArticle(id) {
    await this.api.delete(`/articles/${id}`);
  }

  // Comments endpoints
  async getCommentsByArticle(articleId) {
    const response = await this.api.get(`/comments/article/${articleId}`);
    return response.data;
  }

  async createComment(commentData) {
    const response = await this.api.post('/comments', commentData);
    return response.data;
  }

  async updateComment(id, content) {
    const response = await this.api.put(`/comments/${id}`, content, {
      headers: { 'Content-Type': 'application/json' }
    });
    return response.data;
  }

  async deleteComment(id) {
    await this.api.delete(`/comments/${id}`);
  }

  // BigTalk endpoints
  async createBigTalkRequest(requestData) {
    const response = await this.api.post('/bigtalk/request', requestData);
    return response.data;
  }

  async getBigTalkRequests(status = null) {
    const params = status ? { status } : {};
    const response = await this.api.get('/bigtalk/requests', { params });
    return response.data;
  }

  async getBigTalkRequest(id) {
    const response = await this.api.get(`/bigtalk/requests/${id}`);
    return response.data;
  }

  async reviewBigTalkRequest(id, reviewData) {
    const response = await this.api.post(`/bigtalk/requests/${id}/review`, reviewData);
    return response.data;
  }

  async getActiveBigTalks() {
    const response = await this.api.get('/bigtalk/talks');
    return response.data;
  }
}

export default new ApiService();