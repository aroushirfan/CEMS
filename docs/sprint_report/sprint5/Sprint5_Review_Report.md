# Sprint 5 Review Report

### Duration: 19.03.2026 -> 01.04.2026

## Sprint Goal
Prepare the CEMS application for full multilingual support by implementing UI localization, integrate dynamic language switching, and full GUI localization for two non-Latin languages with LTR/RTL orientation.

## Completed User Stories / Tasks
- Externalized UI static text (labels, buttons, alerts, validation messages, and table headers) into localization resource files (https://www.canva.com/design/DAHEdTKJAUo/hTLd7ummAdt-kLV4O--WOg/edit?utm_content=DAHEdTKJAUo&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton).
- Implemented language selection in the UI and verified runtime language switching behavior.
- Implemented non-Latin GUI localization for Urdu (`ur`) and Thai (`th`).
- Added locale-aware formatting for dates and numbers.
- Verified RTL/LTR behavior and layout alignment for localized screens.
- Updated localization setup document and usage notes for future maintenance or reference by new team members.


## Demo Summary
The team demonstrated runtime language switching between default language (English), Urdu, and Thai; validated localized labels and messages across major screens, and showed locale-based date/number formatting with RTL/LTR layout adaptation.

## What Went Well
- Localization architecture and existing resource-based setup reduced implementation friction.
- Team collaboration between frontend and documentation tasks helped maintain consistency of translated keys.
- Implementation of a per page based resource bundle helped speed up localization of keys and reduce conflicts between team members
- Non-Latin script rendering works in all views.
- RTL layout for Urdu works well in all screens/pages

## What Could Be Improved
- Some screens may require additional visual fine-tuning in RTL mode (spacing and alignment edge cases).
- Translation key naming can be standardized further for consistency.

## Next Sprint Focus
Sprint 6 will focus on database localization planning and implementation, including multilingual schema strategy, localized content persistence, migration approach, and test coverage for multilingual data handling.

## Team Member Contribution and In-Class Task Submission

| Team Member Name | Assigned Tasks                                                                                                         | Time Spent (hrs) | In-class tasks |
|------------------|------------------------------------------------------------------------------------------------------------------------|------------------|----------------|
| Sailesh Karki | English resource bundle updates, Localization documentation update,UI text externalization                             | TBD | Submitted |
| Jiya Jameela | User settings update (frontend and backend integration), bug fixes, Localization integration support                   | TBD | Submitted |
| Puntawat Subhamani | locale formatting checks,Thai UI verification, Thai resource bundle updates                                            | TBD | Submitted |
| Aroush Irfan | Frontend localization wiring, Urdu UI verification,Urdu resource bundle updates, User management bug fixes/adjustments | TBD | Submitted |
| Ayokunle Ogunbiyi | UI text externalization,Language selector integration,English resource bundle updates support                          | TBD | Submitted |

## AI Tools Utilized
- GitHub Copilot (GPT-5.3-Codex): Assisted with documentation drafting, and report structuring.
