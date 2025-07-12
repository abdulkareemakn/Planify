<h1 align="center">Welcome to Planify 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.0-blue.svg?cacheSeconds=2592000" />
  <a href="URL" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="URL" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
  <a href="https://twitter.com/notabdulkareem" target="_blank">
    <img alt="Twitter: notabdulkareem" src="https://img.shields.io/twitter/follow/notabdulkareem.svg?style=social" />
  </a>
</p>

> An Event Building and Management System written in Java and JavaFX.

### ✨ [Demo](URL)

## Features
- Admin Panel for User Login with Password Hashing.
- Dashboard with statistics. 
- Quick Links to View/Add Events.
- View Events page with complete detail of all events.
- Multiple Event Types Supported with unique fields for each.
- Creation and Deletion of Events
- Database driven operations using asynchronous queries.

## Screenshots
- Login Page
![Login Page](./resources/Login_Page.png)

- Reset Password
![Reset Password](./resources/Reset_Password.png)

- Dashboard
![Dashboard](./resources/Dashboard.png)

- Events Dashboard 
![Events Dashboard](./resources/Events.png)

- Birthday Event
![Birthday Event Creation](./resources/Birthday_Event.png)

- Seminar Event
![Seminar Event Creation](./resources/Seminar_Event.png)

- Wedding Event
![Wedding Event Creation](./resources/Wedding_Event.png)
## Install

### Prerequisites
- Java Development Kit (JDK) 21 or higher.

#### 1. Clone the Repository 
```sh
git clone https://github.com/abdulkareemakn/Planify
cd Planify
```

#### 2. Built the Project
```sh
mvn clean install
```

#### Run the Application
```sh
mvn javafx:run
```

## Project Structure

```
Planify
├── db                                 # The SQLite database file is stored here
├── resources                          # Contains screenshots
├── LICENSE                            # MIT License
├── mvnw
├── mvnw.cmd
├── pom.xml                            # Maven Build File
├── README.md
├── src
│   └── main
│       ├── java
│       │   └── org
│       │       └── example
│       │           └── planifyfx
│       │               ├── controller  # Controller Classes (EventController, DashboardController)
│       │               ├── deps        # Dependencies (DBFX-Framework)
│       │               ├── model       # Model Classes (Event, Client)
│       │               ├── repository  # DAO Classes for Database Integration
│       │               └── util        # Utility Classes (Statistics, SceneManager)
│       └── resources
│           └── org
│               └── example
│                   └── planifyfx
│                       ├── db         # Database Schema
│                       └── fxml       # FXML files

```

## Technologies Used
- **Language**: Java 21
- **Framework**: JavaFX
- **Styling**: JavaFX CSS
- **Build Tool**: Maven
- **Database**: SQLite
- **OOP Concepts**: Encapsulation, Abstraction, Inheritance, Generics

## Credits
This project uses the [DBFX-Framework](https://github.com/WaqarAhmad321/dbfx-framework) created by [Waqar Ahmad](https://github.com/WaqarAhmad321). The majority of the database operations were done using this framework. Do check it out.

## Author

👤 **Abdul Kareem**

* Website: abdulkareem.codes
* Twitter: [@notabdulkareem](https://twitter.com/notabdulkareem)
* Github: [@abdulkareemakn](https://github.com/abdulkareemakn)

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2025 [Abdul Kareem](https://github.com/abdulkareemakn).<br />
This project is [MIT](https://github.com/abdulkareemakn/Planify/blob/main/LICENSE) licensed.
