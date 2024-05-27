package com.example.ecogrow;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private TextView seed;
    private TextView n;
    private  TextView p;
    private  TextView k;
    private TextView ph;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView moistureTextView;
    private TextView textViewHeatIndex;
    private TextView textViewSuggestedCrop;
    private static final String[][] cropData = {
            {"rice", "202.9355362", "20.87974371", "6.502985292", "82.00274423", "90", "42", "43"},
            {"rice", "226.6555374", "21.77046169", "7.038096361", "80.31964408", "85", "58", "41"},
            {"rice", "263.9642476", "23.00445915", "7.840207144", "82.3207629", "60", "55", "44"},
            {"rice", "242.8640342", "26.49109635", "6.980400905", "80.15836264", "74", "35", "40"},
            {"rice", "262.7173405", "20.13017482", "7.628472891", "81.60487287", "78", "42", "42"},
            {"rice", "251.0549998", "23.05804872", "7.073453503", "83.37011772", "69", "37", "42"},
            {"rice", "271.3248604", "22.70883798", "5.70080568", "82.63941394", "69", "55", "38"},
            {"wheat", "300.1234567", "15.67891234", "6.123456789", "75.12345678", "85", "40", "45"},
            {"wheat", "310.2345678", "16.78902345", "6.234567891", "76.23456789", "80", "45", "46"},
            {"wheat", "320.3456789", "17.89013456", "6.345678912", "77.34567890", "75", "50", "47"},
            {"corn", "330.4567890", "18.90124567", "6.456789123", "78.45678901", "70", "55", "48"},
            {"corn", "340.5678901", "19.01235678", "6.567891234", "79.56789012", "65", "60", "49"},
            {"corn", "350.6789012", "20.12346789", "6.678912345", "80.67890123", "60", "65", "50"},
            {"soybean", "360.7890123", "21.23457890", "6.789023456", "81.78901234", "55", "70", "51"},
            {"soybean", "370.8901234", "22.34568901", "6.890134567", "82.89012345", "50", "75", "52"},
            {"soybean", "380.9012345", "23.45679012", "6.901245678", "83.90123456", "45", "80", "53"},
            {"barley", "390.0123456", "24.56780123", "6.012356789", "84.01234567", "40", "85", "54"},
            {"barley", "400.1234567", "25.67891234", "6.123467890", "85.12345678", "35", "90", "55"},
            {"barley", "410.2345678", "26.78902345", "6.234578901", "86.23456789", "30", "95", "56"},
            {"oats", "420.3456789", "27.89013456", "6.345689012", "87.34567890", "25", "100", "57"},
            {"oats", "430.4567890", "28.90124567", "6.456790123", "88.45678901", "20", "105", "58"},
            {"oats", "440.5678901", "29.01235678", "6.567801234", "89.56789012", "15", "110", "59"},
            {"sorghum", "450.6789012", "30.12346789", "6.678912345", "90.67890123", "10", "115", "60"},
            {"sorghum", "460.7890123", "31.23457890", "6.789023456", "91.78901234", "5", "120", "61"},
            {"sorghum", "470.8901234", "32.34568901", "6.890134567", "92.89012345", "0", "125", "62"},
            {"millet", "480.9012345", "33.45679012", "6.901245678", "93.90123456", "20", "130", "63"},
            {"millet", "490.0123456", "34.56780123", "7.012356789", "94.01234567", "25", "135", "64"},
            {"millet", "500.1234567", "35.67891234", "7.123467890", "95.12345678", "30", "140", "65"},
            {"peas", "510.2345678", "36.78902345", "7.234578901", "96.23456789", "35", "145", "66"},
            {"peas", "520.3456789", "37.89013456", "7.345689012", "97.34567890", "40", "150", "67"},
            {"peas", "530.4567890", "38.90124567", "7.456790123", "98.45678901", "45", "155", "68"},
            {"beans", "540.5678901", "39.01235678", "7.567801234", "99.56789012", "50", "160", "69"},
            {"beans", "550.6789012", "40.12346789", "7.678912345", "100.67890123", "55", "165", "70"},
            {"beans", "560.7890123", "41.23457890", "7.789023456", "101.78901234", "60", "170", "71"},
            {"lentils", "570.8901234", "42.34568901", "7.890134567", "102.89012345", "65", "175", "72"},
            {"lentils", "580.9012345", "43.45679012", "7.901245678", "103.90123456", "70", "180", "73"},
            {"lentils", "590.0123456", "44.56780123", "8.012356789", "104.01234567", "75", "185", "74"},
            {"chickpeas", "600.1234567", "45.67891234", "8.123467890", "105.12345678", "80", "190", "75"},
            {"chickpeas", "610.2345678", "46.78902345", "8.234578901", "106.23456789", "85", "195", "76"},
            {"chickpeas", "620.3456789", "47.89013456", "8.345689012", "107.34567890", "90", "200", "77"},
            {"potatoes", "630.4567890", "48.90124567", "8.456790123", "108.45678901", "95", "205", "78"},
            {"potatoes", "640.5678901", "49.01235678", "8.567801234", "109.56789012", "100", "210", "79"},
            {"potatoes", "650.6789012", "50.12346789", "8.678912345", "110.67890123", "105", "215", "80"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize TextViews
        temperatureTextView = findViewById(R.id.textViewTemperatureLabel);
        humidityTextView = findViewById(R.id.textViewHumidityLabel);
        moistureTextView = findViewById(R.id.textViewMoistureLabel);
        textViewHeatIndex = findViewById(R.id.textViewHeatIndexLabel);
        textViewSuggestedCrop = findViewById(R.id.textViewSeedsLabel);
        seed=findViewById(R.id.seedAns);
        n=findViewById(R.id.niAns);
        p=findViewById(R.id.pasAns);
        k=findViewById(R.id.kAns);
        ph=findViewById(R.id.phAns);
        // Read sensor data from Firebase
        readSensorData();
    }

    private void readSensorData() {
        // Reference to the "sensors" node
        mDatabase.child("sensors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve values from the database
                    Double temperature = dataSnapshot.child("temperature").getValue(Double.class);
                    Double humidity = dataSnapshot.child("humidity").getValue(Double.class);
                    Double moisture = dataSnapshot.child("moisture").getValue(Double.class);
                    Double heatindex = dataSnapshot.child("heatindex").getValue(Double.class);

                    // Log the values
                    Log.d(TAG, "Temperature: " + temperature + ", Humidity: " + humidity + ", Moisture: " + moisture + ", HeatIndex: " + heatindex);

                    // Update TextViews with retrieved values
                    if (temperature != null) temperatureTextView.setText("Temperature: " + temperature + "°C");
                    if (humidity != null) humidityTextView.setText("Humidity: " + humidity + "%");
                    if (moisture != null) moistureTextView.setText("Moisture: " + moisture + "%");
                    if (heatindex != null) textViewHeatIndex.setText("Heat Index: " + heatindex + "%");
                    findClosestCrops(temperature);
                } else {
                    Log.w(TAG, "No data found at sensors node");
                }
            }
            private void findClosestCrops(double temperature) {
                double minDifference = Double.MAX_VALUE;
                List<String> closestCrops = new ArrayList<>();
                String closestCropName = "";
                double closestCropN = 0;
                double closestCropP = 0;
                double closestCropH = 0;
                double closestCropPh = 0;

                for (String[] crop : cropData) {
                    String cropName = crop[0];
                    double cropTemp = Double.parseDouble(crop[2]);
                    double cropN = Double.parseDouble(crop[5]);
                    double cropP = Double.parseDouble(crop[6]);
                    double cropH = Double.parseDouble(crop[7]);
                    double cropPh = Double.parseDouble(crop[3]);

                    // Calculate the absolute difference between the given temperature and the temperature of the current crop
                    double difference = Math.abs(temperature - cropTemp);

                    if (difference < minDifference) {
                        // If the difference is smaller than the previous minimum difference, update the closest crop
                        minDifference = difference;
                        closestCrops.clear();
                        closestCrops.add(cropName);
                        closestCropName = cropName;
                        closestCropN = cropN;
                        closestCropP = cropP;
                        closestCropH = cropH;
                        closestCropPh = cropPh;
                    } else if (difference == minDifference) {
                        // If the difference is equal to the minimum difference, check if the crop's temperature falls within the same range
                        closestCrops.add(cropName);
                        closestCropName = cropName; // Update the closest crop name
                        closestCropN = cropN; // Update N value
                        closestCropP = cropP; // Update P value
                        closestCropH = cropH; // Update H value
                        closestCropPh = cropPh; // Update pH value
                    }
                }

                // Set the values into UI TextViews
                if (!closestCrops.isEmpty()) {
                    Log.d(TAG, "Crops with closest temperature to " + temperature + "°C:");
                    // Print the closest crops
                    for (String crop : closestCrops) {
                        Log.d(TAG, crop);
                    }

                    TextView seed = findViewById(R.id.seedAns);
                    seed.setText(closestCropName);

                    TextView n = findViewById(R.id.niAns);
                    n.setText(String.valueOf((int) closestCropN) + "%");

                    TextView p = findViewById(R.id.pasAns);
                    p.setText(String.valueOf((int) closestCropP) + "%");

                    TextView k = findViewById(R.id.kAns);
                    k.setText(String.valueOf((int) closestCropH) + " mg/kg");

                    TextView ph = findViewById(R.id.phAns);
                    ph.setText(String.valueOf((int) closestCropPh));

                    // You can print additional information about the closest crops here if needed
                } else {
                    Log.d(TAG, "No suitable crops found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
