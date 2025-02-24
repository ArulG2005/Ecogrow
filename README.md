# EcoGrow

EcoGrow is a smart farming solution that provides farmers with crop recommendations based on soil data. This system utilizes **Android Studio (Java)** for the mobile application, an **ESP32 microcontroller**, and a **soil sensor** to collect and analyze soil properties.

## Features
- **Crop Recommendation**: Suggests suitable crops based on soil parameters.
- **Soil Monitoring**: Collects real-time data on soil moisture, temperature, and pH.
- **Wireless Data Transmission**: Uses ESP32 for sending sensor data to the Android app.
- **File-Based Storage**: Stores sensor data locally for offline access.
- **User-Friendly Interface**: Simple and intuitive UI for farmers to view recommendations.

## Tech Stack
- **Android Studio (Java)** – Mobile application development
- **ESP32** – Microcontroller for sensor data processing
- **Soil Sensor** – Measures soil parameters like moisture, pH, and temperature
- **File-Based Storage** – Stores sensor data without requiring a database

## Project Structure
```
EcoGrow/
├── app/                   # Android app source code
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ecogrow/   # Main Java classes
│   │   │   ├── res/        # UI and drawable resources
├── README.md               # Project overview
```

## Installation & Setup
### 1. Android App Setup
- Install **Android Studio**
- Clone the repository:
  ```sh
  git clone https://github.com/ArulG2005/Ecogrow
  ```
- Open the project in **Android Studio** and build the APK
- Install the APK on an Android device

### 2. ESP32 Setup
- Install **Arduino IDE**
- Add ESP32 board support (via Board Manager)
- Upload the `main.ino` sketch to the ESP32
- Connect the soil sensor to ESP32 following the wiring diagram

## Usage
1. Turn on the **ESP32** to start collecting soil data.
2. Open the **EcoGrow** app on your Android device.
3. View real-time soil parameters.
4. Get crop recommendations based on the soil conditions.

## Future Enhancements
- **Cloud Integration**: Store data online for remote access.
- **AI-based Crop Prediction**: Use machine learning for better recommendations.
- **Multi-Language Support**: Expand accessibility for diverse users.

## Contributing
We welcome contributions! Feel free to fork the project, make improvements, and submit a pull request.

## License
This project is licensed under the **MIT License**.

## Contact
For any queries, reach out at **your-email@example.com** or visit [GitHub](https://github.com/ArulG2005/Ecogrow).

---
🚀 *Empowering Farmers with Smart Crop Recommendations!*
