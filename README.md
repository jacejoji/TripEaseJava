# 🚗 TripEase:Automation of Travel Agency (ATA)

A Java/J2EE-based application for managing vehicle reservations, user profiles, routes, and payments. Designed for both standalone (Swing) and web (JSP/Servlet) environments.

---

## 📁 Project Structure

```plaintext
TripEaseJava/
├── .classpath                # Eclipse classpath configuration
├── .project                  # Eclipse project configuration
├── README.md                 # Project documentation
├── bin/                      # Compiled class files
├── src/                      # Source code
│   └── com/
│       └── lnt/
│           └── ata/
│               ├── bean/     # Data model classes (e.g., ProfileBean, VehicleBean)
│               ├── dao/      # DAO interfaces and implementations
│               ├── service/  # Service interfaces (Administrator, Customer)
│               ├── servlet/  # Servlet controllers for web application
│               ├── ui/       # Swing-based UI classes
│               └── util/     # Utility classes (e.g., DBUtil, Authentication)
└── DD_ATA.pdf                # Database design document
```



---

## 🧩 Features

- **Admin:**
  - Add, modify, delete vehicles, drivers, and routes
  - View booking details
  - Allot drivers to reservations

- **Customer:**
  - View available vehicles and routes
  - Book and cancel reservations
  - View and print booking details

- **Authentication:**
  - Login/logout
  - Change password
  - Register new users

- **Payment:**
  - Credit card validation
  - Fare calculation

---

## 🛠️ Technologies Used

- Java SE (Swing for GUI)
- Java EE (Servlets, JSP for Web)
- JDBC for database connectivity
- Oracle/MySQL (configurable)
- MVC Architecture

---

## 🚀 Getting Started

### Prerequisites

- JDK 8+
- Apache Tomcat (for web deployment)
- Oracle/MySQL database
- IDE (Eclipse/IntelliJ)

### Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/jacejoji/TripEaseJava.git
Import into your IDE as a Java project.

Configure database connection in DBUtil.java.

Run the application:

For GUI: Launch MainUI.java
For Web: Deploy to Tomcat and access via browser
🗃️ Database Schema
Includes tables:

ATA_TBL_User_Credentials
ATA_TBL_User_Profile
ATA_TBL_Vehicle
ATA_TBL_Driver
ATA_TBL_Route
ATA_TBL_Reservation
ATA_TBL_CreditCard
Refer to DD_ATA.pdf for detailed schema and sequences.

📄 License

This project is licensed under the MIT License.
