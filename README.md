# CV Builder Application

A JavaFX-based desktop application for creating and previewing professional curriculum vitae (CV) documents.

## What It Does

This application allows users to:
- Create a new CV from scratch
- Fill in personal information, education, skills, work experience, and projects
- Preview the formatted CV in a professional two-column layout
- View their CV with modern styling and icons

## How It Works

The application follows a simple three-screen flow:

### 1. Home Screen
- Welcome screen with a "Create New CV" button
- Clean interface to start the CV creation process

### 2. Input Form
- Enter personal details: Full Name, Email, Phone, Address
- Add multiple education entries (one per line)
- List skills (one per line)
- Describe work experience (one per line)
- List projects (one per line)
- Form includes validation for:
  - Name cannot contain numbers
  - Valid email format required
  - Valid phone number format
- Click "Generate CV" to proceed

### 3. Preview Screen
- **Left Sidebar:** Contact information with icons (email, phone, address) and skills list
- **Right Panel:** Name displayed prominently, followed by Education, Experience, and Projects sections
- Professional styling with blue accents and modern typography
- Each section clearly separated with headers and icons

## Features

- **Input Validation:** Ensures data quality with real-time error messages
- **Modern UI:** Clean design with FontAwesome icons and CSS styling
- **Two-Column Layout:** Professional CV format with dark sidebar and white main content
- **Dynamic Content:** Sections automatically populate based on user input
- **Success Notification:** Confirmation message when CV is created