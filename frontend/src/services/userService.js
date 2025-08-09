import api from './api';

export const userService = {
  // Create a new user
  createUser: async (userData) => {
    try {
      const response = await api.post('/users', userData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get user by ID
  getUserById: async (id) => {
    try {
      const response = await api.get(`/users/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get user by email
  getUserByEmail: async (email) => {
    try {
      const encodedEmail = encodeURIComponent(email);
      const response = await api.get(`/users/email/${encodedEmail}`, { skipAuth: true });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Generate OTP
  generateOTP: async (otpRequest) => {
    try {
      const response = await api.post('/users/generate-otp', otpRequest);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Verify OTP
  verifyOTP: async (otpVerificationRequest) => {
    try {
      const response = await api.post('/users/verify-otp', otpVerificationRequest);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
}; 