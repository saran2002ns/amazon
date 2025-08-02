import api from './api';

export const orderService = {
  // Get user's orders
  getUserOrders: async (userId) => {
    try {
      const response = await api.get(`/orders/user/${userId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get order by ID
  getOrderById: async (orderId) => {
    try {
      const response = await api.get(`/orders/${orderId}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Create new order
  createOrder: async (userId, orderData) => {
    try {
      const response = await api.post(`/orders/${userId}`, orderData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update order status
  updateOrderStatus: async (orderId, status) => {
    try {
      const response = await api.put(`/orders/${orderId}/status?status=${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get orders by status
  getOrdersByStatus: async (status) => {
    try {
      const response = await api.get(`/orders/status/${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get user's orders by status
  getUserOrdersByStatus: async (userId, status) => {
    try {
      const response = await api.get(`/orders/user/${userId}/status/${status}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Cancel order
  cancelOrder: async (orderId) => {
    try {
      const response = await api.put(`/orders/${orderId}/cancel`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
}; 