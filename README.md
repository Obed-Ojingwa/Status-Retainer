
# 📱 WhatsApp Status Saver

A simple and efficient Android application built with **Kotlin** that allows users to save WhatsApp statuses (images and videos) directly to their device. Unlike the default WhatsApp behavior where statuses disappear after 24 hours, this app extends the retention period to **7 days**, giving users more flexibility to revisit and manage their saved content.

---

## ✨ Features
- Save **images and videos** from WhatsApp statuses.
- Retain saved statuses for up to **7 days**.
- Clean and intuitive **Material Design UI**.
- Lightweight and fast performance.
- Built entirely with **Kotlin** and Android’s modern architecture components.

---

## 🛠️ Tech Stack
- **Language:** Kotlin  
- **Framework:** Android SDK  
- **Architecture:** MVVM (Model–View–ViewModel)  
- **UI:** XML layouts + Material Components  
- **Storage:** Local file system with automatic cleanup after 7 days  

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version recommended)  
- Gradle (managed automatically by Android Studio)  
- Minimum SDK: 21 (Android 5.0 Lollipop)  
- Target SDK: 34  

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Obed-Ojingwa/Status-Retainer.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle files (`File → Sync Project with Gradle Files`).
4. Build and run on an emulator or physical device.

---

## 📂 Project Structure
```
StatusSaver/
 ├── app/
 │   ├── src/
 │   │   ├── main/
 │   │   │   ├── java/com/example/statussaver/   # Kotlin source files
 │   │   │   ├── res/                            # Layouts, drawables, values
 │   │   │   └── AndroidManifest.xml
 │   ├── build.gradle
 ├── build.gradle (Project-level)
 └── settings.gradle
```

---

## ⚙️ How It Works
1. The app scans WhatsApp’s hidden status directory.  
2. Users can preview and save statuses they like.  
3. Saved statuses are stored in the app’s local folder.  
4. A background job automatically deletes files older than **7 days**.  

---

## 📸 Screenshots
*(Add screenshots here once available)*

---

## 🤝 Contributing
Contributions are welcome!  
- Fork the repo  
- Create a new branch (`feature/your-feature`)  
- Commit changes  
- Submit a pull request  

---

## 📜 License
This project is licensed under the MIT License. See the `[Looks like the result wasn't safe to show. Let's switch things up and try something else!]` file for details.

---
