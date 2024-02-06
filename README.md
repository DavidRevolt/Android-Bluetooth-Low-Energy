# Android Bluetooth Low Energy
Performing BLE operations on Android like scanning, connecting, reading, writing and setting up indications or notifications.
It is a **work in progress** 🚧.

This app is built entirely with Kotlin and Jetpack Compose, it follows the [official architecture guidance](https://developer.android.com/jetpack/guide) as closely as possible.


### Features
*   Performing a BLE scan.
*   Connecting to a BLE device.
*   Discovering services.
*   Requesting for a larger ATT MTU.
*   Performing a read or write operation.
*   Subscribing to notifications or indications.

### Highlights
* **MVVM** architecture.
* **Hilt** dependency injection.
* **Android BLE SDK** Handling BLE operations.
* **Google Accompanist** Android runtime permissions support.



  
## Libraries & Dependencies
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-jetpack) - Dependency injection library.
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - Asynchronous programming.
- [Navigation](https://developer.android.com/guide/navigation) - Navigation component.
- [Google Accompanist](https://github.com/google/accompanist) - Android runtime permissions support.


## Modularization
App modularization approach is very similar to [Now in Android](https://github.com/android/nowinandroid/) App which developed by google.

To learn more about this approach check out [modularization learning journey](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md).


## Read More
- [Google Developers](https://developer.android.com/develop/connectivity/bluetooth/ble/ble-overview) - Basic documentation.
- [The Ultimate Guide to Android Bluetooth Low Energy](https://punchthrough.com/android-ble-guide/) - Extensive information.
