import api from './api';

export const productService = {
  // Get all products
  getAllProducts: async () => {
    try {
      const response = await api.get('/products');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get product by ID
  getProductById: async (id) => {
    try {
      const response = await api.get(`/products/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Search products
  searchProducts: async (keyword) => {
    try {
      const response = await api.get(`/products/search?keyword=${encodeURIComponent(keyword)}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get products by type
  getProductsByType: async (type) => {
    try {
      const response = await api.get(`/products/type/${type}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get products by rating
  getProductsByRating: async (minRating) => {
    try {
      const response = await api.get(`/products/rating?minRating=${minRating}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Get products by price range
  getProductsByPriceRange: async (minPrice, maxPrice) => {
    try {
      const response = await api.get(`/products/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Create product (admin only)
  createProduct: async (productData) => {
    try {
      const response = await api.post('/products', productData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Update product (admin only)
  updateProduct: async (id, productData) => {
    try {
      const response = await api.put(`/products/${id}`, productData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  // Delete product (admin only)
  deleteProduct: async (id) => {
    try {
      const response = await api.delete(`/products/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },
}; 