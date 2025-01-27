# Remember the Words Backend README

This README file outlines the structure and components of a Java-based backend for a "Remember the Words" application. This backend will allow users to input 
words along with their corresponding phrases or sentences, store these entries, and retrieve them later.

## Project Overview
The backend is built using Spring Boot (version 3.x) and includes modules such as user management, data storage, API endpoints, authentication, and real-time 
communication. The code is organized into several key components that can be developed and maintained independently.

---

## Prerequisites

Before starting the project setup, ensure you have:

1. Java JDK installed on your system.
2. Maven or Gradle set up for dependency management (we'll use Maven in this guide).
3. Spring Boot Starter Web, Spring Data JPA, and H2 Database as a database solution.

---

## Project Structure

The project is divided into several modules. Each module has its own directory structure:

```
remember-the-words-behind
├── README.md          # This file (current one)
├── .DS_Store         # macOS system file (if needed)
├── .gitignore        # Git ignore files
└── src               # Main source code directory

├── Authentication/     # Authentication layer
│   ├── TokenValidation.java  # JWT validation and token handling
│   └── TokenManager.java      # Token generation, storage, and revocation
├── Users/              # User management module
│   ├── Authenticator.java    # User authentication logic
│   ├── UserRepository.java   # Data storage for users
│   └── UserDao.java          # DTOs (Data Transfer Objects) for users

├── WordsList/           # Words list and data storage module
│   ├── IngestHandler.java     # Handles incoming word lists
│   ├── WordListRepository.java# Data storage layer
│   └── WordListModel.java    # DTOs for word lists

└── API/                 # API gateway and real-time communication module
├── WebSocketManager.java  # WebSocket server for real-time updates
├── PushService.java      # API service layer for pushing notifications
├── APIWebSocket.java       # WebSocket handler class
└── APIRouter.java         # Handles API requests and responses

├── DB/                 # Database interface
│   └── AbstractDatabase.java# Database abstraction layer
└── H2Database.java        # Actual database implementation (H2)

├── IngestAPI/           # API for ingesting new word lists
│   ├── WordListIngestHandler.java  # Handles adding new words to a list
│   └── WordListIngestRepository.java# Data storage layer

└── TrafficManager.java    # Centralized traffic management for load balancing and routing
```

---


## Module Descriptions

### 1. Authentication Layer
This module handles user authentication, token generation, and security-related tasks.

- `TokenValidation.java`: Validates JWT tokens and ensures proper permissions.
- `TokenManager.java`: Manages token creation, storage, and revocation.

### 2. User Management
This module manages user accounts, including registration, login, and update operations.

- `Authenticator.java`: Logic for authenticating users based on provided credentials.
- `UserRepository.java`: Stores user data in the database.
- `UserDao.java`: DTOs (data transfer objects) for user data.
- `UserHandler.java`: Handles API requests for user-related operations.
### 3. Words List
This module handles the creation, storage, and retrieval of word lists.

- `WordListRepository.java`: Stores word lists in the database.
- `WordListModel.java`: DTOs (data transfer objects) for word lists.
- `IngestHandler.java`: Logic to add new words to a list.

### 4. API Gateway
This module provides the API endpoints and handles routing, authentication, and rate limiting.

- `APIWebSocket.java`: WebSocket handler for real-time updates.
- `APIRouter.java`: Manages API requests and responses using Spring Boot's router annotation.
- `PushService.java`: Logic to push notifications via an API gateway (e.g., Firebase Cloud Messaging).

### 5. Database
This module interacts with the database layer.

- `AbstractDatabase.java`: Defines common database operations (e.g., CRUD).
- `H2Database.java`: Implements H2 database using Spring Data JPA.
  
### 6. Ingest API
This module provides APIs to integrate third-party sources of word lists.

- `WordListIngestHandler.java`: Handles adding words from external sources.
- `WordListIngestRepository.java`: Stores ingested data in the database.

---

## Technologies Used

1. **Programming Languages**:
   - Java (version 8+)

2. **Frameworks**:
   - Spring Boot
   - Maven/Gradle Dependency Management
   - Spring Data JPA
   - H2 Database

3. **Real-Time Communication**:
   - Krio WebSocket for WebSocket support.

4. **Authentication**:
   - JWT (JSON Web Tokens)
   - OAuth 2.0

5. **Database**:
   - H2 Database via Spring Data JPA
   - JSON serialization for data storage and transfer.

6. **Testing**:
   - JUnit for unit testing
   - Mockito for mocking dependencies.
   - Spring Boot's native test runner.
---

## Implementation Steps

1. **Setup Dependencies**
   
   Install the required dependencies using Maven:

   ```bash
   mvn clean install
   ```

2. **Configure Environment Variables**

   Set the necessary environment variables for your application, such as:
   - `spring.datasource.url`: Location of your database.
   - `spring.context.path`: Root path for your Spring app.

3. **Implement User Management**

   Start with the User module and implement key features like user authentication and registration.
4. **Develop API Endpoints**

   Create RESTful endpoints to interact with your application, such as:
   - Adding a word list
   - Adding words to a list
   - Getting all lists
   - Retrieving words from a list

5. **Real-Time Communication**

   Implement WebSocket support to allow real-time updates between the frontend and backend.

6. **Authentication and Authorization**

   Implement JWT-based authentication for secure API requests.

---

## Security Considerations

1. **Authentication**:
   - Use HTTPS for all API endpoints.
   - Implement OAuth 2.0 with proper authorization endpoints.
   - Protect sensitive data (e.g., credentials) during transmission.

2. **Database Security**:
   - Encrypt database connections using SSL/TLS.
   - Restrict access to the database by implementing role-based access control (RBAC).

3. **Real-Time Communication**:
   - Use HTTPS for WebSocket traffic.
   - Implement rate limiting and request validation for WebSocket clients.

4. **Token Management**:
   - Store tokens securely in memory or using a token server.
   - Revive and revoke tokens as needed.

---

## Example API Calls

Here's an example of how you might interact with the API:

```java
POST http://localhost:8080/api/v1/word_lists
    Body:
        {
            "name": "Daily Vocabulary",
            "description": "A list of words to learn every day."
        }
    Headers:
        Content-Type: application/json

    Response:
        Status code 201 Created
```

```java
POST http://localhost:8080/api/v1/words_list/{word_list_id}/add_word
    Body:
        {
            "list_id": 1,
            "word": "elephant",
            "definition": "A large animal with a trunk."
        }
    Headers:
        Content-Type: application/json

    Response:
        Status code 200 Ok
```

---
## Conclusion

This high-level overview provides a foundation for building your next-generation word list app. By leveraging Spring Boot, H2 Database, and WebSocket support, 
you can create a robust, scalable, and secure backend for managing word lists.

Let me know if you'd like to dive deeper into any specific module or feature!
