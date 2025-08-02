import api from './api';

export const cartService = {
  // Get user's cart
  getUserCart: async (userId) => {
    try {
      const response = await api.get(`/cart/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Add item to cart
  addToCart: async (userId, productId, quantity = 1, deliveryOption = '1') => {
    try {
      const response = await api.post(`/cart/${userId}/add`, {
        productId,
        quantity,
        deliveryOption
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update cart item
  updateCartItem: async (cartItemId, quantity, deliveryOption) => {
    try {
      const response = await api.put(`/cart/${cartItemId}`, {
        quantity,
        deliveryOption
      });
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Remove item from cart
  removeFromCart: async (userId, productId) => {
    try {
      const response = await api.delete(`/cart/${userId}/remove/${productId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Clear user's cart
  clearCart: async (userId) => {
    try {
      const response = await api.delete(`/cart/${userId}/clear`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get cart item count
  getCartItemCount: async (userId) => {
    try {
      const response = await api.get(`/cart/${userId}/count`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get cart total
  getCartTotal: async (userId) => {
    try {
      const response = await api.get(`/cart/${userId}/total`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
}; 