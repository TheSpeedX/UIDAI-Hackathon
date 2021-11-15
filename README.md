
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]




<br />
<p align="center">
  <a href="https://github.com/TheSpeedX/UIDAI-Hackathon">
    <img src="assets/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h1 align="center"><B>UVerifier</h1>

  <p align="center">
    Your Solution to Aadhar based Auth.
    <br />
    <a href="https://github.com/TheSpeedX/UIDAI-Hackathon/"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/TheSpeedX/UIDAI-Hackathon/">View Demo</a>
    ·
    <a href="https://github.com/TheSpeedX/UIDAI-Hackathon/issues">Report Bug</a>
    ·
    <a href="https://github.com/TheSpeedX/UIDAI-Hackathon/issues">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#screenshots">ScreenShots</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project 

![](assets/1.png)



### Built With

This UVerifier system consist of two application systems - Web and Android

* Flutter
* Python
* JAVA
* FastAPI
* NodeJS
* MongoDB
* Express
* Swagger
* Json Web Tokens(JWT)

#### Native Android Dependencies
##### Project level 

  classpath "com.android.tools.build:gradle:7.0.0"  
  classpath 'com.google.gms:google-services:4.3.10'  
  
##### App level
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'
    implementation files('libs/zip4j-2.9.0.jar')
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    def lottieVersion = "3.4.0"
    implementation "com.airbnb.android:lottie:$lottieVersion"

#### Libraries Used
Apart from the base required libraries from android studio

-   [Foundation](https://developer.android.com/jetpack/components)  - Components for core system capabilities and support for multidex and automated testing.
    -   [AppCompat](https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat)  - Degrade gracefully on older versions of Android.
    -   [Test](https://developer.android.com/training/testing/)  - An Android testing framework for unit and runtime UI tests.
-   [Architecture](https://developer.android.com/jetpack/arch/)  - A collection of libraries that help you design robust, testable, and maintainable apps. Start with classes for managing your UI component lifecycle and handling data persistence.
    -   [Data Binding](https://developer.android.com/topic/libraries/data-binding/)  - Declaratively bind observable data to UI elements.
    -   [Lifecycles](https://developer.android.com/topic/libraries/architecture/lifecycle)  - Create a UI that automatically responds to lifecycle events.
       -   [Threads](https://developer.android.com/reference/java/lang/Thread)  - For multithreading, Doing synchronous activities like loading images 
    -   [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)  - Build data objects that notify views when the underlying database changes.
    -   [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/)  - Handle everything needed for in-app navigation.

    -   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)  - Store UI-related data that isn't destroyed on app rotations. Easily schedule asynchronous tasks for optimal execution.
    -   [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)  - Manage your Android background jobs.
-   [UI](https://developer.android.com/guide/topics/ui)  - Details on why and how to use UI Components in your apps - together or separate
    -   [Animations & Transitions](https://developer.android.com/training/animation/)  - Move widgets and transition between screens.
     -   [Recycler View](https://developer.android.com/guide/topics/ui/layout/recyclerview)  - Move widgets and transition between screens.
    -   [Fragment](https://developer.android.com/guide/components/fragments)  - A basic unit of composable UI.
    -   [Layout](https://developer.android.com/guide/topics/ui/declaring-layout)  - Lay out widgets using different algorithms.
   -   Third party and miscellaneous libraries
    -   [Firebase](https://firebase.google.com/): for  [Real time database](https://firebase.google.com/docs/database)
    -   [Lottie files](https://lottiefiles.com/)  To use lottie file, json formated animations







<!-- GETTING STARTED -->
## Getting Started
![](assets/4.png)

The UVerifer system is a two part application consisting of :
- Aadhar Authenticator
- POC IRCTC App

### Installation

- Clone the repo using 
```sh
git clone https://github.com/TheSpeedX/UIDAI-Hackathon.git
```

- Web
  - Aadhar Authenticator
  ```sh
  pip install -r requirements.txt
  uvicorn main:app 
  ```
  - POC IRCTC APP API
  ```sh
  npm install
  npm start
  ```
- Android
  - UVerifier
  - POC IRCTC APP
  - Verifier Admin



<!-- USAGE EXAMPLES -->
## Screenshots


![](assets/7.png)  
![](assets/8.png)



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Your Name - Team X-Legion Coders(UIDAI Hackathon 2021)

Project Link: [https://github.com/TheSpeedX/UIDAI-Hackathon](https://github.com/TheSpeedX/UIDAI-Hackathon)

Made with :heart: by Team X-Legion Coders




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/othneildrew/Best-README-Template.svg?style=for-the-badge
[contributors-url]: https://github.com/TheSpeedX/UIDAI-Hackathon/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/othneildrew/Best-README-Template.svg?style=for-the-badge
[forks-url]: https://github.com/TheSpeedX/UIDAI-Hackathon/network/members
[stars-shield]: https://img.shields.io/github/stars/othneildrew/Best-README-Template.svg?style=for-the-badge
[stars-url]: https://github.com/TheSpeedX/UIDAI-Hackathon/stargazers
[issues-shield]: https://img.shields.io/github/issues/othneildrew/Best-README-Template.svg?style=for-the-badge
[issues-url]: https://github.com/TheSpeedX/UIDAI-Hackathon/issues
[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge
[license-url]: https://github.com/TheSpeedX/UIDAI-Hackathon//blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/othneildrew
[product-screenshot]: images/screenshot.png
