package android.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    EditText searchET;
    Button searchBtn;
    ProgressBar pgBar;
    ImageView imageView;
    TextView countryName, cityName, tempTV, timeTV;
    TextView latitude, latitude1, longitude, longitude1, humidity, humidity1, sunrise,
            sunrise1, sunset, sunset1, pressure, pressure1, wind_speed, wind_speed1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchET = findViewById(R.id.search_et);
        searchBtn = findViewById(R.id.search_btn);
        imageView = findViewById(R.id.imageView);
        countryName = findViewById(R.id.country_tv);
        cityName = findViewById(R.id.city_tv);
        tempTV = findViewById(R.id.temp_tv);
        timeTV = findViewById(R.id.time_tv);

        latitude = findViewById(R.id.latitude_tv);
        longitude = findViewById(R.id.longitude_tv);
        humidity = findViewById(R.id.humidity_tv);
        sunrise = findViewById(R.id.sunrise_tv);
        sunset = findViewById(R.id.sunset_tv);
        pressure = findViewById(R.id.pressure_tv);
        wind_speed = findViewById(R.id.windspeed_tv);

        latitude1 = findViewById(R.id.lat);
        longitude1 = findViewById(R.id.lon);
        humidity1 = findViewById(R.id.hum);
        sunrise1 = findViewById(R.id.sr);
        sunset1 = findViewById(R.id.ss);
        pressure1 = findViewById(R.id.prsr);
        wind_speed1 = findViewById(R.id.wind);

        pgBar = findViewById(R.id.pg_bar);

        searchBtn.setOnClickListener(v -> {
            pgBar.setVisibility(View.VISIBLE);
            findWeather();
        });
    }

    public void findWeather() {
        String city = searchET.getText().toString();
        final String JSON_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=8f4fd4cc69ca11350b1abfa8e07a55f9";

//        String apiKey = "77e3e2db80403a93bd813fd7337001e6";
//        api.openweathermap.org/data/2.5/weather?q={city name}&appid=77e3e2db80403a93bd813fd7337001e6

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, response -> {
            // calling api
            try {
                JSONObject jsonObject = new JSONObject(response);

                // finding country
                JSONObject object1 = jsonObject.getJSONObject("sys");
                String country = object1.getString("country");
                countryName.setText(country + " : ");

                // finding city
                String city_name = jsonObject.getString("name");
                cityName.setText(city_name);

//                finding temperature
                JSONObject object2 = jsonObject.getJSONObject("main");
                double temp = object2.getDouble("temp");
                tempTV.setText(temp + "°");

                // finding jsonArray for its object to fetch the icon img
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject obj = jsonArray.getJSONObject(0);
                String icon = obj.getString("icon");
                Picasso.get().load("http://openweathermap.org/img/wn/" + icon + "@2x.png").into(imageView);

                // finding date and time
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ss");
                String date = sdf.format(calendar.getTime());
                timeTV.setText(date);

                // finding latitude
                JSONObject object3 = jsonObject.getJSONObject("coord");
                double lat = object3.getDouble("lat");
                latitude.setText(lat + "°  N");
                latitude.setVisibility(View.VISIBLE);
                latitude1.setVisibility(View.VISIBLE);

                // finding longitude
                JSONObject object4 = jsonObject.getJSONObject("coord");
                double lon = object4.getDouble("lon");
                longitude.setText(lon + "°  E");
                longitude.setVisibility(View.VISIBLE);
                longitude1.setVisibility(View.VISIBLE);

                // finding humidity
                JSONObject object5 = jsonObject.getJSONObject("main");
                int hum = object5.getInt("humidity");
                humidity.setText(hum + "  %");
                humidity.setVisibility(View.VISIBLE);
                humidity1.setVisibility(View.VISIBLE);

                // finding sunrise
                JSONObject object6 = jsonObject.getJSONObject("sys");
                String rise = object6.getString("sunrise");
                sunrise.setText(rise + "  am");
                sunrise.setVisibility(View.VISIBLE);
                sunrise1.setVisibility(View.VISIBLE);

                // finding sunset
                JSONObject object7 = jsonObject.getJSONObject("sys");
                String set = object7.getString("sunset");
                sunset.setText(set + "  pm");
                sunset.setVisibility(View.VISIBLE);
                sunset1.setVisibility(View.VISIBLE);

                // finding pressure
                JSONObject object8 = jsonObject.getJSONObject("main");
                int pre = object8.getInt("pressure");
                pressure.setText(pre + "  hPa");
                pressure.setVisibility(View.VISIBLE);
                pressure1.setVisibility(View.VISIBLE);

                // finding wind speed
                JSONObject object9 = jsonObject.getJSONObject("wind");
                double sp = object9.getDouble("speed");
                wind_speed.setText(sp + "  km/h");
                wind_speed.setVisibility(View.VISIBLE);
                wind_speed1.setVisibility(View.VISIBLE);

                pgBar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(MainActivity.this, "Please insert a valid city name!", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error Response is called...");
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}