# 🌤️ WeatherApp

A modern Android weather application built with Jetpack Compose that provides real-time weather information, forecasts, and astronomical data for any city around the world.

---

## Features

- **GPS Location** — Automatically detects your current location and loads local weather on launch
- **City Search** — Search for weather in any city worldwide
- **Current Weather** — Real-time temperature, conditions, humidity, wind speed, and more
- **Forecasts** — Multi-day weather forecasts so you can plan ahead
- **Sunrise & Sunset** — Daily sunrise and sunset times for your location
- **Moon Phase** — Current moon phase displayed alongside weather data
- **Saved Cities** — Save your favourite cities for quick access

---

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose (Material 3) |
| Language | Kotlin |
| Async | Kotlin Coroutines & Flow |
| Networking | Ktor |
| Dependency Injection | Koin |
| Local Storage | DataStore |
| Weather Data | WeatherAPI.com |

---

## Architecture

The app follows a clean architecture approach with clear separation of concerns:

```
app/
├── network/           # API calls, DataStore, repositories
├── domain/         # Models, use cases
├── presentation/   # ViewModels, UI state
└── system/         # Screens, navigation, Compose UI
```
