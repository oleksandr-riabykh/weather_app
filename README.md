# 🌦️ WeatherApp

A modern Android weather application built with **Jetpack Compose**, leveraging **Clean Architecture**, **Kotlin Coroutines**, **MWWM/MVI**, and **Hilt** for scalable and maintainable code.

## 📱 Features

- 🌤️ **Current weather & forecast data** - Real-time weather information and multi-day forecasts
- 📍 **Location-based weather updates** - Automatic weather updates based on your current location using FusedLocationProviderClient
- 🔁 **Background location sync** - Seamless location updates using WorkManager for consistent data
- 🔐 **Smart permission handling** - User-friendly location permission requests with rationale dialogs
- ⚡ **Reactive UI** - Modern Compose interface with StateFlow for responsive user experience
- 🧪 **Comprehensive testing** - Full unit and UI test coverage for reliability

## 🧰 Tech Stack

| **Layer** | **Technology**                               |
|-----------|----------------------------------------------|
| **UI** | Jetpack Compose, Navigation Component        |
| **State Management** | MVI (Model-View-Intent) Pattern, SideEffects |
| **Dependency Injection** | Hilt + Custom Dispatchers                    |
| **Asynchronous Operations** | Kotlin Coroutines + Flow                     |
| **Location Services** | FusedLocationProviderClient                  |
| **Background Processing** | WorkManager                                  |
| **Data Persistence** | DataStore Preferences                        |
| **Image Loading** | Coil with AsyncImage                         |
| **Permissions** | Accompanist Permissions                      |
| **Testing** | JUnit5, Turbine, MockK                       |

## 🏗️ Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
📱 Presentation Layer
├── ViewModels (MVI Pattern)
├── UI States & Intents
└── Jetpack Compose UI

🔧 Domain Layer
├── Use Cases (Business Logic)
├── Repository Interfaces
└── Domain Models

💾 Data Layer
├── Repository Implementations
├── Remote & Local Data Sources
└── Data Models
```

### Architecture Benefits

- **Separation of Concerns**: Each layer has a single responsibility
- **Testability**: Easy to unit test business logic in isolation
- **Maintainability**: Clear boundaries make code changes safer
- **Scalability**: Easy to add new features without breaking existing code

## 📋 Requirements

- **Android Studio**: Koala (2024.1.1) or newer
- **Gradle**: Version 8.0+
- **Kotlin**: Version 1.9.10+
- **JAVA**: Version 17
- **Android SDK**:
    - `minSdk = 26` (Android 8.0)
    - `targetSdk = 34` (Android 14)

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/oleksandr-riabykh/WeatherApp.git
cd WeatherApp
```

### 2. Setup Development Environment
1. Open the project in **Android Studio**
2. Wait for Gradle sync to complete
3. Build the project using `Build > Make Project`

### 3. Run the Application
- Connect an Android device or start an emulator
- Click **Run** or press `Shift + F10`
- Grant location permissions when prompted

## 🔐 Permissions & Location

### Required Permissions
The app requires the following permissions for optimal functionality:

- `ACCESS_FINE_LOCATION` - For precise location-based weather data
- `ACCESS_COARSE_LOCATION` - For approximate location when fine location unavailable

### Permission Handling Features
- **Rationale Dialog**: Explains why location access is needed
- **System Prompts**: Native Android permission dialogs
- **Settings Redirect**: Guides users to app settings if permissions are permanently denied
- **Graceful Degradation**: App remains functional with limited features if permissions denied

### Location Update Strategy
- **WorkManager Integration**: Reliable background location updates
- **FusedLocationProviderClient**: Battery-efficient location services
- **DataStore Persistence**: Location data cached locally for offline access
- **Reactive Updates**: UI automatically reflects location changes

## 🧪 Testing

### Unit Tests
Comprehensive testing coverage for core functionality:

- **Domain Layer**: Business logic and use cases
- **ViewModels**: State management and user interactions
- **Repository Pattern**: Data layer abstractions

### Running Tests

```bash
# Run all unit tests
./gradlew testDebugUnitTest

# Run instrumented tests
./gradlew connectedDebugAndroidTest

```

## 🔄 State Management (MVI)

The app implements the **Model-View-Intent** pattern for predictable state management:

```kotlin
// UI State
sealed class MainUiState<out T> {
    data object Idle : MainUiState<Nothing>()
    data object Loading : MainUiState<Nothing>()
    data class Success<T>(val data: T) : MainUiState<T>()
    data class Error(val error: WeatherError) : MainUiState<Nothing>()
}

// User Intents
sealed class MainIntent {
  data class SearchCity(val name: String) : MainIntent()
  data class LoadWeatherByCoordinates(val lat: Double, val lon: Double) : MainIntent()
  data class ForecastItemClicked(val date: String) : MainIntent()
  data class GetSelectedForecast(val date: String) : MainIntent()
  data object CloseDetails : MainIntent()
  data object LocationDenied : MainIntent()
  data object LocationGranted : MainIntent()
}
```

### Benefits of MVI
- **Unidirectional Data Flow**: Predictable state updates
- **Immutable State**: Prevents accidental state mutations
- **Easy Testing**: Clear inputs and outputs
- **Time Travel Debugging**: State history tracking

## 📁 Project Core Structure

```
app/
├── src/main/java/com/life/totally/great
│   ├── presentation/          # UI Layer
│   │   ├── screens/          # Compose screens
│   │   ├── components/       # Reusable UI components
│   │   └── navigation/       # Navigation setup
│   ├── domain/               # Business Logic
│   │   ├── usecases/        # Application use cases
│   │   ├── repository/      # Repository interfaces
│   │   └── models/          # Domain models
│   ├── data/                # Data Layer
│   │   ├── repository/      # Repository implementations
│   │   ├── datasource/      # Local & remote data sources
│   │   └── models/          # Data models
│   └── di/                  # Dependency injection modules
└── src/test/                # Unit tests
```

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Code Style
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Ensure all tests pass before submitting

## 📄 License

```
MIT License

Copyright (c) 2025 Oleksandr

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👨‍💻 Author

**Oleksandr** - *Initial work and ongoing development*

---

<div>

**Built with ❤️ using modern Android development practices**

[Report Bug](https://github.com/oleksandr-riabykh/weather_app?tab=readme-ov-file) • [Request Feature](https://github.com/oleksandr-riabykh/weather_app?tab=readme-ov-file) • [Documentation](https://github.com/oleksandr-riabykh/weather_app?tab=readme-ov-file)

</div>