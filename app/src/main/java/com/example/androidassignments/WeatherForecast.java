package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class WeatherForecast extends AppCompatActivity {
    private ImageView weatherImageView;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private ProgressBar progressBar;
    private Spinner cityspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        cityspinner = findViewById(R.id.cityspinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.canadian_cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);
        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = adapterView.getItemAtPosition(i).toString();
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + selectedCity + ",ca&APPID=8df62536453f5fadd8a15956f2de25fe&mode=xml&units=metric";

                new ForecastQuery().execute(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String minTemperature;
        private String maxTemperature;
        private String currentTemperature;
        private Bitmap weatherBitmap;

        @Override
        protected String doInBackground(String... strings) {
            String weatherData = "";
            String urlString = strings[0];

            try {
                Log.d("WeatherApp", "Connecting to URL: " + urlString);
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    int responseCode = urlConnection.getResponseCode();
                    String responseMessage = urlConnection.getResponseMessage();
                    Log.d("WeatherApp", "Response Code: " + responseCode + ", Response Message: " + responseMessage);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        weatherData = parseXml(in);
                    } else {
                        Log.e("WeatherApp", "Non-OK response code: " + responseCode);
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherData;
        }

        private String parseXml(InputStream in) {
            String result = "";
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);

                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = parser.getName();
                    if (eventType == XmlPullParser.START_TAG) {
                        if (name != null && name.equals("temperature")) {
                            currentTemperature = parser.getAttributeValue(null, "value");
                            minTemperature = parser.getAttributeValue(null, "min");
                            maxTemperature = parser.getAttributeValue(null, "max");
                            publishProgress(25, 50, 75);
                        } else if (name != null && name.equals("weather")) {
                            weatherBitmap = getBitmapFromURL(parser.getAttributeValue(null, "icon"));
                        }
                    }

                    eventType = parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private Bitmap getBitmapFromURL(String iconName) {
            try {
                String imageURL = "https://openweathermap.org/img/w/" + iconName + ".png";
                if (fileExistance(iconName + ".png")) {
                    Log.i("WeatherApp", "Local file found for: " + iconName);
                    FileInputStream fis = openFileInput(iconName + ".png");
                    return BitmapFactory.decodeStream(fis);
                } else {
                    Log.i("WeatherApp", "Downloading image for: " + iconName);
                    Bitmap image = HTTPUtils.getImage(imageURL);
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(100);
                    return image;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            currentTemp = findViewById(R.id.textViewCurrentTemp);
            minTemp = findViewById(R.id.textViewMinTemp);
            maxTemp = findViewById(R.id.textViewMaxTemp);
            weatherImageView = findViewById(R.id.imageViewWeather);
            progressBar = findViewById(R.id.progressbar);

            currentTemp.setText("Current Temperature: " + currentTemperature);
            minTemp.setText("Min Temperature: " + minTemperature);
            maxTemp.setText("Max Temperature: " + maxTemperature);
            weatherImageView.setImageBitmap(weatherBitmap);
            progressBar.setVisibility(View.INVISIBLE);
        }
        public class HTTPUtils {
            public static Bitmap getImage(String urlString) throws IOException {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            }
        }
    }
}

//public class WeatherForecast extends AppCompatActivity {
//
//    private ImageView weatherImageView;
//    private TextView currentTemp;
//    private TextView minTemp;
//    private TextView maxTemp;
//    private ProgressBar progressBar;
//    private Spinner cityspinner;
//
//    private String apiUrl;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_weather_forecast);
//        cityspinner = findViewById(R.id.cityspinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.canadian_cities, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        cityspinner.setAdapter(adapter);
//        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String selectedCity = cityspinner.getSelectedItem().toString();
//                String apiKey = "8df62536453f5fadd8a15956f2de25fe";
//                apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="
//                        + selectedCity + ",ca&APPID=" + apiKey + "&mode=xml&units=metric";
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//
//
//        weatherImageView = findViewById(R.id.imageViewWeather);
//        currentTemp = findViewById(R.id.textViewCurrentTemp);
//        minTemp = findViewById(R.id.textViewMinTemp);
//        maxTemp = findViewById(R.id.textViewMaxTemp);
//        progressBar = findViewById(R.id.progressbar);
//
//        progressBar.setVisibility(View.VISIBLE);
//        new ForecastQuery().execute();
//    }
//    private class ForecastQuery extends AsyncTask<Void, Void, Void> {
//
//        private String minTemperature;
//        private String maxTemperature;
//        private String currentTemperature;
//        private Bitmap weatherImage;
//        private String weatherIconName;
//
//        @Override
//        protected Void doInBackground(String...strings) {
//            try {
////                String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=&mode=xml&units=metric";
//                HttpURLConnection urlConnection = (HttpURLConnection) new URL(apiUrl).openConnection();
//                InputStream in = urlConnection.getInputStream();
//
////                Log.d("WeatherForecast", "Raw XML data: " + stringBuilder.toString());
//                // Instantiate the XML Pull Parser
//                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
//                xmlPullParserFactory.setNamespaceAware(true);
//                XmlPullParser parser = xmlPullParserFactory.newPullParser();
//
//                // Set the parser's input stream
//                parser.setInput(in, null);
//
//                // Call the method to parse the XML data
//                parseXmlData(parser);
//
//
//                // Close the InputStream and disconnect the connection
//                in.close();
//                urlConnection.disconnect();
//                if (!fileExists(weatherIconName + ".png")) {
//                    // Image file doesn't exist, download and save the image
//                    downloadAndSaveImage(weatherIconName);
//                }
//
//                // Read the image from local storage
//                readImageFromStorage(weatherIconName);
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//        public boolean fileExists(String fileName) {
//            File file = getBaseContext().getFileStreamPath(fileName);
//            return file.exists();
//        }
//        private void downloadAndSaveImage(String iconName) {
//            try {
//                // Construct the URL for the weather icon
//                String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";
//
//                // Open a connection to the image URL
//                HttpURLConnection imageConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
//
//                // Get the InputStream from the connection
//                InputStream imageStream = imageConnection.getInputStream();
//
//                // Decode the InputStream into a Bitmap
//                weatherImage = BitmapFactory.decodeStream(imageStream);
//
//                // Close the InputStream and disconnect the connection
//                imageStream.close();
//                imageConnection.disconnect();
//
//                // Save the Bitmap to local storage
//                saveImageToStorage(iconName);
//
//                // Publish progress
//                publishProgress(100);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void publishProgress(int i) {
//            super.onProgressUpdate();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        // Save the image to local storage
//        private void saveImageToStorage(String iconName) {
//            try {
//                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
//                weatherImage.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                outputStream.flush();
//                outputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        private void readImageFromStorage(String iconName) {
//            FileInputStream fis = null;
//            try {
//                fis = openFileInput(iconName + ".png");
//                // Decode the InputStream into a Bitmap
//                Bitmap bm = BitmapFactory.decodeStream(fis);
//
//                // Set the ImageView with the retrieved Bitmap
//                weatherImageView.setImageBitmap(bm);
//
//                // Log information
//                Log.i("WeatherForecast", "Looking for filename: " + iconName);
//                Log.i("WeatherForecast", "Found the image locally.");
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (fis != null) {
//                        fis.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        private void parseXmlData(XmlPullParser parser) throws XmlPullParserException, IOException {
//            int eventType = parser.getEventType();
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                String tagName = parser.getName();
//
//                switch (eventType) {
//                    case XmlPullParser.START_TAG:
//                        // Handle start tags
//                        if ("temperature".equals(tagName)) {
//                            // Extract temperature values
//                            minTemperature = parser.getAttributeValue(null, "min");
//                            maxTemperature = parser.getAttributeValue(null, "max");
//                            currentTemperature = parser.getAttributeValue(null, "value");
//
//                            publishProgress(25, 50, 75);
//                        }
//                        else if ("weather".equals(tagName)) {
//                            // Extract weather icon
//                            String iconCode = parser.getAttributeValue(null, "icon");
//                            downloadImageForCurrentWeather(iconCode);
//
//                            publishProgress(25, 50, 75);
//                        }
//                        break;
//
//                    // You can handle other events here as needed
//
//                    case XmlPullParser.END_TAG:
//                        // Handle end tags
//                        break;
//                }
//
//                // Move to the next event
//                eventType = parser.next();
//            }
//        }
//
//        private void publishProgress(int i, int i1, int i2) {
//            super.onProgressUpdate();
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        private void downloadImageForCurrentWeather(String iconCode) {
//            // Construct the URL for the weather icon based on the icon code
//            String imageUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
//
//            try {
//                // Open a connection to the image URL
//                HttpURLConnection imageConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
//
//                // Get the InputStream from the connection
//                InputStream imageStream = imageConnection.getInputStream();
//
//                // Decode the InputStream into a Bitmap
//                weatherImage = BitmapFactory.decodeStream(imageStream);
//
//                // Close the InputStream and disconnect the connection
//                imageStream.close();
//                imageConnection.disconnect();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
////        private void onProgressUpdate(Integer... values) {
////            super.onProgressUpdate(values);
////            progressBar.setVisibility(View.VISIBLE);
////            Log.d("WeatherForecast", "Progress: " + values[0] + "%, " + values[1] + "%, " + values[2] + "%");
////        }
//protected void onProgressUpdate(Integer... values) {
//    super.onProgressUpdate();
//
//    // Update ProgressBar visibility to View.VISIBLE
//    progressBar.setVisibility(View.VISIBLE);
//
//    // Set the progress of the ProgressBar based on the value[0] parameter
//    progressBar.setProgress(values[0]);
//}
//
//
//
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            // Update UI with the fetched data
//            minTemp.setText("Min Temperature: " + minTemperature);
//            maxTemp.setText("Max Temperature: " + maxTemperature);
//            currentTemp.setText("Current Temperature: " + currentTemperature);
//            weatherImageView.setImageBitmap(weatherImage);
//
//            // Set ProgressBar visibility to View.INVISIBLE when the task is completed
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        // Example method to download an image from a URL
//        private Bitmap downloadImageForCurrentWeather() {
//            String imageUrl = "http://openweathermap.org/img/w/01d.png"; // Replace with your actual image URL
//            try {
//                InputStream in = new java.net.URL(imageUrl).openStream();
//                return BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }
//}
