# Campus Feedback System

A clean, modern, professional desktop application featuring a **web-inspired desktop GUI built purely with Java Swing / AWT** alongside a full console/CLI mode. Developed as a Software Engineering college mini-project and submitted as an Object-Oriented Programming (OOP) Lab Record. Built purely with Core Java (JDK 17+), without external dependencies, build tools, frameworks, databases, HTML, or JavaScript.

---

## 📌 Project Overview

The **Campus Feedback System** allows college students to provide structured ratings and reviews for key campus entities, categorized into:
- **Events:** Tech Fest 2026, Sports Day 2026, Cultural Night, Hack-the-Cloud Hackathon
- **Courses:** Cloud Computing (CS401), Data Structures & Algorithms (CS201), Machine Learning & AI (CS450), Database Management Systems (CS302)
- **Facilities:** Central Library, Hostel Mess & Dining, Campus Wi-Fi Network, Student Sports Complex

Campus Administrators can view, filter, and analyze student submissions through aggregated performance reports, identify the highest and lowest rated targets, and track campus-wide satisfaction.

---

## ✨ Features

- **Modern Web-Inspired Desktop GUI (Java Swing / JFrame):**
  - Polished single-window SPA-like dashboard with responsive card layouts and sleek dark sidebar navigation.
  - Interactive 5-star rating selector powered by custom Java2D vector stars with live highlight and verbal descriptions (`5 Stars (Excellent)`).
  - Modern `JTable` with alternating row highlights, custom column formatting, and color-coded category badges (Event: Purple, Course: Sky Blue, Facility: Teal).
  - Built-in placeholder text and interactive glowing focus borders on all text fields.
  - Real-time character counter for review text areas (`X / 300 characters (min 5)`).
  - KPI summary metric cards (Total Feedback, Campus Average Rating, Top Target, Lowest Target).
  - Real-time substring search filtering all feedback submissions by student, target, or comment text.
  - Zero emoji font dependencies: 100% vector-drawn Java2D icons (`IconFactory`) that never display missing glyph boxes (`[]`).
- **Dual Presentation Layer (GUI & CLI):**
  - Launches the modern Swing GUI by default.
  - Seamlessly supports `--cli` argument or automated headless fallback for traditional terminal menus.
- **Role-Based Access Control:** Dedicated portals for **Students** and **Administrators**.
- **Student Sign-Up & Login:** Secure authentication with duplicate username detection and minimum password length enforcement.
- **Default Administrator Account:** Automatically initialized on first launch (`admin` / `admin123`).
- **24-Hour Duplication Rule:** Prevents spamming by barring duplicate reviews for the same target by the same student within 24 hours.
- **Personal Feedback History:** Reverse-chronological history display sorted using custom `java.util.Comparator`.
- **Administrative Filtering & Analytics:** Filter feedback by category and generate executive summaries via `Reportable.generateReport()`.
- **Self-Healing CSV Persistence:** Robust file-based storage with RFC 4180-compliant CSV escaping, automatic folder creation, and user privacy protection.
- **Background Auto-Save Thread:** Demonstrates Java multithreading via a daemon thread periodically persisting data.

---

## 🛠️ Technologies Used

- **Language:** Core Java (JDK 17+, tested with JDK 25 LTS).
- **GUI Toolkit:** Java Swing & AWT (`JFrame`, `JPanel`, `CardLayout`, `JTable`, `Graphics2D`, `GeneralPath`).
- **Storage / Database:** Local plain-text CSV file persistence via `java.io` (`BufferedReader`, `BufferedWriter`).
- **Design Architecture:** Model-View-Service-Repository pattern with strict package modularity.
- **External Dependencies:** **None** (zero third-party libraries, Maven/Gradle plugins, or external runtimes required).

---

## 🏛️ OOP Concepts Demonstrated

This project is tailored for academic lab evaluation and viva examinations. Every core OOP concept is implemented in clear, beginner-friendly code:

| OOP Concept | Implementation Highlights |
|---|---|
| **Encapsulation** | Private fields, validation invariants, public getters and setters across all models. |
| **Inheritance & Abstract Classes** | `abstract class User` $\rightarrow$ `Student`, `Admin`<br>`abstract class FeedbackTarget` $\rightarrow$ `Event`, `Course`, `Facility`<br>`JFrame` $\rightarrow$ `MainFrame`, `JPanel` $\rightarrow$ `LoginPanel`, `StudentDashboard`, `AdminDashboard` |
| **Interfaces & Polymorphism** | `Storage<T>` implemented by `UserFileStorage` and `FeedbackFileStorage`<br>`Reportable` implemented by `ReportService` |
| **Method Overriding** | `@Override` of `getRoleDescription()`, `toString()`, `generateReport()`, and `paintComponent()` |
| **Method Overloading** | Overloaded `submitFeedback(...)` and `generateReport(...)` signatures |
| **Constructors & Chaining** | Primary and overloaded constructors chaining with `this(...)` and `super(...)` |
| **Static & Final Modifiers** | Static counter in `User`, static constants and utility methods in `Validator`, `final` entity IDs and `Theme` constants |
| **Custom Checked Exceptions** | `InvalidLoginException`, `DuplicateUserException`, `InvalidFeedbackException`, `DuplicateFeedbackException` |
| **Resource Management** | Safe `try-catch-finally` handling and deterministic `try-with-resources` stream closures |
| **Collections & Sorting** | `ArrayList`, `HashMap`, and `Comparator` sorting |
| **File I/O** | `BufferedReader` and `BufferedWriter` reading and writing CSV files |
| **Multithreading** | Daemon background thread (`AutoSaveService`) running `Runnable` |
| **GUI Event-Driven Programming** | Java Event Dispatch Thread (EDT), `ActionListener`, `DocumentListener`, and `WindowAdapter` |

For a complete line-by-line mapping table, see [docs/oop_concepts.md](docs/oop_concepts.md).

---

## 📂 Folder Structure

```
SWE_A1/
├── bin/                          # Compiled Java bytecode (.class files - ignored by Git)
├── data/                         # CSV storage directory
│   ├── .gitkeep                  # Preserves folder in Git repository
│   ├── feedback.csv              # Student feedback records (ignored by Git)
│   └── users.csv                 # User credentials and accounts (ignored by Git)
├── docs/                         # Software Engineering documentation
│   ├── class_diagram.md          # Complete Mermaid class diagram
│   ├── oop_concepts.md           # OOP concepts mapping table for lab records
│   ├── requirements.md           # Software Requirements Specification (SRS)
│   ├── sample_output.md          # Real console test run transcript
│   └── viva_questions.md         # Comprehensive Viva Voce Q&A preparation guide
├── src/                          # Java source code
│   ├── exception/                # Custom checked domain exceptions
│   │   ├── DuplicateFeedbackException.java
│   │   ├── DuplicateUserException.java
│   │   ├── InvalidFeedbackException.java
│   │   └── InvalidLoginException.java
│   ├── main/                     # Application entry point
│   │   └── Main.java
│   ├── model/                    # Domain entities and OOP hierarchies
│   │   ├── Admin.java
│   │   ├── Course.java
│   │   ├── Event.java
│   │   ├── Facility.java
│   │   ├── Feedback.java
│   │   ├── FeedbackTarget.java
│   │   ├── Student.java
│   │   └── User.java
│   ├── service/                  # Business logic and analytical services
│   │   ├── AuthService.java
│   │   ├── AutoSaveService.java
│   │   ├── FeedbackService.java
│   │   ├── Reportable.java
│   │   └── ReportService.java
│   ├── storage/                  # Persistence contracts and CSV implementations
│   │   ├── FeedbackFileStorage.java
│   │   ├── Storage.java
│   │   └── UserFileStorage.java
│   ├── ui/                       # Modern Java Swing / AWT GUI presentation layer
│   │   ├── AdminDashboard.java   # Admin portal view
│   │   ├── CustomComponents.java # Modern buttons, star rating selector, alert banners, KPI cards
│   │   ├── IconFactory.java      # Pure Java2D vector icon renderer (no broken emojis)
│   │   ├── LoginPanel.java       # Sign in & registration forms with placeholders
│   │   ├── MainFrame.java        # Main responsive JFrame container
│   │   ├── StudentDashboard.java # Student portal view
│   │   └── Theme.java            # Web-style design tokens, colors & typography
│   └── util/                     # Validation rules and console helpers
│       ├── InputHelper.java
│       └── Validator.java
├── build_and_run.bat             # One-click Windows build & run script
├── build_and_run.sh              # One-click Linux/macOS build & run script
└── README.md                     # Project overview and run guide
```

---

## 🗄️ Database & Data Storage

The application uses an embedded, zero-configuration CSV database engine located in the `data/` folder:

### Database Schemas:
- **`data/users.csv`**:
  ```csv
  id,username,password,fullName,role,extraField
  ```
  *(For Students, `extraField` stores the roll number. For Admins, `extraField` stores the administrative department).*
- **`data/feedback.csv`**:
  ```csv
  feedbackId,studentUsername,targetType,targetId,targetName,rating,comment,timestamp
  ```

### Automatic Self-Initialization:
- **No Database Installation Required**: On first launch, the application automatically verifies the `data/` folder, creates `users.csv` and `feedback.csv` if missing, and initializes the default administrator profile.
- **Data Protection & Privacy**: Actual user databases containing real passwords and personal data are strictly excluded from version control via `.gitignore`.

---

## 🚀 How to Compile and Run

### Prerequisites
- Java Development Kit (JDK 17 or later, e.g. JDK 21 / 25).
- Verify installation by running:
  ```bash
  javac -version
  java -version
  ```

---

### Step 1: Compile the Project

From the root project directory (`SWE_A1`), compile all Java source files into the `bin` directory:

#### On Windows (PowerShell):
```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)
```

#### On Windows (Command Prompt - CMD):
```cmd
if not exist bin mkdir bin
javac -d bin src/exception/*.java src/model/*.java src/storage/*.java src/service/*.java src/util/*.java src/ui/*.java src/main/*.java
```

#### On Linux / macOS / Bash:
```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
```

---

### Step 2: Run the Application

#### Launch Modern Java GUI (Default):
```bash
java -cp bin main.Main
```
*(Or simply double-click `build_and_run.bat` on Windows or execute `./build_and_run.sh` on Linux/macOS)*

#### Launch Console / CLI Menu:
```bash
java -cp bin main.Main --cli
```

---

## 🔑 Default Credentials

- **Administrator Account (Auto-Created):**
  - **Username:** `admin`
  - **Password:** `admin123`
- **Student Account:**
  - Create any new student account using the **Sign Up** tab in the GUI or option **2** in the CLI.

---

## 👤 Author & Contributor

- **Name:** Gokkul K S
- **GitHub:** [@gokkul-ks](https://github.com/gokkul-ks)
- **Project:** Software Engineering Mini-Project & OOP Lab Record Submission

---

## 📋 Documentation Quick Links
- [Software Requirements Specification (SRS)](docs/requirements.md)
- [Mermaid Class Diagram](docs/class_diagram.md)
- [OOP Concepts Lab Record Mapping](docs/oop_concepts.md)
- [Viva Voce Q&A Preparation Guide](docs/viva_questions.md)
- [Complete Console Test Run Output](docs/sample_output.md)
