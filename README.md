# Stock Management App

A comprehensive Android application for managing stock inventory, built with modern Android
development practices and 100% Kotlin.

## Architecture

This application follows Google's recommended app architecture, ensuring scalability,
maintainability, and testability.

- **Pattern**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Layered Architecture**:
    - **Presentation Layer**: Handles UI and user interaction (Activities, Fragments, ViewModels).
    - **Domain Layer**: Contains business logic and use cases.
    - **Data Layer**: Manages data sources local database mappers and repositories.

## Features

### Main Features

- User authentication (mocked)
- CRUD operations for products and suppliers
- Stock level tracking with low stock alerts
- Transaction recording (both incoming stock and sales)
- Search and filter functionality
- Basic data validation
- Error handling with appropriate user feedback

### Bonus Features

- **Barcode Scanning**: Quickly add or lookup items using the device camera.
- **Data Export**: Export inventory data for external use via PDF.
- **Notifications**: Scheduled notifications via **WorkManager**.

## Testing

The codebase includes a robust testing strategy:

- **Unit Tests**: comprehensive coverage for mostly for Repositories for some ViewModels.
- **UI Tests**: Key user scenarios automated using **Espresso**.

## Known issues and limitations

- As the app was developed in fast paced environment I had to come up with design on the go which
  resulted with lots of refactoring of UI components
- Codebase isn't completely covered by tests but with App architecture setting tests were a lot
  easier to setup

## Setup & Installation

### Prerequisites

- Android Studio Ladybug or newer.
- JDK 17.

### Building the App

1. Clone the repository:
   ```bash
   git clone https://github.com/quack2124/stock-management-app.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle files to ensure all dependencies are downloaded.
4. Build the project via `Build > Make Project`.

### Running Tests

- **Unit Tests**: Run `./gradlew test` in the terminal or right-click the `test` directory in
  Android Studio.
- **UI Tests**: Run `./gradlew connectedAndroidTest` or right-click the `androidTest` directory.

### 🔑 Login Credentials

For testing and review purposes, the app uses the following hardcoded credentials:

- **Username**: `test`
- **Password**: `test`