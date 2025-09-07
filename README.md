

# 📘 AcadSync: A Smart Academic Timetable Generator

**AcadSync** is a comprehensive Java-based academic timetable generation system built with Object-Oriented Programming principles. It automates the creation of efficient, clash-free class schedules for educational institutions while considering faculty availability, subject load, room capacity, and complex scheduling constraints.

---

## 🚀 Key Features

### 📅 **Smart Scheduling Engine**
- � Intelligent conflict-free timetable generation algorithm
- 🔄 Dynamic regeneration when schedules change
- ⚡ Real-time conflict detection and resolution
- 📊 Optimal resource allocation (faculty, rooms, time slots)

### 🎯 **Advanced Management**
- 🧾 Comprehensive data management (classes, subjects, faculty, rooms)
- �‍🏫 Faculty assignment and availability tracking
- 🏢 Room capacity and utilization management
- 📚 Subject scheduling with hours-per-week constraints

### 📱 **User-Friendly Interface**
- 🖥️ Clean Java Swing GUI with intuitive navigation
- 🔐 Secure role-based authentication (Admin/Faculty)
- 📋 Multiple view modes: Day-wise, Room-wise, Faculty-wise
- 🔍 Interactive schedule browsing and filtering

### 📈 **Visualization & Analytics**
- 📊 Comprehensive schedule views and analytics
- 📤 Export capabilities (PDF/Excel - coming soon)
- 🏢 Room utilization statistics
- 👥 Faculty workload distribution analysis

### 🌐 **Modern Features**
- 💻 Online/offline class support
- 🔔 Real-time notifications and alerts
- 📅 Holiday and special event handling
- 🎛️ Customizable scheduling preferences

---

## 🏗️ Architecture

AcadSync follows a clean **Model-View-Controller (MVC)** architecture:

```
📁 src/
├── 🎯 core/           # Core scheduling logic
│   └── TimetableManager.java
├── 🖥️ gui/            # User interface components
│   ├── LoginGUI.java
│   ├── DashboardGUI.java
│   ├── TimetableView.java
│   └── ...
├── 📊 models/         # Data models
│   ├── Faculty.java
│   ├── Subject.java
│   ├── Room.java
│   └── ...
└── 🛠️ utils/          # Database and utilities
    └── DBHelper.java
```

---

## � Database Schema

The system uses **SQLite** for lightweight, embedded database management:

- **Faculty**: Staff information and availability
- **Subject**: Course details and weekly hours
- **Room**: Classroom capacity and specifications
- **ClassGroup**: Student group organization
- **TimeSlot**: Available scheduling periods
- **TimetableEntry**: Generated schedule assignments

---

## 🚀 Getting Started

### Prerequisites
- ☕ **Java 8+** (JDK required for compilation)
- 📚 **SQLite JDBC Driver** (included: `sqlite-jdbc-3.50.3.0.jar`)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/psreyas09/acadsync.git
   cd acadsync
   ```

2. **Compile the project**
   ```bash
   # Compile with SQLite dependency
   javac -cp "lib/sqlite-jdbc-3.50.3.0.jar:src" src/Main.java src/**/*.java
   ```

3. **Run the application**
   ```bash
   # Run with classpath
   java -cp "lib/sqlite-jdbc-3.50.3.0.jar:src" Main
   ```

### Quick Start

1. **Launch AcadSync** - The login screen will appear
2. **Initial Setup** - Database tables are created automatically
3. **Add Data** - Use the dashboard to add faculty, subjects, and rooms
4. **Generate Schedule** - Click "Generate Timetable" to create schedules
5. **View Results** - Browse schedules by day, room, or faculty

---

## 🎮 Usage Guide

### 👨‍💼 **Admin Functions**
- ➕ **Add/Edit Faculty**: Manage staff and their availability
- � **Subject Management**: Configure courses and weekly hours
- 🏢 **Room Administration**: Set up classrooms and capacities
- 🕐 **Time Slot Configuration**: Define available scheduling periods
- 📊 **Analytics Dashboard**: View utilization and workload statistics

### 👨‍🏫 **Faculty Features**
- 📅 **View Personal Schedule**: See assigned classes and timings
- 🔍 **Check Room Assignments**: Find classroom locations
- 📋 **Schedule Overview**: Access day-wise and week-view schedules

### 🔧 **Schedule Management**
- 🎯 **Smart Generation**: Automatic conflict-free scheduling
- 🔄 **Dynamic Updates**: Real-time schedule regeneration
- 📊 **Multiple Views**: Day-wise, room-wise, and faculty-wise displays
- 🎛️ **Conflict Resolution**: Intelligent handling of scheduling conflicts

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | ☕ Java 8+ |
| **GUI Framework** | 🖥️ Java Swing |
| **Database** | 💾 SQLite |
| **JDBC Driver** | 📚 sqlite-jdbc-3.50.3.0 |
| **Architecture** | 🏗️ MVC Pattern |

---

## � Features in Detail

### 🧠 **Intelligent Scheduling Algorithm**
- Conflict detection for faculty and room double-booking
- Optimal time slot distribution across days
- Faculty workload balancing
- Room capacity matching with class sizes

### 🔍 **Advanced Filtering & Views**
- **Day-wise Schedule**: Complete daily overview
- **Room Timetable**: Individual classroom schedules
- **Faculty Schedule**: Personal teaching assignments
- **All Rooms View**: Campus-wide room utilization

### 📈 **Analytics & Reporting**
- Faculty workload distribution
- Room utilization statistics
- Schedule density analysis
- Conflict resolution reports

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### 🎯 **Areas for Contribution**
- 📤 PDF/Excel export functionality
- 🎨 UI/UX improvements
- 🧪 Unit testing framework
- 📱 Mobile-responsive interface
- 🔧 Performance optimizations

---

## 🐛 Issues & Support

- 🐞 **Bug Reports**: [Open an issue](https://github.com/psreyas09/acadsync/issues)
- 💡 **Feature Requests**: [Suggest improvements](https://github.com/psreyas09/acadsync/issues)
- 📧 **Support**: Contact the development team

---

## 📅 Roadmap

### � **Planned Features**
- [ ] 📤 PDF/Excel export functionality
- [ ] �🌐 Web-based interface
- [ ] 📱 Mobile application
- [ ] 🔄 REST API development
- [ ] 🎨 Enhanced UI themes
- [ ] 📊 Advanced analytics dashboard
- [ ] 🔔 Email notifications
- [ ] 📅 Calendar integration

---

## 📄 License

This project is licensed under the **MIT License** .

---

## 👨‍💻 Author

-**Sreyas P** - [@psreyas09](https://github.com/psreyas09)
-**Akshara**
-**Ashil**
-**Anusree**

---

## 🙏 Acknowledgments

- 🎓 Built as part of academic coursework in Object-Oriented Programming
- 🏫 Designed for educational institutions seeking automated scheduling
- 💡 Inspired by real-world timetabling challenges in academic environments

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

[![GitHub stars](https://img.shields.io/github/stars/psreyas09/acadsync.svg?style=social&label=Star)](https://github.com/psreyas09/acadsync)
[![GitHub forks](https://img.shields.io/github/forks/psreyas09/acadsync.svg?style=social&label=Fork)](https://github.com/psreyas09/acadsync/fork)

</div>

