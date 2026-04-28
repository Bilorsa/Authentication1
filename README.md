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

## 📂 Project Structure
```text
app/src/main/java/com/example/budgetapp/
├── activities/       # UI Controllers (Login, Dashboard, AddExpense)
├── adapters/         # RecyclerView Adapters for transaction lists
├── models/           # Data Blueprints (ExpenseEntry, Category, Goal)
└── network/          # Firebase Manager and Database helpers
