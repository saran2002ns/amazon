import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthGuard = ({ children }) => {
  const navigate = useNavigate();

  useEffect(() => {
    // Check if user is authenticated
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');
    
    if (!userId || !token) {
      // User is not authenticated, redirect to login
      navigate('/', { replace: true });
    }
  }, [navigate]);

  // Check if user is authenticated before rendering children
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('token');
  
  if (!userId || !token) {
    // Show loading while redirecting
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-orange-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Redirecting to login...</p>
        </div>
      </div>
    );
  }

  return children;
};

export default AuthGuard; 