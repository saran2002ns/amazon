# Amazon Clone - Frontend & Backend Connection

This project consists of a React frontend and Spring Boot backend that work together to create an Amazon clone.

## Project Structure

```
amazon/
├── frontend/          # React application (Vite)
├── backend/           # Spring Boot application
└── README.md         # This file
```

## Prerequisites

- **Node.js** (v16 or higher)
- **Java** (JDK 17 or higher)
- **Maven** (for backend)
- **MySQL** database (or use the configured TiDB Cloud)

## Quick Start

### 1. Start the Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### 2. Start the Frontend (React)

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`

## API Endpoints

The backend provides the following REST API endpoints:

### User Management
- `POST /api/users` - Create a new user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email

### OTP Management
- `POST /api/users/generate-otp` - Generate OTP for user
- `POST /api/users/verify-otp` - Verify OTP

### Product Management
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?keyword={keyword}` - Search products
- `GET /api/products/type/{type}` - Get products by type
- `GET /api/products/rating?minRating={rating}` - Get products by rating
- `GET /api/products/price-range?minPrice={price}&maxPrice={price}` - Get products by price range
- `POST /api/products` - Create new product (admin)
- `PUT /api/products/{id}` - Update product (admin)
- `DELETE /api/products/{id}` - Delete product (admin)

### Cart Management
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/{userId}/add` - Add item to cart
- `PUT /api/cart/{cartItemId}` - Update cart item
- `DELETE /api/cart/{userId}/remove/{productId}` - Remove item from cart
- `DELETE /api/cart/{userId}/clear` - Clear user's cart
- `GET /api/cart/{userId}/count` - Get cart item count
- `GET /api/cart/{userId}/total` - Get cart total

### Order Management
- `GET /api/orders/user/{userId}` - Get user's orders
- `GET /api/orders/{orderId}` - Get order by ID
- `POST /api/orders/{userId}` - Create new order
- `PUT /api/orders/{orderId}/status?status={status}` - Update order status
- `GET /api/orders/status/{status}` - Get orders by status
- `GET /api/orders/user/{userId}/status/{status}` - Get user's orders by status

## Frontend-Backend Connection

### API Configuration

The frontend is configured to connect to the backend through:

1. **API Base URL**: `http://localhost:8080/api` (configured in `frontend/src/services/api.js`)
2. **CORS Configuration**: Backend allows requests from `http://localhost:5173`
3. **Axios Interceptors**: Handle authentication tokens and error responses

### Service Layer

The frontend uses service classes to interact with the backend:

- `frontend/src/services/api.js` - Axios configuration
- `frontend/src/services/userService.js` - User-related API calls
- `frontend/src/services/productService.js` - Product-related API calls
- `frontend/src/services/cartService.js` - Cart-related API calls
- `frontend/src/services/orderService.js` - Order-related API calls

### Example Usage

```javascript
import { userService } from '../services/userService';
import { productService } from '../services/productService';
import { cartService } from '../services/cartService';
import { orderService } from '../services/orderService';

// User operations
const newUser = await userService.createUser({
  email: 'user@example.com',
  name: 'John Doe'
});

// Product operations
const products = await productService.getAllProducts();
const searchResults = await productService.searchProducts('socks');

// Cart operations
await cartService.addToCart(userId, productId, 2, '1');
const cartItems = await cartService.getUserCart(userId);

// Order operations
const order = await orderService.createOrder(userId, {
  shippingAddress: '123 Main St',
  paymentMethod: 'credit_card',
  items: [{ productId: '123', quantity: 2, deliveryOption: '1' }]
});
```

## Database Configuration

The backend is configured to use:
- **TiDB Cloud** (MySQL-compatible cloud database)
- **Connection details** are in `backend/src/main/resources/application.properties`

## Development

### Backend Development

1. **Database**: The application uses TiDB Cloud (MySQL-compatible)
2. **Port**: 8080 (configurable via `PORT` environment variable)
3. **CORS**: Configured to allow frontend requests

### Frontend Development

1. **Framework**: React with Vite
2. **Styling**: Tailwind CSS
3. **HTTP Client**: Axios for API calls
4. **Routing**: React Router DOM

## Troubleshooting

### Common Issues

1. **CORS Errors**: Make sure the backend is running on port 8080 and frontend on 5173
2. **Database Connection**: Check the database credentials in `application.properties`
3. **Port Conflicts**: Change ports in configuration files if needed

### Backend Issues

- Check if Java 17+ is installed: `java -version`
- Check if Maven is installed: `mvn -version`
- Check database connection in `application.properties`

### Frontend Issues

- Check if Node.js is installed: `node -version`
- Install dependencies: `npm install`
- Check if the backend is running before testing API calls

## Environment Variables

### Backend (.env or application.properties)
```properties
DATABASE_URL=jdbc:mysql://your-database-url
DATABASE_USERNAME=your-username
DATABASE_PASSWORD=your-password
PORT=8080
```

### Frontend (.env)
```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Production Deployment

### Backend Deployment
- Deploy to Railway, Heroku, or any Java hosting platform
- Set environment variables for database connection
- Update CORS configuration for production domain

### Frontend Deployment
- Build the project: `npm run build`
- Deploy the `dist` folder to any static hosting service
- Update API base URL for production backend

## API Documentation

For detailed API documentation, check the backend controller files:
- `backend/src/main/java/com/backend/amazon/controller/UserController.java`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test both frontend and backend
5. Submit a pull request

## License

## ✅ Frontend-Backend Connection Complete!

Your React frontend and Spring Boot backend are now fully connected! Here's what has been implemented:

### 🔗 **Connected Components**
- **Login Component** - Uses backend API for authentication
- **SignUp Component** - Creates users via backend API
- **AmazonHome Component** - Loads products from backend, manages cart
- **Checkout Component** - Uses backend cart and order APIs
- **OrderConfirmation Component** - Shows order details from backend

### 🚀 **How to Test the Connection**

1. **Start both applications:**
   ```bash
   # Terminal 1 - Backend
   cd backend
   ./mvnw spring-boot:run

   # Terminal 2 - Frontend
   cd frontend
   npm run dev
   ```

2. **Test the connection:**
   - Open browser console on `http://localhost:5173`
   - Run the test script from `test-connection.js`
   - Or simply navigate through the app and watch the network requests

3. **Expected behavior:**
   - Products load from backend API
   - Cart operations work with backend
   - User registration creates accounts in database
   - Orders are created and stored in backend

### 🎯 **Key Features Working**
- ✅ Product catalog from backend database
- ✅ User registration and authentication
- ✅ Shopping cart management
- ✅ Order creation and confirmation
- ✅ Search functionality
- ✅ Real-time cart count updates

### 🔧 **Troubleshooting**
If you encounter issues:
1. Check that backend is running on port 8080
2. Verify database connection in `application.properties`
3. Check browser console for CORS errors
4. Ensure all dependencies are installed

This project is for educational purposes. #   a m a z o n  
 