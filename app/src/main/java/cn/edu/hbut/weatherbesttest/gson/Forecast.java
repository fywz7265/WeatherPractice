package cn.edu.hbut.weatherbesttest.gson;

import com.google.gson.annotations.SerializedName;

//天气数据组成5：预报
public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;


    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
