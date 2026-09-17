# 💰 SpendWise

> A modern personal expense and budget tracking Android application built with Kotlin and Jetpack Compose.

SpendWise is an Android application developed to help users manage their personal finances in a simple and organized way. It allows users to record income and expenses, manage budgets, search and filter transactions, analyze spending patterns, and maintain their financial data locally using Room Database.

---

## 📱 Project Overview

Managing everyday expenses can become difficult when transactions are scattered across different applications or written down manually.

SpendWise provides a centralized mobile solution where users can:

- Track income and expenses
- Categorize transactions
- Monitor their current balance
- Set a monthly budget
- View recent transactions
- Search and filter transactions
- Sort transactions
- Analyze spending patterns
- View category-wise spending
- View daily and periodic spending
- Edit and delete transactions
- Manage student profile information
- Configure application preferences
- Export an expense report
- Reset the application with sample data
- Clear stored transaction data
- Switch between light and dark themes

All core financial data is stored locally on the device using Room Database.

---

## ✨ Key Features

### 🏠 Dashboard

The Home screen provides a quick financial overview.

It displays:

- Current balance
- Total income
- Total expenses
- Monthly budget
- Recent transactions
- Financial summary cards
- Spending overview

The dashboard is designed to give users important financial information at a glance.

---

### 💸 Add Income & Expenses

Users can create financial transactions by entering:

- Amount
- Transaction type
- Category
- Note/description
- Payment method
- Date

The application supports both:

- Income
- Expense

Transactions can be added through an intuitive custom input interface.

---

### 🧾 Transaction Management

The Transactions screen provides a complete list of stored transactions.

Users can:

- View transactions
- Search transactions
- Filter by transaction type
- Filter by category
- Sort transactions
- Open transaction details
- Edit transactions
- Delete transactions

The search functionality supports queries involving:

- Transaction notes
- Categories
- Transaction information

---

### 📊 Analytics

The Analytics screen provides a visual overview of spending behavior.

It includes:

- Total spending
- Category-wise spending
- Spending percentages
- Daily spending information
- Period-based analysis
- Spending charts
- Comparison information

Analytics help users understand their spending habits and identify major expense categories.

---

### 🎯 Budget Management

SpendWise allows users to set and manage a monthly budget.

The budget functionality provides:

- Monthly budget configuration
- Budget amount tracking
- Expense comparison
- Budget status
- Budget-related information

Example:

```text
Monthly Budget: ₹10,000
📤 Expense Report

SpendWise provides an expense report generation feature.

The report contains information such as:

Generated date
Total income
Total expenses
Current balance
Transaction details

The generated report can be copied to the device clipboard for easy sharing or storage.

🌙 Theme Support

The application supports theme customization including:

Light theme
Dark theme

The UI is implemented using Jetpack Compose and Material 3 components.

🛠️ Technology Stack
Technology	Purpose
Kotlin	Primary programming language
Jetpack Compose	Modern declarative UI
Material 3	UI components and design system
Navigation Compose	Screen navigation
Room Database	Local data persistence
SQLite	Local database engine
Kotlin Coroutines	Asynchronous operations
Kotlin Flow	Reactive data streams
StateFlow	UI state management
ViewModel	Application state and business logic
KSP	Room code generation
Gradle	Build automation
Android Studio	Development environment
🏗️ Architecture

SpendWise follows a layered architecture using ViewModel, Repository, DAO, and Room Database.

                    ┌──────────────────────┐
                    │    Jetpack Compose   │
                    │          UI          │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │      ViewModel       │
                    │   UI State & Logic   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │     Repository       │
                    │   Data Management    │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │        DAO           │
                    │ Database Operations   │
                    └──────────┬───────────┘
                               │
                               ▼
                    ┌──────────────────────┐
                    │    Room Database     │
                    │      SQLite           │
                    └──────────────────────┘
🔄 Data Flow
User Interaction
       ↓
Jetpack Compose Screen
       ↓
SpendWiseViewModel
       ↓
SpendWiseRepository
       ↓
Room DAO
       ↓
SQLite Database
       ↓
Flow / StateFlow
       ↓
Compose UI
       ↓
Updated Screen

This architecture separates the user interface, application logic, and data layer, making the project easier to maintain and extend.

🗂️ Project Structure
SpendWise/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       │
│   │       ├── java/
│   │       │   └── com/example/spendwise/
│   │       │       │
│   │       │       ├── MainActivity.kt
│   │       │       ├── SpendWiseApplication.kt
│   │       │       │
│   │       │       ├── data/
│   │       │       │   ├── entity/
│   │       │       │   │   ├── BudgetEntity.kt
│   │       │       │   │   ├── CategoryEntity.kt
│   │       │       │   │   ├── TransactionEntity.kt
│   │       │       │   │   └── UserSettingsEntity.kt
│   │       │       │   │
│   │       │       │   └── local/
│   │       │       │       ├── AppDatabase.kt
│   │       │       │       ├── BudgetDao.kt
│   │       │       │       ├── CategoryDao.kt
│   │       │       │       ├── TransactionDao.kt
│   │       │       │       └── UserSettingsDao.kt
│   │       │       │
│   │       │       ├── model/
│   │       │       │   ├── CategorySpending.kt
│   │       │       │   ├── DailySpending.kt
│   │       │       │   ├── PaymentMethod.kt
│   │       │       │   ├── PeriodType.kt
│   │       │       │   ├── SortOrder.kt
│   │       │       │   ├── TimeRange.kt
│   │       │       │   └── TransactionType.kt
│   │       │       │
│   │       │       ├── repository/
│   │       │       │   └── SpendWiseRepository.kt
│   │       │       │
│   │       │       ├── viewmodel/
│   │       │       │   └── SpendWiseViewModel.kt
│   │       │       │
│   │       │       ├── ui/
│   │       │       │   ├── components/
│   │       │       │   │   ├── BalanceCard.kt
│   │       │       │   │   ├── Charts.kt
│   │       │       │   │   ├── ConfirmationDialog.kt
│   │       │       │   │   ├── CustomKeypad.kt
│   │       │       │   │   ├── EmptyState.kt
│   │       │       │   │   ├── SpendWiseBottomBar.kt
│   │       │       │   │   ├── SpendWiseTopBar.kt
│   │       │       │   │   ├── SummaryCard.kt
│   │       │       │   │   └── TransactionItem.kt
│   │       │       │   │
│   │       │       │   ├── navigation/
│   │       │       │   │   ├── Screen.kt
│   │       │       │   │   └── SpendWiseNavGraph.kt
│   │       │       │   │
│   │       │       │   ├── screens/
│   │       │       │   │   ├── addtransaction/
│   │       │       │   │   ├── analytics/
│   │       │       │   │   ├── budget/
│   │       │       │   │   ├── home/
│   │       │       │   │   ├── settings/
│   │       │       │   │   └── transactions/
│   │       │       │   │
│   │       │       │   └── theme/
│   │       │       │       ├── Color.kt
│   │       │       │       ├── Theme.kt
│   │       │       │       └── Type.kt
│   │       │       │
│   │       │       └── util/
│   │       │           ├── CurrencyUtils.kt
│   │       │           └── DateUtils.kt
│   │       │
│   │       └── res/
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
🗄️ Database Design

SpendWise uses Room Database for local data persistence.

The main database entities are:

TransactionEntity

Stores individual income and expense transactions.

Transaction
├── ID
├── Transaction Type
├── Amount
├── Category
├── Note / Description
├── Date
└── Payment Method
BudgetEntity

Stores budget-related information.

Budget
├── ID
├── Month
├── Year
└── Budget Amount
CategoryEntity

Stores transaction categories.

Category
├── ID
├── Name
└── Type
UserSettingsEntity

Stores user preferences and profile information.

User Settings
├── ID
├── Currency
├── Student Name
├── College / Roll ID
├── Dark Mode
└── Budget Alerts
🧩 Main Application Modules
Data Layer

Responsible for persistent data storage.

data/
├── entity/
└── local/

The DAO classes provide database operations for:

Transactions
Categories
Budgets
User settings
Repository Layer
SpendWiseRepository.kt

The repository acts as the central data-access layer between the ViewModel and Room Database.

It manages:

Transactions
Budgets
Categories
User settings
Spending calculations
Analytics data
ViewModel Layer
SpendWiseViewModel.kt

The ViewModel manages application state and connects the UI with the repository.

It helps provide:

Reactive UI state
Transaction operations
Budget operations
Analytics data
User settings
Database updates
UI Layer

The UI is implemented completely using Jetpack Compose.

Main screens include:

Home
Transactions
Add Transaction
Analytics
Budget
Settings
🧭 Navigation

The application uses Navigation Compose.

The navigation flow can be represented as:

                    SpendWise
                       │
        ┌──────────────┼──────────────┐
        │              │              │
       Home       Transactions     Analytics
        │              │
        │              ├── Details
        │              ├── Edit
        │              └── Delete
        │
        ├── Add Transaction
        │
        ├── Budget
        │
        └── Settings
               │
               ├── Profile
               ├── Currency
               ├── Budget Alerts
               ├── Export Report
               ├── Reset Data
               └── Clear Data
💳 Payment Methods

Transactions support different payment methods through the PaymentMethod model.

Examples include:

UPI
Card
Cash
📁 Data Models

The application contains models for:

Transaction type
Payment method
Category spending
Daily spending
Period type
Sort order
Time range

These models help organize transaction processing and analytics.

🔍 Transaction Search & Filtering

The Transactions screen provides tools to efficiently find financial records.

Users can:

Search
  ↓
Filter by Type
  ↓
Filter by Category
  ↓
Sort Results
  ↓
View Transaction

This makes it easier to locate specific transactions even when many records are stored.

📈 Spending Analysis

SpendWise calculates and displays financial information such as:

Total Income
      ↓
Total Expenses
      ↓
Current Balance
      ↓
Category Spending
      ↓
Daily / Period Spending

The application can present spending information through charts and summary components.

🧪 Testing

The project includes Android testing dependencies such as:

JUnit
AndroidX JUnit
Espresso

Run unit tests using:

.\gradlew.bat test

Run connected Android tests using:

.\gradlew.bat connectedAndroidTest
💻 Requirements

To build and run SpendWise, install:

Android Studio
JDK 21
Android SDK
Android SDK Platform 36
Android device or emulator

The project uses:

Minimum SDK: 24
Target SDK: 36
Compile SDK: 36.1
Java: 21
Gradle: 9.5
⭐ Project Summary

SpendWise demonstrates the development of a modern Android personal finance application using Kotlin, Jetpack Compose, Room Database, ViewModel, Repository, Kotlin Coroutines, and Flow.

The application combines:

Modern Android UI
Local database persistence
CRUD operations
Income and expense tracking
Budget management
Transaction search and filtering
Spending analytics
Reactive UI updates
Profile and application settings
Expense report generation
Theme customization

The primary goal of SpendWise is to provide a simple, structured, and user-friendly solution for personal expense and budget management.
