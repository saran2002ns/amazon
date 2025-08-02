import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { cartService } from '../services/cartService';
import { orderService } from '../services/orderService';
import { moneyCoverter } from '../data/money.js';
import AmazonHeader from './AmazonHeader';

const Checkout = () => {
  const [cartItems, setCartItems] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Get user ID from localStorage (you might want to implement proper auth later)
  const userId = localStorage.getItem('userId') || 1; // Default to user 1 for demo

  useEffect(() => {
    loadCartItems();
  }, []);

  const loadCartItems = async () => {
    try {
      setLoading(true);
      const items = await cartService.getUserCart(userId);
      setCartItems(items);
      
      // Calculate total
      const total = items.reduce((sum, item) => {
        return sum + (item.product.priceCents * item.quantity);
      }, 0);
      setTotalPrice(total);
    } catch (error) {
      console.error('Error loading cart:', error);
      setError('Failed to load cart items');
    } finally {
      setLoading(false);
    }
  };

  const handleQuantityChange = async (cartItemId, newQuantity) => {
    try {
      await cartService.updateCartItem(cartItemId, parseInt(newQuantity));
      await loadCartItems(); // Reload cart to get updated data
    } catch (error) {
      console.error('Error updating quantity:', error);
      setError('Failed to update quantity');
    }
  };

  const handleDeleteItem = async (productId) => {
    try {
      await cartService.removeFromCart(userId, productId);
      await loadCartItems(); // Reload cart to get updated data
    } catch (error) {
      console.error('Error removing item:', error);
      setError('Failed to remove item');
    }
  };

  const handleDeliveryOptionChange = async (cartItemId, deliveryOption) => {
    try {
      await cartService.updateCartItem(cartItemId, null, deliveryOption);
      await loadCartItems(); // Reload cart to get updated data
    } catch (error) {
      console.error('Error updating delivery option:', error);
      setError('Failed to update delivery option');
    }
  };

  const handleProceedToCheckout = async () => {
    try {
      setLoading(true);
      
      // Convert cart items to order format
      const orderItems = cartItems.map(item => ({
        productId: item.product.id,
        quantity: item.quantity,
        deliveryOption: item.deliveryOption
      }));

      // Create order
      const order = await orderService.createOrder(userId, {
        shippingAddress: '123 Main St, City, State 12345', // You might want to add a form for this
        paymentMethod: 'credit_card', // You might want to add payment method selection
        items: orderItems
      });

      // Navigate to order confirmation
      navigate(`/order-confirmation/${order.id}`);
    } catch (error) {
      console.error('Error creating order:', error);
      setError('Failed to create order. Please try again.');
    } finally {
      setLoading(false);
    }
  };



  if (loading && cartItems.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading cart...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Amazon Header */}
      <AmazonHeader showSearch={false} />

      {/* Main Content */}
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-white rounded-lg shadow-md p-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-6">Shopping Cart</h1>
          
          {error && (
            <div className="mb-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}
          
          {cartItems.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500 text-lg mb-4">Your cart is empty</p>
              <Link
                to="/amazon"
                className="inline-block bg-orange-600 text-white px-6 py-3 rounded-md hover:bg-orange-700 transition-colors"
              >
                Continue Shopping
              </Link>
            </div>
          ) : (
            <div className="space-y-6">
              {cartItems.map((item) => (
                <div key={item.id} className="flex items-center space-x-4 p-4 border border-gray-200 rounded-lg">
                  {/* Product Image */}
                  <img 
                    className="w-20 h-20 object-cover rounded"
                    src={`/${item.product.image}`}
                    alt={item.product.name}
                  />
                  
                  {/* Product Details */}
                  <div className="flex-1">
                    <h3 className="font-medium text-gray-900">{item.product.name}</h3>
                    <p className="text-lg font-bold text-gray-900">
                      ${moneyCoverter(item.product.priceCents)}
                    </p>
                  </div>
                  
                  {/* Quantity */}
                  <div className="flex items-center space-x-2">
                    <label className="text-sm text-gray-600">Qty:</label>
                    <select 
                      value={item.quantity}
                      onChange={(e) => handleQuantityChange(item.id, e.target.value)}
                      className="border border-gray-300 rounded px-2 py-1 text-sm"
                      disabled={loading}
                    >
                      {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => (
                        <option key={num} value={num}>{num}</option>
                      ))}
                    </select>
                  </div>
                  
                  {/* Delivery Option */}
                  <div className="flex items-center space-x-2">
                    <label className="text-sm text-gray-600">Delivery:</label>
                    <select 
                      value={item.deliveryOption}
                      onChange={(e) => handleDeliveryOptionChange(item.id, e.target.value)}
                      className="border border-gray-300 rounded px-2 py-1 text-sm"
                      disabled={loading}
                    >
                      <option value="1">Free delivery</option>
                      <option value="2">Fast delivery</option>
                      <option value="3">Same-day delivery</option>
                    </select>
                  </div>
                  
                  {/* Delete Button */}
                  <button 
                    onClick={() => handleDeleteItem(item.product.id)}
                    className="text-red-600 hover:text-red-800 text-sm font-medium"
                    disabled={loading}
                  >
                    Delete
                  </button>
                </div>
              ))}
              
              {/* Total */}
              <div className="border-t pt-6">
                <div className="flex justify-between items-center">
                  <span className="text-xl font-bold text-gray-900">Total:</span>
                  <span className="text-2xl font-bold text-gray-900">
                    ${moneyCoverter(totalPrice)}
                  </span>
                </div>
              </div>
              
              {/* Checkout Button */}
              <div className="flex justify-end">
                <button 
                  onClick={handleProceedToCheckout}
                  disabled={loading}
                  className="bg-orange-600 text-white px-8 py-3 rounded-md hover:bg-orange-700 transition-colors font-medium disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {loading ? 'Processing...' : 'Proceed to Checkout'}
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Checkout; 