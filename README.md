# BudgetTracker Android App 📱💰
 

## 📌 Project Overview
BudgetTracker is a robust Android application designed to help users manage their personal finances. Built using **Java** and **Firebase**, the app allows for real-time data synchronization and offline functionality. Users can track expenses, categorize spending, and set monthly financial goals to maintain better control over their budget.

---

## 🚀 Key Features
- **User Authentication:** Secure login and registration using Firebase Email Authentication.
- **Offline Persistence:** Uses Firebase's local disk persistence to work without an internet connection.
- **Expense Management:** Add, view, and track expenses with details such as date, start/end times, and descriptions.
- **Photo Attachments:** Optionally capture and store receipts or photos for each expense entry.
- **Goal Setting:** Set minimum and maximum monthly spending targets with visual progress tracking.
- **Category Summary:** View a breakdown of total spending per category over user-selected periods.

---

## 🛠 Tech Stack
- **Language:** Java (OOP focus)
- **IDE:** Android Studio
- **Backend:** Firebase Realtime Database & Firebase Auth
- **UI Architecture:** XML with Material Design 3
- **Local Storage:** Firebase Offline Persistence

---
## 👥 Team Roles & Responsibilities

The project work was divided among team members as follows:

* **Member 1 (Euan - ST10441569)**
  Responsible for implementing the **database layer using Room (RoomDB)**.
  This included creating entities, DAO interfaces, and managing data persistence for users, categories, expenses, and goals.

* **Member 2 (Wayne - ST10434859)**
  Responsible for the **User Interface (UI)** design and implementation.
  This included layouts, screens, and overall user experience of the application.

* **Member 3 & Member 4**
  Responsible for **User Authentication (Registration & Login)**.
  This included handling user input, validation, and integrating authentication functionality with the application.

* **Member 5 (Hassan - ST10458988)**
  Responsible for creating the **project demonstration video**, including recording, narration, and presenting the application features.

All members collaborated during integration, testing, and debugging to ensure the application functioned correctly.
---

## 📂 Project Structure
```text
app/src/main/java/com/example/budgetapp/
├── activities/       # UI Controllers (Login, Dashboard, AddExpense)
├── adapters/         # RecyclerView Adapters for transaction lists
├── models/           # Data Blueprints (ExpenseEntry, Category, Goal)
└── network/          # Firebase Manager and Database helpers

