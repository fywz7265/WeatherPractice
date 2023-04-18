package cn.edu.hbut.weatherbesttest.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//天气信息集成
public class Weather {

    public String status; //返回的天气数据中包含有一项status数据，成功返回ok，失败返回具体原因

    public Basic  basic;
    public AQI    aqi;
    public Now    now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;  //多天的预报
}
