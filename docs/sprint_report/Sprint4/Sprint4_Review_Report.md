# 📝 Sprint 4 Review Report

**Project:** Campus Event Management System (CEMS)
**Date:** March 10, 2026
**Sprint Master:** Sailesh Karki

---

## 🎯 Sprint Goal
The primary objective of Sprint 4 was to finalize all prototype functionalities, containerize the application using Docker, and prepare for the final demonstration in Week 8.

---

## ✅ Completed User Stories / Tasks
* **Authentication & Role Logic**: Fully implemented secure JWT-based login and role-based view switching for Students, Faculty, and Admins.
* **Event Management (CRUD)**: Finalized the Admin Event Approval workflow and automated notification feature.
* **Student RSVP**: Completed the RSVP user interface and synchronized backend attendance tracking.
* **Automatic Notification**: Sends an email notification on the event date to the registered users for the specific event.
* **Containerization**: Created a multi-stage Dockerfile and successfully built the CEMS image.
* **Deployment**: Pushed the Docker image to Docker Hub and verified deployment in a test environment.
* **Documentation**: Updated the README with the system architecture (ER, Use Case), technology stack, and API endpoints.

---

## 📺 Demo Summary
During the final review, the team demonstrated the following:
* **Product Walkthrough**: A complete end-to-end user flow from student registration/RSVP to admin approval.
* **Notification System**: Real-time automatic notifications triggered by event updates.
* **Docker Integration**: Live proof of the publicly available Docker Hub image and successful deployment results.
* **CI/CD Pipeline**: Demonstration of the Jenkins pipeline automating builds and JaCoCo coverage reports.

---

## ✨ What Went Well
* **100% Completion**: All user stories selected for the Sprint 4 backlog were implemented with zero postponed items.
* **Team Synergy**: Successfully resolved critical home page and navigation bug conflicts through effective branch merging.
* **Infrastructure**: The established CI/CD pipeline ensured that every final change was automatically tested and verified.

---

## 🛠️ What Could Be Improved
* **Initial Setup**: Early-stage UI documentation was slightly delayed, which could have been started in Sprint 3.
* **Frontend Debugging**: Troubleshooting JavaFX CSS resolution warnings took more time than originally estimated.

---

## 👥 Team Effort & Time Spent

| Member | Role                 | Key Tasks                                                                                                                  | Hours |
| :--- |:---------------------|:---------------------------------------------------------------------------------------------------------------------------|:------|
| **Sailesh Karki** | Scrum Master         | Readme Documentation, Q&A, Testing, Presentation, Trello Borad update, Sprint Review and sprint retrospective.             | 21    |
| **Jiya Jameela** | Backend Developer    | HomePage- pagination  and sidebar modification.                                                                            | 20    |
| **Puntawat Subhamani** | Full Stack Developer | Sidebar navigation, Docker & Jenkins CI/CD, & Debugging                                                                    | 21    |
| **Aroush Irfan** | Front Developer      | Admin pages (user and event management), role based access in frontend, testing for some features                          | 15    |
| **Ayokunle Ogunbiyi** | Full Stack Developer | Notification System Beackend, Bug Fixing, Attendence Tracking frontend Jenkins CI/CD pipeline and Docker Image publishing. | 17    |

---

## 🤖 AI Tools Utilized
* **Gemini 3 Flash**: Used for brainstorming ideas, researching design patterns, generating implementation documentation, visualizing the plan, testing, and debugging.

---

## 🚀 Next Sprint Focus (Final)
* **Scrum Master Handover**: Review sprint 4 and hand over to new srcum master.
* **Focus on Remaining Product Backlog**: Prepare new sprint 5 and accomplished the new tasks and plan for localization and cloud deployment.
