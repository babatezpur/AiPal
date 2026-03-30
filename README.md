# AiPal - Your AI Friend

A native Android app that brings AI-powered facts, quotes, and Q&A conversations to your fingertips. Built with Kotlin and Jetpack Compose, powered by a Flask backend wrapping OpenAI's GPT-4o-mini.

<!-- Screenshots section — see instructions below on how to add images -->
## Screenshots

<p float="left">
  <img src="screenshots/home.png" width="180" />
  <img src="screenshots/search.png" width="180" />
  <img src="screenshots/chat.png" width="180" />
  <img src="screenshots/profile.png" width="180" />
  <img src="screenshots/favourites.png" width="180" />
</p>

## Features

### Facts & Quotes
- Search any topic to get AI-generated facts or quotes
- Save your favourites with a single tap
- Recent activity feed on the home screen for quick access to past searches

### Q&A Conversations
- Start AI-powered conversations on any topic
- WhatsApp-style chat UI with message bubbles
- Up to 5 messages per conversation
- Conversation history with local caching

### Favourites
- Browse saved facts and quotes in two view modes:
  - **Carousel** — swipeable cards with scale and fade animations
  - **List** — compact scrollable view with content preview
- Copy content to clipboard with one tap
- Tap truncated text to read the full content
- Unsave items directly from either view

### Profile
- Editable display name
- Profile photo from gallery (stored locally)
- Quick access to Favourites
- Logout with confirmation dialog

### Authentication
- Email and password signup/login
- JWT-based session management with encrypted local storage
- Automatic session restoration on app launch

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM (ViewModel + Repository) |
| Navigation | Jetpack Navigation Compose |
| Networking | Retrofit + OkHttp |
| Local Database | Room |
| Dependency Injection | Hilt |
| Image Loading | Coil |
| Token Storage | Encrypted DataStore |
| Backend | Flask + PostgreSQL + OpenAI GPT-4o-mini |

## Architecture

```
┌─────────────────────────────────────────────┐
│                   UI Layer                   │
│   Screens (Composables) ← ViewModels        │
│        StateFlow / Compose State            │
├─────────────────────────────────────────────┤
│                 Data Layer                   │
│   Repositories                              │
│     ├── Remote: Retrofit APIs               │
│     └── Local: Room DB + DataStore          │
├─────────────────────────────────────────────┤
│                   DI Layer                   │
│   Hilt Modules (Network, Database)          │
└─────────────────────────────────────────────┘
```

## Project Structure

```
com.saptarshi.aipal/
├── di/                  # Hilt dependency injection modules
├── data/
│   ├── remote/          # Retrofit APIs, DTOs, auth interceptor
│   ├── local/           # Room (DAOs, entities), DataStore (JWT)
│   └── repository/      # Data orchestration (remote + local)
├── domain/model/        # Clean domain models
├── ui/
│   ├── auth/            # Login & Signup screens
│   ├── home/            # Home tab, Facts & Quotes search
│   ├── chat/            # Chat list & conversation screens
│   ├── profile/         # Profile screen with photo & name editing
│   ├── favourites/      # Carousel & list views for saved items
│   ├── components/      # Reusable composables (tiles, cards, bubbles)
│   ├── navigation/      # App & main nav graphs, bottom nav
│   └── theme/           # Colors, typography, shapes
└── utils/               # Resource sealed class, extensions
```

## Backend

The app communicates with a Flask REST API deployed on Render.

**Live API:** https://ai-toolkit-9ycz.onrender.com

> Free tier — sleeps after 15 min of inactivity. First request after sleep takes 30-60s.

Key endpoints:
- `POST /auth/register` and `POST /auth/login` — authentication
- `POST /facts/` and `POST /quotes/` — AI-generated content
- `POST /conversation/start` and `POST /conversation/message` — Q&A chat
- `GET /favourites/`, `POST /favourites/`, `DELETE /favourites/<id>` — saved items

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17+
- Android SDK 34+
- Min SDK: 26 (Android 8.0)

### Setup
1. Clone the repository
   ```bash
   git clone https://github.com/babatezpur/AiPal.git
   ```
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run on an emulator or physical device

No additional configuration needed — the app points to the live backend by default.

## License

This project is for personal/educational use.
