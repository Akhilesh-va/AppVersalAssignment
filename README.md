# 📱 AppVersal Assignment

A modern Android application built with Jetpack Compose, following clean architecture, designed to authenticate users, fetch campaigns, and showcase Instagram-like story viewing UI with support for both image and video content.

---

## 🚀 Features

- 🔐 **Authentication Screen** using App ID & Account ID
- 📡 **Network API integration** with proper access token handling
- 📋 **Campaign List** – dynamic list of campaigns per user
- 🖼️ **Story Group List** – shows story thumbnails (like Instagram)
- 🎥 **Story Viewer**:
  - Supports images and videos
  - Auto-progress indicator (5s for image, 15s for video)
  - Tap to go next/prev, swipe down to exit
- 📦 **Clean Architecture** (ViewModel, Repository, Data layer)
- ⚙️ **Hilt DI**, **Jetpack Navigation**, **Media3**, **Coil**

---

## 🧱 Tech Stack

| Layer          | Technology                      |
|----------------|----------------------------------|
| UI             | Jetpack Compose, Material 3      |
| Navigation     | Jetpack Navigation Compose       |
| Media          | ExoPlayer (Media3)               |
| Image Loading  | Coil                             |
| DI             | Hilt                             |
| Language       | Kotlin                           |
| Architecture   | MVVM + Clean Architecture        |
| Persistence    | SharedPreferences for token      |

---

## 🛠️ Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Akhilesh-va/AppVersalAssignment/tree/CompletedAssignmnet
