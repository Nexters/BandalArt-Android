# 반다라트 Android
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.0-green.svg)](https://gradle.org/)
[![Android Studio](https://img.shields.io/badge/Android%20Studio-2022.3.1%20%28Giraff%29-green)](https://developer.android.com/studio)
[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-24-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-34-orange)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
<br/>

부담 없는 만다라트 계획표로 당신의 목표를 더욱 선명하게, 반다라트 [PlayStore](https://play.google.com/store/apps/details?id=com.nexters.bandalart.android&hl=en-KR)
<br/>
![반다라트 그래픽이미지 2](https://github.com/Nexters/BandalArt-Android/assets/51016231/a357f7aa-d086-47de-bbac-d423cdaffdbe)
<br/>

<p align="center">
<img src="https://github.com/Nexters/BandalArt-Android/assets/51016231/541f9309-bb9a-4131-be46-ac7df5f74fc1" width="30%"/>
<img src="https://github.com/Nexters/BandalArt-Android/assets/51016231/3af26254-8c48-4e53-b79a-9f9764427a60" width="30%"/>
<img src="https://github.com/Nexters/BandalArt-Android/assets/51016231/c772cc49-75df-4e2a-94f7-9d6c1f3e1aa3" width="30%"/>
</p>

## Features

온보딩, 메인 목표 입력


https://github.com/Nexters/BandalArt-Android/assets/51016231/dafd3d98-bf9d-4bcf-a659-f7c5aee4f579



반다라트 목표 달성


https://github.com/Nexters/BandalArt-Android/assets/51016231/acabf0e8-052c-4d25-aa7b-4a6117e7285a



반다라트 링크 공유(웹 페이지 연동)


https://github.com/Nexters/BandalArt-Android/assets/51016231/f8c44a9f-19b7-40aa-a283-d388e776ebf6


반다라트 목록 조회, 추가, 삭제


https://github.com/Nexters/BandalArt-Android/assets/51016231/00e649dc-5623-44ce-ba80-db0ffa96afcf

## Development

### Required

- IDE : Android Studio Giraffe
- JDK : Java 17을 실행할 수 있는 JDK
- Kotlin Language : 1.8.22

### Language

- Kotlin

### Libraries

- AndroidX
  - Activity & Activity Compose
  - Core
  - Lifecycle & ViewModel Compose
  - Navigation
  - DataStore
  - StartUp
  - Splash

- Kotlin Libraries (Coroutine, DateTime, Serialization)
- Compose
  - Material3
  - Navigation

- Dagger Hilt
- Ktor
- Retrofit
- Timber
- Lottie
- Facebook Shimmer
- Firebase(Analytics, Crashlytics)

#### Code analysis

- Ktlint
- Detekt

#### Gradle Dependency

- Gradle Version Catalog

## Architecture
[Google App Architecture](https://developer.android.com/topic/architecture)

## Package Structure
```
├── app
│   ├── navigation
│   └── MainActivity
├── build-logic
├── buildSrc
├── core
│   ├── data
│   ├── datastore
│   ├── designsystem
│   ├── domain
│   ├── network
│   └── ui
├── fastlane
├── feature
│   ├── complete
│   ├── home
│   ├── onboarding
│   └── splash
├── gradle
│   └── libs.versions.toml
└── report
    ├── compose-metrics
    └── compose-reports
```


## Developer

|이지훈|이석규|
|:-:|:-:|
|<img src="https://github.com/Nexters/BandalArt-Android/assets/51016231/e7b05305-b831-4c81-8635-84b478726c55" width=200>|<img src="https://github.com/Nexters/BandalArt-Android/assets/51016231/bbcf9941-5fbb-4f8a-8e8d-8f78db396808" width=200>|
|[@easyhooon](https://github.com/easyhooon)|[@likppi10](https://github.com/likppi10)|
<br/>

