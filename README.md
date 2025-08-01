# 1000faces – Timelapse Selfie Journal

Name: Cosmo Ilin (both bombayy and cosmoil are my accounts they are just different devices)

1000faces is a minimalist Android app that allows users to capture one selfie per day and later generate a personal timelapse video. 
The app focuses on simplicity, privacy, and long-term self-reflection by encouraging users to document their faces over time.

## Features

- Daily selfie capture with a square camera preview using SurfaceView.
- Scrollable gallery view of all photos with delete and (planned) edit functionality.
- Local photo storage in internal app directories.
- MP4 timelapse export functionality using MediaCodec and MediaMuxer.
- Login and registration system with local SQLite user database.
- Offline-first architecture with plans for optional cloud sync.

## Tech Stack

| Layer        | Technologies                            |
|--------------|-----------------------------------------|
| Language     | Java                                    |
| Architecture | MVC (Model-View-Controller)             |
| UI           | XML Layouts, Material 3                 |
| Storage      | Internal Storage, SQLite                |
| Export       | MediaCodec, MediaMuxer (Android 10+)    |

## Project Structure (MVC)

```
com.cosmo.thousandfaces
├── MainActivity
├── controller/
│   ├── ImageController.java
│   ├── LoginController.java
│   └── TimelapseController.java
├── database/
│   └── DatabaseHelper.java
├── model/
│   ├── ImageModel.java
│   └── User.java
├── view/
│   ├── activities/
│   │   ├── CameraActivity.java
│   │   ├── ExportActivity.java
│   │   ├── GalleryActivity.java
│   │   ├── HomeActivity.java
│   │   ├── LoginActivity.java
│   │   └── RegistrationActivity.java
│   └── adapters/
│       └── ImageAdapter.java
├── res/
│   ├── layout/
│   ├── drawable/
│   └── values/
└── AndroidManifest.xml
```
