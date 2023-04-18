package cn.edu.hbut.weatherbesttest.gson;

//天气数据组成2：空气质量信息
public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
