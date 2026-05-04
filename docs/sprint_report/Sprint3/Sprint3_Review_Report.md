# Sprint 3 Review Report
### Duration: 11.02 → 03.03
## Sprint Goal
Integrate CI/CD with Jenkins, extend the functional prototype, implement automated testing with code coverage, and create a basic local Docker image for deployment


## Completed User Stories / Tasks
- Extended the functional prototype (JavaFX frontend + Spring Boot backend)
- Integrated Jenkins CI/CD pipeline
- Implemented additional features and validated core functionalitie
- Expanded automated unit tests and code coverage for new featuress
- Implemented admin page, rsvp functionality and attendance tracking ,user authentication within frontend
- Connected frontend and backend with secondary testing
- Wrote unit tests and generated coverage report using JaCoCo


## UI Framework and Design Approach
- Framework: JavaFX 21 with SceneBuilder (FXML) for layout design.
- Design Pattern: We utilized the MVC (Model-View-Controller) architecture. This allowed us to separate the UI layouts (FXML) from the business logic (Java Controllers), making the application easier to test and maintain.
- Navigation Logic: A centralized SceneNavigator utility was implemented to handle transitions between the Login, Home, and Admin views without reloading the entire application stage.


## Screens Implemented
The following core views were finalized and connected to the Spring Boot REST API:
- Authentication Hub: Secure Login and Account Creation screens with real-time validation feedback.
- Admin Dashboard: A restricted view for event managers to create, edit, and approve campus activities.
- RSVP Interface: A student-facing view allowing users to register for specific events and track their attendance status.
- Event Feed: A paginated home screen displaying active events fetched dynamically from the backend.

## Code Coverage Goals and Tools
-  JUnit 5 for unit tests and JaCoCo for automated coverage reporting.

## Jenkins Pipeline Description
- A declarative Jenkins pipeline was configured (via a Jenkinsfile) to automate the "Build-Test-Report" cycle.
- Build Stage: Maven compiles the multi-module project (shared, backend, and frontend) to ensure there are no syntax or dependency errors.
- Test Stage: Executes all JUnit tests. If a single test fails, the build is marked "Unstable," and the team is notified.
- Coverage Stage: The JaCoCo plugin parses the test results to generate a visual HTML report of the code coverage.

## Demo Summary
The working backend and frontend connection, event management features, user account creation and login, and the functioning authentication and authorization flow. Unit test execution and coverage reporting.

## What Went Well
- Smooth integration of Jenkins CI/CD with automated build and test

- Successful extension of functional prototype and new features

- Backend and frontend fully connected and tested

- Good team collaboration and communication throughout the sprint
- Docker image creation and local testing worked as expected

## What Could Be Improved
- Allocate more time for debugging pipeline failures and Docker optimizations

- Improve documentation for Jenkins pipeline setup and Docker setup

- Conduct more comprehensive end-to-end testing before review
## Next Sprint Focus
Sprint 4 will focus on Creating deployment-ready Docker images ,Improving automated testing and coverage and Adding additional user features

Polishing UI/UX

## Time Spent by Each Member

| Team Member    | Hours Spent |
|----------------|--------|
| Aroush         |   8    |
| Jiya           |  16    |
| Puntawat       |  17    |
| Sailesh        |  18    |
| Ayo            |  15    |
 
