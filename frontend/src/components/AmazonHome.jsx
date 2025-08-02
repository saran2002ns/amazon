import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { productService } from '../services/productService';
import { cartService } from '../services/cartService';
import { moneyCoverter } from '../data/money.js';

const AmazonHome = () => {
  const [products, setProducts] = useState([]);
  const [cartQuantity, setCartQuantity] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  // Get user ID from localStorage (you might want to implement proper auth later)
  const userId = localStorage.getItem('userId') || 1; // Default to user 1 for demo

  useEffect(() => {
    loadProducts();
    loadCartCount();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      setError(''); // Clear any previous errors
      const productsData = await productService.getAllProducts();
      setProducts(productsData);
    } catch (error) {
      console.error('Error loading products:', error);
      if (error.code === 'ECONNABORTED') {
        setError('Request timed out. Please check your connection and try again.');
      } else if (error.response?.status === 404) {
        setError('Products not found. Please try again later.');
      } else if (error.response?.status >= 500) {
        setError('Server error. Please try again later.');
      } else {
        setError('Failed to load products. Please check your connection.');
      }
    } finally {
      setLoading(false);
    }
  };

  const loadCartCount = async () => {
    try {
      const count = await cartService.getCartItemCount(userId);
      setCartQuantity(count);
    } catch (error) {
      console.error('Error loading cart count:', error);
      // Don't show error for cart count, just keep it at 0
    }
  };

  const handleAddToCart = async (productId) => {
    try {
      await cartService.addToCart(userId, productId, 1, '1');
      await loadCartCount(); // Update cart count
    } catch (error) {
      console.error('Error adding to cart:', error);
      setError('Failed to add item to cart');
    }
  };

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      await loadProducts();
      return;
    }

    try {
      setLoading(true);
      const searchResults = await productService.searchProducts(searchTerm);
      setProducts(searchResults);
    } catch (error) {
      console.error('Error searching products:', error);
      setError('Failed to search products');
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const handleRetry = () => {
    loadProducts();
  };

  const handleLogout = () => {
    // Show confirmation dialog
    const confirmed = window.confirm('Are you sure you want to sign out?');
    
    if (confirmed) {
      // Clear user data from localStorage
      localStorage.removeItem('userId');
      localStorage.removeItem('token');
      localStorage.removeItem('userEmail');
      
      // Navigate to login page and replace current history entry
      navigate('/', { replace: true });
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Amazon Header */}
      <div className="bg-black text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between py-4">
            {/* Left Section */}
            <div className="flex items-center">
              <Link to="/home" className="flex items-center">
                <img 
                  className="h-8 w-auto hidden md:block" 
                  src="/images/amazon-logo-white.png" 
                  alt="Amazon"
                />
                <img 
                  className="h-6 w-auto md:hidden" 
                  src="/images/amazon-mobile-logo-white.png" 
                  alt="Amazon"
                />
              </Link>
            </div>

            {/* Middle Section */}
            <div className="flex-1 max-w-2xl mx-8">
              <div className="flex">
                <input 
                  className="flex-1 px-4 py-2 rounded-l-md text-black focus:outline-none focus:ring-2 focus:ring-orange-500"
                  type="text" 
                  placeholder="Search"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyPress={handleKeyPress}
                />
                <button 
                  onClick={handleSearch}
                  className="bg-orange-500 px-4 py-2 rounded-r-md hover:bg-orange-600 transition-colors"
                >
                  <img className="h-5 w-5" src="/images/icons/search-icon.png" alt="Search" />
                </button>
              </div>
            </div>

            {/* Right Section */}
            <div className="flex items-center space-x-6">
              <Link to="/orders" className="text-sm hover:text-orange-300 transition-colors">
                <div className="text-xs">Returns</div>
                <div className="font-semibold">& Orders</div>
              </Link>
              
              <Link to="/checkout" className="relative text-sm hover:text-orange-300 transition-colors">
                <img className="h-8 w-8 mx-auto" src="/images/icons/cart-icon.png" alt="Cart" />
                <div className="absolute -top-1 -right-1 bg-orange-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {cartQuantity}
                </div>
                <div className="text-xs mt-1">Cart</div>
              </Link>

              {/* Logout Button */}
              <button 
                onClick={handleLogout}
                className="text-sm hover:text-orange-300 transition-colors"
              >
                <div className="font-semibold">Sign Out</div>
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {error && (
          <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
            <div className="flex justify-between items-center">
              <span>{error}</span>
              <button 
                onClick={handleRetry}
                className="text-orange-600 hover:text-orange-700 underline text-sm font-medium"
              >
                Retry
              </button>
            </div>
          </div>
        )}

        {loading ? (
          <div className="flex items-center justify-center py-12">
            <div className="text-center">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto"></div>
              <p className="mt-4 text-gray-600">Loading products...</p>
            </div>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {products.map((item) => (
              <div key={item.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
                {/* Product Image */}
                <div className="aspect-square overflow-hidden">
                  <img 
                    className="w-full h-full object-cover"
                    src={`/${item.image}`}
                    alt={item.name}
                  />
                </div>

                {/* Product Info */}
                <div className="p-4">
                  {/* Product Name */}
                  <div className="text-sm font-medium text-gray-900 line-clamp-2 mb-2">
                    {item.name}
                  </div>

                  {/* Rating */}
                  <div className="flex items-center mb-2">
                    <img 
                      className="h-4 w-auto mr-1"
                      src={`/images/ratings/rating-${item.rating.stars * 10}.png`}
                      alt={`${item.rating.stars} stars`}
                    />
                    <span className="text-xs text-blue-600 hover:underline cursor-pointer">
                      {item.rating.count}
                    </span>
                  </div>

                  {/* Price */}
                  <div className="text-lg font-bold text-gray-900 mb-3">
                    ${moneyCoverter(item.priceCents)}
                  </div>

                  {/* Add to Cart Button */}
                  <button 
                    onClick={() => handleAddToCart(item.id)}
                    className="w-full bg-orange-500 text-white py-2 px-4 rounded-md hover:bg-orange-600 transition-colors text-sm font-medium"
                  >
                    Add to Cart
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {!loading && products.length === 0 && (
          <div className="text-center py-12">
            <p className="text-gray-500 text-lg">No products found</p>
            {searchTerm && (
              <button 
                onClick={loadProducts}
                className="mt-4 text-orange-600 hover:text-orange-700 underline"
              >
                Clear search and show all products
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default AmazonHome; 