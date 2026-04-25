# Sprint 5 Planning Report

### Duration: 19.03.2026 -> 01.04.2026 (2 weeks)

### 1. Sprint Goal

Prepare the CEMS application for full multilingual support by implementing UI localization, integrate dynamic language switching, and full GUI localization for two non-Latin languages with LTR/RTL orientation.

### 2. Selected Product Backlog Items

- **US-1: Externalize UI text**
	Document all visible static UI text to be localized so that the application can be translated consistently.
- **US-2: Language selector, locale formatting and Non-Latin language support**
	Users can change language from the UI and interact with the application in the selected locale format.
- **US-3: LTR and RTL compatibility**
	The application maintains layout based on the selected language, the layout direction adapts correctly to RTL and LTR so that content is readable and natural.
- **US-4: Localization documentation and repository updates**
	Document a clear setup and translation instructions so localization work can continue in future sprints.
- **US-5: User settings**
	Users can view and update their profiles.

### 3. Planned Tasks

- Audit frontend and backend UI flows to identify all hardcoded user-facing text.
- Externalize labels, buttons, tooltips, alerts, validation messages, table headers, and status texts into a table format for translation.
- Translate All texts and messages in external document to selected languages.
- Build and integrate a language selector in the GUI.
- create resource bundle for selected languages and implement gui localization
- Add locale-aware formatting for:
	- Date and time displays
	- Number formatting
- Implement and test the two non-Latin languages:
	- Urdu (`ur`) for RTL verification
	- Thai (`th`) for LTR verification .
- Update README with language selection and localized run instructions.
- Add/update existing UML diagrams.

### 4. Acceptance Criteria and Estimation

| Backlog Item | Acceptance Criteria                                                                                                        |
|---------|----------------------------------------------------------------------------------------------------------------------------|
| US-1 | No hardcoded user-facing static strings remain in localized screens; all keys exist in resource files.                     |
| US-2 | Language selector is functional and updates UI text plus date/number/table formatting according to locale.                 |
| US-3 | GUI is fully usable in Urdu and Thai; non-Latin scripts render correctly and updates layout based on language orientation. |
| US-4 | README and localization docs are updated; localization framework/library and workflow are documented.                      |
| US-5 | Gui supports users viewing their real time profile details and edit functionality work.                                    |


### 5. Team Capacity & Assumptions

It is agreed that the team will be able to complete the selected user stories within the sprint duration. The team will
also regulate communication and tracking of tasks using Trello.