package cn.edu.hbut.weatherbesttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.qweather.sdk.view.HeConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //账户初始化
        HeConfig.init("HE2303042054241418", "ffbac23d09da45f1991a797f5ad8ddf2");
        //切换至免费订阅
        HeConfig.switchToDevService();

        //如果本地缓存有天气信息，就不出现城市选择，直接显示天气
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getString("weather", null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }

    }
}