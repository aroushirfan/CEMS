# Sprint 4 Planning Report

**Project:** Campus Event Management System (CEMS)  
**Sprint Number:** 4  
**Dates:** 05/03/2026 – 10/03/2026

---

## 🎯 Sprint Goal
The goal of Sprint 4 is to finalize all system functionality, containerize the application for deployment using Docker, and prepare for the final demonstration.

---

## 📋 Selected Product Backlog Items
* **US-Admin: Event Approval Workflow**: Finalize administrative oversight for event visibility.
* **US-Data: Admin Attendance Metrics**: Implement real-time participation analytics for faculty and management.
* **US-User: RSVP UI Implementation**: Develop the student-facing interface for event registration.
* **US-System: Automatic Notifications**: Establish triggers for real-time user updates via email.

---

## ⚙️ Planned Tasks / Breakdown
### **1. Finalize Functionality**
* **Landing Page & Home Page Fixes**: Resolve UI conflicts and navigation bugs.
* **Frontend-Backend Attendance Sync**: Ensure data integrity between the JavaFX client and Spring Boot server.
* **Notification Logic**: Implement event status alert triggers.

### **2. Containerization & Sharing**
* **Dockerfile Creation**: Define build steps for the integrated system (Frontend).
* **Docker Hub Integration**: Tag and push the final image to a public repository.
* **Deployment Testing**: Verify the image functionality in a test environment.

### **3. Final Review & Documentation**
* **Update README**: Include architecture diagrams, technology stack, and API endpoints.
* **Presentation Preparation**: Create slides and rehearse the product walkthrough.

---

## 👥 Team Capacity & Assumptions
* **Capacity**: The team is committed to a 100% completion rate for this final sprint.
* **Hours**: Estimated total effort of approximately 12–21 hours per member.
* **Assumptions**:
    * The Shared DTO module remains the stable contract for all communications.
    * The Jenkins CI/CD pipeline remains operational for final automated testing.

---

## ✅ Definition of Done
* **Functional**: All planned features are fully implemented and operational.
* **Tested**: Code passes all automated unit tests with JaCoCo coverage reports generated.
* **Deployed**: The Docker image is publicly accessible on Docker Hub and successfully runs the application.
* **Documented**: All sprint reports and technical documentation are updated in GitHub and Trello.