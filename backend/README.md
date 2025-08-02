# Amazon Backend - Simple OTP System

This is a Spring Boot application that provides a simple OTP (One-Time Password) functionality with email delivery.

## Features

- Simple OTP generation with email
- OTP delivery via email
- OTP verification using OTP ID
- Automatic OTP expiration (5 minutes)
- Database persistence for OTPs

## API Endpoints

### OTP Management

#### Generate OTP
```
POST /api/users/generate-otp
Content-Type: application/json

{
    "email": "john@example.com"
}
```

**Response:**
```json
{
    "message": "OTP sent to your email successfully",
    "otpId": 123,
    "email": "john@example.com",
    "success": true
}
```

#### Verify OTP
```
POST /api/users/verify-otp
Content-Type: application/json

{
    "otpId": 123,
    "otpCode": "123456"
}
```

**Response:**
```json
{
    "message": "OTP verified successfully",
    "otpId": 123,
    "email": "john@example.com",
    "success": true
}
```

## OTP Features

- **6-digit numeric OTP**: Generated randomly
- **Email delivery**: OTP is sent to user's email address
- **5-minute expiration**: OTPs automatically expire after 5 minutes
- **Single-use**: Each OTP can only be used once
- **OTP ID**: Frontend gets an OTP ID to use for verification

## Email Configuration

The application uses Gmail SMTP for sending OTP emails. You need to configure the following environment variables:

```bash
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Setting up Gmail App Password:

1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account settings
   - Security → 2-Step Verification → App passwords
   - Generate a new app password for "Mail"
3. Use this app password in the `EMAIL_PASSWORD` environment variable

## Database Schema

### OTPs Table
- `id` (Primary Key) - This is the OTP ID returned to frontend
- `email`
- `otp_code`
- `created_at`
- `expires_at`
- `used` (boolean)

## Usage Example

1. **Generate OTP:**
   ```bash
   curl -X POST http://localhost:8080/api/users/generate-otp \
     -H "Content-Type: application/json" \
     -d '{"email":"john@example.com"}'
   ```

2. **Check email for OTP code**

3. **Verify OTP:**
   ```bash
   curl -X POST http://localhost:8080/api/users/verify-otp \
     -H "Content-Type: application/json" \
     -d '{"otpId":123,"otpCode":"123456"}'
   ```

## Frontend Integration

1. Call the generate OTP endpoint with user's email
2. Store the returned `otpId` in your frontend state
3. User receives OTP code via email
4. When user enters the OTP code, call verify endpoint with `otpId` and `otpCode`
5. Check the `success` field in response

## Running the Application

1. Set up email configuration:
   ```bash
   export EMAIL_USERNAME=your-email@gmail.com
   export EMAIL_PASSWORD=your-app-password
   ```

2. Ensure MySQL is running and configured in `application.properties`

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. The application will start on port 8080 (or the port specified in `PORT` environment variable)

## Notes

- OTP codes are sent via email for security
- The OTP code is not returned in the API response
- Consider implementing rate limiting for OTP generation
- Add proper error handling and validation
- For production, consider using a dedicated email service like SendGrid or AWS SES 