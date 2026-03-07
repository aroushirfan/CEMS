# CEMS
Students, faculty, and event organizers face difficulties keeping track of campus events due to scattered communication channels. Existing solutions lack a unified platform for event discovery, RSVP, and attendance tracking. Our product provides a simple campus management system where users can discover events, RSVP, track attendance, and receive notifications—streamlining participation and improving engagement.

## Technology Stack

Frontend: JavaFX (Because we are familiar with it)  
Backend: Spring Boot (Because it is the only backend framework we know)  
Database: MariaDB (Because we want to use a relational database, and we are familiar with it)  
Additional Tools & Frameworks: JWT Token (Because it is secure and it prevents users from tampering with the token)
Infrastructure: Dockerized environments and Jenkins CI/CD pipelines.

## System Structure

    The project is organized into modular directories to maintain a clean separation of concerns:
    
    /backend: Contains the Spring Boot logic, including AuthFilter and JwtService for security.
    
    /frontend: Contains the JavaFX application, UI controllers, and the ApiEventService for backend interaction.
    
    /shared: Hosts common data models and resources shared between the client and server.
    
    Jenkinsfile: Automates the build, test, and deployment stages.

    Dockerfile: Provides the environment blueprint for consistent application execution.
    
project-cems/

    ├── backend/               
    ├── frontend/              
    ├── shared/                
    ├── .env                 
    ├── Jenkinsfile            
    └── Dockerfile             

