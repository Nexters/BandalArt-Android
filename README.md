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

|온보딩|메인 목표 입력|메인목표달성|
|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/e00aec2e-d9ca-4057-9a8e-af14b8da89bf.gif">|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/e402dfcb-b490-43fa-9dca-ee843920c187.gif">|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/d554c9bd-0067-429f-acee-10d9bf018f6a.gif">|

|반다라트 추가|반다라트 삭제|반다라트 공유|
|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/b85bbed8-7c2e-4fa5-9a27-f5e327ae71f6.gif">|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/addaf2a8-31f8-4c1c-8cad-c49c5fd24a48.gif">|<img width="240" src="https://github.com/Nexters/BandalArt-Android/assets/51016231/cd9776e0-0be0-46e3-87ea-7ef88e215054.gif">

## Article
[Custom Splash Screen 만드는 방법(Splash Screen API을 사용하는 경우)](https://velog.io/@mraz3068/How-to-make-Custom-Splash-Screen-with-Splash-Screen-API)

## Development

### Required

- IDE : Android Studio Giraffe
- JDK : Java 17을 실행할 수 있는 JDK
- Kotlin Language : 1.9

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

#### Test & Code analysis

- Ktlint
- Detekt
- Kotest

#### Gradle Dependency

- Gradle Version Catalog

## Architecture
Based on [Now In Android](https://github.com/android/nowinandroid) Clean Architecture

<img width="760" alt="image" src="https://github.com/easyhooon/watcha-assignment/assets/51016231/2837a3b6-32f8-46aa-a102-3fb5b3e3826a">

<img width="760" alt="image" src="https://github.com/easyhooon/watcha-assignment/assets/51016231/b29020a1-69aa-482b-8af4-ddb27122a440">

## Package Structure
```
├── app
│   ├── navigation
│   ├── MainActivity
│   └── Application
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

