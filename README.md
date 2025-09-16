# Dormventory - Smart Dormitory Inventory Management System

## Project Overview

**Dormventory** is a comprehensive dormitory inventory management system designed to help students and dormitory administrators efficiently track, manage, and share items within dormitory rooms. The system combines modern web technologies with AI-powered receipt processing to create an intuitive and automated inventory management experience.

## Core Functionality

### For Students
- **Personal Inventory Management** - Track your personal items with detailed information
- **Room Inventory Sharing** - View and share items with roommates
- **Smart Receipt Processing** - Upload receipts and automatically extract item information using AI
- **Item Transfer System** - Easily transfer items between roommates
- **Secure Authentication** - JWT-based authentication with role-based access control
- **Responsive Web Interface** - Access from any device with a modern, intuitive UI

### For Administrators
- **User Management** - Manage student accounts and room assignments
- **Inventory Analytics** - Monitor item usage and room occupancy
- **Administrative Controls** - Full system access with admin privileges
- **System Monitoring** - Real-time system health and performance metrics

## System Architecture

### Backend Architecture
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Microservices │
│   (React)       │◄──►│   (Spring Boot) │◄──►│   (Spring Boot) │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Data Layer    │
                       │   (PostgreSQL)  │
                       └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   Cloud Services│
                       │   (AWS S3,      │
                       │    Textract)    │
                       └─────────────────┘
```

### Security Architecture
- **JWT Authentication** - Stateless token-based authentication
- **Spring Security** - Method-level authorization and security
- **Role-Based Access Control** - USER and ADMIN roles
- **Password Encryption** - BCrypt hashing for secure password storage
- **CORS Configuration** - Secure cross-origin request handling

## Technology Stack

### Backend Technologies
- **Java 17** - Modern Java with latest features
- **Spring Boot 3.2.0** - Rapid application development framework
- **Spring Security** - Comprehensive security framework
- **Spring Data JPA** - Data persistence and ORM
- **PostgreSQL** - Robust relational database
- **JWT (JSON Web Tokens)** - Secure authentication tokens
- **Lombok** - Reduces boilerplate code
- **Spring Boot Validation** - Input validation and error handling

### Frontend Technologies
- **React** - Modern JavaScript library for building user interfaces
- **Modern CSS** - Responsive and beautiful user interface
- **Responsive Design** - Mobile-first approach for all devices

### Cloud & Infrastructure
- **AWS Services**
  - **S3** - File storage for receipts and images
  - **Textract** - AI-powered document text extraction
  - **ECS Fargate** - Serverless container hosting
  - **RDS** - Managed PostgreSQL database
  - **ALB** - Application load balancer
  - **ECR** - Container image registry
  - **Secrets Manager** - Secure credential storage
  - **CloudWatch** - Logging and monitoring

### DevOps & Deployment
- **Docker** - Containerization for consistent deployments
- **Docker Compose** - Local development environment
- **Terraform** - Infrastructure as Code (IaC)
- **Maven** - Dependency management and build automation
- **JUnit 5** - Comprehensive testing framework
- **Mockito** - Mocking framework for unit tests

## Key Features

### 1. Smart Receipt Processing
- **AI-Powered Extraction** - Uses AWS Textract to automatically extract item information from receipts
- **Intelligent Parsing** - Identifies product names, prices, and quantities
- **Manual Confirmation** - Users can review and confirm extracted items before adding to inventory

### 2. Advanced Inventory Management
- **Item Tracking** - Complete item lifecycle management
- **Room-Based Organization** - Items organized by dormitory rooms
- **Ownership Management** - Clear ownership tracking with transfer capabilities
- **Transfer System** - Easy item transfer between roommates

### 3. User Management & Security
- **Secure Authentication** - JWT-based authentication system
- **Role-Based Access** - Different permissions for students and administrators
- **Data Protection** - Encrypted passwords and secure data handling
- **API Security** - Protected endpoints with proper authorization

### 4. Scalable Architecture
- **Cloud-Native Design** - Built for AWS cloud deployment
- **Auto-Scaling** - ECS Fargate with automatic scaling capabilities
- **Load Balancing** - Application Load Balancer for high availability
- **Monitoring** - CloudWatch integration for comprehensive monitoring

## Project Structure

```
Dormventory/
├── src/
│   ├── main/
│   │   ├── java/com/dormventory/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST API controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── exception/      # Custom exception handling
│   │   │   ├── model/          # JPA entities
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── security/       # Security configuration
│   │   │   └── service/        # Business logic layer
│   │   └── resources/
│   │       └── application.yml # Application configuration
│   └── test/                   # Test classes
├── src/components/             # React frontend components
├── terraform/                  # Infrastructure as Code
├── docker-compose.yml         # Local development setup
├── Dockerfile                 # Container configuration
├── pom.xml                    # Maven dependencies
└── SECURITY.md               # Security documentation
```

## Development Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 14+
- AWS CLI (for cloud deployment)

### Local Development
```bash
# Clone the repository
git clone https://github.com/gleecode04/Dormventory.git
cd Dormventory

# Start with Docker Compose
docker-compose up -d

# Access the application
open http://localhost:8080
```

### Production Deployment
```bash
# Deploy infrastructure with Terraform
cd terraform
terraform init
terraform apply

# Build and deploy application
docker build -t dormventory .
# Push to ECR and deploy to ECS
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User authentication

### User Management
- `GET /api/users/me` - Get current user profile
- `GET /api/users/{id}` - Get user by ID (with authorization)

### Inventory Management
- `GET /api/items/my-items` - Get current user's items
- `GET /api/items/room-items` - Get room items
- `POST /api/items` - Add new item
- `DELETE /api/items/{id}` - Delete item (owner only)
- `POST /api/items/{id}/transfer` - Transfer item (owner only)

### Receipt Processing
- `POST /api/receipts/upload` - Upload receipt for processing
- `POST /api/receipts/confirm` - Confirm extracted items

## Testing Strategy

### Unit Tests
- **Service Layer Testing** - Business logic validation
- **Repository Testing** - Data access layer testing
- **Security Testing** - Authentication and authorization testing

### Integration Tests
- **API Testing** - End-to-end API functionality
- **Database Testing** - Data persistence validation
- **Security Integration** - Complete security flow testing

## Security Features

### Authentication & Authorization
- JWT token-based authentication
- Role-based access control (USER/ADMIN)
- Method-level security annotations
- Secure password storage with BCrypt

### Data Protection
- Encrypted database connections
- Secure file storage in S3
- Environment variable configuration
- Secrets management with AWS Secrets Manager

### API Security
- CORS configuration
- Input validation
- SQL injection prevention
- XSS protection

## Performance & Scalability

### Performance Optimizations
- Connection pooling for database
- Efficient JPA queries
- Caching strategies (ready for implementation)
- Optimized Docker images

### Scalability Features
- Stateless application design
- Auto-scaling ECS services
- Load balancer distribution
- Database read replicas (ready for implementation)
licensed under the MIT License - see the LICENSE file for details.
