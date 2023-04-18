package cn.edu.hbut.weatherbesttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.edu.hbut.weatherbesttest.gson.Forecast;
import cn.edu.hbut.weatherbesttest.gson.Now;
import cn.edu.hbut.weatherbesttest.gson.Weather;
import cn.edu.hbut.weatherbesttest.util.HttpUtil;
import cn.edu.hbut.weatherbesttest.util.JsonUtility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    //访问天气申请到的APIKey
    public static final String API_Key = "ffbac23d09da45f1991a797f5ad8ddf2";
    //必应(cn.bing.com)提供了一个图片的接口，我们可以通过访问这个接口来获取每日更新的图片
    public static final String backgroundImageUrl = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private ImageView weatherBackgroundImage;


    //下拉刷新
    public SwipeRefreshLayout swipeRefreshLayout;
    private String mWeatherId;

    //滑动菜单
    public DrawerLayout drawerLayout;
    private Button btnCitySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //为了融合背景图和状态栏
        if(Build.VERSION.SDK_INT >=21){  //Android5.0以上系统才支持
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText =findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather", null);
        if(weatherString != null){  //有缓存就直接解析
            Weather weather = JsonUtility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{  //无缓存就去和风服务器查询
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        weatherBackgroundImage = findViewById(R.id.weather_bg_image);
        String bgImage = preferences.getString("bgImage", null);
        if(bgImage != null)
            Glide.with(this).load(bgImage).into(weatherBackgroundImage);
        else
            loadWeatherBackgroundImage(backgroundImageUrl);

        //下拉刷新
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(com.google.android.material.R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(()->{
            requestWeather(mWeatherId);
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        btnCitySelect = findViewById(R.id.button_city_select);
        btnCitySelect.setOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    //根据每个区域的天气id(weather_id)查询和风天气，另外还还要在和风天气上注册并申请一个API Key(ffbac23d09da45f1991a797f5ad8ddf2)
    //查询接口为： http://guolin.tech/api/weather?cityid=weather_id&key=API_Key
    public void requestWeather(final String weatherId){

        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key="+API_Key;
        String weatherUrl1 = "https://devapi.qweather.com/v7/weather/now?location=101010100"+"&key="+API_Key;
        String weatherUrl2 = "https://devapi.qweather.com/v7/weather/now?"+"key="+API_Key+"&location=114.31,30.49";
        loadWeatherData(weatherUrl1);
    }

    private void loadWeatherData(String weatherUrl){

        HttpUtil.sendHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(()->{
                    Toast.makeText(WeatherActivity.this, "获取天气数据失败", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = JsonUtility.handleWeatherResponse(responseText);
                runOnUiThread(()->{
                    if(weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weather", responseText);
                        editor.apply();
                        mWeatherId = weather.basic.weatherId;
                        showWeatherInfo(weather);
                    }else{
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    //处理并显示weather实体类中的数据
    private void showWeatherInfo(Weather weather){

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if(weather.aqi != null){
            aqiText.setText((weather.aqi.city.aqi));
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport   = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);

        //启动后台定时刷新服务
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadWeatherBackgroundImage(String backgroundImageUrl){

        HttpUtil.sendHttpRequest(backgroundImageUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //final String bingPic = response.body().string();
                final String bingPic = JsonUtility.handleBackgroundResponse(response.body().string());
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bgImage", bingPic);
                editor.apply();

                runOnUiThread(()->{
                    Glide.with(WeatherActivity.this).load(bingPic).into(weatherBackgroundImage);
                });
            }
        });

    }
}