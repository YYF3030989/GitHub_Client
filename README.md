# GitHub Client App

A modern Android application built with Kotlin and Jetpack Compose, designed to explore GitHub users and their public activities. This project demonstrates clean architecture, efficient paging, OAuth login integration, and localization support — optimized for performance and scalability.

![screenshot](./screenshots/github_client_ui.png)

## Why This Project?
This app was built to deepen my Android development skills and demonstrate:
- **Clean architecture principles**
- **Dependency injection**
- **Asynchronous UI with Compose**
- **GitHub API integration**
- **Kotlin best practices**

## ✨ Features

- **List GitHub Users** by name with instant feedback
- **User Detail Page** showing user profile and latest events
- **Event Feed** with lazy loading (pagination) using Paging 3
- **GitHub OAuth Login** with secure token handling
- **Localization Support** (English & Japanese)
- **Dependency Injection** using Hilt
- **Unit-Testable Architecture** with repository pattern

---

## 🛠️ Built With

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit + OkHttp](https://square.github.io/retrofit/)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Hilt](https://dagger.dev/hilt/)
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [GitHub REST API v3](https://docs.github.com/en/rest)

---

## 📁 Architecture
<pre>
com.example.githubclient 
│   App.kt
│   MainActivity.kt
├───core
│   ├───network     // Retrofit client generator 
│   └───util        // Secure token storage 
├───data    
│   ├───model       // Data models from GitHub API 
│   └───remote
│       ├───api     // GitHub API & Auth Services 
│       └───paging  // PagingSources for events and users 
├───domin
│   └───repository
├───presentation
│   ├───common      // UI constants/utilities 
│   ├───component   // Reusable UI components 
│   ├───screen      // Screens (Compose) 
│   └───viewmodel   // ViewModels with state handling 
└───ui
    └───theme       // App theming 
</pre>

---

## 🔑 GitHub OAuth Setup

1. Create a `.github.properties` file in the root project directory:
```
client_id=your_client_id 
client_secret=your_client_secret
```

> ⚠️ This file is `.gitignored` by default to keep your credentials safe.

---

## 🌐 Localization

App is available in:

- 🇺🇸 English
- 🇯🇵 日本語 — with helpful user indicators like `"すべてのイベントを表示しました"` ("All events displayed")

---

## 📦 Future Improvements

- 🔔 Push notifications for user events
- 🌙 Dark mode support
- 💬 More detailed user insights & follower graph
- 🔍 Search GitHub Users by name with instant feedback

---

## 💼 About the Developer

👨‍💻 **YANG Yifan**  
Android Engineer.  
Experienced in Kotlin, Jetpack Compose, NDK, GNSS integration, and large-scale SDK maintenance.

Looking for new challenges in mobile innovation — feel free to [connect on LinkedIn](https://www.linkedin.com)!

---

## 🖼️ UI Preview

![screenshot](./screenshots/github_client_ui.png)

---

## 📄 License

MIT License - feel free to use or adapt this project for your needs.

