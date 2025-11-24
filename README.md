# CV Builder Application

A JavaFX-based desktop application for creating, previewing, and saving professional curriculum vitae (CV) documents with SQLite database persistence.

## What It Does

This application allows users to:
- Create a new CV from scratch
- Fill in personal information, education, skills, work experience, and projects
- Preview the formatted CV in a professional two-column layout
- View their CV with modern styling and icons
- **Save CVs to local database** for persistent storage
- **Load and manage multiple CVs** from the database

## How to Use - Complete Guide

### 📝 **Creating a New CV**

**Step 1: Home Screen**
- Launch the application
- Click **"Create New CV"** button

**Step 2: Fill Out Form**
- Enter **Full Name** (letters, spaces, dots, hyphens only)
- Enter **Email** (valid format: user@example.com)
- Enter **Phone** (optional, numbers/spaces/symbols: + ( ) -)
- Enter **Address** (multi-line)
- Add **Education** entries (one per line, e.g., "B.Sc. Computer Science - XYZ University")
- List **Skills** (one per line, e.g., "Java", "Python", "JavaScript")
- Describe **Work Experience** (one per line, e.g., "Software Engineer at ABC Corp (2020-2023)")
- List **Projects** (one per line, e.g., "E-commerce Website with React and Node.js")
- Click **"Generate CV"**

**Step 3: Preview & Save**
- Review your formatted CV (two-column professional layout)
- Click **"Save to Database"** to store the CV
- Success message shows CV ID
- Click **"← Back to Home"** to return

---

### 💾 **Loading & Managing Saved CVs**

**Step 1: Access CV List**
- From Home screen, click **"Load Saved CVs"**
- Table displays all saved CVs with ID, Name, Email, Phone, Date

**Step 2: Search CVs**
- Use search bar to filter by name or email
- Click **"Refresh"** to reload the list

**Step 3: View a CV**
- Select a CV from the table
- Click **"View CV"** to see preview (read-only)

**Step 4: Edit a CV**
- Select a CV from the table
- Click **"Edit CV"** to load into form
- Make your changes
- Click **"Generate CV"** → **"Save to Database"** (updates existing CV)

**Step 5: Delete a CV**
- Select a CV from the table
- Click **"Delete CV"**
- Confirm deletion in popup dialog
- CV permanently removed from database

---

### 🔄 **Navigation**
- **Home → Create New CV → Form → Preview → Save**
- **Home → Load Saved CVs → View/Edit/Delete**
- All screens have **"← Back to Home"** button
- Database operations run in background (no UI freezing)

## Features

### Core Functionality
- **Input Validation:** Ensures data quality with real-time error messages
- **Modern UI:** Clean design with FontAwesome icons and CSS styling
- **Two-Column Layout:** Professional CV format with dark sidebar and white main content
- **Dynamic Content:** Sections automatically populate based on user input
- **Success Notification:** Confirmation message when CV is created

### Database & Persistence
- **SQLite Integration:** Local database (`cvbuilder.db`) for persistent CV storage
- **Async Operations:** Background database tasks using JavaFX Service/Task to prevent UI freezing
- **ObservableList:** Reactive data binding for skills, education, experience, and projects
- **JSON Storage:** List data stored as JSON in database for easy serialization
- **CRUD Operations:** Full create, read, update, and delete functionality

## Technical Stack

- **JavaFX 21** - UI framework
- **SQLite JDBC** - Database connectivity (`cvbuilder.db` file)
- **Gson** - JSON conversion for list data
- **FontAwesomeFX** - Icon library (8 icons used)
- **Maven** - Build and dependency management
- **Java Concurrency** - Service/Task for async database operations

## Running the Application

```cmd
.\mvnw.cmd clean javafx:run
```

Or use IntelliJ IDEA run configurations (already configured).

## Database File

- Location: `cvbuilder.db` (created in project root on first run)
- Tables: `cvs` with columns for personal info and JSON-encoded lists
- Auto-creates on app startup if not exists