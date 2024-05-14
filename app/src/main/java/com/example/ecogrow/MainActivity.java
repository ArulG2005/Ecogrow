package com.example.ecogrow;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothActivity";
    private static final String DEVICE_ADDRESS = "YOUR_ARDUINO_DEVICE_ADDRESS";
    private static final UUID UUID_SERIAL_PORT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;

    private TextView textViewMoisture;
    private TextView textViewTemperature;
    private TextView textViewHumidity;
    private TextView textViewHeatIndex;
    private TextView textViewSuggestedCrop;

    // Example data from Excel sheet
    private static final String[][] cropData = {
            {"rice", "202.9355362", "20.87974371", "6.502985292", "82.00274423", "90", "42", "43"},
            {"rice", "226.6555374", "21.77046169", "7.038096361", "80.31964408", "85", "58", "41"},
            {"rice", "263.9642476", "23.00445915", "7.840207144", "82.3207629", "60", "55", "44"},
            {"rice", "242.8640342", "26.49109635", "6.980400905", "80.15836264", "74", "35", "40"},
            {"rice", "262.7173405", "20.13017482", "7.628472891", "81.60487287", "78", "42", "42"},
            {"rice", "251.0549998", "23.05804872", "7.073453503", "83.37011772", "69", "37", "42"},
            {"rice", "271.3248604", "22.70883798", "5.70080568", "82.63941394", "69", "55", "38"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMoisture = findViewById(R.id.textViewMoistureLabel);
        textViewTemperature = findViewById(R.id.textViewTemperatureLabel);
        textViewHumidity = findViewById(R.id.textViewHumidityLabel);
        textViewHeatIndex = findViewById(R.id.textViewHeatIndexLabel);
        textViewSuggestedCrop = findViewById(R.id.textViewSeedsLabel);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if Bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            // Request to enable Bluetooth
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothAdapter.enable();
        }

        connectToDevice();
    }

    private void connectToDevice() {
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT);
            bluetoothSocket.connect();

            inputStream = bluetoothSocket.getInputStream();

            // Read data from Arduino
            readDataFromArduino();

        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Bluetooth device: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void readDataFromArduino() {
        byte[] buffer = new byte[1024];
        int bytes;

        try {
            bytes = inputStream.read(buffer); // Read from Arduino
            String arduinoData = new String(buffer, 0, bytes);

            // Parse Arduino data (assuming comma-separated format)
            String[] sensorValues = arduinoData.split(",");

            if (sensorValues.length >= 3) {
                double moisture = Double.parseDouble(sensorValues[0]);
                double temperature = Double.parseDouble(sensorValues[1]);
                double humidity = Double.parseDouble(sensorValues[2]);

                // Update UI with sensor readings
                runOnUiThread(() -> {
                    textViewMoisture.setText("Moisture: " + moisture);
                    textViewTemperature.setText("Temperature: " + temperature);
                    textViewHumidity.setText("Humidity: " + humidity);
                    // You can calculate heat index if needed
                });

                // Suggest crop based on sensor values
                suggestCropAndDisplay(moisture, temperature, humidity);
            }

        } catch (IOException e) {
            Log.e(TAG, "Error reading data from Arduino: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void suggestCropAndDisplay(double moisture, double temperature, double humidity) {
        // Compare sensor values with crop data to suggest a crop
        for (String[] data : cropData) {
            double dataMoisture = Double.parseDouble(data[1]);
            double dataTemperature = Double.parseDouble(data[2]);
            double dataHumidity = Double.parseDouble(data[4]);

            // Define threshold values for comparison (adjust as needed)
            double moistureThreshold = 10.0; // Example threshold for moisture
            double temperatureThreshold = 2.0; // Example threshold for temperature
            double humidityThreshold = 5.0; // Example threshold for humidity

            // Check if sensor readings match the current data entry within thresholds
            if (Math.abs(moisture - dataMoisture) <= moistureThreshold &&
                    Math.abs(temperature - dataTemperature) <= temperatureThreshold &&
                    Math.abs(humidity - dataHumidity) <= humidityThreshold) {
                // Display suggested crop and associated data
                runOnUiThread(() -> {
                    textViewSuggestedCrop.setText("Suggested Crop: " + data[0]);
                    displayCropData(data);
                });
                break; // Exit loop after finding the first matching crop
            }
        }
    }

    private void displayCropData(String[] cropData) {
        // Display crop data (pH, N, P, K)
        String ph = cropData[3];
        String nitrogen = cropData[5];
        String phosphorus = cropData[6];
        String potassium = cropData[7];

        runOnUiThread(() -> {

            TextView textViewCropData = findViewById(R.id.textViewSeedsLabel);
            textViewCropData.setText("pH: " + ph + "\n" +
                    "Nitrogen (N): " + nitrogen + "\n" +
                    "Phosphorus (P): " + phosphorus + "\n" +
                    "Potassium (K): " + potassium);
        });
    }
    private void simulateCropData() {
        // Simulate crop data (replace with actual test values)
        String[] simulatedCropData = {"rice", "202.9355362", "20.87974371", "6.502985292", "82.00274423", "90", "42", "43"};

        // Display the simulated crop data
        displayCropData(simulatedCropData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing Bluetooth socket: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
