import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import ConfirmationModal from './ConfirmationModal';

const AmazonHeader = ({ cartQuantity = 0, searchTerm = '', onSearchChange, onSearch, showSearch = true }) => {
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogout = () => {
    setShowLogoutModal(true);
  };

  const confirmLogout = () => {
    // Clear user data from localStorage
    localStorage.removeItem('userId');
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    
    // Navigate to login page and replace current history entry
    navigate('/', { replace: true });
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && onSearch) {
      onSearch();
    }
  };

  return (
    <div className="bg-black text-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between py-4">
          {/* Left Section - Amazon Logo */}
          <div className="flex items-center">
            <Link to="/amazon" className="flex items-center">
              <i className="bi bi-amazon text-4xl text-orange-500"></i>
            </Link>
          </div>

          {/* Middle Section - Search Bar */}
          {showSearch && (
            <div className="flex-1 max-w-2xl mx-8">
              <div className="flex">
                <input 
                  className="flex-1 px-4 py-2 rounded-l-md text-black focus:outline-none focus:ring-2 focus:ring-orange-500"
                  type="text" 
                  placeholder="Search Amazon"
                  value={searchTerm}
                  onChange={onSearchChange}
                  onKeyPress={handleKeyPress}
                />
                <button 
                  onClick={onSearch}
                  className="bg-orange-500 px-4 py-2 rounded-r-md hover:bg-orange-600 transition-colors"
                >
                  <i className="bi bi-search text-white"></i>
                </button>
              </div>
            </div>
          )}

          {/* Right Section - Navigation */}
          <div className="flex items-center space-x-6">
            <Link to="/orders" className="text-sm hover:text-orange-300 transition-colors">
              <div className="text-xs">Returns</div>
              <div className="font-semibold">& Orders</div>
            </Link>
            
            <Link to="/checkout" className="relative text-sm hover:text-orange-300 transition-colors">
              <i className="bi bi-cart3 text-2xl"></i>
              {cartQuantity > 0 && (
                <div className="absolute -top-1 -right-1 bg-orange-500 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                  {cartQuantity}
                </div>
              )}
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

      {/* Confirmation Modal */}
      <ConfirmationModal
        isOpen={showLogoutModal}
        onClose={() => setShowLogoutModal(false)}
        onConfirm={confirmLogout}
        title="Sign Out"
        message="Are you sure you want to sign out?"
        confirmText="Sign Out"
        cancelText="Cancel"
        confirmButtonClass="bg-red-600 hover:bg-red-700"
        cancelButtonClass="bg-gray-300 hover:bg-gray-400"
      />
    </div>
  );
};

export default AmazonHeader; 