# Acceptance Test Plan

## User Stories

- US-1 Add events: As an administrator, I want to add events so that students and teachers can attend.
- US-2 Approve events: As an admin, I want to approve events that have been added so that they can be confirmed and viewed in the list of events.
- US-3 List of upcoming events: As a user, I want to see the list of upcoming events so that I can choose events I want to attend.
- US-4 Update events: As an admin and faculty member, I want to update events so that the latest information is always available.
- US-5 Delete events.
- US-6 Signup page: As a user, I want to have a sign-up page so that I can sign up with my information.
- US-7 Login page: As a user, I want to have a login page so that I can log in with my credentials.
- US-8 Track attendance: As an administrator, I want to track attendance of my events so that I can view it later.
- US-9 Profile management: Users can update personal information.
- US-10 CI/CD integration: Automate builds, tests, and code coverage with Jenkins.
- US-11 Event notifications: As a user, I want to receive notifications of events I have signed up to.

## Acceptance Criteria

CEMS acceptance criteria by user stories:

**US-1: Add Events**

Scenario: Admin adds a new event
Acceptance Criteria:

- The system allows the admin/faculty to enter event details (title, date, time, location, description).
- Required fields must be validated before submission.
- The event is saved successfully and stored in the system.
- A confirmation message is displayed after successful creation.

**US-2: Approve Events**

Scenario: Admin approves a submitted event
Acceptance Criteria:

- The system displays a list of pending events.
- Admin can approve or reject an event.
- Approved events become visible in the events list.
- Rejected events are not displayed to users.
- Status of the event is updated accordingly.

**US-3: List of Upcoming Events**

Scenario: User views upcoming events
Acceptance Criteria:

- The system displays a list of upcoming events sorted by date.
- Each event shows key details (title, date, location).
- Only approved events are visible.
- Users can select an event to view more details.

**US-4: Update Events**

Scenario: Admin or faculty updates an event
Acceptance Criteria:

- Authorized users can edit event details.
- Changes are saved and reflected immediately.
- Validation rules apply to updated fields.
- A confirmation message is displayed after update.

**US-5: Delete Events**

Scenario: Admin deletes an event
Acceptance Criteria:

- Admin can delete an existing event.
- A confirmation prompt appears before deletion.
- Deleted events are removed from the system.
- Deleted events no longer appear in the event list.

**US-6: Signup Page**

Scenario: User signs up for an account
Acceptance Criteria:

- The system provides a signup form with required fields.
- User account is created upon successful submission.
- A success message is shown to the user after signup.

**US-7: Login Page**

Scenario: User logs into the system
Acceptance Criteria:

- The system allows users to enter valid credentials.
- Incorrect credentials display an error message.
- Successful login redirects the user to the homepage.
- Session is maintained securely (token exists after closing and re-opening the application).

**US-8: Track Attendance**

Scenario: Admin tracks attendance for an event
Acceptance Criteria:

- Admin and faculty can view a list of attendees for each event.
- Attendance can be marked by registered users.

**US-9: Profile Management**

Scenario: User updates personal information
Acceptance Criteria:

- Users can view their profile detail
- Users can edit their profile details.
- Changes are saved successfully.
- Updated information is visible in the application after update.

**US-10: CI/CD Integration**

Scenario: Automated build and test pipeline runs
Acceptance Criteria:

- Automated tests are executed during the pipeline.
- Code coverage reports are generated.

**US-11: Event Notifications**

Scenario: User receives event notifications
Acceptance Criteria:

- Users receive notifications for events they signed up for.
- Notifications are sent before the event (timing determined by product owner).
- Notifications include event details.

## Acceptance Criteria by Requirement

### Functional Acceptance Criteria

- Signup:
    - New users can create an account with valid mandatory details.
    - Invalid or duplicate input is rejected with clear feedback.
- Authentication:
    - Valid user credentials must produce successful login and session token.
    - Invalid credentials must be rejected with clear error feedback.
- Authorization/RBAC:
    - Student, faculty, and admin users should only see actions permitted for their role.
- Add Event:
    - Admin can create an event with required fields.
    - Newly created events are persisted and visible in management views.
- Event Approval:
    - Admin approval status controls event visibility as defined by workflow.
- Upcoming Events:
    - Users can view current upcoming events sorted by event date/time.
- Update Event:
    - Admin/faculty can update event details and changes are reflected immediately.
- Delete Event:
    - Admin/faculty can delete event records and removed events no longer appear in lists.
- Attendance Tracking:
    - Admin can view attendance for events and metrics reflect actual participant records.
- Profile Management:
    - Users can view and update profile details with validation.
- Event Notifications:
    - Notification events are generated for users signed up to events when configured triggers occur.

### Usability Acceptance Criteria

- New users can complete sign-up and first login without external help in less than 3 minutes.
- Error messages are clear, concise, and shown near the failed input/action.
- Users can locate event listing and register for an event without guidance.

### Performance and Reliability Acceptance Criteria

- Reliability:
    - Notification trigger do not crash the system.
- Deployability Reliability:
    - Dockerized image builds successfully and runs on any machine.
- CI/CD Reliability:
    - Jenkins pipeline succeeds when run multiple times with the same parameters and on different operating systems and machines.

## Acceptance Test Cases (Examples)

### Functional Test Cases

| User Story | Title                                     | Preconditions            | Steps                                                                       | Expected Outcome                                                                                                          |
| ---------- | ----------------------------------------- | ------------------------ | --------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| US-6       | User sign-up with valid details           | User is not registered   | 1) Open sign-up page. 2) Enter valid mandatory fields. 3) Submit.           | Account is created; success message shown; user redirected to login.                                                      |
| US-7       | User login with valid/invalid credentials | User exists in system    | 1) Login with valid credentials. 2) Logout. 3) Login with invalid password. | Valid login succeeds and token received, user is redirected to home; invalid login rejected with clear and concise error. |
| US-1       | Faculty/admin creates an event            | Faculty/admin logged in  | 1) Open create event form. 2) Fill valid details. 3) Save.                  | Event is persisted and appears in event management list.                                                                  |
| US-2       | Admin approves pending event              | Pending event exists     | 1) Admin opens pending events. 2) Approves one event.                       | Event status changes to approved and becomes visible per workflow.                                                        |
| US-3       | User views approved events                | At least 2 events exist  | 1) Open home page / all event list page.                                    | approved events are shown.                                                                                                |
| US-4       | Admin/faculty updates event details       | Existing event available | 1) Open event edit page. 2) Modify title/time/location. 3) Save.            | Updated values persist and appear in event lists/details.                                                                 |
| US-5       | Admin/faculty deletes event               | Existing event available | 1) Open event management page. 2) Delete selected event. 3) Refresh list.   | Deleted event no longer appears and cannot be opened.                                                                     |

### Usability Test Cases

| User Story       | Title                                 | Preconditions                                                                                     | Steps                                                                                           | Expected Outcome                                                             |
| ---------------- |---------------------------------------|---------------------------------------------------------------------------------------------------| ----------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| US-6, US-7       | First-time user onboarding            | 1 or more test Participants with no knowledge of the application.                                 | 1) present participant with the application. 2) ask to create an account. 3) observe without assistance. 4) record errors, difficulties or drawbacks user encountered | user completes the registration process and creates an account without assistance help.                              |
| US-3             | Event discovery and registration      | 1 or more test participants with an account, user is logged in and approved events are available. | 1) Open events list. 2) Use search/filter to find an exact event. 3) register for event.     | Users can find, open and register for relevant events efficiently without assistance.                |
| US-1, US-4, US-5 | Admin event operations                | an Admin/faculty account is logged in, an unapproved event exists                                 | 1) Open event management. 2) approve an event. 3) Update an existing event. 4) Delete event.  | Admin/faculty users can navigate event management actions with ease. |

### Performance and Reliability Test Cases

| User Story                   | Title                      | Preconditions                                             | Steps                                                                                                | Expected Outcome                                                                            |
| ---------------------------- |----------------------------|-----------------------------------------------------------|------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| US-11                        | Notification failure       | Notification provider can be toggled to failure mode.     | 1) Simulate notification provider failure. 2) Execute notification-triggering action.                | Application should handle failures gracefully and event workflows should continue as usual. |
| US-10                        | Docker startup reliability | Docker engine is running and docker files are configured. | 1) create docker image. 2) build the image. 3) run the container                                     | Containerized environment runs successfully.                                                |
| US-10                        | Jenkins pipeline           | Jenkins file and and script exists.                       | 1) run jenkins pipeline with the same parameters                                                     | the pipeline builds with no errors under the same conditions.                               |